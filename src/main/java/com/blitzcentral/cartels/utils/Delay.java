package com.blitzcentral.cartels.utils;

import com.blitzcentral.cartels.Cartels;
import org.bukkit.Bukkit;

public class Delay {

    public static void until(long time, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(Cartels.getInstance(), runnable, time);
    }
}