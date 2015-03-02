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

package de.minestar.core;

import java.io.File;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.minestar.core.listener.ConnectionListener;
import de.minestar.core.manager.DatabaseManager;
import de.minestar.core.manager.PlayerManager;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class MinestarCore extends AbstractCore {

    public static MinestarCore INSTANCE = null;
    public static String NAME = "MinestarCore";

    public static File dataFolder;
    public static GroupManager groupManager = null;
    public static DatabaseManager databaseManager;

    /**
     * Manager
     */
    private static PlayerManager playerManager;

    /**
     * Listener
     */
    private ConnectionListener connectionListener;

    public MinestarCore() {
        super(NAME);
        INSTANCE = this;
    }

    @Override
    protected boolean commonDisable() {
        // SAVE PLAYERS
        playerManager.savePlayers();
        return super.commonDisable();
    }

    @Override
    protected boolean loadingConfigs(File dataFolder) {
        // GET GROUPMANAGER
        this.getGroupManager();

        MinestarCore.dataFolder = this.getDataFolder();

        // CREATE FOLDER "playerdata"
        File playerFolder = new File(this.getDataFolder(), "playerdata");
        playerFolder.mkdir();

        return super.loadingConfigs(dataFolder);
    }

    @Override
    protected boolean createManager() {
        MinestarCore.databaseManager = new DatabaseManager(MinestarCore.NAME, new File(getDataFolder(), "sql_playerData.yml"));
        MinestarCore.playerManager = new PlayerManager();
        return super.createManager();
    }

    @Override
    protected boolean createListener() {
        this.connectionListener = new ConnectionListener(playerManager);
        return super.createListener();
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(this.connectionListener, this);
//        pm.registerEvents(new SurvivalListener(), this);
        return super.registerEvents(pm);
    }

    private void getGroupManager() {
        Plugin gm = Bukkit.getServer().getPluginManager().getPlugin("GroupManager");
        if (gm != null && gm.isEnabled())
            MinestarCore.groupManager = (GroupManager) gm;
        else
            ConsoleUtils.printError(MinestarCore.NAME, "Can't find GroupManager was not found!");
    }

    public static MinestarPlayer getPlayer(String playerName) {
        return playerManager.getPlayer(playerName);
    }

    public static MinestarPlayer getPlayer(Player player) {
        return MinestarCore.getPlayer(player.getName());
    }
}
