package com.spigotcodingacademy.deviousminescartel.listeners;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
import com.spigotcodingacademy.deviousminescartel.utils.Delay;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        DeviousMines.getCartelManager().genPlayer(player);

        String prefix = DeviousMines.getCartelManager().getCartel(player);

        if (DeviousMines.getCartelManager().inCartel(player)) {
            if (!DeviousMines.getCartelManager().isOwner(player, DeviousMines.getCartelManager().getCartel(player))) {
                try{
                    File Cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
                    YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartel);
                    List<String> members = CartelData.getStringList("Members");

                    if (!members.contains(player.getUniqueId().toString())) {
                        members.add(player.getUniqueId().toString());
                        CartelData.set("Members", members);
                        CartelData.save(Cartel);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}