package xyz.devvydont.keeps.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class EnchantmentUtils {

    // A way to reference the keeping of blessing enchantment. Used during registration and querying.
    public static final TypedKey<Enchantment> KEEPING_BLESSING = TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("keeps", "blessing"));

    /**
     * Helper method to get the keeping enchantment. Keep in mind that this is dangerous if the enchantment has not been
     * registered yet, and can be null.
     * @return The keeping enchantment
     */
    @Nullable
    public static Enchantment getKeepingEnchantment() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(KEEPING_BLESSING);
    }

    /**
     * Helper method to determine if an item has the blessing of keeping enchantment.
     * @param itemStack The item to check
     * @return true if it is enchanted with keeping of blessing, false otherwise
     */
    public static boolean hasKeepingBlessing(ItemStack itemStack) {
        try {
            return itemStack.containsEnchantment(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(KEEPING_BLESSING));
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

}
