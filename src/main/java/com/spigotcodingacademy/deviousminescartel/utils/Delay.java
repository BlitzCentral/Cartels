package com.spigotcodingacademy.deviousminescartel.utils;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import org.bukkit.Bukkit;

public class Delay {

    public static void until(long time, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(DeviousMines.getInstance(), runnable, time);
    }
}