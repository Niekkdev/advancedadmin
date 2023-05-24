package me.niekkdev.advancedadmin.Commands.FlyCommands;

import me.niekkdev.advancedadmin.Configuration.messages;
import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SPEED_OPTIONS = new ArrayList<String>() {{
        add("slow");
        add("normal");
        add("fast");
        add("sonic");
    }};
    public messages messagesConfig;
    private final Plugin plugin;

    public FlyCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (!(sender instanceof Player)) {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            toggleFly(player, null, null);
        } else if (args.length == 1) {
            String arg = args[0].toLowerCase();
            if (SPEED_OPTIONS.contains(arg)) {
                setFlySpeed(player, arg);
            } else {
                String message = Main.messagesConfig.getMessage("fly.invalid_speed");
                player.sendMessage(prefix + message);
            }
        } else if (args.length == 2) {
            String speedArg = args[0].toLowerCase();
            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                String message = Main.messagesConfig.getMessage("fly.player_not_found");
                player.sendMessage(prefix + message);
                return true;
            }

            if (SPEED_OPTIONS.contains(speedArg)) {
                setFlySpeed(target, speedArg);
                String message = Main.messagesConfig.getMessage("fly.playerspeed");
                player.sendMessage(prefix + message.replace("%player%", target.getName().replace("%speed%", speedArg)));
            } else {
                String message = Main.messagesConfig.getMessage("fly.invalid_speed");
                player.sendMessage(prefix + message);
            }
        } else {
            String message = Main.messagesConfig.getMessage("fly.usage_speed_player");
            player.sendMessage(prefix + message);
            return true;
        }

        return true;
    }

    private void toggleFly(Player player, Boolean enableFly, Float flySpeed) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (enableFly == null) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                String message = Main.messagesConfig.getMessage("fly.disabled");
                player.sendMessage(prefix + message);
            } else {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setFlying(false);
                String message = Main.messagesConfig.getMessage("fly.enabled");
                player.sendMessage(prefix + message);
            }
        } else {
            player.setAllowFlight(enableFly);
            player.setFlying(enableFly);
            if (enableFly) {
                String message = Main.messagesConfig.getMessage("fly.enabled");
                player.sendMessage(prefix + message);
            } else {
                String message = Main.messagesConfig.getMessage("fly.disabled");
                player.sendMessage(prefix + message);
            }
        }

        if (flySpeed != null) {
            player.setFlySpeed(flySpeed);
        }
    }

    private void setFlySpeed(Player player, String speed) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        float flySpeed;

        if (speed.equals("slow")) {
            flySpeed = 0.1f;
        } else if (speed.equals("normal")) {
            flySpeed = 0.2f;
        } else if (speed.equals("fast")) {
            flySpeed = 0.3f;
        } else if (speed.equals("sonic")) {
            flySpeed = 1.0f;
        } else {
            String message = Main.messagesConfig.getMessage("fly.invalid_speed");
            player.sendMessage(prefix + message);
            return;
        }

        toggleFly(player, player.getAllowFlight(), flySpeed);
        String message = Main.messagesConfig.getMessage("fly.speed");
        player.sendMessage(prefix + message.replace("%speed%", speed));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partialSpeed = args[0].toLowerCase();

            for (String speed : SPEED_OPTIONS) {
                if (speed.startsWith(partialSpeed)) {
                    completions.add(speed);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase(ChatColor.GREEN + "enable") || args[0].equalsIgnoreCase(ChatColor.RED + "disable"))) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }
}
