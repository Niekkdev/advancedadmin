package me.niekkdev.advancedadmin.Commands.SpawnCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Plugin plugin;

    public SpawnCommand(Plugin plugin) {
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
        if (player.hasPermission("advancedadmin.setspawn")) {
            UUID playerUUID = player.getUniqueId();
            if (command.getName().equalsIgnoreCase("setspawn")) {
                Location spawnLocation = player.getLocation();
                plugin.getConfig().set("spawn.world", player.getWorld().getName());
                plugin.getConfig().set("spawn.x", spawnLocation.getX());
                plugin.getConfig().set("spawn.y", spawnLocation.getY());
                plugin.getConfig().set("spawn.z", spawnLocation.getZ());
                plugin.getConfig().set("spawn.yaw", spawnLocation.getYaw());
                plugin.getConfig().set("spawn.pitch", spawnLocation.getPitch());
                plugin.saveConfig();
                String message = Main.messagesConfig.getMessage("spawn.current_location");
                player.sendMessage(prefix + message);
                return true;
            } else if (command.getName().equalsIgnoreCase("spawn")) {
                if (player.hasPermission("advancedadmin.spawn")) {
                    long cooldownTime = plugin.getConfig().getLong("cooldown", 3) * 1000;
                    long currentTime = System.currentTimeMillis();

                    if (cooldowns.containsKey(playerUUID) && currentTime - cooldowns.get(playerUUID) < cooldownTime) {
                        long timeLeft = (cooldowns.get(playerUUID) + cooldownTime - currentTime) / 1000;
                        String message = Main.messagesConfig.getMessage("spawn.wait_time");
                        player.sendMessage(prefix + message.replace("%time%", Long.toString(timeLeft)));
                        return true;
                    }

                    String spawnWorld = plugin.getConfig().getString("spawn.world");
                    double spawnX = plugin.getConfig().getDouble("spawn.x");
                    double spawnY = plugin.getConfig().getDouble("spawn.y");
                    double spawnZ = plugin.getConfig().getDouble("spawn.z");
                    float spawnYaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                    float spawnPitch = (float) plugin.getConfig().getDouble("spawn.pitch");

                    if (spawnWorld == null) {
                        String message = Main.messagesConfig.getMessage("spawn.set_spawn");
                        player.sendMessage(prefix + message);
                        return true;
                    }

                    World world = plugin.getServer().getWorld(spawnWorld);
                    if (world == null) {
                        String message = Main.messagesConfig.getMessage("spawn.invalid_world");
                        player.sendMessage(prefix + message);
                        return true;
                    }

                    Location spawnLocation = new Location(plugin.getServer().getWorld(spawnWorld), spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
                    player.teleport(spawnLocation);
                    String message = Main.messagesConfig.getMessage("spawn.teleportation_success");
                    player.sendMessage(prefix + message);

                    cooldowns.put(playerUUID, currentTime);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldowns.remove(playerUUID);
                        }
                    }.runTaskLater(plugin, cooldownTime / 50);

                    return true;
                }

                return false;
            }
        }
        return false;
    }
}