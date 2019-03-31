package com.blitzcentral.cartels.listeners;

import com.blitzcentral.cartels.Cartels;
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

        Cartels.getCartelManager().genPlayer(player);

        String prefix = Cartels.getCartelManager().getCartel(player);

        if (Cartels.getCartelManager().inCartel(player)) {
            if (!Cartels.getCartelManager().isOwner(player, Cartels.getCartelManager().getCartel(player))) {
                try{
                    File Cartel = new File(Cartels.getInstance().getDataFolder() + "/data/cartels", Cartels.getCartelManager().getCartel(player) + ".yml");
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