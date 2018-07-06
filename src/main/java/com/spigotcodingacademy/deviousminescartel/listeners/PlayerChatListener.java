package com.spigotcodingacademy.deviousminescartel.listeners;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.manager.PlayerData;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;

public class PlayerChatListener implements Listener {

    public String getPrefixSuffix(Player player, String type){
        if(player.hasMetadata(type)){
            return player.getMetadata(type).get(0).asString();
        }
        return "";
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String pn = player.getDisplayName();

        String prefix = getPrefixSuffix(player, "prefix");

        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);
        String cartelName = CartelData.getString("Player.Cartel");

        /*if (PlayerData.cartelChat.containsKey(player)) {
            event.setCancelled(true);

            for (Player p : PlayerData.cartelChat) {
                if (!DeviousMines.getCartelManager().getCartel(p).equals(DeviousMines.getCartelManager().getCartel(player))) {
                    return;
                } else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&cC&8] " + "&8[&b" + cartelName + "&8] &f" + pn + " &8&l>> &7" + event.getMessage()));
                    return;
                }
            }
            return;
        }*/

        if (!PlayerData.cartelChat.containsKey(player)) {

            if (!DeviousMines.getCartelManager().inCartel(player)) {
                event.setFormat(Chat.color( prefix + " &f" + pn + " &8&l>> &f" + event.getMessage()));
                return;
            }

            event.setFormat(Chat.color("&8[&6" + cartelName + "&8] " + prefix + " &f" + pn + " &8&l>> &f" + event.getMessage()));
        }
    }
}