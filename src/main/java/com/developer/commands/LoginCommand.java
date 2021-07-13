package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.models.User;
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
