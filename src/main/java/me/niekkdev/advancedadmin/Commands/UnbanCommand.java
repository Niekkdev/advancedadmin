package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.BanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UnbanCommand implements CommandExecutor {

    private final Plugin plugin;

    public UnbanCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (command.getName().equalsIgnoreCase("unban")) {
            if (args.length < 1) {
                String message = Main.messagesConfig.getMessage("unban.usage_player");
                sender.sendMessage(prefix + message);
                return true;
            }

            String targetName = args[0];
            BanList banList = sender.getServer().getBanList(BanList.Type.NAME);

            if (!banList.isBanned(targetName)) {
                String message = Main.messagesConfig.getMessage("unban.player_not_ban");
                sender.sendMessage(prefix + message);
                return true;
            }
            banList.pardon(targetName);
            String message = Main.messagesConfig.getMessage("unban.player_unban");
            sender.sendMessage(prefix + message.replace("%player%", args[0]));
            return true;
        }
        return false;
    }
}
