package be.gsnauw.commandAlias;

import be.gsnauw.commandAlias.command.ReloadCommand;
import be.gsnauw.commandAlias.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public final class CommandAlias extends JavaPlugin {

    @Getter
    private ChatUtil chatUtil;
    @Getter
    private static CommandAlias instance;

    @Override
    public void onEnable() {
        instance = this;
        chatUtil = new ChatUtil(this);

        saveDefaultConfig();
        registerDynamicCommands();

        Objects.requireNonNull(getCommand("commandalias")).setExecutor(new ReloadCommand(this));
        getLogger().info("The plugin has started, Hello World!");
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin has been disabled, goodbye!");
    }

    public void registerDynamicCommands() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            for (String key : Objects.requireNonNull(getConfig().getConfigurationSection("commands")).getKeys(false)) {
                String targetCommand = getConfig().getString("commands." + key);

                PluginCommand command = getCommand(key);
                if (command == null) {
                    command = createPluginCommand(key);
                    commandMap.register(getDescription().getName(), command);
                }

                command.setExecutor((sender, command1, label, args) -> {
                    if (!(sender instanceof Player p)) {
                        sender.sendMessage("Alleen spelers kunnen dit gebruiken.");
                        return true;
                    }

                    if (targetCommand != null) {
                        p.performCommand(targetCommand);
                    }
                    return true;
                });

                command.setDescription("Mapped to /" + targetCommand);
                getLogger().info("Geregistreerd: /" + key + " -> /" + targetCommand);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to create a PluginCommand instance reflectively
    private PluginCommand createPluginCommand(String name) throws Exception {
        Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);
        return constructor.newInstance(name, this);
    }
}