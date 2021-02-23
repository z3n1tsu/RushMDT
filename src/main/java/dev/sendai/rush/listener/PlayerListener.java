package dev.sendai.rush.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import dev.sendai.rush.MDT;
import dev.sendai.rush.enums.GroupColor;
import dev.sendai.rush.enums.PlayerState;
import dev.sendai.rush.manager.GameManager;
import dev.sendai.rush.manager.PlayerManager;
import dev.sendai.rush.utils.Timer;

public class PlayerListener implements Listener {
	
    public static Timer timer;
	
	@EventHandler
	public void PlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Player player = event.getPlayer();
        PlayerManager pm = new PlayerManager(player, player.getUniqueId());
        pm.createPlayerData(player);
        pm.setUuid(player.getUniqueId());
        pm.reset(player, GameMode.SURVIVAL);
        pm.sendKit(MDT.getInstance().spawnitems);
        pm.teleport(player, MDT.getInstance().getSpawn());
        if (pm.getTeam() == null) {
        	pm.setTeam("null");
        }
        else {
        	MDT.getInstance().teamonline.add(player.getUniqueId());
        }
	}
	
	public void PlayerQuit(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		final Player player = event.getPlayer();
		if (MDT.getInstance().ing.contains(player.getUniqueId())) {
			MDT.getInstance().ing.remove(player.getUniqueId());
		}
		if (MDT.getInstance().orange.contains(player.getUniqueId())) {
			MDT.getInstance().orange.remove(player.getUniqueId());
		}
		if (MDT.getInstance().violet.contains(player.getUniqueId())) {
			MDT.getInstance().violet.remove(player.getUniqueId());
		}
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		pm.saveData(pm);
	}
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        PlayerManager playerManager = PlayerManager.getPlayerManagers().get(player.getUniqueId());
        if (playerManager.getStatus() == PlayerState.WAITING) {
            event.setCancelled(true);
            if (current.hasItemMeta()) {
            	if (inventory.getName().equals(ChatColor.DARK_GRAY + "Couleur:")) {
            		event.setCancelled(true);
                	if (current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Orange")) {
                		event.setCancelled(true);
                		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
                		if (pm.getColor() == GroupColor.VIOLET) {
                			player.sendMessage(ChatColor.RED + "Vous quittez l'equipe violet!");
                			MDT.getInstance().violet.remove(player.getUniqueId());
                		}
                		GameManager.setOrangeSize(GameManager.getOrangeSize() + 1);
                		MDT.getInstance().orange.add(player.getUniqueId());
                		pm.setColor(GroupColor.ORANGE);
                		player.sendMessage(ChatColor.GRAY + "» " + ChatColor.WHITE + "Vous avez rejoins l'équipe " + ChatColor.GOLD + "Orange");
                		player.closeInventory();
                		return;
                	}
                	if (current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Violet")) {
                		event.setCancelled(true);
                		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
                		if (pm.getColor() == GroupColor.ORANGE) {
                			player.sendMessage(ChatColor.RED + "Vous quittez l'equipe violet!");
                			MDT.getInstance().orange.remove(player.getUniqueId());
                		}
                		GameManager.setVioletSize(GameManager.getVioletSize() + 1);
                		MDT.getInstance().violet.add(player.getUniqueId());
                		pm.setColor(GroupColor.VIOLET);
                		player.sendMessage(ChatColor.GRAY + "» " + ChatColor.WHITE + "Vous avez rejoins l'équipe " + ChatColor.DARK_PURPLE + "Violet");
                		player.closeInventory();
                		return;
                	}
            	}
            }
        }
	}
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack current = event.getItem();
        if(current == null) return;
        Action action = event.getAction();
        if(current.hasItemMeta()) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                PlayerManager playerManager = PlayerManager.getPlayerManagers().get(player.getUniqueId());
                if(playerManager.getStatus() == PlayerState.SPAWN) {
                    event.setCancelled(true);
                    if(current.getType() == Material.DIAMOND_SWORD && current.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Jouer")) {
                    	playerManager.sendKit(MDT.getInstance().queueitems);
                        timer = new Timer(MDT.getInstance(), -1, false);
                        timer.start();
                    	playerManager.teleport(player, MDT.getInstance().getQueueing());
                    	playerManager.setStatus(PlayerState.WAITING);
                    	MDT.getInstance().ing.add(player.getUniqueId());
                    	GameManager.queue(player);
                    }
                }
                if(playerManager.getStatus() == PlayerState.WAITING) {
                    event.setCancelled(true);
                    if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Choisissez votre couleur")) {
                    	player.openInventory(MDT.getInstance().colorinv.inventory());
                    	return;
                    }
                    if(current.getType() == Material.TORCH && current.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Retourner au spawn!")) {
                    	playerManager.reset(player, GameMode.SURVIVAL);
                    	playerManager.setStatus(PlayerState.SPAWN);
                    	playerManager.teleport(player, MDT.getInstance().getSpawn());
                    	playerManager.sendKit(MDT.getInstance().spawnitems);
                    	MDT.getInstance().ing.remove(player.getUniqueId());
                    	if (MDT.getInstance().violet.contains(player.getUniqueId())) {
                    		MDT.getInstance().violet.remove(player.getUniqueId());
                    		return;
                    	}
                    	if (MDT.getInstance().orange.contains(player.getUniqueId())) {
                    		MDT.getInstance().orange.remove(player.getUniqueId());
                    		return;
                    	}
                    	return;
                    }
                }
                else {
                	event.setCancelled(false);
                }
            }
        }
	}
	
	public static Timer getTimer() {
		return timer;
	}
	
	public static void setTimer(Timer timer) {
		PlayerListener.timer = timer;
	}
	
	
	@EventHandler
	public void PlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		if (pm.getTeam() != "null") {
			event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.GRAY + " [" + ChatColor.RED + pm.getTeam() + ChatColor.GRAY + "] " + "%1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
		}
		else {
			event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.GRAY + " %1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			final PlayerManager psm = PlayerManager.getPlayerManagers().get(players.getUniqueId());
			if (psm.getStatus() == PlayerState.PLAYING) {
				if (pm.getColor() == GroupColor.ORANGE) {
					if (pm.getTeam() == "null") {
						event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.GOLD + " %1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
					}
					else {
						event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.GRAY + " [" + ChatColor.RED + pm.getTeam() + ChatColor.GRAY + "] " + ChatColor.LIGHT_PURPLE + "%1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
					}
				}
				if (pm.getColor() == GroupColor.VIOLET) {
					if (pm.getTeam() == "null") {
						event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.LIGHT_PURPLE + " %1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
					}
					else {
						event.setFormat(ChatColor.GRAY + "[" + ChatColor.WHITE + pm.getPoint() + ChatColor.GRAY + "]" + ChatColor.GRAY + " [" + ChatColor.RED + pm.getTeam() + ChatColor.GRAY + "] " + ChatColor.LIGHT_PURPLE + "%1$s" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + "%2$s");
					}
				}
			}
		}
	}
	
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
    	if (PlayerManager.getPlayerManagers().get(e.getPlayer().getUniqueId()).getStatus() == PlayerState.SPAWN) {
    		e.setCancelled(true);
    		return;
    	}
        if (PlayerManager.getPlayerManagers().get(e.getPlayer().getUniqueId()).getStatus() == PlayerState.PLAYING) {
        	if (e.getBlock().getType() == Material.SANDSTONE || e.getBlock().getType() == Material.ENDER_STONE || e.getBlock().getType() == Material.TNT) {
        		e.setCancelled(false);
        		return;
        	}
        	else {
        		e.setCancelled(true);
        	}
        }
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(e.getPlayer().getLocation());
    }
	
	@EventHandler
	public void PlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity().getPlayer();
		final Player killer = event.getEntity().getKiller();
		final PlayerManager pmp = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		if (pmp.getColor() == GroupColor.ORANGE) {
			event.setDeathMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + ChatColor.WHITE + " a était tué par " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + killer.getName());
			pmp.teleport(player, MDT.getInstance().getRush1());
		}
		if (pmp.getColor() == GroupColor.VIOLET) {
			event.setDeathMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + player.getName() + ChatColor.WHITE + " a était tué par " + ChatColor.GOLD + ChatColor.BOLD + killer.getName());
			pmp.teleport(player, MDT.getInstance().getRush2());
		}
		final PlayerManager pmk = PlayerManager.getPlayerManagers().get(killer.getUniqueId());
		pmk.setPoint(pmk.getPoint() + 5);
		if (pmp.getPoint() == 0) {
			pmp.setPoint(0);
		}
		else {
			pmp.setPoint(pmp.getPoint() - 2);
		}
		player.spigot().respawn();
		GameManager.respawn(player);
	}
	
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        PlayerManager playerManager = PlayerManager.getPlayerManagers().get(e.getPlayer().getUniqueId());
        if(playerManager.getStatus() == PlayerState.PLAYING) {
            if(e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD
                    || e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD
                    || e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD
                    || e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD){ e.setCancelled(true);
                    return; }
            Bukkit.getScheduler().runTaskLater(MDT.getInstance(), (Runnable) new Runnable() {
                public void run() {
                    e.getItemDrop().remove();
                }
            }, 200);
        }
        else{
            e.setCancelled(true);
        }
    }

}
