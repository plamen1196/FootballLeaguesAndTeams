package com.example.FootballLeagues.web.scheduler;

import com.example.FootballLeagues.model.entity.Result;
import com.example.FootballLeagues.model.view.TeamDetailsView;
import com.example.FootballLeagues.service.ResultService;
import com.example.FootballLeagues.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);
    private final TeamService teamService;
    private final ResultService resultService;

    public ScheduledTask(TeamService teamService, ResultService resultService) {
        this.teamService = teamService;
        this.resultService = resultService;
    }

    //Every day at 03:00 and 15:00 the scheduler will
    // write in the Logger the number of matches of each team
    // and all the matches that were played.
    @Scheduled(cron = "0 0 3,15 * * *")
    public void showNumberOfMatchesOfEachTeam(){
        List<TeamDetailsView> teams = teamService.findAllTeams();
        if (!teams.isEmpty()){
            teams
                    .forEach(team -> LOGGER.info(String
                            .format("Team with name: %s has played %d match/es. Til %s"
                                    , team.getName(), team.getMatches(), LocalDateTime.now())));
        }
        List<Result> results = resultService.findAllResultsByDate();

        results.forEach(result -> {
            LOGGER.info(String
                    .format("%s %d : %d %s this match was played at %s."
                            , result.getHomeTeam(), result.getHomeGoals(),
                            result.getAwayGoals(), result.getAwayTeam(), result.getTimeOfMatch()));

        });

    }
}
