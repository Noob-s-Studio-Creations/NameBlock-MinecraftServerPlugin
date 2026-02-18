package com.prayoadmii.nameblock;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class NameBlock extends JavaPlugin implements Listener {

    private List<String> blockedNames;
    private List<String> brBlockedNames;
    private boolean useRegex;
    private boolean floodgateBypass;
    private Component kickComponent;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage(
            Component.text("NameBlock Was Started!", NamedTextColor.GREEN)
        );
    }

    private boolean checkBlocked(String playerName, List<String> patterns) {
        for (String blocked : patterns) {

            if (useRegex) {
                if (Pattern.compile(blocked, Pattern.CASE_INSENSITIVE)
                        .matcher(playerName)
                        .find()) {
                    return true;
                }
            } else {
                if (playerName.toLowerCase().contains(blocked.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();

        blockedNames = config.getStringList("blocked-names");
        brBlockedNames = config.getStringList("br-blocked-name");

        useRegex = config.getBoolean("use-regex");
        floodgateBypass = config.getBoolean("floodgate-bypass");

        String raw = config.getString("kick-message");

        kickComponent = LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(raw);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        String playerName = event.getName();
        UUID uuid = event.getUniqueId();

        boolean isFloodgate = isFloodgatePlayer(uuid);

        if (floodgateBypass && isFloodgate) {
            if (checkBlocked(playerName, brBlockedNames)) {
                event.disallow(
                        AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        kickComponent
                );
            }
            return;
        }

        if (checkBlocked(playerName, blockedNames)) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    kickComponent
            );
        }
    }

    private boolean isFloodgatePlayer(UUID uuid) {
        return Bukkit.getPluginManager().isPluginEnabled("Floodgate")
                && uuid.toString().startsWith("00000000-0000-0000");
    }
}
