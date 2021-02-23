package dev.sendai.rush.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.sendai.rush.utils.LocationHelper;
import net.md_5.bungee.api.ChatColor;

public class RushCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.WHITE + "Unknown Command.");
			return false;
		}
		else {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.RED + "/rush setlocation <spawn> <1/2>");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
				return false;
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setlocation")) {
					if (args[1].equals("1")) {
						final Player player = (Player) sender;
	                    LocationHelper.getLocationHelper(args[1]).setLocation(player.getLocation());
						player.sendMessage(ChatColor.GREEN + "Vous avez bien mis la premiere location!");
						return false;
					}
					if (args[1].equals("2")) {
						final Player player = (Player) sender;
	                    LocationHelper.getLocationHelper(args[1]).setLocation(player.getLocation());
						player.sendMessage(ChatColor.GREEN + "Vous avez bien mis la seconde location!");
						return false;
					}
					if (args[1].equalsIgnoreCase("spawn")) {
						final Player player = (Player) sender;
	                    LocationHelper.getLocationHelper(args[1]).setLocation(player.getLocation());
						player.sendMessage(ChatColor.GREEN + "Vous avez bien mis le spawn!");
						return false;
					}
					if (args[1].equalsIgnoreCase("queue")) {
						final Player player = (Player) sender;
	                    LocationHelper.getLocationHelper(args[1]).setLocation(player.getLocation());
						player.sendMessage(ChatColor.GREEN + "Vous avez bien mis la station d'attente!");
						return false;
					}
					else {
						sender.sendMessage(ChatColor.RED + "Entrer une valeur d'argument valide!");
						return false;
					}
				}
				else {
					return false;
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.RED + "/rush setlocation <spawn> <1/2>");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
				return false;
			}
		}
	}

}
