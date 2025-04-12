package be.gsnauw.commandAlias.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtil {

    private final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    private final JavaPlugin plugin;

    public ChatUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public net.kyori.adventure.text.Component format(String text) {
        return LEGACY_SERIALIZER.deserialize(text)
                .colorIfAbsent(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.UNDERLINED, false);
    }

    public Component prefix(String text) {
        return format("&7[&b&lSnow&f&lVille&7]&8 » &f" + text);
    }

    public Component noPermission() {
        return prefix("&cJe hebt hier geen permissie voor.");
    }

    public void info(String text) {
        plugin.getLogger().info(text);
    }
}
