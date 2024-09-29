package xyz.devvydont.keeps.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.keeps.Keeps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Class responsible for the main command for admins to tweak the plugin. Admins can either use these commands, or
 * edit the config directly.
 */
public class KeepsCommand implements BasicCommand {

    public static final String[] ARGS = new String[]{"reload", "adddefault", "removedefault", "setenchantchance", "toggledefaults", "toggleenchant", "toggledeathcertificate"};

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        if (strings.length <= 0) {
            commandSourceStack.getSender().sendMessage(Component.text("/keeps <reload | adddefault | removedefault | setenchantchance | toggledefaults | toggleenchant | toggledeathcertificate>").color(NamedTextColor.RED));
            return;
        }

        switch (strings[0]) {

            case "reload":
                Keeps.getInstance().reloadConfig();
                commandSourceStack.getSender().sendMessage(Component.text("Reload the Keeps config!").color(NamedTextColor.GREEN));
                break;

            case "adddefault":
                List<String> materials = Keeps.getInstance().getConfig().getStringList("keeping-default-items");

                try {
                    Material material = Material.valueOf(strings[1].toUpperCase());
                    if (materials.contains(material.toString().toUpperCase())) {
                        commandSourceStack.getSender().sendMessage(Component.text("This material is already set to drop by default! No need to do anything :p", NamedTextColor.RED));
                        return;
                    }
                    materials.add(material.toString().toUpperCase());
                    Keeps.getInstance().getConfig().set("keeping-default-items", materials);
                    Keeps.getInstance().saveConfig();
                    Keeps.getInstance().reloadConfig();
                    commandSourceStack.getSender().sendMessage(Component.text("Added the " + material + " material to the list of default materials!").color(NamedTextColor.GREEN));

                } catch (IllegalArgumentException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Invalid material provided. Try again.", NamedTextColor.RED));
                    return;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Please provide a material!").color(NamedTextColor.RED));
                    return;
                }

                break;

            case "removedefault":

                List<String> alreadyDefinedMaterials = Keeps.getInstance().getConfig().getStringList("keeping-default-items");

                try {
                    Material material = Material.valueOf(strings[1].toUpperCase());
                    alreadyDefinedMaterials.remove(material.toString().toUpperCase());
                    Keeps.getInstance().getConfig().set("keeping-default-items", alreadyDefinedMaterials);
                    Keeps.getInstance().saveConfig();
                    Keeps.getInstance().reloadConfig();
                    commandSourceStack.getSender().sendMessage(Component.text("Removed the " + material + " material to the list of default materials!").color(NamedTextColor.GREEN));

                } catch (IllegalArgumentException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Invalid material provided. Try again.", NamedTextColor.RED));
                    return;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Please provide a material!").color(NamedTextColor.RED));
                    return;
                }
                break;

            case "setenchantchance":

                try {
                    int chance = Integer.parseInt(strings[1]);
                    chance = Math.clamp(chance, 0, 100);
                    Keeps.getInstance().getConfig().set("keeping-enchantment-table-chance", chance);
                    Keeps.getInstance().saveConfig();
                    Keeps.getInstance().reloadConfig();
                    commandSourceStack.getSender().sendMessage(Component.text("Set enchantment table chance for Blessing of Keeping to " + chance + "%").color(NamedTextColor.GREEN));
                } catch (NumberFormatException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Invalid number provided. Try again.", NamedTextColor.RED));
                    return;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    commandSourceStack.getSender().sendMessage(Component.text("Please provide a chance! Use a number from 0-100").color(NamedTextColor.RED));
                    return;
                }
                break;

            case "toggledefaults":
                boolean useDefaults = Keeps.getInstance().getConfig().getBoolean("want-keeping-defaults");
                Keeps.getInstance().getConfig().set("want-keeping-defaults", !useDefaults);
                Keeps.getInstance().saveConfig();
                Keeps.getInstance().reloadConfig();
                commandSourceStack.getSender().sendMessage(Component.text("Setting for using default keeping items has been set to " + !useDefaults + "!").color(NamedTextColor.GREEN));
                break;

            case "toggleenchant":
                boolean useEnchant = Keeps.getInstance().getConfig().getBoolean("want-keeping-enchantment");
                Keeps.getInstance().getConfig().set("want-keeping-enchantment", !useEnchant);
                Keeps.getInstance().saveConfig();
                Keeps.getInstance().reloadConfig();
                commandSourceStack.getSender().sendMessage(Component.text("Setting for enchantment functionality has been set to " + !useEnchant + "!").color(NamedTextColor.GREEN));
                break;

            case "toggledeathcertificate":
                boolean useCert = Keeps.getInstance().getConfig().getBoolean("want-death-certificates");
                Keeps.getInstance().getConfig().set("want-death-certificates", !useCert);
                Keeps.getInstance().saveConfig();
                Keeps.getInstance().reloadConfig();
                commandSourceStack.getSender().sendMessage(Component.text("Setting for using death certificates has been set to " + !useCert + "!").color(NamedTextColor.GREEN));
                break;


            default:
                commandSourceStack.getSender().sendMessage("Unknown argument. Use /keeps to see valid arguments.");
                break;
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        if (args.length == 0) {
            return List.of(ARGS);
        }

        if (args.length > 1)
            return List.of();

        List<String> validArgs = new ArrayList<>();
        for (String arg : ARGS)
            if (arg.contains(args[0].toLowerCase()))
                validArgs.add(arg);

        return validArgs;
    }

    @Override
    public @Nullable String permission() {
        return "keeps.admin";
    }
}
