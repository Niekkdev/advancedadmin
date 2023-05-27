package me.niekkdev.advancedadmin.Commands.SpawnCommands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
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
                File file = new File("plugins/AdvancedAdmin", "spawn.yml");
                FileConfiguration spawn = YamlConfiguration.loadConfiguration(file);
                Location spawnLocation = player.getLocation();
                spawn.set("spawn.world", player.getWorld().getName());
                spawn.set("spawn.x", spawnLocation.getX());
                spawn.set("spawn.y", spawnLocation.getY());
                spawn.set("spawn.z", spawnLocation.getZ());
                spawn.set("spawn.yaw", spawnLocation.getYaw());
                spawn.set("spawn.pitch", spawnLocation.getPitch());
                try {
                    spawn.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

                    FileConfiguration spawn = YamlConfiguration.loadConfiguration(new File("plugins/AdvancedAdmin", "spawn.yml"));
                    String spawnWorld = spawn.getString("spawn.world");
                    double spawnX = spawn.getDouble("spawn.x");
                    double spawnY = spawn.getDouble("spawn.y");
                    double spawnZ = spawn.getDouble("spawn.z");
                    float spawnYaw = (float) spawn.getDouble("spawn.yaw");
                    float spawnPitch = (float) spawn.getDouble("spawn.pitch");

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