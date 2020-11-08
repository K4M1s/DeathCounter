package me.k4m1s.deathcounter.Database.Interfaces;

import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;

public interface IPlayerDeathCount {
    void onQueryDone(PlayerDeathCount playerDeathCount);
}
