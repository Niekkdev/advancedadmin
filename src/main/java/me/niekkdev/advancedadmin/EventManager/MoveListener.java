package me.niekkdev.advancedadmin.EventManager;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MoveListener implements Listener {
    private final Plugin plugin;

    public MoveListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (Main.getFreezed().contains(uuid)) {
            boolean showFreezeMessage = plugin.getConfig().getBoolean("showFreezeMessage");
            if (showFreezeMessage) {
                e.setCancelled(true);
                String message = Main.messagesConfig.getMessage("freeze.frozen_message");
                player.sendMessage(message);
            } else {
                e.setCancelled(true);
            }
        }
    }
}
