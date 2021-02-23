package dev.sendai.rush.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import dev.sendai.rush.MDT;
import dev.sendai.rush.config.Config;
import dev.sendai.rush.enums.GroupColor;
import dev.sendai.rush.enums.PlayerState;
import dev.sendai.rush.enums.TeamInvites;
import dev.sendai.rush.enums.TeamRank;
import dev.sendai.rush.items.iKit;
import dev.sendai.rush.utils.LocationHelper;

public class PlayerManager {
	
	private Player username;
	private UUID uuid;
	private PlayerState status;
	private GroupColor color;
	private TeamRank rankt;
	private TeamInvites invites;
	private int point;
	private int death;
	private int kill;
	private String team;
	private static Map<UUID, PlayerManager> playerManagers;
	
	public PlayerManager(final Player username, final UUID uuid) {
		this.username = username;
		this.uuid = uuid;
		this.status = PlayerState.SPAWN;
		this.point = 0;
		this.death = 0;
		this.kill = 0;
		PlayerManager.playerManagers.put(uuid, this);
	}
	
	public void setInvites(TeamInvites invites) {
		this.invites = invites;
	}
	
	public TeamInvites getInvites() {
		return invites;
	}
	
	public void setRankt(TeamRank rankt) {
		this.rankt = rankt;
	}
	
	public TeamRank getRankt() {
		return rankt;
	}
	
	public int getDeath() {
		return death;
	}
	
	public int getKill() {
		return kill;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public void setColor(GroupColor color) {
		this.color = color;
	}
	
	public GroupColor getColor() {
		return color;
	}
	
    public void sendKit(final iKit kit) {
    	System.out.println("TEST DU SENDKIT FDP");
        final Player player = Bukkit.getPlayer(this.uuid);
        player.getInventory().clear();
        player.getInventory().setContents(kit.content());
        player.getInventory().setArmorContents(kit.armor());
        player.updateInventory();
    	System.out.println("TEST DU SENDKIT FDP");
    }
	
    public void reset(final Player player, final GameMode gameMode) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.updateInventory();
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setMaximumNoDamageTicks(20);
        player.setNoDamageTicks(20);
        player.setHealthScale(20.0);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setSaturation(10.0f);
        player.setGameMode(gameMode);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setLevel(0);
    }
    
    public static Map<UUID, PlayerManager> getPlayerManagers() {
		return playerManagers;
	}
	
	public void setUsername(Player username) {
		this.username = username;
	}
	
	public Player getUsername() {
		return username;
	}
	
	public void setStatus(PlayerState status) {
		this.status = status;
	}
	
	public PlayerState getStatus() {
		return status;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	
	public int getPoint() {
		return point;
	}
	
    public void teleport(final Player player, final LocationHelper locationHelper) {
        if (locationHelper == null || locationHelper.getLocation() == null) {
            player.sendMessage(ChatColor.RED + "You can't be teleported because this location is not set!");
        }
        else {
            player.teleport(locationHelper.getLocation());
        }
    }
	
	public void createPlayerData(Player player) {
		PlayerManager data = new PlayerManager(player, player.getUniqueId());
		PlayerManager.playerManagers.put(data.getUuid(), data);
		this.loadData(data);
	}

	private void loadData(PlayerManager playerData) {
		Config config = new Config("/players/" + playerData.getUuid().toString(), MDT.getInstance());
		FileConfiguration fileConfig = config.getConfig();
		ConfigurationSection playerDataSelection = fileConfig.getConfigurationSection("playerdata");
		if (playerDataSelection != null) {
			playerData.setPoint(playerDataSelection.getInt("point"));
			playerData.setTeam(playerDataSelection.getString("team"));
			playerData.setDeath(playerDataSelection.getInt("death"));
			playerData.setKill(playerDataSelection.getInt("kill"));
		}
		playerData.setStatus(PlayerState.SPAWN);
	}

	public void removePlayerData(UUID uuid) {
		MDT.getInstance().getServer().getScheduler().runTaskAsynchronously(MDT.getInstance(), () -> {
			this.saveData((PlayerManager)PlayerManager.getPlayerManagers().get(uuid));
			PlayerManager.playerManagers.remove(uuid);
		});
	}

	public void saveData(PlayerManager playerData) {
		if (playerData != null) {
			Config config = new Config("/players/" + playerData.getUuid().toString(), MDT.getInstance());
			config.getConfig().set("playerdata.point", playerData.getPoint());
			config.getConfig().set("playerdata.team", playerData.getTeam());
			config.getConfig().set("playerdata.death", playerData.getDeath());
			config.getConfig().set("playerdata.kill", playerData.getKill());
			config.save();
		}
	}
	
    static {
        PlayerManager.playerManagers = new HashMap<UUID, PlayerManager>();
    }
}