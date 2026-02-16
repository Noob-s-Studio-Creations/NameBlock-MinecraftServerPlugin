package com.prayoadmii.userblock.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginListener implements Listener {

    private final JavaPlugin plugin;

    public LoginListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        boolean isFloodgate = false;

        try {
            Class<?> apiClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object api = apiClass.getMethod("getInstance").invoke(null);
            isFloodgate = (boolean) apiClass
                    .getMethod("isFloodgatePlayer", java.util.UUID.class)
                    .invoke(api, event.getPlayer().getUniqueId());
        } catch (Exception e) {
            plugin.getLogger().warning("Floodgate Is Not Installed On Your Server Or Not Accessible...");
        }

        if (isFloodgate) {
            event.getPlayer().sendMessage("You Are A Bedrock Player!");
        }
    }
}
