package ru.javaops.rest_vot_test.web.controller;

import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.to.DishTo;
import ru.javaops.rest_vot_test.util.JsonUtil;
import ru.javaops.rest_vot_test.web.BaseControllerTest;
import ru.javaops.rest_vot_test.web.testdata.UserTD;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static ru.javaops.rest_vot_test.web.testdata.CommonTD.NOT_FOUND_ID;
import static ru.javaops.rest_vot_test.web.testdata.DishTD.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static ru.javaops.rest_vot_test.web.testdata.RestaurantTD.restaurantNoma;
import static ru.javaops.rest_vot_test.web.testdata.UserTD.ADMIN_MAIL;
import static ru.javaops.rest_vot_test.web.testdata.UserTD.USER_MAIL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminDishControllerTest extends BaseControllerTest {
    public static final String REST_URL = AdminDishController.REST_URL + "/";

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1, dish2, dish3));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = new Dish(null, "New Dish", 52.5, restaurantNoma);
        DishTo newTo = fromDish(newDish);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());
        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        DishTo invalid = new DishTo(null, null, 0, 0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        DishTo to = fromDish(dish1);
        to.setName("Updated");
        Dish dish = copy(dish1);
        dish.setName("Updated");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.getById(DISH_1_ID), dish);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateNotBelong() throws Exception {
        DishTo to = fromDish(dish1, RESTAURANT_WITHOUT_DISH_1_ID);

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        DishTo invalid = fromDish(dish1);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        DishTo updated = fromDish(dish1);
        updated.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_9_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(DISH_9_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}