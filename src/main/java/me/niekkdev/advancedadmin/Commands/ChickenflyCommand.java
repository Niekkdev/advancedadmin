package me.niekkdev.advancedadmin.Commands;

import me.niekkdev.advancedadmin.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChickenflyCommand implements CommandExecutor, Listener {

    private final Plugin plugin;
    private BossBar bossBar;
    private int timerTaskId;
    private int eggDropTaskId;
    private int timerSeconds;

    public ChickenflyCommand(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        bossBar = null;
        timerTaskId = -1;
        eggDropTaskId = -1;
        timerSeconds = 40;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) {
                String message = Main.messagesConfig.getMessage("chickenfly.execute_command");
                player.sendMessage(prefix + message);
                return true;
            }

            if (isWearingChickenHead(player)) {
                removeChickenHead(player);
                cancelEggDropping();
                cancelTimer();

                String message = Main.messagesConfig.getMessage("chickenfly.disabled");
                player.sendMessage(prefix + message);
                return true;
            }

            ItemStack chickenHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) chickenHead.getItemMeta();
            skullMeta.setDisplayName(ChatColor.WHITE + "Chicken Head");

            String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYzMDM1MzEwNTk1MiwKICAicHJvZmlsZUlkIiA6ICIxYjMyYmY0OWUyZmE0YzRhYjg2ZjA4OGUxYTcxZTYxMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJIZWxsb1N0dWRpbyIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lZ";

            skullMeta.setOwner("MHF_Chicken");

            // Setting the player head's texture manually is not possible in Minecraft 1.13.
            // Instead, we set the owner of the skull to a known chicken player head.

            chickenHead.setItemMeta(skullMeta);

            if (player.getInventory().getHelmet() != null) {
                String message = Main.messagesConfig.getMessage("chickenfly.cannot_wear");
                player.sendMessage(prefix + message);
                return true;
            }

            if (player.getInventory().getChestplate() == null) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
                chestplateMeta.setColor(org.bukkit.Color.WHITE);
                chestplate.setItemMeta(chestplateMeta);
                player.getInventory().setChestplate(chestplate);
            }

            if (player.getInventory().getLeggings() == null) {
                ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
                leggingsMeta.setColor(org.bukkit.Color.WHITE);
                leggings.setItemMeta(leggingsMeta);
                player.getInventory().setLeggings(leggings);
            }

            if (player.getInventory().getBoots() == null) {
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
                bootsMeta.setColor(org.bukkit.Color.WHITE);
                boots.setItemMeta(bootsMeta);
                player.getInventory().setBoots(boots);
            }

            player.getInventory().setHelmet(chickenHead);

            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 800, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 800, 0));
            String message = Main.messagesConfig.getMessage("chickenfly.enabled");
            player.sendMessage(prefix + message);

            startTimer(player);
            startEggDropping(player);

            return true;
        }

        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                boolean isChickenHead = isChickenHeadItem(clickedItem);

                if (!isChickenHead) {
                    removeChickenHead(player);
                    cancelTimer();
                    cancelEggDropping();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem != null && droppedItem.getType() == Material.PLAYER_HEAD) {
            boolean isChickenHead = isChickenHeadItem(droppedItem);

            if (!isChickenHead) {
                removeChickenHead(event.getPlayer());
                cancelTimer();
                cancelEggDropping();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        removeChickenHead(event.getEntity());
        cancelTimer();
        cancelEggDropping();
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);

        if (isWhiteLeatherArmor(chestplate)) {
            player.getInventory().setChestplate(null);
        }
        if (isWhiteLeatherArmor(leggings)) {
            player.getInventory().setLeggings(null);
        }
        if (isWhiteLeatherArmor(boots)) {
            player.getInventory().setBoots(null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
            removeChickenHead(event.getPlayer());
            cancelTimer();
            cancelEggDropping();
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);

            if (isWhiteLeatherArmor(chestplate)) {
                player.getInventory().setChestplate(null);
            }
            if (isWhiteLeatherArmor(leggings)) {
                player.getInventory().setLeggings(null);
            }
            if (isWhiteLeatherArmor(boots)) {
                player.getInventory().setBoots(null);
            }
        }

    private boolean isChickenHeadItem(ItemStack item) {
        if (item != null && item.getType() == Material.PLAYER_HEAD && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                return skullMeta.hasOwner() && skullMeta.getOwner().equals("MHF_Chicken") && skullMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && skullMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            }
        }
        return false;
    }

    private boolean isWearingChickenHead(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        return isChickenHeadItem(helmet);
    }

    private void removeChickenHead(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        player.getInventory().setHelmet(null);

        if (isWhiteLeatherArmor(chestplate)) {
            player.getInventory().setChestplate(null);
        }
        if (isWhiteLeatherArmor(leggings)) {
            player.getInventory().setLeggings(null);
        }
        if (isWhiteLeatherArmor(boots)) {
            player.getInventory().setBoots(null);
        }

        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }

    private boolean isWhiteLeatherArmor(ItemStack item) {
        if (item != null && item.getType().name().contains("LEATHER_") && item.hasItemMeta()) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();
            org.bukkit.Color color = armorMeta.getColor();
            return color.equals(org.bukkit.Color.WHITE);
        }
        return false;
    }

    private void startTimer(Player player) {
        String prefix = Main.showPrefix(plugin.getConfig().getString("prefix"));
        if (timerTaskId != -1) {
            plugin.getServer().getScheduler().cancelTask(timerTaskId);
        }

        bossBar = plugin.getServer().createBossBar(ChatColor.GREEN + "Chicken Fly Timer: " + timerSeconds + "s",
                BarColor.GREEN, BarStyle.SOLID);
        bossBar.setProgress(1);
        bossBar.addPlayer(player);

        timerTaskId = new BukkitRunnable() {
            int timeLeft = timerSeconds;

            @Override
            public void run() {
                timeLeft--;

                if (timeLeft > 0) {
                    bossBar.setTitle(ChatColor.GREEN + "Chicken Fly Timer: " + timeLeft + "s");
                    bossBar.setProgress((double) timeLeft / timerSeconds);
                } else {
                    bossBar.removeAll();
                    cancelTimer();
                    removeChickenHead(player);
                    cancelEggDropping();
                    String message = Main.messagesConfig.getMessage("chickenfly.time_ended");
                    player.sendMessage(prefix + message);
                }
            }
        }.runTaskTimer(plugin, 20, 20).getTaskId();
    }

    private void cancelTimer() {
        if (timerTaskId != -1) {
            plugin.getServer().getScheduler().cancelTask(timerTaskId);
            timerTaskId = -1;
            bossBar.removeAll();
        }
    }

    private void startEggDropping(Player player) {
        if (eggDropTaskId != -1) {
            plugin.getServer().getScheduler().cancelTask(eggDropTaskId);
        }

        eggDropTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.EGG));
                player.getWorld().playSound(player.getLocation(), "entity.item.pickup", 1.0f, 1.0f);
            }
        }.runTaskTimer(plugin, 40, 100).getTaskId();
    }

    private void cancelEggDropping() {
        if (eggDropTaskId != -1) {
            plugin.getServer().getScheduler().cancelTask(eggDropTaskId);
            eggDropTaskId = -1;
        }
    }
}
