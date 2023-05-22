package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BanCommand implements CommandExecutor {

    private final Plugin plugin;

    public BanCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (command.getName().equalsIgnoreCase("ban")) {
            if (args.length < 2) {
                String message = Main.messagesConfig.getMessage("ban.usage_player_reason");
                sender.sendMessage(prefix + message);
                return true;
            }
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                String message = Main.messagesConfig.getMessage("ban.player_not_found");
                sender.sendMessage(prefix + message);
                return true;
            }
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            String reason = reasonBuilder.toString().trim();
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), ChatColor.RED + reason, null, sender.getName());
            target.kickPlayer(ChatColor.WHITE + "You are banned from this server.\nReason: " + ChatColor.RED + reason);
            String message = Main.messagesConfig.getMessage("ban.ban_player_reason");
            sender.sendMessage(prefix + message.replace("%player%", args[0]).replace("%reason%", reason));
            return true;
        }
        return false;
    }
}


