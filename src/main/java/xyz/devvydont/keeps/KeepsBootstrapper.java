package xyz.devvydont.keeps;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.keeps.commands.KeepsCommand;
import xyz.devvydont.keeps.util.EnchantmentUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class responsible for bootstrapping the plugin for implementing commands and enchantments.
 */
public class KeepsBootstrapper implements PluginBootstrap {

    /*
     * Called by Paper to bootstrap the plugin.
     * @param context the server provided context
     */
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        // Primitively load the configuration and attempt to read certain config files that we need for bootstrapping.
        FileConfiguration cfg = new YamlConfiguration();
        // If we fail to load the config, it is no big deal. Just means we can't use the config values defined.
        try {
            cfg.load(new File("./plugins/Keeps/config.yml"));
        } catch (IOException | InvalidConfigurationException ignored) {
            ignored.printStackTrace();
        }

        // Bootstrap the commands we want.
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    "keeps",
                    new KeepsCommand()
            );
        });

        // If the config is defined to not want the enchantment whatsoever, we can stop here. No need for registration.
        if (!cfg.getBoolean("want-keeping-enchantment", true))
            return;

        // Bootstrap the blessing of keeping enchantment into the server.
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            event.registry().register(
                    EnchantmentUtils.KEEPING_BLESSING,
                    b -> b.description(Component.text("Blessing of Keeping").color(NamedTextColor.YELLOW))
                            .primaryItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_VANISHING))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_VANISHING))
                            .anvilCost(1)
                            .maxLevel(1)
                            .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 2))
                            .activeSlots(EquipmentSlotGroup.ANY)
                            .exclusiveWith(RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.VANISHING_CURSE, EnchantmentKeys.BINDING_CURSE))
            );
        }));

        // Consider various ways that the blessing of keeping should be obtainable using the config.
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {

            if (cfg.getBoolean("keeping-enchantment-villager-trading", true)) {
                event.registrar().addToTag(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("tradeable")), List.of(EnchantmentUtils.KEEPING_BLESSING));
                event.registrar().addToTag(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("on_traded_equipment")), List.of(EnchantmentUtils.KEEPING_BLESSING));
            }

            if (cfg.getBoolean("keeping-enchantment-random-loot", true))
                event.registrar().addToTag(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("on_random_loot")), List.of(EnchantmentUtils.KEEPING_BLESSING));
        }));

    }
}
