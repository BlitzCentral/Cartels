package com.blitzcentral.cartels.utils;

import com.blitzcentral.cartels.DeviousMines;
import org.bukkit.Bukkit;

public class Delay {

    public static void until(long time, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(DeviousMines.getInstance(), runnable, time);
    }
}