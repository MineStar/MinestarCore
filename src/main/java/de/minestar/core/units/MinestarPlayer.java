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
        this.group = UtilPermissions.getGroupName(player);

        // INITIALIZE NICKNAME & LISTNAME
        this.setValue("nickName", this.playerName);
        this.setValue("listName", this.playerName);

        // LOAD DATA
        this.loadData();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNickName() {
        return this.getValueAsString("nickName");
    }

    public void setNickName(String nickName) {
        this.setValue("nickName", nickName);
        this.updateBukkitPlayer();
    }

    public String getListName() {
        return this.getValueAsString("listName");
    }

    public void setListName(String listName) {
        this.setValue("nickName", listName);
        this.updateBukkitPlayer();
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

    public void setValue(String key, String value) {
        this.valueList.put(key, value);
    }

    public void setValue(String key, boolean value) {
        this.setValue(key, String.valueOf(value));
    }

    public void setValue(String key, int value) {
        this.setValue(key, String.valueOf(value));
    }

    public void setValue(String key, double value) {
        this.setValue(key, String.valueOf(value));
    }

    public void setValue(String key, float value) {
        this.setValue(key, String.valueOf(value));
    }

    public void setValue(String key, Location location) {
        this.setValue(key, BlockUtils.LocationToString(location));
    }

    // ///////////////////////////////////////////////
    //
    // GET VALUES
    //
    // ///////////////////////////////////////////////

    public String getValueAsString(String key) {
        return this.valueList.get(key);
    }

    public boolean getValueAsBoolean(String key) {
        return Boolean.valueOf(this.getValueAsString(key));
    }

    public int getValueAsInteger(String key) {
        return Integer.valueOf(this.getValueAsString(key));
    }

    public double getValueAsDouble(String key) {
        return Double.valueOf(this.getValueAsString(key));
    }

    public float getValueAsFloat(String key) {
        return Float.valueOf(this.getValueAsString(key));
    }

    public Location getValueAsLocation(String key) {
        return BlockUtils.LocationFromString(this.getValueAsString(key));
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
