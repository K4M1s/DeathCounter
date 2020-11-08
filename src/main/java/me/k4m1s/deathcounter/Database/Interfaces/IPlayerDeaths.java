package me.k4m1s.deathcounter.Database.Interfaces;

import me.k4m1s.deathcounter.Database.Models.PlayerDeath;

public interface IPlayerDeaths {
    void onQueryDone(PlayerDeath[] playerDeathCount);
}
