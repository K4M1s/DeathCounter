package me.k4m1s.deathcounter.Commands;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.Database.Models.PlayerDeath;
import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.text.SimpleDateFormat;

public class DeathCounterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;

        switch(args[0]) {
            case "add":
                if (sender instanceof Player) {
                    if ( !( sender.hasPermission("deathcounter.add")) ) {
                        Messages.sendMessage(sender, "&cYou do not have access to this command.");
                        return true;
                    }
                }
                if (args.length < 3) return this.cmdHelp(sender);
                String playerName = args[1];
                String method = args[2];
                String type = null;
                String mobName = null;
                String killerName = null;

                if (args.length > 3 && args.length < 6) {
                    type = args[3];
                    if (type.equalsIgnoreCase("mob")) {
                        mobName = args[4];
                    } else if(type.equalsIgnoreCase("player")) {
                        killerName = args[4];
                    }
                }


                OfflinePlayer player = null;
                for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                    if (p.getName() == null) continue;
                    if (p.getName().equalsIgnoreCase(playerName)) {
                        player = p;
                    }
                }

                if (player == null) {
                    Messages.sendMessage(sender, "Player not found.");
                    return true;
                }

                DamageCause damageCause = null;
                for(DamageCause dc : DamageCause.values()) {
                    if (dc.toString().equalsIgnoreCase(method)) {
                        damageCause = dc;
                    }
                }

                if (damageCause == null) {
                    Messages.sendMessage(sender, "DamageCause is invalid.");
                    return true;
                }


                OfflinePlayer killer = null;
                if (type != null && type.equalsIgnoreCase("player")) {
                    for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                        if (p.getName() == null) continue;
                        if (p.getName().equalsIgnoreCase(killerName)) {
                            killer = p;
                        }
                    }
                    if (killer == null) {
                        Messages.sendMessage(sender, "Killer not found.");
                        return true;
                    }
                }
                return this.cmdAdd(sender, player, damageCause, killer, mobName);
            case "remove":
                if (sender instanceof Player) {
                    if ( !( sender.hasPermission("deathcounter.remove")) ) {
                        Messages.sendMessage(sender, "&cYou do not have access to this command.");
                        return true;
                    }
                }
                if (args.length < 2) return this.cmdHelp(sender);
                try {
                    int ID = Integer.parseInt(args[1]);
                    return this.cmdRemove(sender, ID);
                } catch(NumberFormatException e) {
                    Messages.sendMessage(sender, "&cID is not valid");
                    return false;
                }
            case "list":
                if (sender instanceof Player) {
                    if ( !( sender.hasPermission("deathcounter.list")) ) {
                        Messages.sendMessage(sender, "&cYou do not have access to this command.");
                        return true;
                    }
                }
                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        Messages.sendMessage(sender, "&cPage is not valid");
                        return false;
                    }
                }
                return this.cmdList(sender, page);
            case "details":
                if (sender instanceof Player) {
                    if ( !( sender.hasPermission("deathcounter.details")) ) {
                        Messages.sendMessage(sender, "&cYou do not have access to this command.");
                        return true;
                    }
                }
                if (args.length < 2) return this.cmdHelp(sender);
                OfflinePlayer dPlayer = null;
                for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                    if (p.getName() == null) continue;
                    if (p.getName().equalsIgnoreCase(args[1])) {
                        dPlayer = p;
                    }
                }
                int dPage = 1;
                if (args.length >= 3) {
                    try {
                        dPage = Integer.parseInt(args[2]);
                    } catch(NumberFormatException e) {
                        Messages.sendMessage(sender, "&cPage is not valid");
                        return false;
                    }
                }
                return this.cmdDetails(sender, dPlayer, dPage);
            case "me":
                if (sender instanceof Player) {
                    if ( !( sender.hasPermission("deathcounter.me")) ) {
                        Messages.sendMessage(sender, "&cYou do not have access to this command.");
                        return true;
                    }
                }
                if (args.length < 2) return this.cmdHelp(sender);
                int mPage = 1;
                if (args.length >= 3) {
                    try {
                        mPage = Integer.parseInt(args[2]);
                    } catch(NumberFormatException e) {
                        Messages.sendMessage(sender, "&cPage is not valid");
                        return false;
                    }
                }
                return this.cmdDetails(sender, (OfflinePlayer) sender, mPage);

            default:
                return this.cmdHelp(sender);
        }
    }

    /**
     * Sends help message.
     *
     * @param sender Message recipient
     * @return true
     */
    private boolean cmdHelp(CommandSender sender) {
        Messages.sendHeader(sender, "DeathCounter Help");
        Messages.sendMessage(sender, "&l/deathcounter help &r- Displays help message.", false);
        Messages.sendMessage(sender, "&l/deathcounter add <Player> <DamageCause> mob/player <mob/player> &r- Adds death to given player.", false);
        Messages.sendMessage(sender, "&l/deathcounter remove <ID> &r- Removes  player death.", false);
        Messages.sendMessage(sender, "&l/deathcounter list <Page> &r- Lists players and their death count.", false);
        Messages.sendMessage(sender, "&l/deathcounter details <Player> <Page> &r- Lists single player deaths.", false);
        Messages.sendFooter(sender);
        return true;
    }

    /**
     * Helper function for death add command.
     *
     * @param sender Command sender
     * @param player The player we want to add death to.
     * @param damageCause The damage cause
     * @param killer Player killer
     * @param mobName Mob name
     * @return true
     */
    private boolean cmdAdd(final CommandSender sender, OfflinePlayer player, DamageCause damageCause, OfflinePlayer killer, String mobName) {
        DatabaseHelper.addPlayerDeath(player.getUniqueId().toString(), damageCause.toString(), (killer == null) ? null : killer.getUniqueId().toString(), mobName, result -> Messages.sendMessage(sender, "Death has " + (result ? "" : "not ") + "been added!"));
        return true;
    }

    /**
     * Helper function for removing death.
     *
     * @param sender Command sender.
     * @param ID Death ID
     * @return true
     */
    private boolean cmdRemove(CommandSender sender, int ID) {
        DatabaseHelper.removePlayerDeath(ID, result -> Messages.sendMessage(sender, "Death has " + (result ? "" : "not ") + "been removed!"));
        return true;
    }

    /**
     * Helper function for Listing players death counts.
     *
     * @param sender Command sender
     * @param page Page
     * @return true
     */
    private boolean cmdList(final CommandSender sender, final int page) {
        DatabaseHelper.getPlayersCount(result -> {
            if (result < 0) return;
            int offset = 0;
            if (page > 1) {
                offset = 6;
                offset *= (page - 1);
            }
            final int maxPages = ((int) Math.ceil(result / 6D) > 0? (int) Math.ceil(result / 6D) : 1);
            if (page > maxPages) {
                Messages.sendMessage(sender, "&cThis page does not exists.");
                return;
            }
            DatabaseHelper.getPlayersDeathCount(offset, 6, playerDeathCount -> {
                Messages.sendHeader(sender, "Page " + page + "/" + maxPages);
                for(PlayerDeathCount pdc : playerDeathCount) {
                    Messages.sendMessage(sender, pdc.getPlayer().getName() + ": &c" + pdc.getCount(), false);
                }
                Messages.sendFooter(sender);
            });
        });
        return true;
    }

    /**
     * Helper function for Listing player deaths with description.
     *
     * @param sender Command sender
     * @param player The player we want to see information of.
     * @param page Page
     * @return true
     */
    private boolean cmdDetails(CommandSender sender, OfflinePlayer player, int page) {
        DatabaseHelper.getPlayerDeathCount(player, playerDeathCount -> {
            if (playerDeathCount == null) return;
            int offset = 0;
            if (page > 1) {
                offset = 6;
                offset *= (page - 1);
            }
            final int maxPages = ((int) Math.ceil(playerDeathCount.getCount() / 6D) > 0? (int) Math.ceil(playerDeathCount.getCount() / 6D) : 1);
            if (page > maxPages) {
                Messages.sendMessage(sender, "&cThis page does not exists.");
                return;
            }
            DatabaseHelper.getPlayerDeaths(player, offset, 6, playerDeath -> {
                Messages.sendHeader(sender, "Page " + page + "/" + maxPages);
                for(PlayerDeath pd : playerDeath) {
                    Messages.sendMessage(sender, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(pd.getDeathTime()) + ", Death cause: " + pd.getDamageCause().toString() + (pd.getKiller() != null ? ", Killer: " + pd.getKiller().getName() : (pd.getMobName() != null ? ", Mob: " + pd.getMobName() : "")) , false);
                }
                Messages.sendFooter(sender);
            });
        });
        return true;
    }

}
