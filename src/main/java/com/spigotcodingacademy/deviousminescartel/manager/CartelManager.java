package com.spigotcodingacademy.deviousminescartel.manager;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
import com.spigotcodingacademy.deviousminescartel.utils.Delay;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
                PlayerData.set("Player.inCartel", "false");
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
            PlayerData.set("Player.inCartel", "false");
            PlayerData.save(Players);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public void leaveCartel(Player player) {
        try {
            File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
            File Cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
            YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
            YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartel);
            PlayerData.set("Player.Cartel", "");
            PlayerData.set("Player.inCartel", "false");

            List<String> members = CartelData.getStringList("Members");
            members.remove(player.getUniqueId().toString());
            CartelData.set("Members", members);

            CartelData.save(Cartel);
            PlayerData.save(Players);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Chat.msg(player, Chat.prefix + "&7Cartel left successfully!");
    }

    public void inviteCartel(Player player, String string) {
        Chat.msg(player, Chat.prefix + "&7Invitation accepted successfully!");
        try {
            File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
            File Cartels = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
            YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
            YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartels);
            PlayerData.set("Player.Cartel", string);
            PlayerData.set("Player.inCartel", "true");

            List<String> members = CartelData.getStringList("Members");
            members.add(player.getUniqueId().toString());
            CartelData.set("Members", members);

            PlayerData.save(Players);
            CartelData.save(Cartels);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String prefix = DeviousMines.getCartelManager().getCartel(player);
    }

    public boolean inCartel(Player player) {
        File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);

        if (PlayerData.get("Player.inCartel").equals("true")) {
            return true;
        }
        return false;
    }

    public boolean isOwner(Player player, String string) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);

        if (CartelData.get("Cartel.Owner").equals(player.getUniqueId().toString())) {
            return true;
        }
        return false;
    }

    public String getCartel(Player player) {
        File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
        String cartelName = PlayerData.get("Player.Cartel").toString();

        if (cartelName != null) {
            return cartelName;
        }
        return null;
    }

    public void setCartelLocation(Player player) {

        Location loc = player.getLocation();

        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
        if (!cartel.exists()) {
            return;
        } else{
            try{
                YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);
                CartelData.set("Cartel.Home.isSet", "true");
                CartelData.set("Cartel.Home.x", loc.getBlockX());
                CartelData.set("Cartel.Home.y", loc.getBlockY());
                CartelData.set("Cartel.Home.z", loc.getBlockZ());
                CartelData.set("Cartel.Home.Yaw", loc.getYaw());
                CartelData.set("Cartel.Home.Pitch", loc.getPitch());
                CartelData.save(cartel);
                Chat.msg(player, Chat.prefix + "&7Cartel home has been set!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean cartelHasHome(Player player) {

        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);

        if (CartelData.get("Cartel.Home.isSet").equals("true")) {
            return true;
        }
        return false;
    }

    public void teleportPlayerHome(Player player) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);
        double x = CartelData.getDouble("Cartel.Home.x");
        double y = CartelData.getDouble("Cartel.Home.y");
        double z = CartelData.getDouble("Cartel.Home.z");
        int yaw = CartelData.getInt("Cartel.Home.Yaw");
        int pitch = CartelData.getInt("Cartel.Home.Pitch");

        player.teleport(new Location(player.getWorld() ,x, y, z, yaw, pitch));

        PlayerData.homeCooldown.add(player);

        Delay.until(DeviousMines.getInstance().getConfig().getInt("homeDelay") * 1200, () -> PlayerData.homeCooldown.remove(player));
    }

    public String getOwner(Player player) {
        File Cartels = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
        YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartels);
        String uuid = CartelData.getString("Cartel.Owner");
        String cartelOwnerName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();

        return cartelOwnerName;
    }

    public void kickMember(Player player) {
        File Player = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        File Cartels = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", DeviousMines.getCartelManager().getCartel(player) + ".yml");
        try {
            Player.createNewFile();
            YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Player);
            YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartels);
            PlayerData.set("Player.inCartel", "false");
            PlayerData.set("Player.Cartel", "");

            List<String> members = CartelData.getStringList("Members");
            members.remove(player.getUniqueId().toString());
            CartelData.set("Members", members);

            CartelData.save(Cartels);
            PlayerData.save(Player);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Chat.msg(player, Chat.prefix + "&7You have been kicked from the cartel!");
    }

    public void createCartel(Player player, String string) {
        File cartel = new File(DeviousMines.getInstance().getDataFolder() + "/data/cartels", string + ".yml");
        File Players = new File(DeviousMines.getInstance().getDataFolder() + "/data/players", player.getUniqueId().toString() + ".yml");
        if (cartel.exists()) {
            Chat.msg(player, Chat.prefix + "&7Cartel already exists!");
        } else{
            try {
                cartel.createNewFile();
                YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(cartel);
                YamlConfiguration PlayerData = YamlConfiguration.loadConfiguration(Players);
                CartelData.set("Cartel.Name", string);
                CartelData.set("Cartel.Owner", player.getUniqueId().toString());
                CartelData.set("Cartel.Home.isSet", "false");
                PlayerData.set("Player.inCartel", "true");
                CartelData.set("Members", "");
                PlayerData.save(Players);
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

        Bukkit.broadcastMessage(Chat.color(Chat.prefix + "&7Cartel &b" + string + " &7has been created!"));
    }
}