package ru.javaops.rest_vot_test.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.VoteRepository;
import ru.javaops.rest_vot_test.service.VoteService;
import ru.javaops.rest_vot_test.util.JsonUtil;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.rest_vot_test.web.TestData.ForRestaurant.restaurantMirazur;
import static ru.javaops.rest_vot_test.web.TestData.ForRestaurant.restaurantNoma;
import static ru.javaops.rest_vot_test.web.TestData.ForUser.*;
import static ru.javaops.rest_vot_test.web.TestData.ForVote.*;
import static ru.javaops.rest_vot_test.web.TestData.NOT_FOUND_ID;

class VoteControllerTest extends BaseControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_VOTE_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_VOTE_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", "2020-05-20")
                .param("endDate", "2020-05-21")
                .param("user", String.valueOf(USER_ID))
                .param("restaurant", String.valueOf(restaurantNoma.id())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote3, userVote1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getMyBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "my")
                .param("startDate", "2020-05-20")
                .param("endDate", "2020-05-21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote3, userVote1));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_VOTE_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Nested
    class TimeDependedTest {
        @AfterEach
        void reset() {
            VoteService.resetClock();
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void delete() throws Exception {
            VoteService.setClock(voteBorderClock(true));
            perform(MockMvcRequestBuilders.delete(REST_URL + USER_VOTE_TODAY_ID))
                    .andExpect(status().isNoContent());
            assertFalse(repository.findById(USER_VOTE_TODAY_ID).isPresent());
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void deleteAfterTimeBorder() throws Exception {
            VoteService.setClock(voteBorderClock(false));
            perform(MockMvcRequestBuilders.delete(REST_URL + USER_VOTE_TODAY_ID))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void update() throws Exception {
            VoteService.setClock(voteBorderClock(true));
            Vote updated = new Vote(userVote5Today);
            updated.setRestaurant(restaurantNoma);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            VOTE_MATCHER.assertMatch(repository.getById(USER_VOTE_TODAY_ID), updated);
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateInvalid() throws Exception {
            VoteService.setClock(voteBorderClock(true));
            Vote invalid = new Vote(null, null, null, null);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(invalid)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateAfterTimeBorder() throws Exception {
            VoteService.setClock(voteBorderClock(false));
            Vote updated = new Vote(userVote5Today);
            updated.setRestaurant(restaurantNoma);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createWithLocation() throws Exception {
            VoteService.setClock(voteBorderClock(true));
            Vote nv = getNewVote(admin, restaurantMirazur);
            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(nv)))
                    .andExpect(status().isCreated());
            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            nv.setId(newId);
            VOTE_MATCHER.assertMatch(created, nv);
            VOTE_MATCHER.assertMatch(repository.getById(newId), nv);
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createAfterTimeBorder() throws Exception {
            VoteService.setClock(voteBorderClock(false));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(getNewVote(admin, restaurantMirazur))))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createInvalid() throws Exception {
            VoteService.setClock(voteBorderClock(true));
            Vote invalid = new Vote(null, null, null, null);
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(invalid)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional(propagation = Propagation.NEVER)
        @WithUserDetails(value = USER_MAIL)
        void createDuplicate() {
            VoteService.setClock(voteBorderClock(true));
            Vote duplicate = new Vote(null, userVote5Today.getDate(), user, restaurantNoma);
            assertThrows(Exception.class, () ->
                    perform(MockMvcRequestBuilders.post(REST_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.writeValue(duplicate)))
                            .andDo(print())
                            .andExpect(status().isUnprocessableEntity()));
        }


    }
}