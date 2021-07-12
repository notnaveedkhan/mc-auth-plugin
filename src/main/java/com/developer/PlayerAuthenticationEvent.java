package com.developer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerAuthenticationEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PluginPlayer pluginPlayer = new PluginPlayer();
        pluginPlayer.setName(player.getName());
        pluginPlayer.setLoggedIn(false);
        pluginPlayer.setGameMode(player.getGameMode());

        Location location = player.getLocation();
        pluginPlayer.setX(location.getX());
        pluginPlayer.setY(location.getY());
        pluginPlayer.setZ(location.getZ());

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.DEFAULT);
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1);
        armorStand.addPotionEffect(potionEffect);

        Bukkit.getScheduler().runTaskLater(AuthPlugin.plugin, () -> {
            player.setGameMode(GameMode.SPECTATOR);
            armorStand.setPassenger(player);
            armorStand.setGravity(false);
            armorStand.setInvisible(true);
            armorStand.setCustomName("auth-" + player.getName());
            player.setSpectatorTarget(armorStand);
            pluginPlayer.setArmorStand(armorStand);
            freeze(player);
        }, 10L);

        AuthPlugin.players.add(pluginPlayer);

        if (AuthPlugin.database.userExistByName(player.getName())) {
            player.sendMessage("Please login to continue your game. Use /login [password]");
        } else {
            player.sendMessage("Please register to continue your game. Use /register [password] [email]");
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        String entityName = event.getDismounted().getCustomName();
        Player player = ((Player) event.getEntity());
        String playerName = player.getName();
        if (entityName.equals("auth-" + playerName) && player.getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        String leavingPlayerName = player.getName();
        for (int i = 0; i < AuthPlugin.players.size(); i++) {
            if (leavingPlayerName.equals(AuthPlugin.players.get(i).getName())) {
                player.setGameMode(AuthPlugin.players.get(i).getGameMode());
                AuthPlugin.players.get(i).getArmorStand().removePassenger(player);
                AuthPlugin.players.get(i).getArmorStand().remove();
            }
        }
    }

    public static void freeze(Player player) {
        Bukkit.getScheduler().runTaskLater(AuthPlugin.plugin, () -> {
            for (int i = 0; i < AuthPlugin.players.size(); i++) {
                AuthPlugin.players.get(i).getArmorStand().setPassenger(player);
                player.setSpectatorTarget(AuthPlugin.players.get(i).getArmorStand());
            }
            if (player.getGameMode().compareTo(GameMode.SPECTATOR) == 0) {
                freeze(player);
            }
        }, 20L);
    }

}
