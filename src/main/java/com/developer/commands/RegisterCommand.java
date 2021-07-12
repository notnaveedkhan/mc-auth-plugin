package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.PluginPlayer;
import com.developer.models.User;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (((Player) commandSender).getPlayer());
            if (strings.length == 2) {
                List<PluginPlayer> pluginPlayers = AuthPlugin.players;
                String commandSenderName = player.getName();
                String password = strings[0];
                String email = strings[1];

                for (int i = 0; i < pluginPlayers.size(); i++) {
                    if (AuthPlugin.players.get(i).getName().equals(commandSenderName)) {
                        if (!AuthPlugin.database.userExistByName(commandSenderName)) {
                            AuthPlugin.database.save(new User(commandSenderName, email, password));

                            player.setGameMode(AuthPlugin.players.get(i).getGameMode());

                            AuthPlugin.players.get(i).getArmorStand().removePassenger(player);
                            AuthPlugin.players.get(i).getArmorStand().remove();

                            Location location = player.getLocation();
                            location.setX(AuthPlugin.players.get(i).getX());
                            location.setY(AuthPlugin.players.get(i).getY());
                            location.setZ(AuthPlugin.players.get(i).getZ());
                            player.teleport(location);

                            AuthPlugin.players.remove(i);

                            player.sendMessage("You are now registered.");
                            return true;
                        }
                        player.sendMessage("You are already registered.");
                        return true;
                    }
                }
                player.sendMessage("You are already authorized.");
            } else {
                player.sendMessage("[Error] /register [password] [email]");
            }
        }
        return true;
    }
}
