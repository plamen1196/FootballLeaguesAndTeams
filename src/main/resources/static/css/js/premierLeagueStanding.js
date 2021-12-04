const tableStanding = document.getElementById('standing');
fetch("http://api.football-data.org/v2/competitions/2021/standings", {
    headers: {
        'X-Auth-Token': '6511fab43bd44f81b35b79091f035a21'
    }
}).then(data => data.json())
    .then(info => {
            for (let i = 0; i < 20; i++) {
                    let row = document.createElement("TR");
                    let position = document.createElement("TD");
                    let logoElement = document.createElement("TD");
                    let logo = document.createElement("IMG");
                    logo.style.width = "20px"
                    logo.style.height = "20px"
                    let teamName = document.createElement("TD");
                    let matches = document.createElement("TD");
                    let wins = document.createElement("TD");
                    let draws = document.createElement("TD");
                    let loses = document.createElement("TD");
                    let points = document.createElement("TD");
                    position.innerText = info.standings[0].table[i].position;
                    logo.src = info.standings[0].table[i].team.crestUrl;
                    teamName.innerText = info.standings[0].table[i].team.name;
                    matches.innerText = info.standings[0].table[i].playedGames;
                    wins.innerText = info.standings[0].table[i].won;
                    draws.innerText = info.standings[0].table[i].draw;
                    loses.innerText = info.standings[0].table[i].lost;
                    points.innerText = info.standings[0].table[i].points;
                    logoElement.appendChild(logo);
                    row.appendChild(position);
                    row.appendChild(logoElement);
                    row.appendChild(teamName);
                    row.appendChild(matches);
                    row.appendChild(wins);
                    row.appendChild(draws);
                    row.appendChild(loses);
                    row.appendChild(points);
                    tableStanding.appendChild(row);
            }
    })
