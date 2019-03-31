package com.blitzcentral.cartels.cmds;

import com.blitzcentral.cartels.Cartels;
import com.blitzcentral.cartels.manager.PlayerData;
import com.blitzcentral.cartels.utils.Chat;
import com.blitzcentral.cartels.utils.Delay;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class CartelCmds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("cartel")) {
            if (args.length == 0) {
                Chat.msg(
                        player,
                        Chat.prefix + "Cartel Commands:",
                        "&6&l* &7/cartel create <name>",
                        "&6&l* &7/cartel disband <name>",
                        "&6&l* &7/cartel invite <player>",
                        "&6&l* &7/cartel join <name>",
                        "&6&l* &7/cartel kick <player>",
                        "&6&l* &7/cartel list",
                        "&6&l* &7/cartel leave",
                        "&6&l* &7/cartel home",
                        "&6&l* &7/cartel sethome"
                );
                return true;
            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length < 2) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please enter a cartel name!",
                                "&6&l* &7Try /cartel create <name>"
                        );
                        return true;
                    }

                    if (PlayerData.createCooldown.contains(player.getUniqueId())) {
                        Chat.msg(player, Chat.prefix + "&7Cooldown in affect! Try again later.");
                        return true;
                    }

                    String cartelName = args[1];

                    if (Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You are already in a cartel!");
                        return true;
                    }

                    if (Cartels.getCartelManager().doesExist(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel already exists!");
                        return true;
                    }

                    if (args[1].length() > 11) {
                        Chat.msg(player, Chat.prefix + "&7Cartel name to long!");
                        return true;
                    }

                    if (args[1].length() < 2) {
                        Chat.msg(player, Chat.prefix + "&7Cartel name to small!");
                        return true;
                    }

                    Cartels.getCartelManager().createCartel(player, cartelName);

                    PlayerData.createCooldown.add(player.getUniqueId());

                    Delay.until(6000, () -> PlayerData.createCooldown.remove(player.getUniqueId()));

                    return true;
                }

                if (args[0].equalsIgnoreCase("sethome")) {
                    if (PlayerData.createhomeCooldown.contains(player)) {
                        Chat.msg(player, Chat.prefix + "&7Cooldown in affect! Try again later.");
                    }

                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7You must be in a Cartel to run this command!"
                        );
                        return true;
                    }

                    if (!Cartels.getCartelManager().isOwner(player, Cartels.getCartelManager().getCartel(player))) {
                        Chat.msg(player, Chat.prefix + "&7You must be the Cartel owner to run that command!");
                        return true;
                    }

                    Cartels.getCartelManager().setCartelLocation(player);

                    PlayerData.createhomeCooldown.add(player);

                    Delay.until(3000, () -> PlayerData.createhomeCooldown.remove(player));

                    return true;
                }

                if (args[0].equalsIgnoreCase("home")) {

                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a Cartel to run this command!");
                        return true;
                    }

                    if (!Cartels.getCartelManager().cartelHasHome(player)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel has no home!");
                        return true;
                    }

                    if (PlayerData.homeCooldown.contains(player)) {
                        Chat.msg(player, Chat.prefix + "&7Home cooldown in effect!");
                        return true;
                    }

                    Chat.msg(player, Chat.prefix + "Teleporting to Cartel home!");
                    Cartels.getCartelManager().teleportPlayerHome(player);
                    return true;
                }

                /*if (args[0].equalsIgnoreCase("chat")) {
                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a Cartel to run this command!");

                        return true;
                    }

                    if (PlayerData.cartelChat.containsKey(player)) {
                        PlayerData.cartelChat.remove(player);
                        Chat.msg(player, Chat.prefix + "&7Cartel chat Disabled!");

                        return true;
                    } else {
                        PlayerData.cartelChat.put(player, Cartels.getCartelManager().getCartel(player));
                        Chat.msg(player, Chat.prefix + "&7Cartel chat Enabled!");

                        return true;
                    }
                }*/

                if (args[0].equalsIgnoreCase("disband")) {
                    if (args.length < 2) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please enter your cartel name!",
                                "&6&l* &7Try /cartel disband <name>"
                        );
                        return true;
                    }

                    String disbandName = args[1];

                    if (!Cartels.getCartelManager().doesExist(disbandName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel does not exist!");
                        return true;
                    }

                    if (!Cartels.getCartelManager().isOwner(player, disbandName)) {
                        Chat.msg(player, Chat.prefix + "&7You must be the owner of the cartel to disband!");
                        return true;
                    }

                    if (Cartels.getCartelManager().isOwner(player, disbandName)) {
                        Cartels.getCartelManager().disbandCartel(player, disbandName);
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length < 2) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please select a player!",
                                "&6&l* &7Try /cartel kick <player>"
                        );
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);

                    if (!Cartels.getCartelManager().isOwner(player, Cartels.getCartelManager().getCartel(player))) {
                        Chat.msg(player, Chat.prefix + "&7You must be the Cartel owner to run this command!");
                        return true;
                    }

                    if (target == null) {
                        Chat.msg(player, Chat.prefix + "&7Player selected does not exist or is not online!");
                        return true;
                    }

                    if (target == player) {
                        Chat.msg(player, Chat.prefix + "&7You can not kick yourself!");
                        return true;
                    }

                    if (!Cartels.getCartelManager().getCartel(player).equals(Cartels.getCartelManager().getCartel(target))) {
                        Chat.msg(player, Chat.prefix + "&7Player not found in your cartel!");
                        return true;
                    }

                    Cartels.getCartelManager().kickMember(target);
                    return true;
                }

                if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length < 2) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please select a player!",
                                "&6&l* &7Try /cartel invite <player>"
                        );
                        return true;
                    }

                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a cartel to run that command!");
                        return true;
                    }

                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Chat.msg(player, Chat.prefix + "&7Player is not online or doesn't exist!");
                        return true;
                    }

                    if (Cartels.getCartelManager().inCartel(target)) {
                        Chat.msg(player, Chat.prefix + "&b" + target.getName() + " &7is already in a cartel!");
                        return true;
                    }

                    if (target == player) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please select a player!",
                                "&6&l* &7Try /cartel invite <player>"
                        );
                        return true;
                    }

                    if (PlayerData.invites.containsKey(target)) {
                        Chat.msg(player, Chat.prefix + "&7Player has already been invited!");
                        return true;
                    }

                    if (!PlayerData.invites.containsKey(target)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&b" + target.getName() + " &7has been invited to &b" + Cartels.getCartelManager().getCartel(player) + "&7!",
                                Chat.prefix + "&7Warning! Invite will timeout in 30 seconds!"
                                );
                        Chat.msg(target, Chat.prefix + "&7You have been invited to join &b" + Cartels.getCartelManager().getCartel(player) + "&7!");
                        Chat.msg(target, Chat.prefix + "&7To accept do:&b /cartel join " + Cartels.getCartelManager().getCartel(player));

                        PlayerData.invites.put(target, Cartels.getCartelManager().getCartel(player));

                        Delay.until(600, () -> {
                            if (!Cartels.getCartelManager().inCartel(target)) {
                                Chat.msg(
                                        player,
                                        Chat.prefix + "&7Cartel invite timed out."
                                );
                                Chat.msg(target, Chat.prefix + "&7Cartel invite timed out.");
                                PlayerData.invites.remove(target, Cartels.getCartelManager().getCartel(player));
                            }

                            PlayerData.invites.remove(target, Cartels.getCartelManager().getCartel(player));
                        });
                        return true;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("leave")) {
                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You are not in a cartel!");
                        return true;
                    }

                    String cartel = Cartels.getCartelManager().getCartel(player);
                    if (Cartels.getCartelManager().isOwner(player, cartel)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7You are the owner of the cartel!",
                                "&6&l* &7Try /cartel disband <name>"
                        );
                        return true;
                    }

                    Cartels.getCartelManager().leaveCartel(player);
                    return true;

                }

                if (args[0].equalsIgnoreCase("join")) {
                    if (args.length < 2) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7Please enter the cartel name!",
                                "&6&l* &7Try /cartel join <name>"
                        );
                        return true;
                    }

                    String cartelName = args[1];

                    if (!PlayerData.invites.containsValue(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7No invites found!");
                        return true;
                    }

                    if (!Cartels.getCartelManager().doesExist(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel does not exist!");
                        return true;
                    }

                    Cartels.getCartelManager().inviteCartel(player, cartelName);
                    PlayerData.invites.remove(player);
                }

                if (args[0].equalsIgnoreCase("list")) {

                    if (!Cartels.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a cartel to run that command!");
                        return true;
                    }

                    File Cartels = new File(com.blitzcentral.cartels.Cartels.getInstance().getDataFolder() + "/data/cartels", com.blitzcentral.cartels.Cartels.getCartelManager().getCartel(player) + ".yml");
                    YamlConfiguration CartelData = YamlConfiguration.loadConfiguration(Cartels);

                    List<String> members = CartelData.getStringList("Members");
                    Chat.msg(
                            player,
                            Chat.prefix + "&7Cartel Members for &b" + com.blitzcentral.cartels.Cartels.getCartelManager().getCartel(player) + "&7:",
                            "&6* &b" + com.blitzcentral.cartels.Cartels.getCartelManager().getOwner(player)
                    );

                    for (String name : members) {
                        Chat.msg(player, "&6* &7" + Bukkit.getOfflinePlayer(UUID.fromString(name)).getName());
                    }

                    return true;
                }
            }
        }
        return false;
    }
}