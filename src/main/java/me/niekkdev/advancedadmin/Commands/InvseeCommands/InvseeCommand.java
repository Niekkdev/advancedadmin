package me.niekkdev.advancedadmin.Commands.InvseeCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class InvseeCommand implements CommandExecutor {

    private final Plugin plugin;

    public InvseeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer != null) {
                    Player player = (Player) sender;
                    Inventory targetInventory = targetPlayer.getInventory();

                    player.openInventory(targetInventory);
                } else {
                    String message = Main.messagesConfig.getMessage("invsee.player_not_found");
                    sender.sendMessage(prefix + message);
                }
            } else {
                String message = Main.messagesConfig.getMessage("invsee.please_provide");
                sender.sendMessage(prefix + message);
            }
        } else {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
        }

        return true;
    }
}