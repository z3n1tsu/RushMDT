package dev.sendai.rush.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.sendai.rush.MDT;
import dev.sendai.rush.enums.GroupColor;
import dev.sendai.rush.enums.PlayerState;
import dev.sendai.rush.utils.Timer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class GameManager {
	
    private static PlayerState playerStatus;
    public static Timer timer;
    private GroupColor group;
    public static int OrangeSize;
    public static int VioletSize;
    public static int CurrentSize;
    public static ArrayList<UUID> teamOrange;
    public static ArrayList<UUID> teamViolet;
    public static TeamManager members;
    
    public static void queue(final Player player) {
    	final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
    	if (MDT.getInstance().ing.size() == 2) {
            new BukkitRunnable() {
                int i = 20;
                @Override
                public void run() {
                    if (pm.getStatus() == PlayerState.SPAWN) {
                        this.cancel();
                    } else {
                        i -= 1;
                        if (i <= 0) {
                        	if (MDT.getInstance().ing.size() == 2) {
                        		final ArrayList<UUID> orangel = MDT.getInstance().orange;
                        		final ArrayList<UUID> violetl = MDT.getInstance().violet;
                        		for (UUID uuid1 : orangel) {
                        			final Player player1 = Bukkit.getPlayer(uuid1);
                        			for (UUID uuid2 : violetl) {
                        				final Player player2 = Bukkit.getPlayer(uuid2);
                        				start(player1.getUniqueId(), player2.getUniqueId());
                        			}
                        		}
                        	}
                            this.cancel();
                            timer = new Timer(MDT.getInstance(), -1, false);
                            timer.start();
                        }
                    }
                }
            }.runTaskTimer((Plugin)MDT.getInstance(), 80L, 80L);
            
    	}
    }
	
	public static void start(final UUID teamOne, final UUID teamTwo) {
		CurrentSize = 0;
		playerStatus = PlayerState.WAITING;
		timer = new Timer(MDT.getInstance(), 5, true);
		final Player player1 = Bukkit.getPlayer(teamOne);
		final PlayerManager pm1 = PlayerManager.getPlayerManagers().get(player1.getUniqueId());
		pm1.setColor(GroupColor.ORANGE);
		if (pm1.getColor() == GroupColor.ORANGE) {
			pm1.teleport(player1, MDT.getInstance().getRush1());
		}
		else {
			pm1.teleport(player1, MDT.getInstance().getRush2());
		}
		player1.sendMessage(ChatColor.WHITE + "Vous avez bien était mis en jeux!");
		pm1.sendKit(MDT.getInstance().rushitems);
		final Player player2 = Bukkit.getPlayer(teamTwo);
		final PlayerManager pm2 = PlayerManager.getPlayerManagers().get(player2.getUniqueId());
		pm2.setColor(GroupColor.VIOLET);
		if (pm2.getColor() == GroupColor.ORANGE) {
			pm2.teleport(player2, MDT.getInstance().getRush1());
		}
		else {
			pm2.teleport(player2, MDT.getInstance().getRush2());
		}
		player2.sendMessage(ChatColor.WHITE + "Vous avez bien était mis en jeux!");
		pm2.sendKit(MDT.getInstance().rushitems);
        new BukkitRunnable() {
            int i = 5;
            @Override
            public void run() {
                if (playerStatus == PlayerState.SPAWN) {
                    this.cancel();
                } else {
                	playerStatus = PlayerState.PLAYING;
                    player1.sendTitle(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Lancement de la partie!", ChatColor.WHITE.toString() + i);
                    i -= 1;
                    if (i <= 0) {
                        this.cancel();
                        timer = new Timer(MDT.getInstance(), -1, false);
                        timer.start();
                    }
                }
            }
        }.runTaskTimer((Plugin)MDT.getInstance(), 20L, 20L);
	}
	
	public static void respawn(final Player player) {
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		if (pm.getColor() == GroupColor.ORANGE) {
			pm.teleport(player, MDT.getInstance().getRush1());
			pm.sendKit(MDT.getInstance().rushitems);
		}
		if (pm.getColor() == GroupColor.VIOLET) {
			pm.teleport(player, MDT.getInstance().getRush2());
			pm.sendKit(MDT.getInstance().rushitems);
		}
        new BukkitRunnable() {
            int i = 5;
            @Override
            public void run() {
                if (playerStatus == PlayerState.SPAWN) {
                    this.cancel();
                } else {
                    i -= 1;
                    if (i <= 0) {
                        this.cancel();
                        timer = new Timer(MDT.getInstance(), -1, false);
                        timer.start();
                    }
                }
            }
        }.runTaskTimer((Plugin)MDT.getInstance(), 20L, 20L);
	}
	
	public static void setCurrentSize(int currentSize) {
		CurrentSize = currentSize;
	}
	
	public static int getCurrentSize() {
		return CurrentSize;
	}
	
	public static int getOrangeSize() {
		return OrangeSize;
	}
	
	public static void setOrangeSize(int orangeSize) {
		OrangeSize = orangeSize;
	}
	
	public static int getVioletSize() {
		return VioletSize;
	}
	
	public static void setVioletSize(int violetSize) {
		VioletSize = violetSize;
	}
	
    public void sendGlobalMessage(final String message, final Player... players) {
        for (final Player player : players) {
            player.sendMessage(message);
        }
    }
    
    public void sendGlobalMessage(final String[] message, final Player... players) {
        for (final Player player : players) {
            player.sendMessage(message);
        }
    }
    
    public void sendGlobalMessage(final TextComponent message, final Player... players) {
        for (final Player player : players) {
            player.spigot().sendMessage((BaseComponent)message);
        }
    }
    
    public GroupColor getGroup() {
		return group;
	}
    
    public void setGroup(GroupColor group) {
		this.group = group;
	}
    
    public Timer getTimer() {
		return timer;
	}
    
    public static void setTimer(Timer timer) {
		GameManager.timer = timer;
	}
}
