package com.spigotcodingacademy.deviousminescartel.cmds;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.manager.CartelManager;
import com.spigotcodingacademy.deviousminescartel.manager.PlayerData;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
import com.spigotcodingacademy.deviousminescartel.utils.Delay;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

                    String cartelName = args[1];

                    if (DeviousMines.getCartelManager().doesExist(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel already exists!");
                        return true;
                    }

                    if (DeviousMines.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You are already in a cartel!");
                        return true;
                    }

                    if (args[1].length() > 8) {
                        Chat.msg(player, Chat.prefix + "&7Cartel name to long!");
                        return true;
                    }

                    if (args[1].length() < 2) {
                        Chat.msg(player, Chat.prefix + "&7Cartel name to small!");
                        return true;
                    }

                    DeviousMines.getCartelManager().createCartel(player, cartelName);

                    return true;
                }

                if (args[0].equalsIgnoreCase("sethome")) {
                    if (!DeviousMines.getCartelManager().inCartel(player)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7You must be in a Cartel to run this command!"
                        );
                        return true;
                    }

                    if (!DeviousMines.getCartelManager().isOwner(player, DeviousMines.getCartelManager().getCartel(player))) {
                        Chat.msg(player, Chat.prefix + "&7You must be the Cartel owner to run that command!");
                        return true;
                    }

                    DeviousMines.getCartelManager().setCartelLocation(player);

                    return true;
                }

                if (args[0].equalsIgnoreCase("home")) {

                    if (PlayerData.homeCooldown.contains(player)) {
                        Chat.msg(player, Chat.prefix + "&7Home cooldown in effect!");
                        return true;
                    }

                    if (!DeviousMines.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a Cartel to run this command!");
                        return true;
                    }

                    if (DeviousMines.getCartelManager().cartelHasHome(player)) {
                        DeviousMines.getCartelManager().teleportPlayerHome(player);
                    }
                }

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

                    if (!DeviousMines.getCartelManager().doesExist(disbandName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel does not exist!");
                        return true;
                    }

                    if (!DeviousMines.getCartelManager().isOwner(player, disbandName)) {
                        Chat.msg(player, Chat.prefix + "&7You must be the owner of the cartel to disband!");
                        return true;
                    }

                    if (DeviousMines.getCartelManager().isOwner(player, disbandName)) {
                        DeviousMines.getCartelManager().disbandCartel(player, disbandName);
                    }
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

                    if (!DeviousMines.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You must be in a cartel to run that command!");
                        return true;
                    }

                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target == null) {
                        Chat.msg(player, Chat.prefix + "&7Player is not online or doesn't exist!");
                        return true;
                    }

                    if (DeviousMines.getCartelManager().inCartel(target)) {
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

                    if (!PlayerData.invites.containsKey(target)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&b" + target.getName() + " &7has been invited to &b" + DeviousMines.getCartelManager().getCartel(player) + "&7!",
                                Chat.prefix + "&7Warning! Invite will timeout in 15 seconds!"
                                );
                        Chat.msg(target, Chat.prefix + "&7You have been invited to join &b" + DeviousMines.getCartelManager().getCartel(player) + "&7!");

                        PlayerData.invites.put(player, DeviousMines.getCartelManager().getCartel(player).toLowerCase());

                        Delay.until(300, () -> {

                            if (DeviousMines.getCartelManager().getCartel(target) == null) {
                                Chat.msg(
                                        player,
                                        Chat.prefix + "&7Invite timed out!"
                                );
                            }

                            PlayerData.invites.remove(player);
                        });
                        return true;
                    } else{
                        Chat.msg(player, Chat.prefix + "&7Please has already been invited!");
                    }

                    return true;
                }

                if (args[0].equalsIgnoreCase("leave")) {
                    if (!DeviousMines.getCartelManager().inCartel(player)) {
                        Chat.msg(player, Chat.prefix + "&7You are not in a cartel!");
                        return true;
                    }

                    String cartel = DeviousMines.getCartelManager().getCartel(player);
                    if (DeviousMines.getCartelManager().isOwner(player, cartel)) {
                        Chat.msg(
                                player,
                                Chat.prefix + "&7You must disband to leave to cartel!",
                                "&6&l* &7Try /cartel disband <name>"
                        );
                        return true;
                    }

                    DeviousMines.getCartelManager().leaveCartel(player);
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

                    String cartelName = args[1].toLowerCase();

                    if (!PlayerData.invites.containsValue(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7No invites found!");
                        return true;
                    }

                    if (!DeviousMines.getCartelManager().doesExist(cartelName)) {
                        Chat.msg(player, Chat.prefix + "&7Cartel does not exist!");
                        return true;
                    }

                    DeviousMines.getCartelManager().inviteCartel(player, DeviousMines.getCartelManager().getCartel(player));
                    PlayerData.invites.remove(player);
                }
            }
        }
        return false;
    }
}
