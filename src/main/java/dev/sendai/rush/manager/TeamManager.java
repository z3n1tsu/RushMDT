package dev.sendai.rush.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import dev.sendai.rush.MDT;
import dev.sendai.rush.config.Config;
import dev.sendai.rush.enums.TeamInvites;
import dev.sendai.rush.enums.TeamRank;

public class TeamManager {
	
	private TeamRank rank;
	private Player player;
	private ArrayList<UUID> members;
	static Map<String, TeamManager> teamManagers;
	private static String name;
	private int pointteam;
	public static int currentOnline;
	private static UUID leader;
	
	public TeamManager(final Player player, final String name) {
		this.player = player;
		this.rank = TeamRank.LEADER;
		TeamManager.name = name;
		this.members.add(leader);
		this.pointteam = 0;
		TeamManager.teamManagers.put(name, this);
	}
	
	public static void join(final Player player) {
		final Player leaderTeam = Bukkit.getPlayer(leader);
		final PlayerManager ltpm = PlayerManager.getPlayerManagers().get(leaderTeam.getUniqueId());
		if (ltpm.getInvites() != TeamInvites.SEND) {
			player.sendMessage(ChatColor.RED + "Vous n'avez pas d'invitation!");
			return;
		}
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		pm.setTeam(ltpm.getTeam());
		setCurrentOnline(getCurrentOnline() + 1);
		player.sendMessage(ChatColor.GREEN + "Vous avez bien rejoint la team de: " + ChatColor.WHITE + leaderTeam.getName() + ChatColor.GREEN + " nommée: " + ChatColor.WHITE + ltpm.getTeam());
		pm.setRankt(TeamRank.MEMBRE);
		pm.saveData(pm);
		createTeamData(player);
	}
	
	public static void create(final Player player, final String name) {
		if (getTeamManagers().get(name) != null) {
			player.sendMessage(ChatColor.RED + "Désolée mais le nom de cette team est déja pris!");
			return;
		}
		new TeamManager(player, name);
		player.sendMessage(ChatColor.GREEN + "Vous avez bien crée votre team avec le nom: " + ChatColor.WHITE + name);
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		pm.setTeam(name);
		setCurrentOnline(1);
		pm.setRankt(TeamRank.LEADER);
		pm.saveData(pm);
		createTeamData(player);
		return;
	}
	
	public void setLeader(UUID leader) {
		TeamManager.leader = leader;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public static void setCurrentOnline(int currentOnline) {
		TeamManager.currentOnline = currentOnline;
	}
	
	public static int getCurrentOnline() {
		return currentOnline;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public static Map<String, TeamManager> getTeamManagers() {
		return teamManagers;
	}
	
	public TeamRank getRank() {
		return rank;
	}
	
	public ArrayList<UUID> getMembers() {
		return members;
	}
	
	public static String getName() {
		return name;
	}
	
	public int getPointteam() {
		return pointteam;
	}
	
	public void setRank(TeamRank rank) {
		this.rank = rank;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPointteam(int pointteam) {
		this.pointteam = pointteam;
	}
	
    static {
        TeamManager.teamManagers = new HashMap<String, TeamManager>();
    }
    
    
	public static void createTeamData(Player player) {
		TeamManager data = new TeamManager(player, getName());
		TeamManager.getTeamManagers().put(data.getName(), data);
		loadData(data);
	}

	public static void loadData(TeamManager playerData) {
		Config config = new Config("/team/" + playerData.getName().toString(), MDT.getInstance());
		FileConfiguration fileConfig = config.getConfig();
		ConfigurationSection playerDataSelection = fileConfig.getConfigurationSection("teamdata");
		if (playerDataSelection != null) {
			playerData.setName(playerDataSelection.getString("name"));
			playerData.setPointteam(playerDataSelection.getInt("point"));
		}
	}

	public static void removePlayerData(String name) {
		MDT.getInstance().getServer().getScheduler().runTaskAsynchronously(MDT.getInstance(), () -> {
			saveData((TeamManager)TeamManager.getTeamManagers().get(name));
			TeamManager.getTeamManagers().remove(name);
		});
	}

	public static void saveData(TeamManager playerData) {
		if (playerData != null) {
			Config config = new Config("/team/" + playerData.getName().toString(), MDT.getInstance());
			config.getConfig().set("teamdata.point", playerData.getPointteam());
			config.getConfig().set("teamdata.name", playerData.getName());
			config.save();
		}
	}

}
