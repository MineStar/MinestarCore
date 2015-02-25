/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MinestarCore.
 * 
 * MinestarCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MinestarCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MinestarCore.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.core.listener;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.minestar.core.MinestarCore;
import de.minestar.core.manager.PlayerManager;
import de.minestar.core.units.MinestarGroup;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.minestarlibrary.events.PlayerChangedNameEvent;

public class ConnectionListener implements Listener {

    private PlayerManager playerManager;

    public ConnectionListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        MinestarPlayer thisPlayer = this.playerManager.getPlayer(event.getPlayer());
        thisPlayer.setOnline();
        thisPlayer.updateGroup();
        thisPlayer.updateBukkitPlayer();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.onPlayerDisconnect(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerKick(PlayerKickEvent event) {
        // EVENT IS CANCELLED? => RETURN
        if (event.isCancelled())
            return;

        this.onPlayerDisconnect(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangeNick(PlayerChangedNameEvent event) {
        // get old player
        MinestarPlayer thisPlayer = this.playerManager.getPlayer(event.getOldName());

        // just to make sure: set nicks
        thisPlayer.setNickName(event.getNewName());
        thisPlayer.setListName(event.getNewName());

        // get old group
        MinestarGroup oldMSGroup = thisPlayer.getMinestarGroup();

        // unload both playernames
        this.playerManager.removePlayer(event.getOldName());
        this.playerManager.removePlayer(event.getNewName());

        // rename file
        File oldPlayerFile = new File(new File(MinestarCore.dataFolder, "playerdata"), event.getOldName() + ".dat");
        File newPlayerFile = new File(new File(MinestarCore.dataFolder, "playerdata"), event.getNewName() + ".dat");
        newPlayerFile.delete();
        oldPlayerFile.renameTo(newPlayerFile);

        // reload player
        this.playerManager.getPlayer(event.getNewName()).setGroup(oldMSGroup);
    }

    private void onPlayerDisconnect(Player player) {
        this.playerManager.removePlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.playerManager.getPlayer(event.getPlayer()).updateBukkitPlayer();
    }

}
