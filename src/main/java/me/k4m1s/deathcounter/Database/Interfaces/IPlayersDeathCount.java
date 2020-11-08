package me.k4m1s.deathcounter.Database.Interfaces;

import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;

public interface IPlayersDeathCount {
    void onQueryDone(PlayerDeathCount[] playerDeathCount);
}
