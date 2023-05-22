package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class KillCommand implements CommandExecutor {

    private final Plugin plugin;

    public KillCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (command.getName().equalsIgnoreCase("kill")) {
            if (args.length < 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.setHealth(0.0);
                    String message = Main.messagesConfig.getMessage("kill.player");
                    player.sendMessage(prefix + message);
                    return true;
                } else {
                    String message = Main.messagesConfig.getMessage("kill.usage_kill");
                    sender.sendMessage(prefix + message);
                    return true;
                }
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                String message = Main.messagesConfig.getMessage("kill.player_not_found");
                sender.sendMessage(prefix + message);
                return true;
            }

            target.setHealth(0.0);
            String message = Main.messagesConfig.getMessage("kill.killed_player");
            sender.sendMessage(prefix + message.replace("%player%", target.getName()));
            return true;
        }

        if (command.getName().equalsIgnoreCase("die")) {
            if (args.length < 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.setHealth(0.0);
                    String message = Main.messagesConfig.getMessage("kill.player");
                    player.sendMessage(prefix + message);
                    return true;
                } else {
                    String message = Main.messagesConfig.getMessage("kill.usage_die");
                    sender.sendMessage(prefix + message);
                    return true;
                }
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                String message = Main.messagesConfig.getMessage("kill.player_not_found");
                sender.sendMessage(prefix + message);
            }

            target.setHealth(0.0);
            String message = Main.messagesConfig.getMessage("kill.killed_player");
            sender.sendMessage(prefix + message.replace("%player%", target.getName()));
            return true;
        }
        return false;
    }
}