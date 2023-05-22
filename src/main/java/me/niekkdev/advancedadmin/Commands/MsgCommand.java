package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class MsgCommand implements CommandExecutor {

    private final Plugin plugin;

    public MsgCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (command.getName().equalsIgnoreCase("msg")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 2) {
                    String targetPlayerName = args[0];
                    Player targetPlayer = getServer().getPlayer(targetPlayerName);
                    if (targetPlayer != null) {
                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }
                        String finalMessage = message.toString().trim();
                        String msg_player_prefix = Main.messagesConfig.getMessage("msg.msg_player_prefix");
                        targetPlayer.sendMessage(msg_player_prefix.replace("%player%", player.getName()).replace("%message%", finalMessage));
                        String player_msg_prefix = Main.messagesConfig.getMessage("msg.player_msg_prefix");
                        player.sendMessage(player_msg_prefix.replace("%player%", targetPlayer.getName()).replace("%message%", finalMessage));
                        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
                    } else {
                        String message = Main.messagesConfig.getMessage("msg.player_not_found");
                        player.sendMessage(prefix + message.replace("%player%", targetPlayerName));
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("msg.usage_msg");
                    player.sendMessage(prefix + message);
                }
            } else {
                String message = Main.messagesConfig.getMessage("general.error");
                sender.sendMessage(message);
            }
            return true;
        }
        return false;
    }
}
