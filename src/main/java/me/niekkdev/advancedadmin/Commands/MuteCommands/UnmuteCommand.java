package me.niekkdev.advancedadmin.Commands.MuteCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class UnmuteCommand implements CommandExecutor {

    private final Plugin plugin;

    public UnmuteCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (sender instanceof Player player) {

            if (player.hasPermission("advancedadmin.unmute")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Bukkit.getPlayer(args[0]);
                        UUID uuid = target.getUniqueId();
                        if (Main.getMuted().contains(uuid)) {
                            Main.getMuted().remove(uuid);
                            String message = Main.messagesConfig.getMessage("unmute.player_unmuted");
                            player.sendMessage(prefix + message.replace("%player%", args[0]));
                        } else {
                            String message = Main.messagesConfig.getMessage("unmute.player_not_muted");
                            player.sendMessage(prefix + message.replace("%player%", args[0]));
                        }
                    } else {
                        String message = Main.messagesConfig.getMessage("unmute.player_not_found");
                        sender.sendMessage(prefix + message);
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("unmute.usage_unmute");
                    player.sendMessage(prefix + message);
                }
            }
            return true;
        }
        return false;
    }
}