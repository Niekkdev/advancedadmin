package me.niekkdev.advancedadmin.Commands.KickCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class KickCommand implements CommandExecutor {

    private final Plugin plugin;

    public KickCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("advancedadmin.kick")) {
                if (command.getName().equalsIgnoreCase("kick")) {
                    if (args.length < 2) {
                        String message = Main.messagesConfig.getMessage("kick.usage_kick");
                        player.sendMessage(prefix + message);
                        return true;
                    }
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target == null) {
                        String message = Main.messagesConfig.getMessage("kick.player_not_found");
                        player.sendMessage(prefix + message);
                        return true;
                    }
                    StringBuilder reasonBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        reasonBuilder.append(args[i]).append(" ");
                    }
                    String reason = reasonBuilder.toString().trim();
                    target.kickPlayer(ChatColor.WHITE + "You have been kicked! Reason: " + ChatColor.RED + reason);
                    String targetMessage = Main.messagesConfig.getMessage("kick.kick_player_reason");
                    sender.sendMessage(prefix + targetMessage.replace("%player%", args[0]).replace("%reason%", reason));
                    return true;
                }
            }
        }
        return false;
    }
}