package com.developer;

import com.developer.commands.LoginCommand;
import com.developer.commands.RegisterCommand;
import com.developer.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthPlugin extends JavaPlugin {

    public static AuthPlugin plugin;
    public static Database database;

    @Override
    public void onEnable() {
        database = new Database();
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayerAuthenticationEvent(), this);
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());
    }

    @Override
    public void onDisable() {
    }

}
