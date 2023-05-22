package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    private final Plugin plugin;

    public MuteCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (sender instanceof Player player) {

            if (player.hasPermission("advancedadmin.mute")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Bukkit.getPlayer(args[0]);
                        UUID uuid = target.getUniqueId();
                        if (Main.getMuted().contains(uuid)) {
                            String message = Main.messagesConfig.getMessage("mute.player_is_muted");
                            player.sendMessage(prefix + message);
                        } else {
                            Main.getMuted().add(uuid);
                            String playermutemessage = Main.messagesConfig.getMessage("mute.mute_player");
                            player.sendMessage(prefix + playermutemessage.replace("%player%", args[0]));
                            String muteplayermessage = Main.messagesConfig.getMessage("mute.mute_by_player");
                            target.sendMessage(prefix + muteplayermessage.replace("%player%", player.getDisplayName()));
                        }
                    } else {
                        String message = Main.messagesConfig.getMessage("mute.player_not_found");
                        player.sendMessage(prefix + message);
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("mute.usage_mute");
                    player.sendMessage(prefix + message);
                }
                return true;
            }
        }
        return false;
    }
}
