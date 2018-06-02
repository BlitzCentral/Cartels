package com.spigotcodingacademy.deviousminescartel.cmds;

import com.spigotcodingacademy.deviousminescartel.DeviousMines;
import com.spigotcodingacademy.deviousminescartel.utils.Chat;
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
                        "&6&l* &7/cartel info",
                        "&6&l* &7/cartel create <name>",
                        "&6&l* &7/cartel disband <name>",
                        "&6&l* &7/cartel invite <player>"
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
            }
        }
        return false;
    }
}
