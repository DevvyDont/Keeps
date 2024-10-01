package xyz.devvydont.keeps.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import xyz.devvydont.keeps.Keeps;
import xyz.devvydont.keeps.util.EnchantmentUtils;

import java.util.Random;

public class EnchantingListener implements Listener {

    /*
     * When an item is enchanted, we should consider rolling the keeping enchantment on top of the enchantments
     * that are already being rolled. This is affected by the config options for the chance and whether or not it is enabled.
     */
    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {

        // If the enchantment is disabled, ignore
        if (!Keeps.getInstance().getConfig().getBoolean("want-keeping-enchantment", true))
            return;

        Enchantment keeping = EnchantmentUtils.getKeepingEnchantment();
        if (keeping == null)
            return;

        // Get the chance. If it is 0, ignore
        int chance = Keeps.getInstance().getConfig().getInt("keeping-enchantment-table-chance", 20);
        if (chance <= 0)
            return;

        // Roll for if we should apply
        if (new Random().nextInt(100) > chance)
            return;

        // Add the enchantment
        event.getEnchantsToAdd().put(keeping, 1);
    }

}
