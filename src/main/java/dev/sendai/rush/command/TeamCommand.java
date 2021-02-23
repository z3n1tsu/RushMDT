package dev.sendai.rush.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.sendai.rush.enums.TeamInvites;
import dev.sendai.rush.enums.TeamRank;
import dev.sendai.rush.manager.PlayerManager;
import dev.sendai.rush.manager.TeamManager;
import net.md_5.bungee.api.ChatColor;

public class TeamCommand implements CommandExecutor {
	
	public static TeamInvites invites;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "» " + ChatColor.LIGHT_PURPLE + "/team create <name>");
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "» " + ChatColor.LIGHT_PURPLE + "/team invite <player>");
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "» " + ChatColor.LIGHT_PURPLE + "/team promote <player>");
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "» " + ChatColor.LIGHT_PURPLE + "/team kick <player>");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-»-----------------«-");
			return false;
		}
		if (args[0].equalsIgnoreCase("create")) {
			if (pm.getTeam() != null) {
				sender.sendMessage(ChatColor.RED + "Vous avez déja une team.");
				return false;
			}
			else {
				final String name = args[1];
				TeamManager.create(player, name);
				return false;
			}
		}
		if (args[0].equalsIgnoreCase("invite")) {
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "/team invite <player>");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			final PlayerManager tm = PlayerManager.getPlayerManagers().get(target.getUniqueId());
			if (tm.getTeam() != null) {
				sender.sendMessage(ChatColor.RED + "Désolée mais ce joueur est déjà dans une team!");
				return false;
			}
			target.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.WHITE + " vous à invité dans ça team!");
			target.sendMessage(ChatColor.WHITE + "Faites /team join " + player.getName() + " !");
			tm.setInvites(TeamInvites.SEND);
		}
		if (args[0].equalsIgnoreCase("join")) {
			final Player target = Bukkit.getPlayer(args[1]);
			final PlayerManager pmt = PlayerManager.getPlayerManagers().get(target.getUniqueId());
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "/team join <player>");
				return false;
			}
			if (pm.getInvites() != TeamInvites.SEND) {
				sender.sendMessage(ChatColor.RED + "Désolée vous n'avez pas d'invitation");
				return false;
			}
			TeamManager.setCurrentOnline(TeamManager.getCurrentOnline() + 1);
			player.sendMessage(ChatColor.GREEN + "Vous avez bien rejoint la team de: " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " nommée: " + ChatColor.WHITE + pmt.getTeam());
			pm.setRankt(TeamRank.MEMBRE);
			pm.saveData(pm);
			sender.sendMessage("Vous avez rejoint la team!");
		}
		return false;
	}

}
