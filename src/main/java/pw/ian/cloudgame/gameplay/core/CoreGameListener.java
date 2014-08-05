/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pw.ian.cloudgame.gameplay.core;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.ian.cloudgame.game.Game;
import pw.ian.cloudgame.gameplay.GameListener;
import pw.ian.cloudgame.gameplay.Gameplay;
import pw.ian.cloudgame.stats.Death;

/**
 *
 * @author ian
 * @param <T>
 */
public class CoreGameListener extends GameListener {

    public CoreGameListener(Gameplay gameplay) {
        super(gameplay);
    }

    @EventHandler
    public void onPlayerMoveOutOfArena(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            return;
        }

        Game game = game(e.getPlayer());
        if (game == null) {
            return;
        }

        if (!game.getParticipants().isStarted()) {
            return;
        }

        if (game.getArena().getMain().contains(e.getTo())) {
            return;
        }

        game.events().quit(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Location location = p.getLocation();
        Game game = game(location); // use location in case of being queued
        if (game == null) {
            return;
        }

        UUID player = p.getUniqueId();

        UUID killer = null;
        Player killerP = p.getKiller();
        if (killerP != null) {
            killer = killerP.getUniqueId();
        }

        DamageCause cause = null;
        EntityDamageEvent lastDamageCause = e.getEntity().getLastDamageCause();
        if (lastDamageCause != null) {
            cause = lastDamageCause.getCause();
        }

        long timestamp = System.currentTimeMillis();

        Death death = new Death(player, killer, cause, timestamp, location);
        game.getStats().addDeath(death);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Game g = game(e.getPlayer());
        if (g != null) {
            g.events().quit(e.getPlayer());
        }
    }

}
