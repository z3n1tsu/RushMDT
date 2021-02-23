package dev.sendai.rush.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.sendai.rush.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class QueueInventory implements iKit {

    @Override
    public ItemStack[] content() {
        return new ItemStack[]{
        		new ItemBuilder(Material.WOOL).setUnBreakable().setName(ChatColor.DARK_PURPLE + "Choisissez votre couleur").toItemStack(),
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                new ItemBuilder(Material.TORCH).setUnBreakable().setName(ChatColor.DARK_PURPLE + "Retourner au spawn!").toItemStack(),
        };
    }

    @Override
    public ItemStack[] armor() {
        return new ItemStack[0];
    }
}