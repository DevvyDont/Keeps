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
        FileConfiguration config = this.getConfig();
        config.addDefault("keeping-message", "&a{NUM_ITEMS} &6item(s) were retained from death!");
        config.addDefault("want-keeping-enchantment", true);
        config.addDefault("keeping-enchantment-table-chance", 20);
        config.addDefault("want-keeping-defaults", true);
        config.addDefault("keeping-default-items", List.of("BARRIER", "LIGHT", "COMMAND_BLOCK"));
        config.addDefault("want-death-certificates", true);
        this.saveDefaultConfig();
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
