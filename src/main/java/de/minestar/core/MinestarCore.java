package de.minestar.core;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.core.listener.ConnectionListener;
import de.minestar.core.manager.PlayerManager;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class MinestarCore extends JavaPlugin {

    public static String pluginName = "MinestarCore";
    public static File dataFolder;

    /**
     * Manager
     */
    private static PlayerManager playerManager;

    /**
     * Listener
     */
    private ConnectionListener connectionListener;

    @Override
    public void onDisable() {
        // SAVE PLAYERS
        playerManager.savePlayers();

        // PRINT INFO
        ConsoleUtils.printInfo(pluginName, "Disabled v" + this.getDescription().getVersion() + "!");
    }

    @Override
    public void onEnable() {
        dataFolder = this.getDataFolder();
        dataFolder.mkdirs();

        File playerFolder = new File(dataFolder, "\\playerdata");
        playerFolder.mkdir();

        // CREATE MANAGER, LISTENER, COMMANDS
        this.createManager();
        this.createListener();

        // REGISTER EVENTS
        this.registerEvents();

        // PRINT INFO
        ConsoleUtils.printInfo(pluginName, "Enabled v" + this.getDescription().getVersion() + "!");
    }

    private void createManager() {
        playerManager = new PlayerManager();
    }

    private void createListener() {
        this.connectionListener = new ConnectionListener(playerManager);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.connectionListener, this);
    }

    public static MinestarPlayer getPlayer(Player player) {
        return playerManager.getPlayer(player);
    }
}
