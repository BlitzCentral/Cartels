package com.spigotcodingacademy.deviousminescartel;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class DeviousMines extends JavaPlugin {

    @Getter
    public static DeviousMines instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

    }
}