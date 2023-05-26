package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class VanishCommand implements CommandExecutor, Listener {
    private Set<UUID> vanishedPlayers = new HashSet<>();
    private Plugin plugin;

    public VanishCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String message = Main.messagesConfig.getMessage("general.error");
            sender.sendMessage(message);
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();


        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (vanishedPlayers.contains(uuid)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(player);
            }
            String consoleCommand = "minecraft:effect clear " + player.getName() + " minecraft:invisibility";
            getServer().dispatchCommand(getServer().getConsoleSender(), consoleCommand);
            String message = Main.messagesConfig.getMessage("vanish.visible");
            sender.sendMessage(prefix + message);
            vanishedPlayers.remove(uuid);
        } else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(player);
            }
            String consoleCommand = "minecraft:effect give " + player.getName() + " minecraft:invisibility infinite 0 true";
            getServer().dispatchCommand(getServer().getConsoleSender(), consoleCommand);
            String message = Main.messagesConfig.getMessage("vanish.invisible");
            sender.sendMessage(prefix + message);
            vanishedPlayers.add(uuid);

            startRepeatingTask(player);
        }

        return true;
    }

    private void startRepeatingTask(Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (vanishedPlayers.contains(player.getUniqueId())) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.hidePlayer(player);
                    }
                } else {
                    cancel();
                }
            }
        };

        task.runTaskTimer(plugin, 0L, 40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (vanishedPlayers.contains(uuid)) {
            vanishedPlayers.remove(uuid);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(player);
            }
            String consoleCommand = "minecraft:effect clear " + player.getName() + " minecraft:invisibility";
            getServer().dispatchCommand(getServer().getConsoleSender(), consoleCommand);
        }
    }
}
