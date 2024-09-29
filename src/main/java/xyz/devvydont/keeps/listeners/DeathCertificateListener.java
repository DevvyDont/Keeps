package xyz.devvydont.keeps.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.keeps.Keeps;
import xyz.devvydont.keeps.util.ComponentUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeathCertificateListener implements Listener {

    private Component getEnvironmentComponent(World.Environment environment) {

        return switch (environment) {
            case THE_END -> Component.text("The End", NamedTextColor.LIGHT_PURPLE);
            case NETHER -> Component.text("the ", NamedTextColor.GRAY).append(Component.text("Nether", NamedTextColor.RED));
            case NORMAL -> Component.text("the ", NamedTextColor.GRAY).append(Component.text("Overworld", NamedTextColor.DARK_GREEN));
            case CUSTOM -> Component.text("Some unknown world", NamedTextColor.LIGHT_PURPLE);
        };

    }

    /*
     * When a player respawns, "award" them a death certificate to alert them where their items are at.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerPostRespawnEvent event) {

        // Are death certificates enabled?
        if (!Keeps.getInstance().getConfig().getBoolean("want-death-certificates", true))
            return;

        // Has the player not died yet somehow?
        if (event.getPlayer().getLastDeathLocation() == null)
            return;

        // Create a piece of paper with information on it and add it to their inventory
        ItemStack paper = ItemType.PAPER.createItemStack(meta -> {
            meta.displayName(
                    Component.text("Death Certificate", NamedTextColor.RED)
                            .append(Component.text(String.format(" (%s)", event.getPlayer().getName()), NamedTextColor.DARK_GRAY)).decoration(TextDecoration.ITALIC, false)
            );

            long timestamp = System.currentTimeMillis();
            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateString = formatter.format(date);
            Location deathLocation = event.getPlayer().getLastDeathLocation();
            meta.lore(ComponentUtils.cleanItalics(List.of(
                    Component.empty(),
                    Component.text(event.getPlayer().getName(), NamedTextColor.AQUA).append(Component.text(" died in ", NamedTextColor.GRAY)).append(getEnvironmentComponent(deathLocation.getWorld().getEnvironment())),
                    Component.text("Coordinates: ", NamedTextColor.GRAY).append(Component.text(String.format("%s %s %s", deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ()), NamedTextColor.BLUE)),
                    Component.empty(),
                    Component.text("Death occurred at: " + dateString + "EST", NamedTextColor.DARK_GRAY)
            )));
            meta.setEnchantmentGlintOverride(true);
        });

        event.getPlayer().getInventory().addItem(paper);
    }

}
