/*
 * Copyright (C) 2013 MineStar.de 
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

package de.minestar.core.units;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minestar.core.listener.SurvivalListener;

public class RespawnThread implements Runnable {

    private Player player;
    private SurvivalListener listener;

    public RespawnThread(SurvivalListener listener, Player player) {
        this.listener = listener;
        this.player = player;
    }

    @Override
    public void run() {
        String deathWorld = listener.getWorld(player.getName());
        if (deathWorld == null) {
            deathWorld = "main";
        }

        if (deathWorld.toLowerCase().startsWith("survival")) {
            deathWorld = "survival";
        }

        String toWorld = "main";
        if (player.getWorld().getName().toLowerCase().startsWith("survival")) {
            toWorld = "survival";
        }
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if ((deathWorld.equalsIgnoreCase("survival") && toWorld.equalsIgnoreCase("main")) || (deathWorld.equalsIgnoreCase("main") && toWorld.equalsIgnoreCase("survival"))) {
            craftPlayer.setHealth(20);
            craftPlayer.setSaturation(20);
            craftPlayer.setExp(0);
            craftPlayer.setTotalExperience(0);
            if (deathWorld.equalsIgnoreCase("survival")) {
                listener.getDataHandler().savePlayerData("survival", craftPlayer.getHandle());
            } else if (deathWorld.equalsIgnoreCase("main")) {
                listener.getDataHandler().savePlayerData("main", craftPlayer.getHandle());
            }
            listener.setWorld(player.getName(), toWorld);
            listener.getDataHandler().loadPlayerData(toWorld, craftPlayer.getHandle());
        } else if (deathWorld.equalsIgnoreCase(toWorld)) {
            listener.getDataHandler().savePlayerData(toWorld, craftPlayer.getHandle());
        }
    }

}
