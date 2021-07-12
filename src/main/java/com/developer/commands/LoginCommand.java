package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.models.User;
import org.bukkit.Bukkit;
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
            Player player = (((Player) commandSender).getPlayer());
            if (strings.length == 1) {
                String password = strings[0];
                User user = AuthPlugin.database.getUserByName(player.getName());
                if (user == null) {
                    player.sendMessage("You are not registered yet. Please use /register command.");
                } else {
                    if (user.getPassword().equals(password)) {
                        for (int i = 0; i < AuthPlugin.players.size(); i++) {
                            if (AuthPlugin.players.get(i).getName().equals(player.getName())) {

                                player.setGameMode(AuthPlugin.players.get(i).getGameMode());
                                AuthPlugin.players.get(i).getArmorStand().removePassenger(player);
                                AuthPlugin.players.get(i).getArmorStand().remove();

                                Location location = player.getLocation();
                                location.setX(AuthPlugin.players.get(i).getX());
                                location.setY(AuthPlugin.players.get(i).getY());
                                location.setZ(AuthPlugin.players.get(i).getZ());
                                player.teleport(location);

                                AuthPlugin.players.remove(i);
                                player.sendMessage("You are logged in.");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage("[Error] Incorrect password.");
                    }
                }
            } else {
                player.sendMessage("[Error] /login [password]");
            }
        }
        return true;
    }
}
