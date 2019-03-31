package com.blitzcentral.cartels;

import com.blitzcentral.cartels.listeners.PlayerJoinListener;
import com.blitzcentral.cartels.manager.CartelManager;
import com.blitzcentral.cartels.cmds.CartelCmds;
import com.blitzcentral.cartels.listeners.PlayerChatListener;
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
        File players = new File(getDataFolder(), "data/players");
        players.mkdir();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);

        getCommand("cartel").setExecutor(new CartelCmds());

    }
}