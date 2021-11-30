package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.service.impl.LeagueServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails("admin")
@SpringBootTest
@AutoConfigureMockMvc
class LeagueControllerTest {

    private static int TEST_CAPACITY = 10;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeagueServiceImpl leagueService;

    @Autowired
    private LeagueRepository leagueRepository;
    private League league1;


    private void initLeagues() {
        league1 = new League();
        league1.setLevel("testLevel1");
        league1.setCapacity(TEST_CAPACITY);

        league1 = leagueRepository.save(league1);
    }

    @AfterEach
    void tearDown() {
        leagueRepository.deleteAll();
    }

    @Test
    public void testGetAddLeaguePage() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/league/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-league"))
                .andExpect(model().attributeExists("addLeagueBindingModel"));
    }

    @Test
    public void testPostAddLeaguePage() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/league/add")
                        .param("capacity", String.valueOf(TEST_CAPACITY))
                        .param("level", "testLevel")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<League> league = leagueRepository.findByLevel("testLevel");

        Assertions.assertTrue(league.isPresent());
    }

    @Test
    public void testPostAddLeaguePageWithNotValid() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/league/add")
                        .param("capacity", "")
                        .param("level", "")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addLeagueBindingModel"));
    }

    @Test
    public void testGetEditLeague() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/league/edit/{id}", league1.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("edit-league"))
                .andExpect(model().attributeExists("bad_credentials"))
                .andExpect(model().attributeExists("tooSmallCapacity"))
                .andExpect(model().attributeExists("league"));
    }

    @Test
    public void testPatchEditLeague() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/league/edit/{id}", league1.getId())
                        .param("capacity", String.valueOf(TEST_CAPACITY))
                        .param("level", "newTestLevel")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(view().name("redirect:/league/details/" + league1.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<League> league = leagueRepository.findByLevel("newTestLevel");

        Assertions.assertTrue(league.isPresent());
        leagueRepository.deleteAll();
    }

    @Test
    public void testPatchEditLeagueWithNotValid() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/league/edit/{id}", league1.getId())
                        .param("capacity", "")
                        .param("level", "")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(view().name("redirect:" + league1.getId() + "/errors"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addLeagueBindingModel"));
    }
    @Test
    public void testGetErrorLeaguePage() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/edit/{id}/errors", league1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllLeagues() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/league/allleagues")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("all-leagues"))
                .andExpect(model().attributeExists("leagues"));
    }

    @Test
    public void testGetDetailsLeague() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/league/details/{id}", league1.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("details-league"))
                .andExpect(model().attributeExists("teamsByPoints"))
                .andExpect(model().attributeExists("league"));

    }
    @Test
    public void testDeleteLeague() throws Exception {
        initLeagues();
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/league/delete/{id}", league1.getId())
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        Assertions.assertNull(leagueService.findLeagueByLevel(league1.getLevel()));
    }
}