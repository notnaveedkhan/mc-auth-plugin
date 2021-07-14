package com.developer;

import com.developer.models.Detail;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        player.teleport(AuthPlugin.authWorld.getSpawnLocation());
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().getWorld().getName().equals("auth-world")) {
            Player player = event.getPlayer();
            Location location = player.getLocation();

            Detail detail = new Detail();
            for (int i = 0; i < AuthPlugin.users.size(); i++) {
                if (player.getName().equals(AuthPlugin.users.get(i).getUsername())) {
                    detail.setUserId(AuthPlugin.users.get(i).getId());
                }
            }
            detail.setWorldName(location.getWorld().getName());
            detail.setX(location.getX());
            detail.setY(location.getY());
            detail.setZ(location.getZ());
            AuthPlugin.database.update(detail);
        }
    }

}
