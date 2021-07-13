package com.developer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerAuthenticationEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (AuthPlugin.database.getUserByUsername(player.getName()) != null) {
            player.sendMessage("Please login by using /login [password] command.");
        } else {
            player.sendMessage("Please register by using /register [email] [password] command.");
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

}
