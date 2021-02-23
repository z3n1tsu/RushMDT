package dev.sendai.rush.board;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

import dev.sendai.rush.MDT;
import dev.sendai.rush.enums.GroupColor;
import dev.sendai.rush.enums.PlayerState;
import dev.sendai.rush.listener.PlayerListener;
import dev.sendai.rush.manager.GameManager;
import dev.sendai.rush.manager.PlayerManager;
import dev.sendai.rush.utils.Timer;

public class ScoreboardRunnable extends BukkitRunnable {

    private static Map<UUID, ScoreboardHelper> boardMap = Maps.newConcurrentMap();
    public static Timer timer;

    @Override
    public void run() {
        for (Map.Entry<UUID, ScoreboardHelper> entry : boardMap.entrySet()){
            UUID uuid = entry.getKey();
            if (Bukkit.getPlayer(uuid) == null)boardMap.remove(uuid);
        }

        int players = Bukkit.getOnlinePlayers().size();
        int ing = MDT.getInstance().ing.size();
        for (Player player : Bukkit.getOnlinePlayers()){	
            final UUID uuid = player.getUniqueId();
            ScoreboardHelper board = boardMap.get(uuid);

            if (board == null){
                board = new ScoreboardHelper(player, ScoreboardUtils.SCOREBOARD_TITLE);
                boardMap.put(uuid, board);
            }
           
            PlayerManager pm = PlayerManager.getPlayerManagers().get(player.getUniqueId());
            PlayerState status = pm.getStatus();
            board.clear();
            if (status == PlayerState.SPAWN) {
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
                board.add(ChatColor.DARK_PURPLE + "Connectée: " + ChatColor.WHITE + players);
                board.add("");
                board.add(ChatColor.LIGHT_PURPLE + "Point: " + ChatColor.WHITE + pm.getPoint());
            	board.add("");
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
            }
            if (status == PlayerState.WAITING) {
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
                board.add(ChatColor.DARK_PURPLE + "En jeu: " + ChatColor.WHITE + ing + ChatColor.GRAY + "/" + ChatColor.GREEN + "8");
                board.add("");
                if (pm.getColor() == GroupColor.ORANGE) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.GOLD + "Orange");
                }
                if (pm.getColor() == GroupColor.VIOLET) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.DARK_PURPLE + "Violet");
                }
                board.add(ChatColor.LIGHT_PURPLE + "Team: " + ChatColor.WHITE + pm.getTeam());
                board.add(ChatColor.LIGHT_PURPLE + "Temps: " + ChatColor.WHITE + PlayerListener.getTimer().getTime());
            	board.add("");
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
            }
            if (status == PlayerState.RESPAWN) {
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
                board.add(ChatColor.DARK_PURPLE + "Connecté: " + ChatColor.WHITE);
                board.add("");
                if (pm.getColor() == GroupColor.ORANGE) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.GOLD + "Orange");
                }
                if (pm.getColor() == GroupColor.VIOLET) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.DARK_PURPLE + "Violet");
                }
                board.add(ChatColor.LIGHT_PURPLE + "Team: " + ChatColor.WHITE + pm.getTeam());
                board.add(ChatColor.LIGHT_PURPLE + "Temps: " + ChatColor.WHITE + GameManager.timer.getTime());
                board.add(ChatColor.LIGHT_PURPLE + "Kill: " + ChatColor.WHITE + pm.getKill());
                board.add(ChatColor.LIGHT_PURPLE + "Death: " + ChatColor.WHITE + pm.getDeath());
            	board.add("");
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
            }
            if (status == PlayerState.PLAYING) {
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
                board.add(ChatColor.DARK_PURPLE + "Connecté: " + ChatColor.WHITE);
                board.add("");
                if (pm.getColor() == GroupColor.ORANGE) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.GOLD + "Orange");
                }
                if (pm.getColor() == GroupColor.VIOLET) {
                	board.add(ChatColor.LIGHT_PURPLE + "Equipe: " + ChatColor.DARK_PURPLE + "Violet");
                }
                board.add(ChatColor.LIGHT_PURPLE + "Team: " + ChatColor.WHITE + pm.getTeam());
                board.add(ChatColor.LIGHT_PURPLE + "Temps: " + ChatColor.WHITE + GameManager.timer.getTime());
                board.add(ChatColor.LIGHT_PURPLE + "Kill: " + ChatColor.WHITE + pm.getKill());
                board.add(ChatColor.LIGHT_PURPLE + "Death: " + ChatColor.WHITE + pm.getDeath());
            	board.add("");
                board.add(ScoreboardUtils.SCOREBOARD_LINE);
            }
            board.update();
        }
    }
}
