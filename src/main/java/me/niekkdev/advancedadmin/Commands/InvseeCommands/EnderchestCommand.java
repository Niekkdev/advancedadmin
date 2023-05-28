package me.niekkdev.advancedadmin.Commands.InvseeCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EnderchestCommand implements CommandExecutor {

    private final Plugin plugin;

    public EnderchestCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                // If no arguments provided, open player's own ender chest
                player.openInventory(player.getEnderChest());
                return true;
            } else if (args.length == 1) {
                // If one argument provided, try to find the specified player and open their ender chest
                Player targetPlayer = player.getServer().getPlayer(args[0]);
                if (targetPlayer != null) {
                    player.openInventory(targetPlayer.getEnderChest());
                    return true;
                } else {
                    String message = Main.messagesConfig.getMessage("enderchest.player_not_found");
                    player.sendMessage(prefix + message);
                    return false;
                }
            } else {
                String message = Main.messagesConfig.getMessage("enderchest.please_provide");
                player.sendMessage(prefix + message);
                return false;
            }
        } else {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
            return false;
        }
    }
}