package me.niekkdev.advancedadmin.EventManager;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class StaffChat implements CommandExecutor, Listener {

    private final Plugin plugin;

    public StaffChat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("@") || message.startsWith("/staffchat")) {
            event.setCancelled(true);

            message = message.replaceFirst("[@/][ ]?", "");

            if (player.hasPermission("advancedadmin.usestaffchat")) {
                sendStaffChatMessage(player, message);
            }
        }
    }

    private void sendStaffChatMessage(Player player, String message) {
        String staffchat_prefix = Main.showPrefix(plugin.getConfig().getString("staffchat_prefix"));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("advancedadmin.usestaffchat")) {
                onlinePlayer.sendMessage(staffchat_prefix.replace("%player%", player.getName()).replace("%message%", message ));
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("staffchat")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command can only be used by a player.");
                return true;
            }

            if (!player.hasPermission("advancedadmin.usestaffchat")) {
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Usage: /staffchat <message>");
                return true;
            }

            String message = String.join(" ", args);
            sendStaffChatMessage(player, message);

            return true;
        }

        return false;
    }
}