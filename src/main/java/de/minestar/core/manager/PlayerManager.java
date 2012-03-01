package de.minestar.core.manager;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.core.units.MinestarPlayer;

public class PlayerManager {

    private ConcurrentHashMap<String, MinestarPlayer> onlinePlayerList, offlinePlayerList;

    public PlayerManager() {
        this.refreshPlayerList();
    }

    // /////////////////////////////////////////////
    //
    // UPDATE METHODS
    //
    // /////////////////////////////////////////////

    private void refreshPlayerList() {
        offlinePlayerList = new ConcurrentHashMap<String, MinestarPlayer>();
        onlinePlayerList = new ConcurrentHashMap<String, MinestarPlayer>();
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            this.addPlayer(player);
        }
    }

    public void savePlayers() {
        for (MinestarPlayer player : this.offlinePlayerList.values())
            player.saveData();

        for (MinestarPlayer player : this.onlinePlayerList.values())
            player.saveData();
    }

    // /////////////////////////////////////////////
    //
    // ADD A PLAYER
    //
    // /////////////////////////////////////////////

    public MinestarPlayer addPlayer(Player player) {
        MinestarPlayer thisPlayer = this.offlinePlayerList.get(player.getName());
        if (thisPlayer == null) {
            thisPlayer = new MinestarPlayer(player);
        } else {
            this.offlinePlayerList.remove(player.getName());
        }
        thisPlayer.setOnline();
        this.onlinePlayerList.put(player.getName(), thisPlayer);
        return thisPlayer;
    }

    // /////////////////////////////////////////////
    //
    // REMOVE A PLAYER
    //
    // /////////////////////////////////////////////

    public MinestarPlayer removePlayer(Player player) {
        return this.removePlayer(player.getName());
    }

    public MinestarPlayer removePlayer(String playerName) {
        MinestarPlayer removedPlayer = this.onlinePlayerList.get(playerName);
        if (removedPlayer != null) {
            this.offlinePlayerList.put(playerName, removedPlayer);
            removedPlayer.setOffline();
        }
        return this.onlinePlayerList.remove(playerName);
    }

    // /////////////////////////////////////////////
    //
    // GET A PLAYER
    //
    // /////////////////////////////////////////////

    public MinestarPlayer getPlayer(Player player) {
        MinestarPlayer thisPlayer = this.onlinePlayerList.get(player.getName());
        return (thisPlayer != null ? thisPlayer : this.addPlayer(player));
    }
}
