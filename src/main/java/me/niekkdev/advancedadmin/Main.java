package me.niekkdev.advancedadmin;

import me.niekkdev.advancedadmin.Commands.*;
import me.niekkdev.advancedadmin.Commands.BanCommands.BanCommand;
import me.niekkdev.advancedadmin.Commands.BanCommands.UnbanCommand;
import me.niekkdev.advancedadmin.Commands.FlyCommands.FlyCommand;
import me.niekkdev.advancedadmin.Commands.FreezeCommands.FreezeCommand;
import me.niekkdev.advancedadmin.Commands.FreezeCommands.UnfreezeCommand;
import me.niekkdev.advancedadmin.Commands.GamemodeCommands.GamemodeCommands;
import me.niekkdev.advancedadmin.Commands.GodmodeCommands.GodmodeCommand;
import me.niekkdev.advancedadmin.Commands.InvseeCommands.EnderchestCommand;
import me.niekkdev.advancedadmin.Commands.InvseeCommands.InvseeCommand;
import me.niekkdev.advancedadmin.Commands.KickCommands.KickCommand;
import me.niekkdev.advancedadmin.Commands.KillCommands.KillCommand;
import me.niekkdev.advancedadmin.Commands.KillCommands.KillmobsCommand;
import me.niekkdev.advancedadmin.Commands.MsgCommands.MsgCommand;
import me.niekkdev.advancedadmin.Commands.MuteCommands.MuteCommand;
import me.niekkdev.advancedadmin.Commands.MuteCommands.UnmuteCommand;
import me.niekkdev.advancedadmin.Commands.PingCommands.PingCommand;
import me.niekkdev.advancedadmin.Commands.SpawnCommands.SpawnCommand;
import me.niekkdev.advancedadmin.Commands.TeleportCommands.TeleportCommand;
import me.niekkdev.advancedadmin.Commands.VanishCommands.VanishCommand;
import me.niekkdev.advancedadmin.Configuration.messages;
import me.niekkdev.advancedadmin.EventManager.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin implements Listener {
    private static final List<UUID> freezed = new ArrayList<>();
    private static final List<UUID> muted = new ArrayList<>();
    private final List<Player> mutedPlayers = new ArrayList<>();
    public static messages messagesConfig;

    public static List<UUID> getFreezed() {
        return freezed;
    }

    public static List<UUID> getMuted() {
        return muted;
    }

    private Map<String, Location> warps;

    public static String showPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        new me.niekkdev.advancedadmin.UpdateChecker(this, 94311).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("There is a new update available. Download it at the SpigotMC page!");
            }
        });

        int pluginId = 18532;
        Metrics metrics = new Metrics(this, pluginId);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new VanishCommand(this), this);

        messagesConfig = new messages(this);
        registerCommands();
        registerEventListeners();

        getLogger().info("AdvancedAdmin has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("AdvancedAdmin is disabled!");
        messagesConfig.saveMessages();
    }

    private void registerCommands() {
        getCommand("cannon").setExecutor(new CannonCommand(this));
        getCommand("staffchat").setExecutor(new StaffChat(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("kill").setExecutor(new KillCommand(this));
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("unfreeze").setExecutor(new UnfreezeCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("killmob").setExecutor(new KillmobsCommand(this));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("advancedadmin").setExecutor(new AVACommand(this));
        getCommand("advancedadmin").setTabCompleter(new AVACommand(this));
        getCommand("setspawn").setExecutor(new SpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("invsee").setExecutor(new InvseeCommand(this));
        getCommand("enderchest").setExecutor(new EnderchestCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("gamemode").setExecutor(new GamemodeCommands(this));
        getCommand("gamemode").setTabCompleter(new GamemodeCommands(this));
        getCommand("ping").setExecutor(new PingCommand(this));
        getCommand("chickenfly").setExecutor(new ChickenflyCommand(this));
        getCommand("god").setExecutor(new GodmodeCommand(this));
        getCommand("godmode").setExecutor(new GodmodeCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new StaffChat(this), this);
        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getServer().getPluginManager().registerEvents(new MuteListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChickenflyCommand(this), this);
        getServer().getPluginManager().registerEvents(new VanishCommand(this), this);
    }

    public List<Player> getMutedPlayers() {
        return mutedPlayers;
    }

}