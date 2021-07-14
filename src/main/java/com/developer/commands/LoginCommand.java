package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.models.Detail;
import com.developer.models.User;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class LoginCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                if (AuthPlugin.database.getUserByUsername(player.getName()) != null) {
                    String password = strings[0];
                    User user = AuthPlugin.database.getUserByUsername(player.getName());
                    if (user.getPassword().equals(password)) {
                        Detail detail = AuthPlugin.database.getDetailByUserId(user.getId());
                        if (detail == null) {
                            player.sendMessage("[Error] Your login details are not saved. Please contact admins.");
                        } else {
                            Location location = new Location( AuthPlugin.getWorld(detail.getWorldName()), detail.getX(), detail.getY(), detail.getZ());
                            player.teleport(location);
                            player.setGameMode(GameMode.SURVIVAL);
                        }
                        AuthPlugin.users.add(user);
                        player.sendMessage("Logged In successfully.");
                    } else {
                        player.sendMessage("[Error] Incorrect password.");
                    }
                } else {
                    player.sendMessage("[Error] User is not registered.");
                }
            } else {
                player.sendMessage("[Error] Invalid command syntax.");
            }
        }
        return true;
    }
}
