package com.developer;

import com.developer.commands.ForgotCommand;
import com.developer.commands.LoginCommand;
import com.developer.commands.RegisterCommand;
import com.developer.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AuthPlugin extends JavaPlugin {

    public static AuthPlugin plugin;
    public static List<PluginPlayer> players = new ArrayList<>();
    public static Database database = new Database();

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayerAuthenticationEvent(), this);
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("forgot").setExecutor(new ForgotCommand());
    }

    @Override
    public void onDisable() {
    }

}
