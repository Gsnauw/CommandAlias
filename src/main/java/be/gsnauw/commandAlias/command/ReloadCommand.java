package be.gsnauw.commandAlias.command;

import be.gsnauw.commandAlias.CommandAlias;
import be.gsnauw.commandAlias.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final CommandAlias plugin;

    ChatUtil chat = CommandAlias.getInstance().getChatUtil();

    public ReloadCommand(CommandAlias plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player p)) {
            return true;
        }

        if (!p.hasPermission("commandalias.use")) {
            p.sendMessage(chat.noPermission());
            return true;
        }

        if (!(args.length == 1)) {
            p.sendMessage(chat.prefix("&cGebruik /commandalias reload"));
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            p.sendMessage(chat.prefix("&cGebruik /commandalias reload"));
            return true;
        }

        if (!p.hasPermission("commandalias.reload")) {
            p.sendMessage(chat.noPermission());
            return true;
        }

        plugin.reloadConfig();
        plugin.registerDynamicCommands();
        chat.info("Plugin reloaded.");
        p.sendMessage(chat.prefix("Plugin reloaded"));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
        }
        return completions;
    }
}