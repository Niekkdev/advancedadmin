package me.niekkdev.advancedadmin.Commands.VanishCommands;

import me.niekkdev.advancedadmin.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class VanishCommand implements CommandExecutor, Listener {

    public static ArrayList<Player> vanished = new ArrayList<>();

    private final Plugin plugin;

    public VanishCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (vanished.contains(p)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(plugin, p);
                }
                vanished.remove(p);
                String message = Main.messagesConfig.getMessage("vanish.invisible");
                p.sendMessage(prefix + message);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(plugin, p);
                }
                vanished.add(p);
                String message = Main.messagesConfig.getMessage("vanish.visible");
                p.sendMessage(prefix + message);
            }

            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        for (Player vanishedPlayer : vanished) {
            joinedPlayer.hidePlayer(plugin, vanishedPlayer);
        }
    }
}