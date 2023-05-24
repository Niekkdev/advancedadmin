package me.niekkdev.advancedadmin.Commands.FreezeCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class FreezeCommand implements CommandExecutor {

    private final Plugin plugin;

    public FreezeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("advancedadmin.freeze")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Bukkit.getPlayer(args[0]);
                        UUID uuid = target.getUniqueId();
                        if (Main.getFreezed().contains(uuid)) {
                            String message = Main.messagesConfig.getMessage("freeze.player_is_frozen");
                            player.sendMessage(prefix + message);
                        } else {
                            Main.getFreezed().add(uuid);
                            String playerMessage = Main.messagesConfig.getMessage("freeze.player_frozen");
                            player.sendMessage(prefix + playerMessage.replace("%player%", args[0]));
                            String targetMessage = Main.messagesConfig.getMessage("freeze.frozen_by_player");
                            target.sendMessage(prefix + targetMessage.replace("%player%", player.getName()));
                        }
                    } else {
                        String message = Main.messagesConfig.getMessage("freeze.player_not_found");
                        player.sendMessage(prefix + message);
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("freeze.usage_freeze");
                    player.sendMessage(prefix + message);
                }
                return true;
            }
        }
        return false;
    }
}