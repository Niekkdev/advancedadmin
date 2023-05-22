package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class CannonCommand implements CommandExecutor {
    private final Plugin plugin;

    public CannonCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("advancedadmin.cannon")) {
                if (command.getName().equalsIgnoreCase("cannon")) {
                    Location playerLocation = player.getLocation();

                    double x = playerLocation.getX();
                    double y = playerLocation.getY() + 1.5;
                    double z = playerLocation.getZ();

                    Location cannonLocation = new Location(playerLocation.getWorld(), x, y, z);

                    MagmaCube magmaCube = (MagmaCube) playerLocation.getWorld().spawnEntity(cannonLocation, EntityType.MAGMA_CUBE);
                    magmaCube.setSize(1);

                    Vector direction = playerLocation.getDirection().multiply(2);

                    magmaCube.setVelocity(direction);

                    boolean letCannon = plugin.getConfig().getBoolean("letCannonBreakBlocks");
                    String message = Main.messagesConfig.getMessage("cannon.launch_message");
                    sender.sendMessage(message);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (magmaCube.isValid() && !magmaCube.isDead()) {
                            Location explosionLocation = magmaCube.getLocation();
                            explosionLocation.getWorld().playSound(explosionLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                            explosionLocation.getWorld().createExplosion(explosionLocation, 2.0f, false, letCannon);
                            magmaCube.remove();
                        }
                    }, 16);

                    return true;
                }
            }
        }

        return false;
    }
}
