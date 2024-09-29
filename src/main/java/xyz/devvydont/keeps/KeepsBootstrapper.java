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
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.keeps.commands.KeepsCommand;
import xyz.devvydont.keeps.util.EnchantmentUtils;

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

        // Bootstrap the commands we want.
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    "keeps",
                    new KeepsCommand()
            );
        });

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

        // This will make the blessing of keeping able to be obtained via villager trading.
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {
            event.registrar().addToTag(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("tradeable")), List.of(EnchantmentUtils.KEEPING_BLESSING));
        }));

    }
}
