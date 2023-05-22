package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AVACommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public AVACommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        String message = Main.messagesConfig.getMessage("general.no_permission");
        Player player = (Player) sender;

        if (player.hasPermission("advancedadmin.use")) {
            if (command.getName().equalsIgnoreCase("advancedadmin")) {
                if (args.length == 0) {
                    sender.sendMessage(prefix + "This is the AdvancedAdmin plugin. Use '/advancedadmin reload' to reload the plugin.");
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("advancedadmin.reload")) {
                        reloadConfiguration();
                        sender.sendMessage(prefix + "The plugin has been successfully reloaded.");
                    } else {
                        sender.sendMessage(prefix + message);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(prefix + ChatColor.GOLD + "This is the AdvancedAdmin plugin! Made by " + ChatColor.AQUA + "Niekkdev" + ChatColor.GOLD + "!");
                    return true;
                }
            }
            return false;
        } else {
            player.sendMessage(message);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("advancedadmin") || command.getAliases().contains("aa")) {
            if (args.length == 1) {
                return Arrays.asList("reload", "info");
            }
        }
        return Collections.emptyList();
    }

    private void reloadConfiguration() {
        Main.messagesConfig.reloadMessages();
        plugin.reloadConfig();
    }
}