package com.spigotcodingacademy.deviousminescartel.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Chat {

    public static String prefix = color("&7(&6DM-Cartel&7) ");

    public static void msg(Player player, String... message) {
        Arrays.stream(message).forEach(s -> player.sendMessage(color(s)));
    }

    public static String color(String message) { return ChatColor.translateAlternateColorCodes('&', message);}
}