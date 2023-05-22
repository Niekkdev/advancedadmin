package me.niekkdev.advancedadmin.Configuration;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class messages {

    public final Main plugin;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public messages(Main plugin) {
        this.plugin = plugin;
        createMessagesFile();
        reloadMessages();
    }

    private void createMessagesFile() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }
    }

    public void reloadMessages() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String path) {
        if (messagesConfig.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString(path));
        }
        plugin.getLogger().log(Level.WARNING, "Message not found in messages.yml: " + path);
        return "";
    }

    public void saveMessages() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save messages.yml.", e);
        }
    }
}