package me.niekkdev.advancedadmin.EventManager;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ChatEvent implements Listener {

    private final Plugin plugin;

    public ChatEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        String chat_prefix = Main.showPrefix(plugin.getConfig().getString("chat_prefix"));
        Player player = event.getPlayer();

        if (isPlayerMuted(player)) {
            event.setCancelled(true);
            return;
        }

        String message = (chat_prefix.replace("%player%", player.getName()));
        String originalMessage = event.getMessage();
        String filteredMessage = filterMessage(originalMessage);

        if (filteredMessage == null || filteredMessage.equals("")) {
            event.setCancelled(true);
            return;
        }

        String component = message + filteredMessage;

        for (Player recipient : event.getRecipients()) {
            if (!isPlayerMuted(recipient)) {
                recipient.sendMessage(component);
            }
        }
        event.setCancelled(true);
    }

    private String filterMessage(String originalMessage) {
        if (originalMessage.startsWith("@") && originalMessage.length() > 1) {
            return null;
        }
        return originalMessage;
    }

    private boolean isPlayerMuted(Player player) {
        UUID uuid = player.getUniqueId();
        return Main.getMuted().contains(uuid);
    }
}
