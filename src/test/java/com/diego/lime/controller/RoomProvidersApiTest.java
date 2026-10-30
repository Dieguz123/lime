package com.diego.lime.controller;

import com.diego.lime.model.Provider;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.repository.ProviderRepository;
import com.diego.lime.repository.RoomRepository;
import com.diego.lime.repository.UserRepository;
import com.diego.lime.service.RoomService;
import com.diego.lime.service.TokenService;
import com.diego.lime.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Deliberately not @Transactional: a test transaction would keep the Hibernate session open and
//hide lazy loading errors that only happen in production, so the data is cleaned up by hand.
@SpringBootTest
@AutoConfigureMockMvc
class RoomProvidersApiTest {

    private static final long NETFLIX = 8L;
    private static final long DISNEY = 337L;
    private static final long UNKNOWN_PROVIDER = 999L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProviderRepository providerRepository;

    private User creator;
    private User partner;
    private User stranger;
    private String code;

    @BeforeEach
    void setUp() {
        providerRepository.save(new Provider(NETFLIX, "Netflix", "/netflix.jpg"));
        providerRepository.save(new Provider(DISNEY, "Disney Plus", "/disney.jpg"));

        creator = userService.register("api-creator", "password123");
        partner = userService.register("api-partner", "password123");
        stranger = userService.register("api-stranger", "password123");

        Room room = roomService.createRoom(creator.getId(), Set.of(NETFLIX));
        code = room.getInviteCode();
        roomService.joinRoom(code, partner.getId());
    }

    @AfterEach
    void cleanUp() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        providerRepository.deleteAll();
    }

    @Test
    void listProviders_returnsAllSortedByName() throws Exception {
        mockMvc.perform(get("/api/providers").header(HttpHeaders.AUTHORIZATION, bearer(creator)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", contains("Disney Plus", "Netflix")))
                .andExpect(jsonPath("$[0].logoPath").value("/disney.jpg"));
    }

    @Test
    void listProviders_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRoom_savesChosenProviders() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(stranger))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(NETFLIX, DISNEY)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.providers[*].name", contains("Disney Plus", "Netflix")));
    }

    @Test
    void createRoom_withoutProviders_isAllowed() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(stranger))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.providers", hasSize(0)));
    }

    @Test
    void createRoom_unknownProvider_returns404() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(stranger))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(UNKNOWN_PROVIDER)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRoom_includesProviders() throws Exception {
        mockMvc.perform(get("/api/rooms/{code}", code).header(HttpHeaders.AUTHORIZATION, bearer(partner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers[*].name", contains("Netflix")));
    }

    @Test
    void joinRoom_includesProviders() throws Exception {
        Room room = roomService.createRoom(creator.getId(), Set.of(NETFLIX));

        mockMvc.perform(post("/api/rooms/{code}/join", room.getInviteCode())
                        .header(HttpHeaders.AUTHORIZATION, bearer(partner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers[*].name", contains("Netflix")));
    }

    @Test
    void getMyRooms_withManyProviders_returnsEachRoomOnce() throws Exception {
        roomService.updateProviders(code, creator.getId(), Set.of(NETFLIX, DISNEY));

        mockMvc.perform(get("/api/rooms/mine").header(HttpHeaders.AUTHORIZATION, bearer(creator)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].providers", hasSize(2)));
    }

    @Test
    void updateProviders_creator_replacesProviders() throws Exception {
        updateProviders(creator, body(DISNEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers[*].name", contains("Disney Plus")));
    }

    @Test
    void updateProviders_partner_replacesProviders() throws Exception {
        updateProviders(partner, body(NETFLIX, DISNEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers[*].name", contains("Disney Plus", "Netflix")));
    }

    @Test
    void updateProviders_emptyList_removesFilter() throws Exception {
        updateProviders(creator, body())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers", hasSize(0)));
    }

    @Test
    void updateProviders_notMember_returns403AndKeepsProviders() throws Exception {
        updateProviders(stranger, body(DISNEY))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/rooms/{code}", code).header(HttpHeaders.AUTHORIZATION, bearer(creator)))
                .andExpect(jsonPath("$.providers[*].name", contains("Netflix")));
    }

    @Test
    void updateProviders_withoutToken_returns401() throws Exception {
        mockMvc.perform(put("/api/rooms/{code}/providers", code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(DISNEY)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProviders_unknownRoom_returns404() throws Exception {
        mockMvc.perform(put("/api/rooms/{code}/providers", "NOROOM")
                        .header(HttpHeaders.AUTHORIZATION, bearer(creator))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(DISNEY)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProviders_unknownProvider_returns404AndKeepsProviders() throws Exception {
        updateProviders(creator, body(DISNEY, UNKNOWN_PROVIDER))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/rooms/{code}", code).header(HttpHeaders.AUTHORIZATION, bearer(creator)))
                .andExpect(jsonPath("$.providers[*].name", contains("Netflix")));
    }

    @Test
    void updateProviders_missingList_returns400() throws Exception {
        updateProviders(creator, "{}")
                .andExpect(status().isBadRequest());
    }

    private ResultActions updateProviders(User user, String body) throws Exception {
        return mockMvc.perform(put("/api/rooms/{code}/providers", code)
                .header(HttpHeaders.AUTHORIZATION, bearer(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private String bearer(User user) {
        return "Bearer " + tokenService.createToken(user);
    }

    private String body(Long... providerIds) {
        return "{\"providerIds\": " + java.util.Arrays.toString(providerIds) + "}";
    }
}
