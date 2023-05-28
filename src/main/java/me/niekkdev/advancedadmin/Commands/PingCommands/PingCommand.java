package me.niekkdev.advancedadmin.Commands.PingCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PingCommand implements CommandExecutor {

    private final Plugin plugin;

    public PingCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (!(sender instanceof Player)) {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
            return true;
        }

        Player player = (Player) sender;
        int ping = player.spigot().getPing();
        String message = Main.messagesConfig.getMessage("ping.ping");
        sender.sendMessage(prefix + message.replace("%ping%", String.valueOf(ping)));

        return true;
    }
}