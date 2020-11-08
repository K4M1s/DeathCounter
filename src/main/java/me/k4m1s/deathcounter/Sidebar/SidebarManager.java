package me.k4m1s.deathcounter.Sidebar;


import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;
import me.k4m1s.deathcounter.FastBoard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SidebarManager {

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public void update() {
        DatabaseHelper.getPlayersDeathCount(0, 10, playerDeathCount -> {
            if (playerDeathCount == null) return;
            for (FastBoard board : boards.values()) {
                updateBoard(board, playerDeathCount);
            }
        });
    }

    public void createBoardForPlayer(Player player) {
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.RED + "TOP 10 Deaths");
        boards.put(player.getUniqueId(), board);
    }

    public void removePlayerBoard(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    private void updateBoard(FastBoard board, PlayerDeathCount[] data) {
        for(int i=0; i<data.length; i++) {
            board.updateLine(i, data[i].getPlayer().getName() + ": " + data[i].getCount());
        }
    }
}
