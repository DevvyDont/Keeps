package xyz.devvydont.keeps.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.keeps.Keeps;
import xyz.devvydont.keeps.util.EnchantmentUtils;

public class KeepingListener implements Listener {

    /**
     * Check whether an item is valid for keeping on death. This is either from config or if it
     * is enchanted with the blessing of keeping.
     *
     * @param item Some itemstack to check
     * @return true if it should be kept on death, false otherwise
     */
    private boolean shouldKeepItemOnDeath(ItemStack item) {
        return (Keeps.getInstance().getConfig().getBoolean("want-keeping-enchantment", true) && EnchantmentUtils.hasKeepingBlessing(item)) ||
                (Keeps.getInstance().getConfig().getBoolean("want-keeping-defaults", true) && Keeps.getInstance().getConfig().getStringList("keeping-default-items").contains(item.getType().toString()));
    }

    /*
     * When a player dies, consider any items we should keep whether it is the blessing of keeping
     * enchantment or it is hard set to retain on death in the config.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDiedWithKeepingItem(PlayerDeathEvent event) {

        int numItems = 0;

        // Loop through every item in the drops. If it is enchanted with our blessing, remove it from the drops
        // and set it as an item to keep.
        for (ItemStack drop : event.getDrops().stream().toList()) {

            // Should we keep it?
            if (!shouldKeepItemOnDeath(drop))
                continue;

            // Remove from the drops and set as a keep item
            event.getDrops().remove(drop);
            event.getItemsToKeep().add(drop);
            numItems += drop.getAmount();
        }

        String message = Keeps.getInstance().getConfig().getString("keeping-message", "");
        if (message.isEmpty() || numItems == 0)
            return;

        // Replace placeholders
        message = message.replace("{NUM_ITEMS}", String.valueOf(numItems));
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
