package com.developer;

import com.developer.commands.LoginCommand;
import com.developer.commands.RegisterCommand;
import com.developer.database.Database;
import com.developer.models.User;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AuthPlugin extends JavaPlugin {

    public static AuthPlugin plugin;
    public static Database database;
    public static List<User> users;
    public static World authWorld;

    @Override
    public void onEnable() {
        users = new ArrayList<>();
        authWorld = createVirtualWorld();

        database = new Database();
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerAuthenticationEvent(), this);
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());
    }

    private World createVirtualWorld() {
        WorldCreator creator = new WorldCreator("auth-world");
        creator.type(WorldType.FLAT);
        creator.generatorSettings("{\"structures\": {\"structures\": {\"village\": {\"salt\": 8015723, \"spacing\": 32, \"separation\": 8}}},\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"plains\"}");
        creator.generateStructures(false);
        return creator.createWorld();
    }

    @Override
    public void onDisable() {
    }

    public static World getWorld(String name) {
        return plugin.getServer().getWorld(name);
    }

}
