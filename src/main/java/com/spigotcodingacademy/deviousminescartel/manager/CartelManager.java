package com.spigotcodingacademy.deviousminescartel.manager;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CartelManager {

    private DeviousMines main;

    public CartelManager(DeviousMines main) {
        this.main = main;
    }

    public void genPlayer(Player player) {

        File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        if (!Players.exists()){
            try {
                Players.createNewFile();
                YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
                PlayerData.set("Player.UUID", player.getUniqueId().toString());
                PlayerData.set("Player.Name", player.getDisplayName());
                PlayerData.set("Player.Cartel", "");
                PlayerData.save(Players);
            } catch (IOException el){
                el.printStackTrace();
            }
        }
    }

    public boolean doesExist(String string) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");

        if (cartel.exists()) {
            return true;
        }
        return false;
    }

    public void disbandCartel(Player player, String string) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
        Chat.msg(player, Chat.prefix + "&b" + string + " &7has been deleted!");
        cartel.delete();

        try {
            File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
            YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
            PlayerData.set("Player.Cartel", "");
            PlayerData.save(Players);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public boolean isOwner(Player player, String string) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);

        if (CartelData.get("Cartel.Owner").equals(player.getUniqueId().toString())) {
            return true;
        }
        return false;
    }

    public void createCartel(Player player, String string) {

        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
        if (cartel.exists()) {
            Chat.msg(player, Chat.prefix + "&7Cartel already exists!");
        } else{
            try {
                cartel.createNewFile();
                YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);
                CartelData.set("Cartel.Name", string);
                CartelData.set("Cartel.Owner", player.getUniqueId().toString());
                CartelData.save(cartel);
            } catch (IOException el) {
                el.printStackTrace();
            }
        }

        File players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        if (players.exists()) {
            try{
                YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(players);
                PlayerData.set("Player.Cartel", string);
                PlayerData.save(players);
            } catch (IOException el){
                el.printStackTrace();
            }
        }

        Chat.msg(player, Chat.prefix + "&7Cartel &b" + string + " &7has been created!");
    }
}