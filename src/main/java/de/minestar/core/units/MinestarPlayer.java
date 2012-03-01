package de.minestar.core.units;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.core.Core;
import de.minestar.core.exceptions.BukkitPlayerOfflineException;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class MinestarPlayer {
    private boolean online = true;
    private final String playerName;
    private String group;

    private HashMap<String, String> valueList = new HashMap<String, String>();

    public MinestarPlayer(Player player) {
        this.playerName = player.getName();
        this.update();
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

    public void update() {
        // ONLY UPDATE, IF ONLINE
        if (this.isOffline())
            return;

        // GET THE PLAYER
        Player player = PlayerUtils.getOnlinePlayer(this.playerName);
        if (player == null)
            throw new BukkitPlayerOfflineException(this.playerName);

        // UPDATE THE PLAYER
        this.updateGroup();

        // INITIALIZE NICKNAME & LISTNAME
        this.setString("nickName", this.playerName);
        this.setString("listName", this.playerName);

        // LOAD DATA
        this.loadData();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNickName() {
        return this.getString("nickName");
    }

    public void setNickName(String nickName) {
        this.setString("nickName", nickName);
        this.updateBukkitPlayer();
    }

    public String getListName() {
        return this.getString("listName");
    }

    public void setListName(String listName) {
        this.setString("nickName", listName);
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

    // ///////////////////////////////////////////////
    //
    // SET VALUES
    //
    // ///////////////////////////////////////////////

    public void setString(String key, String value) {
        this.valueList.put(key, value);
    }

    public void setBoolean(String key, boolean value) {
        this.setString(key, String.valueOf(value));
    }

    public void setByte(String key, long value) {
        this.setString(key, String.valueOf(value));
    }

    public void setInteger(String key, int value) {
        this.setString(key, String.valueOf(value));
    }

    public void setDouble(String key, double value) {
        this.setString(key, String.valueOf(value));
    }

    public void setFloat(String key, float value) {
        this.setString(key, String.valueOf(value));
    }

    public void setLong(String key, long value) {
        this.setString(key, String.valueOf(value));
    }

    public void setShort(String key, short value) {
        this.setString(key, String.valueOf(value));
    }

    public void setLocation(String key, Location location) {
        this.setString(key, BlockUtils.LocationToString(location));
    }

    // ///////////////////////////////////////////////
    //
    // GET VALUES
    //
    // ///////////////////////////////////////////////

    public String getString(String key) {
        return this.valueList.get(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(this.getString(key));
    }

    public byte getByte(String key) {
        return Byte.valueOf(this.getString(key));
    }

    public int getInteger(String key) {
        return Integer.valueOf(this.getString(key));
    }

    public double getDouble(String key) {
        return Double.valueOf(this.getString(key));
    }

    public float getFloat(String key) {
        return Float.valueOf(this.getString(key));
    }

    public long getLong(String key) {
        return Long.valueOf(this.getString(key));
    }

    public short getShort(String key) {
        return Short.valueOf(this.getString(key));
    }

    public Location getLocation(String key) {
        return BlockUtils.LocationFromString(this.getString(key));
    }

    /**
     * This method will save the playerfile
     */
    public void saveData() {
        File file = new File(Core.dataFolder, "\\playerdata\\" + this.playerName + ".dat");
        try {
            // CREAT TEMP-FILE
            File tmpFile = new File(Core.dataFolder, "\\playerdata\\tmp_" + this.playerName + ".dat");
            FileOutputStream fileoutputstream = new FileOutputStream(tmpFile);
            NBTTagCompound NBTTag = new NBTTagCompound();
            for (Map.Entry<String, String> entry : this.valueList.entrySet()) {
                NBTTag.setString(entry.getKey(), entry.getValue());
            }

            // GZIP THE TEMP-FILE
            CompressedStreamTools.writeGzippedCompoundToOutputStream(NBTTag, fileoutputstream);

            // DELETE OLD FILE
            fileoutputstream.close();
            if (file.exists()) {
                file.delete();
            }
            // RENAME THE TMP FILE
            tmpFile.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is a helper method for reading single values out of the file
     * 
     * @param NBTTag
     * @param key
     * @return the value as a string
     */
    private String loadValueAsString(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return null;
        } else {
            return NBTTag.getString(key);
        }
    }

    /**
     * This method will load all the playerdata from the file
     */
    private void loadData() {
        File file = new File(Core.dataFolder, "\\playerdata\\" + this.playerName + ".dat");
        if (file != null && file.exists()) {
            try {
                // OPEN STREAM
                FileInputStream FIS = new FileInputStream(file);
                NBTTagCompound NBTTag = CompressedStreamTools.loadGzippedCompoundFromOutputStream(FIS);

                // LOAD DATA
                String value;
                for (Object base : NBTTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        value = this.loadValueAsString(NBTTag, thisTag.getName());
                        if (value != null)
                            this.valueList.put(thisTag.getName(), value);
                    }
                }

                // CLOSE STREAM
                FIS.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
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
}
