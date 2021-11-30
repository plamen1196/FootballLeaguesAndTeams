package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.entity.Player;
import com.example.FootballLeagues.model.entity.Stat;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.entity.enums.FootEnum;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.model.entity.enums.PositionEnum;
import com.example.FootballLeagues.repository.*;
import com.example.FootballLeagues.service.impl.PlayerServiceImpl;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails("admin")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerControllerTest {

    private static final String FULL_NAME = "Player Player";
    private static final String TEAM = "testName";
    private static final String POSITION = PositionEnum.Striker.name();
    private static final String FOOT = FootEnum.Both.name();
    private static final Integer NUMBER = 1;
    private static final Integer PHYSICAL = 1;
    private static final Integer PASSING = 1;
    private static final Integer ATTACK = 1;
    private static final Integer DEFENCE = 1;
    private static final Integer SHOOTING = 1;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private StatRepository statRepository;

    private Player testPlayer;
    private Team testTeam;
    private Stat testStat;

    @BeforeAll
    void setUpBeforeAll() {
        Team team = new Team();
        team.setName("testName");
        team.setLogo(LogoEnum.LOGO_1);
        team.setYear(2000);
        team.setUser(userRepository.findByUsername("admin").get());
        team.setPoints(10);
        team.setWins(0);
        team.setMatches(0);
        team.setLoses(0);
        team.setDraws(0);

        testTeam = teamRepository.save(team);
    }

    @BeforeEach
    void setUp() {

        Player player = new Player();
        player.setTeam(testTeam);
        player.setFullName("testPlayerName");
        player.setUser(userRepository.findByUsername("admin").get());
        player.setNumber(1);

        Stat stat = new Stat();
        stat.setPlayer(player);
        stat.setAttack(1);
        stat.setDefence(1);
        stat.setFoot(FootEnum.Both);
        stat.setPassing(1);
        stat.setPhysical(1);
        stat.setPosition(PositionEnum.Striker);
        stat.setShooting(1);

        testPlayer = playerRepository.save(player);

        testStat = statRepository.save(stat);
    }

    @AfterEach
    void tearDown() {
        statRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void testGetAddPlayer() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/player/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-player"))
                .andExpect(model().attributeExists("teamsByUser"));
    }

    @Test
    void testPostAddPlayer() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/player/add")
                        .param("number", String.valueOf(NUMBER))
                        .param("fullName", FULL_NAME)
                        .param("team", TEAM)
                        .param("physical", String.valueOf(PHYSICAL))
                        .param("defence", String.valueOf(DEFENCE))
                        .param("attack", String.valueOf(ATTACK))
                        .param("passing", String.valueOf(PASSING))
                        .param("shooting", String.valueOf(SHOOTING))
                        .param("position", String.valueOf(POSITION))
                        .param("foot", String.valueOf(FOOT))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<Player> player = playerRepository.findByFullName(FULL_NAME);

        Assertions.assertTrue(player.isPresent());
    }

    @Test
    void testGetEditPlayer() throws Exception {
        Player byId = playerRepository.findById(testPlayer.getId()).get();
        Team team = byId.getTeam();
        List<Stat> all = statRepository.findAll();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/player/edit/{id}", testPlayer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("edit-player"))
                .andExpect(model().attributeExists("player"))
                .andExpect(model().attributeExists("stat"))
                .andExpect(model().attributeExists("teams"));
    }

    @Test
    void testPatchEditPlayer() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/player/edit/{id}", testPlayer.getId())
                        .param("number", String.valueOf(NUMBER))
                        .param("fullName", FULL_NAME)
                        .param("team", TEAM)
                        .param("physical", String.valueOf(PHYSICAL))
                        .param("defence", String.valueOf(DEFENCE))
                        .param("attack", String.valueOf(ATTACK))
                        .param("passing", String.valueOf(PASSING))
                        .param("shooting", String.valueOf(SHOOTING))
                        .param("position", String.valueOf(POSITION))
                        .param("foot", String.valueOf(FOOT))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<Player> player = playerRepository.findById(testPlayer.getId());

        Assertions.assertTrue(player.isPresent());
        Assertions.assertEquals(FULL_NAME, player.get().getFullName());
        Assertions.assertEquals(NUMBER, player.get().getNumber());
    }

    @Test
    void testPatchEditPlayerWithWrongField() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/player/edit/{id}", testPlayer.getId())
                        .param("number", String.valueOf(NUMBER))
                        .param("fullName", "")
                        .param("team", TEAM)
                        .param("physical", "")
                        .param("defence", String.valueOf(DEFENCE))
                        .param("attack", String.valueOf(ATTACK))
                        .param("passing", String.valueOf(PASSING))
                        .param("shooting", String.valueOf(SHOOTING))
                        .param("position", String.valueOf(POSITION))
                        .param("foot", String.valueOf(FOOT))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(2))
                .andExpect(view().name("redirect:" + testPlayer.getId() + "/errors"))
                .andExpect(flash().attributeExists("addPlayerBindingModel"));
    }

    @Test
    public void testDeletePlayer() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/player/delete/{id}", testPlayer.getId())
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> playerService.findPlayerById(testPlayer.getId(), testPlayer.getUser().getUsername()));
    }

    @Test
    public void testGetDetailsPlayer() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/player/details/{id}", testPlayer.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("details-player"))
                .andExpect(model().attributeExists("stats"))
                .andExpect(model().attributeExists("player"));

    }
}