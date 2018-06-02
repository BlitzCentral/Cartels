package com.spigotcodingacademy.deviousminescartel.listeners;

import com.nametagedit.plugin.NametagEdit;
import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        DeviousMines.getCartelManager().genPlayer(player);

        String prefix = DeviousMines.getCartelManager().getCartel(player);

        if (DeviousMines.getCartelManager().inCartel(player)) {
            NametagEdit.getApi().setNametag(player, "&7" + prefix + " &f", null);
        }
    }
}