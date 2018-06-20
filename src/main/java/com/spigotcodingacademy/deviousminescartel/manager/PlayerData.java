package com.spigotcodingacademy.deviousminescartel.manager;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    public static HashMap<Player, String> invites = new HashMap<>();

    public static ArrayList<Player> homeCooldown = new ArrayList<>();
    public static ArrayList<UUID> createCooldown = new ArrayList<>();

}
