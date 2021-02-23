package dev.sendai.rush.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import dev.sendai.rush.utils.ItemBuilder;

public class ColorGui implements iGui {

	public Inventory inventory() {
		final Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Couleur:");
		inv.setItem(3, new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.ORANGE).setName(ChatColor.GOLD + "Orange").addLoreLine(ChatColor.GRAY + "Cliquez ici pour rejoindre l'equipe orange!").toItemStack());
		inv.setItem(5, new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.PURPLE).setName(ChatColor.DARK_PURPLE + "Violet").addLoreLine(ChatColor.GRAY + "Cliquez ici pour rejoindre l'equipe violet!").toItemStack());
		return inv;
	}

}
