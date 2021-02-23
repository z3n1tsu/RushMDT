package dev.sendai.rush;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev.sendai.rush.board.ScoreboardRunnable;
import dev.sendai.rush.command.RushCommand;
import dev.sendai.rush.command.TeamCommand;
import dev.sendai.rush.inventory.ColorGui;
import dev.sendai.rush.inventory.iGui;
import dev.sendai.rush.items.QueueInventory;
import dev.sendai.rush.items.RushInventory;
import dev.sendai.rush.items.SpawnInventory;
import dev.sendai.rush.items.iKit;
import dev.sendai.rush.listener.PlayerListener;
import dev.sendai.rush.utils.LocationHelper;


public class MDT extends JavaPlugin {
	
	private static MDT instance;
    private File locationFile;
    private YamlConfiguration locationConfig;
	public ArrayList<UUID> ing = new ArrayList<>();
	public ArrayList<UUID> teamonline = new ArrayList<>();
	public ArrayList<UUID> orange = new ArrayList<>();
	public ArrayList<UUID> violet = new ArrayList<>();
	private LocationHelper spawn;
	private LocationHelper rush1;
	private LocationHelper rush2;
	private LocationHelper queueing;
	public iKit spawnitems;
	public iKit rushitems;
	public iKit queueitems;
	public iGui colorinv;
	
	public MDT() {
		this.spawn = new LocationHelper("spawn");
		this.rush1 = new LocationHelper("1");
		this.rush2 = new LocationHelper("2");
		this.queueing = new LocationHelper("queue");
		this.spawnitems = new SpawnInventory();
		this.rushitems = new RushInventory();
		this.queueitems = new QueueInventory();
		this.colorinv = new ColorGui();
	}
	
	public void onEnable() {
    	new ScoreboardRunnable().runTaskTimer(this, 0, 1);
        MDT.instance = this;
        this.registerRessource();
        this.registerListener();
        this.registerCommand();
        this.registerFile();
        this.registerLocation();
	}
	
	public void onDisable() {
        LocationHelper.getAll().forEach(locationHelper -> locationHelper.save());
        try {
            this.locationConfig.save(this.locationFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    private void registerRessource() {
        this.saveResource("locations.yml", false);
	}

	private void registerListener() {
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
		
	}

	private void registerCommand() {
		this.getCommand("rush").setExecutor(new RushCommand());
		this.getCommand("team").setExecutor(new TeamCommand());
	}
	
	private void registerFile() {
        this.locationFile = new File(this.getDataFolder() + "/locations.yml");
        this.locationConfig = YamlConfiguration.loadConfiguration(this.locationFile);	
	}
	
	private void registerLocation() {
        for (final LocationHelper locationHelper : LocationHelper.getAll()) {
            final String message = locationHelper.load() ? ("The location" + locationHelper.getName() + " is successfully registered!") : ("The location " + locationHelper.getName() + " is not registered!");
            this.getServer().getConsoleSender().sendMessage(message);
        }
    }
	
	public LocationHelper getQueueing() {
		return queueing;
	}
	
	public void setQueueing(LocationHelper queueing) {
		this.queueing = queueing;
	}
	
	public LocationHelper getRush1() {
		return rush1;
	}
	
	public LocationHelper getRush2() {
		return rush2;
	}
	
	public LocationHelper getSpawn() {
		return spawn;
	}
	
	public YamlConfiguration getLocationConfig() {
		return locationConfig;
	}
	
	public File getLocationFile() {
		return locationFile;
	}
	
	public static MDT getInstance() {
		return instance;
	}

}
