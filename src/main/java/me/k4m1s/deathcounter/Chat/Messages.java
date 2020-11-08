package me.k4m1s.deathcounter.Chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {
    private static final String prefix = "&6&l[DeathCounter]&r ";

    /**
     * Sends message to server console.
     *
     * @param msg Message to send.
     */
    public static void sendMessage(String msg) {
        System.out.println(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }

    /**
     * Send message to command sender.
     *
     * @param sender Subject that message should be sent to.
     * @param msg Message to send.
     */
    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }

    /**
     * Sends message to command sender.
     *
     * @param sender Subject that message should be sent to.
     * @param msg Message to send.
     * @param bPrefix Include prefix?
     */
    public static void sendMessage(CommandSender sender, String msg, boolean bPrefix) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', bPrefix ? prefix : "" + msg));
    }

    /**
     * Sends header for better chat formatting.
     *
     * @param msg Message
     */
    public static void sendHeader(String msg) {
        int headerLength = 42 - msg.length() - 2;
        String headerPart = "&6&l" + new String(new char[headerLength/2]).replace("\0", "=");
        String message = headerPart + " &r&l" + msg + " " + headerPart;
        System.out.println(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Sends header for better chat formatting.
     *
     * @param sender Message Recipient
     * @param msg Message
     */
    public static void sendHeader(CommandSender sender, String msg) {
        int headerLength = 42 - msg.length() - 2;
        String headerPart = "&6&l" + new String(new char[(int)Math.floor(headerLength/2D)]).replace("\0", "=");
        String message = headerPart + " &r&l" + msg + " " + headerPart;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Sends footer for better chat formatting.
     */
    public static void sendFooter() {
        String footer = "&6&l" + new String(new char[42]).replace("\0", "=");
        System.out.println(ChatColor.translateAlternateColorCodes('&', footer));
    }

    /**
     * Sends footer for better chat formatting.
     *
     * @param sender Recipient
     */
    public static void sendFooter(CommandSender sender) {
        String footer = "&6&l" + new String(new char[42]).replace("\0", "=");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', footer));
    }
}
