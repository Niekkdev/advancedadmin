package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TeleportCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public TeleportCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (command.getName().equalsIgnoreCase("tp") || command.getName().equalsIgnoreCase("teleport")) {
            if (args.length >= 3) {
                double x, y, z;
                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    String message = Main.messagesConfig.getMessage("teleport.invalid_numbers");
                    sender.sendMessage(prefix + message);
                    return true;
                }

                Player player;
                if (args.length == 3 && sender instanceof Player) {
                    player = (Player) sender;
                } else if (args.length == 4) {
                    String playerName = args[3];
                    player = Bukkit.getPlayer(playerName);

                    if (player == null) {
                        String message = Main.messagesConfig.getMessage("teleport.player_not_found");
                        sender.sendMessage(prefix + message);
                        return true;
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("teleport.usage_tp");
                    sender.sendMessage(prefix + message);
                    return true;
                }

                player.teleport(player.getWorld().getBlockAt((int) x, (int) y, (int) z).getLocation());
                String message = Main.messagesConfig.getMessage("teleport.coords");
                sender.sendMessage(prefix + message.replace("%X%", Double.toString(x)).replace("%Y%", Double.toString(y)).replace("%Z%", Double.toString(z)));
                return true;
            } else if (args.length == 1 && sender instanceof Player) {
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);
                Player player = (Player) sender;

                if (target == null) {
                    String message = Main.messagesConfig.getMessage("teleport.player_not_found");
                    sender.sendMessage(prefix + message);
                    return true;
                }

                player.teleport(target.getLocation());
                String message = Main.messagesConfig.getMessage("teleport.tp_to_player");
                sender.sendMessage(prefix + message.replace("%player%", target.getName()));
                return true;
            } else if (args.length == 2) {
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    String message = Main.messagesConfig.getMessage("teleport.player_not_found");
                    sender.sendMessage(prefix + message);
                    return true;
                }

                String destinationName = args[1];
                Player destination = Bukkit.getPlayer(destinationName);

                if (destination == null) {
                    String message = Main.messagesConfig.getMessage("teleport.player_not_found");
                    sender.sendMessage(prefix + message);
                    return true;
                }

                target.teleport(destination.getLocation());
                String message = Main.messagesConfig.getMessage("teleport.player_tp_to_player");
                sender.sendMessage(prefix + message.replace("%player%", target.getName()).replace("%destination%", destination.getName()));
                return true;
            } else if (args.length == 1) {
                String message = Main.messagesConfig.getMessage("teleport.usage_tp");
                sender.sendMessage(prefix + message);
                return true;
            } else {
                String message = Main.messagesConfig.getMessage("teleport.usage_tp");
                sender.sendMessage(prefix + message);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            return completions;
        } else if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return null;
    }
}