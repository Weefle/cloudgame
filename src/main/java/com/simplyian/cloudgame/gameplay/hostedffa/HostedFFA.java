/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simplyian.cloudgame.gameplay.hostedffa;

import com.simplyian.cloudgame.CloudGame;
import com.simplyian.cloudgame.game.Game;
import com.simplyian.cloudgame.gameplay.Gameplay;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author ian
 * @param <T>
 */
public abstract class HostedFFA<T extends HostedFFAState> extends Gameplay<T> {

    private Game<T> game;

    private Set<UUID> prizes = new HashSet<>();

    protected HostedFFA(CloudGame plugin, String name) {
        super(plugin, name);
    }

    public Game<T> getGame() {
        return game;
    }

    public void setGame(Game<T> game) {
        this.game = game;
    }

    /**
     * Adds the player to the list of people who deserve prizes.
     *
     * @param p
     */
    public void addPrize(Player p) {
        prizes.add(p.getUniqueId());
    }

    /**
     * Tries to redeem a prize.
     *
     * @param p
     * @return
     */
    public boolean redeemPrize(Player p) {
        if (!prizes.contains(p.getUniqueId())) {
            return false;
        }
        prizes.remove(p.getUniqueId());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ccrates give 3 " + p.getName() + " 3");
        return true;
    }
}