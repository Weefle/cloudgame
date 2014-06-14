/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simplyian.cloudgame.gameplay;

import com.simplyian.cloudgame.CloudGame;
import com.simplyian.cloudgame.game.Game;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Contains all game gameplay.
 *
 * @author ian
 * @param <T> The type of state associated with this game.
 */
public abstract class Gameplay<T extends State> {

    private final CloudGame plugin;

    private final String id;

    protected Gameplay(CloudGame plugin, String id) {
        this.plugin = plugin;
        this.id = id;
    }

    public CloudGame getPlugin() {
        return plugin;
    }

    public String getId() {
        return id;
    }

    /**
     * Called when the plugin is enabled. Set up listeners etc here.
     */
    public void onEnable() {
    }

    /**
     * Called when the plugin is disabled.
     */
    public void onDisable() {
    }

    /**
     * Creates a new state associated with this game gameplay.
     *
     * @return
     */
    public T newState() {
        try {
            return (T) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Gameplay.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Gameplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Sends a game related message to the given player.
     *
     * @param p
     * @param message
     */
    public void sendGameMessage(Player p, String message) {
        p.sendMessage(ChatColor.DARK_RED + "[" + id + "] " + ChatColor.RED + message);
    }

    /**
     * Sets up the gameplay.
     *
     * @param g
     */
    public abstract void setup(Game<T> g);

    /**
     * Called when a message is sent.
     *
     * @param game
     * @param type
     * @param message
     */
    public abstract void onReceive(Game<T> game, String type, Map<String, Object> message);

    /**
     * Handles when a player quits. They are not necessarily in this game.
     *
     * @param game The game
     * @param player The quitting player
     */
    public abstract void handleQuit(Game<T> game, Player player);

}
