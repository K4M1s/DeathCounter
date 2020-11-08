package me.k4m1s.deathcounter.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;

public class DeathCounterTabCompleter implements TabCompleter {

    private final List<String> methods;
    private final List<String> damageCauses;
    private final List<String> killerType;
    private final List<String> mobNames;

    public DeathCounterTabCompleter() {
        methods = new ArrayList<>();
        methods.add("help"); methods.add("add"); methods.add("remove"); methods.add("list"); methods.add("details"); methods.add("me");

        damageCauses = new ArrayList<>();
        for (DamageCause dc : DamageCause.values()) {
            damageCauses.add(dc.toString());
        }

        killerType = new ArrayList<>();
        killerType.add("player"); killerType.add("mob");

        mobNames = new ArrayList<>();
        for(EntityType ent : EntityType.values()) {
            if (ent.getEntityClass() == null) continue;
            if (!Monster.class.isAssignableFrom(ent.getEntityClass())) continue;
            mobNames.add(ent.toString());
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (sender instanceof Player && args.length >= 1 && !sender.hasPermission("deathcounter." + args[0])) return result;


        if (args.length == 1) {
            for(String m : methods) {
                if (m.toLowerCase().contains(args[0].toLowerCase())) {
                    result.add(m);
                }
            }
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "add":
                case "details":
                    for(OfflinePlayer ply : Bukkit.getOfflinePlayers()) {
                        if (ply.getName() == null) break;
                        if (ply.getName().toLowerCase().contains(args[1].toLowerCase())) {
                            result.add(ply.getName());
                        }
                    }
                    break;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                for(String dc : damageCauses) {
                    if (dc.toLowerCase().contains(args[2].toLowerCase())) {
                        result.add(dc);
                    }
                }
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                for(String killer : killerType) {
                    if (killer.toLowerCase().contains(args[3].toLowerCase())) {
                        result.add(killer);
                    }
                }
            }
        }

        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("add")) {
                switch(args[3]) {
                    case "player":
                        for(OfflinePlayer ply : Bukkit.getOfflinePlayers()) {
                            if (ply.getName() == null) break;
                            if (ply.getName().toLowerCase().contains(args[4].toLowerCase())) {
                                result.add(ply.getName());
                            }
                        }
                        break;
                    case "mob":
                        for (String mob : mobNames) {
                            if (mob.toLowerCase().contains(args[4])) {
                                result.add(mob);
                            }
                        }
                        break;
                }
            }
        }
        return result;
    }
}
