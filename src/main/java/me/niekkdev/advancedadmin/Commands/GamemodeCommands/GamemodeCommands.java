package me.niekkdev.advancedadmin.Commands.GamemodeCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCommands implements CommandExecutor, TabCompleter {
    private static final String[] GAME_MODES = {"creative", "survival", "adventure", "spectator"};

    private final Plugin plugin;

    public GamemodeCommands(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                GameMode gameMode = parseGameMode(args[0]);
                if (gameMode != null) {
                    player.setGameMode(gameMode);
                    String message = Main.messagesConfig.getMessage("gamemode.player_gamemode");
                    sender.sendMessage(prefix + message.replace("%gamemode%", args[0]));
                } else {
                    String message = Main.messagesConfig.getMessage("gamemode.usage_gamemode");
                    sender.sendMessage(prefix + message);
                }
                return true;
            } else {
                String message = Main.messagesConfig.getMessage("general.error");
                sender.sendMessage(message);
                return true;
            }
        } else if (args.length == 2) {
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer != null) {
                GameMode gameMode = parseGameMode(args[0]);
                if (gameMode != null) {
                    targetPlayer.setGameMode(gameMode);
                    String message = Main.messagesConfig.getMessage("gamemode.setplayer_gamemode");
                    sender.sendMessage(prefix + message.replace("%player%", targetPlayer.getName().replace("%gamemode%", args[0])));

                }
                return true;
            } else {
                String message = Main.messagesConfig.getMessage("gamemode.player_not_found");
                sender.sendMessage(prefix + message);
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partialGameMode = args[0].toLowerCase();
            for (String gameMode : GAME_MODES) {
                if (gameMode.startsWith(partialGameMode)) {
                    completions.add(gameMode);
                }
            }
        } else if (args.length == 2) {
            String partialPlayerName = args[1].toLowerCase();
            List<String> playerNames = new ArrayList<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                playerNames.add(onlinePlayer.getName());
            }
            StringUtil.copyPartialMatches(partialPlayerName, playerNames, completions);
        }
        return completions;
    }

    private GameMode parseGameMode(String arg) {
        switch (arg.toLowerCase()) {
            case "creative":
                return GameMode.CREATIVE;
            case "survival":
                return GameMode.SURVIVAL;
            case "adventure":
                return GameMode.ADVENTURE;
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }
}