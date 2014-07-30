/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pw.ian.cloudgame.commands.hostedffa;

import org.bukkit.entity.Player;
import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.cloudgame.gameplay.hostedffa.HostedFFA;
import pw.ian.cloudgame.model.arena.Arena;

/**
 *
 * @author ian
 */
public class FFASetSpawnCommand extends PlayerCommandHandler {

    private final HostedFFA ffa;

    public FFASetSpawnCommand(HostedFFA ffa) {
        super(ffa.getPlugin(), "setspawn");
        this.ffa = ffa;
        setDescription("Sets a spawn on the " + ffa.getName() + " map.");
        setUsage("/" + ffa.getId() + " setspawn <hill region> <main region>");
        setPermission("mattmg.admin");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("matttmg.admin")) {
            ffa.sendGameMessage(player, "You can't use this command.");
            return;
        }

        if (args.length <= 1) {
            sendUsageMessage(player);
            return;
        }

        Arena arena = ffa.getPlugin().getModelManager().getArenas().find(player, args[0]);
        if (arena == null) {
            ffa.sendGameMessage(player, "That arena does not exist.");
            return;
        }

        int spawnNumber = 0;
        try {
            spawnNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            ffa.sendGameMessage(player, "That spawn id is an invalid number.");
            return;
        }

        if (spawnNumber < 1 || spawnNumber > 5) {
            ffa.sendGameMessage(player, "You can only set spawns 1 through 5.");
            return;
        }

        arena.setSpawn(spawnNumber - 1, player.getLocation());
        ffa.sendGameMessage(player, "Spawn " + spawnNumber + " has been set.");
    }

}
