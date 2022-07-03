package ru.javaops.rest_vot_test.web.controller.menuItem;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.rest_vot_test.web.BaseControllerTest;
import ru.javaops.rest_vot_test.web.testdata.UserTD;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.web.testdata.MenuItemTD.*;

class MenuItemControllerTest extends BaseControllerTest {
    static final String REST_URL = MenuItemController.REST_URL + "/";

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getByFilterWithoutParams() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItemTodayID3, menuItemTodayID4, menuItemTodayID7, menuItemTodayID8, menuItemTodayID11, menuItemTodayID12));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getByFilterByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_ASADOR_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItemTodayID11, menuItemTodayID12));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getByFilterByAllParams() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_MIRAZUR_ID))
                .param("startDate", String.valueOf(currentDate().minusDays(1)))
                .param("endDate", String.valueOf(currentDate().minusDays(1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItemTodayID5, menuItemTodayID6));
    }
}
