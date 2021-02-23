package dev.sendai.rush.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import dev.sendai.rush.utils.ItemBuilder;

public class RushInventory implements iKit {

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DURABILITY, 5).toItemStack(),
        		new ItemBuilder(Material.WOOD_PICKAXE).addEnchant(Enchantment.DURABILITY, -3).toItemStack(),
                air,
                air,
                air,
                air,
                air,
                air,
                air,

                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,

                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,

                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,

        };
        return Contents;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {new ItemBuilder(Material.LEATHER_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_FALL, 4).toItemStack(),
                new ItemBuilder(Material.LEATHER_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.LEATHER_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.LEATHER_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack()
        };
        return Armor;
    }
}
