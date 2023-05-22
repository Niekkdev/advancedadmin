package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class UnfreezeCommand implements CommandExecutor {

    private final Plugin plugin;

    public UnfreezeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (sender instanceof Player player) {

            if (player.hasPermission("advancdadmin.unfreeze")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Bukkit.getPlayer(args[0]);
                        UUID uuid = target.getUniqueId();
                        if (Main.getFreezed().contains(uuid)) {
                            Main.getFreezed().remove(uuid);
                            String message = Main.messagesConfig.getMessage("unfreeze.is_unfrozen");
                            player.sendMessage(prefix + message.replace("%player%", args[0]));
                        } else {
                            String message = Main.messagesConfig.getMessage("unfreeze.is_frozen");
                            player.sendMessage(prefix + message.replace("%player%", args[0]));
                        }
                    } else {
                        String message = Main.messagesConfig.getMessage("unfreeze.player_not_found");
                        player.sendMessage(prefix + message);
                    }
                } else {
                    String message = Main.messagesConfig.getMessage("unfreeze.usage_unfreeze");
                    player.sendMessage(prefix + message);
                }
                return true;
            }
        }

        return false;
    }
}
