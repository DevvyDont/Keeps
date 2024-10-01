package xyz.devvydont.keeps;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devvydont.keeps.listeners.DeathCertificateListener;
import xyz.devvydont.keeps.listeners.EnchantingListener;
import xyz.devvydont.keeps.listeners.KeepingListener;

import java.util.List;

public final class Keeps extends JavaPlugin {

    public static Keeps INSTANCE = null;

    public static Keeps getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        // Config handling
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);

        // Register events
        this.getServer().getPluginManager().registerEvents(new KeepingListener(), this);
        this.getServer().getPluginManager().registerEvents(new EnchantingListener(), this);
        this.getServer().getPluginManager().registerEvents(new DeathCertificateListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
