package com.prayoadmii.nameblock;

// Imports :P
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.net.URL;
import java.net.URI;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private boolean alertUpdate;

    // When Plugin Was Enabled
    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getConsoleSender().sendMessage(
            Component.text("[NameBlock] NameBlock Was Started!", NamedTextColor.GREEN)
        );

        if (alertUpdate) {
            checkForUpdates();
        }

        if (floodgateBypass && !Bukkit.getPluginManager().isPluginEnabled("Floodgate")) {
            getLogger().warning("Bro What? Floodgate Is Not Installed But floodgate-bypass Is Enabled???");
        }
    }

    // Check Did The Players Name Was Blocked?
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

    // Loads All Configs
    private void loadConfigValues() {
        FileConfiguration config = getConfig();

        blockedNames = config.getStringList("blocked-names");
        brBlockedNames = config.getStringList("br-blocked-name");

        useRegex = config.getBoolean("use-regex");
        floodgateBypass = config.getBoolean("floodgate-bypass");

        alertUpdate = config.getBoolean("alert-update");

        String raw = config.getString("kick-message");

        kickComponent = LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(raw);
    }

    // On Player Join :>
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

    // Floodgate Checks o_0
    private boolean isFloodgatePlayer(UUID uuid) {
        return Bukkit.getPluginManager().isPluginEnabled("Floodgate")
                && uuid.toString().startsWith("00000000-0000-0000");
    }

    // 1.0.3+ Check For Updates!
    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {

            try {

                URL url = URI.create("https://api.modrinth.com/v2/project/x5JA2QRT/version").toURL();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                StringBuilder jsonText = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonText.append(line);
                }

                reader.close();

                JSONArray versions = new JSONArray(jsonText.toString());
                JSONObject latest = versions.getJSONObject(0);

                String latestVersion = latest.getString("version_number");
                String currentVersion = getPluginMeta().getVersion();

                if (!latestVersion.equalsIgnoreCase(currentVersion)) {
                    Bukkit.getConsoleSender().sendMessage(
                        Component.text("[NameBlock] Update Available!", NamedTextColor.YELLOW)
                    );

                    Bukkit.getConsoleSender().sendMessage(
                        Component.text("Current: " + currentVersion + " | Latest: " + latestVersion, NamedTextColor.GRAY)
                    );

                    Bukkit.getConsoleSender().sendMessage(
                        Component.text("Please Download At: https://modrinth.com/plugin/name-block", NamedTextColor.AQUA)
                    );
                }

            } catch (Exception e) {
                getLogger().warning("Failed To Check For Updates");
            }

        });

    }

    // Commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("nameblock")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(
                Component.text("NameBlock Commands:", NamedTextColor.YELLOW)
            );
            sender.sendMessage(
                Component.text("/nameblock reload", NamedTextColor.GRAY)
            );
            sender.sendMessage(
                Component.text("/nameblock updates", NamedTextColor.GRAY)
            );
            return true;
        }

        // /nameblock reload
        if (args[0].equalsIgnoreCase("reload")) {

            reloadConfig();
            loadConfigValues();

            sender.sendMessage(
                Component.text("[NameBlock] Configs Was Reloaded!", NamedTextColor.GREEN)
            );

            return true;
        }

        // nameblock updates
        if (args[0].equalsIgnoreCase("updates")) {

            sender.sendMessage(
                Component.text("[NameBlock] Checking For Updates... Please See Console For Resault", NamedTextColor.YELLOW)
            );

            checkForUpdates();

            return true;
        }

        sender.sendMessage(
            Component.text("Unknown Subcommand!", NamedTextColor.RED)
        );

        return true;
    }
}