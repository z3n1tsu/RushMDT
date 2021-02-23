package dev.sendai.rush.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev.sendai.rush.enums.PlayerState;
import dev.sendai.rush.manager.PlayerManager;

public class EntityListener implements Listener {
	
	@EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
            return;
        }
        final Player entity = (Player)e.getEntity();
        Player damager;
        if (e.getDamager() instanceof Player) {
            damager = (Player)e.getDamager();
        }
        else {
            if (!(e.getDamager() instanceof Projectile)) {
                return;
            }
            damager = (Player)((Projectile)e.getDamager()).getShooter();
        }
        final PlayerManager entityData = PlayerManager.getPlayerManagers().get(entity.getUniqueId());
        final PlayerManager damagerData = PlayerManager.getPlayerManagers().get(damager.getUniqueId());
        if (entityData == null || damagerData == null) {
            e.setCancelled(true);
            return;
        }
        if (entityData.getStatus() == PlayerState.SPAWN || damagerData.getStatus() == PlayerState.SPAWN) {
        	e.setCancelled(true);
        	return;
        }
        if (entityData.getStatus() == PlayerState.RESPAWN && damagerData.getStatus() == PlayerState.PLAYING) {
        	e.setCancelled(true);
        	damager.sendMessage(ChatColor.RED + "Le spawn-kill est interdit!");
        	return;
        }
        if (entityData.getStatus() == PlayerState.SPECTATEUR || damagerData.getStatus() == PlayerState.SPECTATEUR) {
        	e.setCancelled(true);
        	return;
        }
        if (entityData.getStatus() == PlayerState.WAITING || damagerData.getStatus() == PlayerState.WAITING) {
        	e.setCancelled(true);
        	return;
        }
	}

}
