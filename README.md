# Keeps (Paper  Plugin)

A simple Paper plugin that allows more customization with death item dropping behavior. 
By default, this plugin comes with the following features:
- the `Blessing of Keeping` enchantment. This enchantment (by default) has a 20% chance to be applied to items when enchanted. The enchantment can also be obtained via villager trading.
- Provides a global list of item types to prevent from dropping via death regardless if they have the **Blessing of Keeping** enchantment or not. By default, this is just **Barriers, Lights, and Command Blocks**.
- Death Certificates. These are pieces of paper that are received upon respawning and give players information regarding their death location.

All aspects of the plugin can be configured by modifying the `config.yml` file or using the `/keeps` command.

## Default Config.yml

Below is the default config.yml and what the different config options mean:
```yml
# The message to display to users when they die. This will only display if there were some items to be kept from death.
# Use the {NUM_ITEMS} placeholder to reference the number of items.
# Set to an empty string to disable the message.
keeping-message: "&a{NUM_ITEMS} &6item(s) were retained from death!"

# Set to 'true' if you want the "Blessing of Keeping" enchantment to function.
# By setting this to true, the enchantment will work as expected and be able to be obtained
# via villager trading.
want-keeping-enchantment: true

# The chance for the enchanting table to enchant an item with blessing of keeping along with any other
# enchants that are rolled. "want-keeping-enchantment" must be set to true for this to function.
# NOTE: This does not affect any enchantments previously rolled for enchants, this is simply just an additional enchantment.
# Set to 0 if you do not want this enchantment to be rolled via the enchanting table.
keeping-enchantment-table-chance: 20

# Set to 'true' if you want certain items to be kept on death no matter what as if they were enchanted with
# the blessing of keeping. Set to false to ignore the list of items defined below.
want-keeping-defaults: true

# Set to 'true' if you want players to receive death certificates when they die. These are essentially just pieces
# paper that inform players when and where they died so they can easily find out where they died.
want-death-certificates: true

# The list of items to keep on death by default. These behave similarly as if they were enchanted
# with the blessing of keeping enchantment. Add to this list by using the vanilla minecraft material key.
# Note that "want-keeping-defaults" must be set to true for this to function.
# For example, if you wanted to make diamond pickaxes unable to be lost on death, add:
# - DIAMOND_PICKAXE
keeping-default-items:
  - BARRIER
  - LIGHT
  - COMMAND_BLOCK
```

## `/keeps` Command

Alternatively, the config can also be tweaked by using in game commands with the `keeps.admin` permission.

- `/keeps reload`: Reloads the configuration if any edits were made to `config.yml` (so you don't have to restart the server)
- `/keeps adddefault <MATERIAL>`: Adds an item type to globally prevent dropping from death.
- `/keeps removedefault <MATERIAL>`: Removes an item type to globally prevent dropping from death.
- `/keeps setenchantchance <CHANCE>`: Sets the percentage chance to receive the **Blessing of Keeping** enchantment when using an enchanting table
- `/keeps toggledefaults`: Toggles the option for considering a global list of preventing item type drops. 
- `/keeps toggleenchant`: Toggles the option for allowing the **Blessing of Keeping** enchantment to function. If this is false, the enchantment will still exist but will not work.
- `/keeps toggledeathcertificate`: Toggles the ability to receive death certificates when respawning.

## Installation
1. Go to the [releases tab](https://github.com/DevvyDont/Keeps/releases) and download the latest `Keeps.jar` file.
2. Place it in your `/plugins` directory in your **Paper** server.
3. Start your server!
4. (Optional) Edit the `/plugins/Keeps/config.yml` file after starting your server, or use the `/keeps` command to tweak the settings to your liking.