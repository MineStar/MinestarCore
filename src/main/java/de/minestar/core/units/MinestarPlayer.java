package de.minestar.core.units;

import java.io.File;

import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.core.MinestarCore;
import de.minestar.core.data.Data;
import de.minestar.core.data.DataType;
import de.minestar.core.exceptions.BukkitPlayerOfflineException;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class MinestarPlayer {
    private boolean online = true;
    private final String playerName;
    private String group;
    private Data data;

    public MinestarPlayer(Player player) {
        this.playerName = player.getName();
        this.update(true);
    }

    public void setOnline() {
        this.online = true;
    }

    public void setOffline() {
        this.online = false;
    }

    public boolean isOnline() {
        return this.online;
    }

    public boolean isOffline() {
        return !this.isOnline();
    }

    private void initialize() {
        // ONLY UPDATE, IF ONLINE
        if (this.isOffline())
            return;

        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.playerName);
        if (player == null)
            throw new BukkitPlayerOfflineException(this.playerName);

        // UPDATE THE PLAYER
        this.updateGroup();

        this.data = new Data(new File(MinestarCore.dataFolder, "playerdata"), playerName, DataType.NBT);
    }

    public void update(boolean forceUpdate) {
        // FORCE UPDATE = RELOAD FILE
        if (forceUpdate) {
            this.saveData();
            this.initialize();
        }

        // INITIALIZE NICKNAME & LISTNAME
        this.data.setString("nickName", this.playerName);
        this.data.setString("listName", this.playerName);

        // LOAD DATA
        this.data.load();
        this.updateBukkitPlayer();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNickName() {
        return this.data.getString("nickName");
    }

    public void setNickName(String nickName) {
        this.data.setString("nickName", nickName);
        this.updateBukkitPlayer();
    }

    public String getListName() {
        return this.data.getString("listName");
    }

    public void setListName(String listName) {
        this.data.setString("listName", listName);
        this.updateBukkitPlayer();
    }

    public String updateGroup() {
        // ONLY UPDATE, IF ONLINE
        if (this.isOffline())
            return "default";

        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.playerName);
        if (player == null)
            throw new BukkitPlayerOfflineException(this.playerName);

        this.group = UtilPermissions.getGroupName(player);
        return getGroup();
    }

    public String getGroup() {
        return group;
    }

    public boolean isInGroup(String groupName) {
        return this.group.equalsIgnoreCase(groupName);
    }

    /**
     * Update the bukkitplayer
     */
    public void updateBukkitPlayer() {
        // ONLY UPDATE, IF ONLINE
        if (this.isOffline())
            return;

        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.playerName);
        if (player == null)
            throw new BukkitPlayerOfflineException(this.playerName);

        player.setDisplayName(this.getNickName());
        player.setPlayerListName(this.getListName());
    }

    public void saveData() {
        this.data.save();
    }
}
