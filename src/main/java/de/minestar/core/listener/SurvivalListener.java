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

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;

import de.minestar.core.MinestarCore;
import de.minestar.core.units.DataHandler;
import de.minestar.core.units.RespawnThread;

public class SurvivalListener implements Listener {

    private DataHandler dataHandler;

    private HashMap<String, String> playerMap = new HashMap<String, String>();
    private HashMap<String, String> deadPlayer = new HashMap<String, String>();

    public SurvivalListener() {
        this.dataHandler = new DataHandler();
    }

    public String getWorld(String player) {
        return this.playerMap.get(player);
    }

    public void setWorld(String player, String world) {
        this.playerMap.put(player, world);
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        String fromWorld = this.getWorld(event.getPlayer().getName());
        if (fromWorld == null) {
            fromWorld = "main";
        }
        if (event.getFrom().getWorld().getName().toLowerCase().startsWith("survival")) {
            fromWorld = "survival";
        }

        String toWorld = "main";
        if (event.getTo().getWorld().getName().toLowerCase().startsWith("survival")) {
            toWorld = "survival";
        }

        if (this.getWorld(event.getPlayer().getName()) == null || !fromWorld.equalsIgnoreCase(toWorld)) {

            CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
            this.dataHandler.savePlayerData(fromWorld, craftPlayer.getHandle());

            this.setWorld(event.getPlayer().getName(), toWorld);
            if (dataHandler.playerDataExists(toWorld, event.getPlayer().getName())) {
                this.dataHandler.loadPlayerData(toWorld, craftPlayer.getHandle());
            } else if (toWorld.equalsIgnoreCase("survival")) {
                craftPlayer.getInventory().clear();
                craftPlayer.getInventory().setHelmet(null);
                craftPlayer.getInventory().setChestplate(null);
                craftPlayer.getInventory().setLeggings(null);
                craftPlayer.getInventory().setBoots(null);
                craftPlayer.getEnderChest().clear();
                craftPlayer.setExp(0);
                craftPlayer.setTotalExperience(0);
                craftPlayer.setLevel(0);
                craftPlayer.setFoodLevel(20);
                craftPlayer.setAllowFlight(false);
                for (PotionEffect effect : craftPlayer.getActivePotionEffects()) {
                    craftPlayer.removePotionEffect(effect.getType());
                }
                craftPlayer.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String deathWorld = event.getEntity().getWorld().getName();
        if (deathWorld.toLowerCase().startsWith("survival")) {
            deathWorld = "survival";
        } else {
            deathWorld = "main";
        }
        this.deadPlayer.put(event.getEntity().getName(), deathWorld);
        this.setWorld(event.getEntity().getName(), deathWorld);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        RespawnThread thread = new RespawnThread(this, event.getPlayer());
        Bukkit.getScheduler().scheduleSyncDelayedTask(MinestarCore.INSTANCE, thread, 1l);
    }
}
