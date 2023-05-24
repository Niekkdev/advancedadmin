package me.niekkdev.advancedadmin.Commands.KillCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KillmobsCommand implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public KillmobsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (args.length < 1) {
            String message = Main.messagesConfig.getMessage("killmob.usage_killmob");
            sender.sendMessage(prefix + message);
            return true;
        }

        String option = args[0].toLowerCase();
        if (option.equals("chunk")) {
            if (!(sender instanceof Player)) {
                String message = Main.messagesConfig.getMessage("general.error");
                sender.sendMessage(message);
                return true;
            }

            Player player = (Player) sender;
            World world = player.getWorld();
            int chunkX = player.getLocation().getChunk().getX();
            int chunkZ = player.getLocation().getChunk().getZ();

            for (Entity entity : world.getChunkAt(chunkX, chunkZ).getEntities()) {
                if (entity instanceof Player) continue;
                entity.remove();
            }

            String message = Main.messagesConfig.getMessage("killmob.remove_chunk");
            sender.sendMessage(message);
        } else if (option.equals("world")) {
            if (!(sender instanceof Player)) {
                String message = Main.messagesConfig.getMessage("general.error");
                sender.sendMessage(message);
                return true;
            }

            Player player = (Player) sender;
            World world = player.getWorld();

            for (Entity entity : world.getEntities()) {
                if (entity instanceof Player) continue;
                entity.remove();
            }

            String message = Main.messagesConfig.getMessage("killmob.remove_world");
            sender.sendMessage(message);
        } else {
            String message = Main.messagesConfig.getMessage("killmob.usage_killmob");
            sender.sendMessage(prefix + message);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("chunk");
            completions.add("world");
        }
        return completions;
    }
}