package me.niekkdev.advancedadmin.EventManager;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MuteListener implements Listener {
    private final Plugin plugin;

    public MuteListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (Main.getMuted().contains(uuid)) {
            boolean showMuteMessage = plugin.getConfig().getBoolean("showMuteMessage");
            if (showMuteMessage) {
                e.setCancelled(true);
                String message = Main.messagesConfig.getMessage("mute.mute_message");
                player.sendMessage(message);
            } else {
                e.setCancelled(true);
            }
        }
    }
}

