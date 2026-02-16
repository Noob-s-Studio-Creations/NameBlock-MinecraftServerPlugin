package com.prayoadmii.userblock;

import org.bukkit.plugin.java.JavaPlugin;
import com.prayoadmii.userblock.listener.LoginListener;

public final class UserBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(
                new LoginListener(this), this
        );
        getLogger().info("UserBlock Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("UserBlock Disabled!");
    }
}
