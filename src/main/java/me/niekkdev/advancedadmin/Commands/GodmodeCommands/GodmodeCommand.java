package me.niekkdev.advancedadmin.Commands.GodmodeCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GodmodeCommand implements CommandExecutor {

    private final Plugin plugin;

    public GodmodeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (!(sender instanceof Player)) {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
            return true;
        }

        Player player = (Player) sender;
        player.setInvulnerable(!player.isInvulnerable());

        if (player.isInvulnerable()) {
            String message = Main.messagesConfig.getMessage("godmode.enabled");
            player.sendMessage(prefix + message);
        } else {
            String message = Main.messagesConfig.getMessage("godmode.disabled");
            sender.sendMessage(prefix + message);
        }

        return true;
    }
}