package com.spigotcodingacademy.deviousminescartel.utils;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Chat {

    public static String prefix = color(DeviousMines.getInstance().getConfig().getString("prefix"));

    public static void msg(Player player, String... message) {
        Arrays.stream(message).forEach(s -> player.sendMessage(color(s)));
    }

    public static String color(String message) { return ChatColor.translateAlternateColorCodes('&', message);}
}