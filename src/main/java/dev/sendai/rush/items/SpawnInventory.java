package dev.sendai.rush.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.sendai.rush.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class SpawnInventory implements iKit {

    @Override
    public ItemStack[] content() {
        return new ItemStack[]{
        		new ItemBuilder(Material.EMERALD).setUnBreakable().setName(ChatColor.LIGHT_PURPLE + "Leaderboard").toItemStack(),
                air,
                air,
                air,
                new ItemBuilder(Material.DIAMOND_SWORD).setUnBreakable().setName(ChatColor.DARK_PURPLE + "Jouer").toItemStack(),
                air,
                air,
                air,
                new ItemBuilder(Material.PAPER).setUnBreakable().setName(ChatColor.LIGHT_PURPLE + "Information").toItemStack(),
        };
    }

    @Override
    public ItemStack[] armor() {
        return new ItemStack[0];
    }
}
