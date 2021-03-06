package ru.javaops.rest_vot_test.web.controller.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.util.JsonUtil;
import ru.javaops.rest_vot_test.web.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.rest_vot_test.web.testdata.CommonTD.EXIST_ID;
import static ru.javaops.rest_vot_test.web.testdata.CommonTD.NOT_FOUND_ID;
import static ru.javaops.rest_vot_test.web.testdata.RestaurantTD.RESTAURANT_MATCHER;
import static ru.javaops.rest_vot_test.web.testdata.RestaurantTD.of;
import static ru.javaops.rest_vot_test.web.testdata.UserTD.ADMIN_MAIL;
import static ru.javaops.rest_vot_test.web.testdata.UserTD.USER_MAIL;

class AdminRestaurantControllerTest extends BaseControllerTest {
    static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private  RestaurantRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant nr = of(null,"Test Restaurant", "+1-111-222-3344");
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(nr)))
                .andExpect(status().isCreated());
        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        nr.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, nr);
        RESTAURANT_MATCHER.assertMatch(repository.getById(newId), nr);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = of(EXIST_ID, "Updatable Restaurant", "+1-111-222-3344");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(repository.getById(EXIST_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = of(EXIST_ID, "", "");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Restaurant updated = of(EXIST_ID, "Updatable Restaurant", "+1-111-222-3344");
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + EXIST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(EXIST_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
