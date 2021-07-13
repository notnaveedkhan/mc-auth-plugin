package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.models.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 2) {
                if (AuthPlugin.database.getUserByUsername(player.getName()) == null) {
                    User user = new User();
                    user.setUsername(player.getName());
                    user.setEmail(strings[0]);
                    user.setPassword(strings[1]);
                    AuthPlugin.database.save(user);
                    player.sendMessage("You are registered successfully.");
                } else {
                    player.sendMessage("[Error] User is already registered.");
                }
            } else {
                player.sendMessage("[Error] Invalid command syntax.");
            }
        }
        return true;
    }
}
