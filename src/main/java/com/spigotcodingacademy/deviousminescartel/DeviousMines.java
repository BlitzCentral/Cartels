package com.spigotcodingacademy.deviousminescartel;

import com.spigotcodingacademy.deviousminescartel.cmds.CartelCmds;
import com.spigotcodingacademy.deviousminescartel.listeners.PlayerChatListener;
import com.spigotcodingacademy.deviousminescartel.listeners.PlayerJoinListener;
import com.spigotcodingacademy.deviousminescartel.manager.CartelManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DeviousMines extends JavaPlugin {

    @Getter
    public static DeviousMines instance;
    @Getter
    private static CartelManager cartelManager;

    @Override
    public void onEnable() {

        instance = this;
        cartelManager = new CartelManager(this);

        saveDefaultConfig();

        File data = new File(getDataFolder(), "data");
        data.mkdir();
        File cartels = new File(getDataFolder(), "data/cartels");
        cartels.mkdir();
        File cartelsBackup = new File(getDataFolder(), "data/cartels/disbanded");
        cartelsBackup.mkdir();
        File players = new File(getDataFolder(), "data/players");
        players.mkdir();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);

        getCommand("cartel").setExecutor(new CartelCmds());

    }
}