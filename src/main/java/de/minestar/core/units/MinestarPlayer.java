package de.minestar.core.units;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    // LISTS
    private ConcurrentHashMap<String, String> stringList = new ConcurrentHashMap<String, String>();
    private ConcurrentHashMap<String, Boolean> booleanList = new ConcurrentHashMap<String, Boolean>();
    private ConcurrentHashMap<String, Byte> byteList = new ConcurrentHashMap<String, Byte>();
    private ConcurrentHashMap<String, Integer> integerList = new ConcurrentHashMap<String, Integer>();
    private ConcurrentHashMap<String, Double> doubleList = new ConcurrentHashMap<String, Double>();
    private ConcurrentHashMap<String, Float> floatList = new ConcurrentHashMap<String, Float>();
    private ConcurrentHashMap<String, Long> longList = new ConcurrentHashMap<String, Long>();
    private ConcurrentHashMap<String, Short> shortList = new ConcurrentHashMap<String, Short>();
    private ConcurrentHashMap<String, String> locationList = new ConcurrentHashMap<String, String>();

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
        this.stringList.put(key, value);
    }

    public void setBoolean(String key, boolean value) {
        this.booleanList.put(key, value);
    }

    public void setByte(String key, byte value) {
        this.byteList.put(key, value);
    }

    public void setInteger(String key, int value) {
        this.integerList.put(key, value);
    }

    public void setDouble(String key, double value) {
        this.doubleList.put(key, value);
    }

    public void setFloat(String key, float value) {
        this.floatList.put(key, value);
    }

    public void setLong(String key, long value) {
        this.longList.put(key, value);
    }

    public void setShort(String key, short value) {
        this.shortList.put(key, value);
    }

    public void setLocation(String key, Location location) {
        this.locationList.put(key, BlockUtils.LocationToString(location));
    }

    // ///////////////////////////////////////////////
    //
    // GET VALUES
    //
    // ///////////////////////////////////////////////

    public String getString(String key) {
        return this.stringList.get(key);
    }

    public boolean getBoolean(String key) {
        return this.booleanList.get(key);
    }

    public byte getByte(String key) {
        return this.byteList.get(key);
    }

    public int getInteger(String key) {
        return this.integerList.get(key);
    }

    public double getDouble(String key) {
        return this.doubleList.get(key);
    }

    public float getFloat(String key) {
        return this.floatList.get(key);
    }

    public long getLong(String key) {
        return this.longList.get(key);
    }

    public short getShort(String key) {
        return this.shortList.get(key);
    }

    public Location getLocation(String key) {
        return BlockUtils.LocationFromString(this.locationList.get(key));
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

            // SAVE STRINGS
            for (Map.Entry<String, String> entry : this.stringList.entrySet()) {
                NBTTag.setString(entry.getKey(), entry.getValue());
            }

            // SAVE SHORT
            for (Map.Entry<String, Short> entry : this.shortList.entrySet()) {
                NBTTag.setShort(entry.getKey(), entry.getValue());
            }

            // SAVE INTEGER
            for (Map.Entry<String, Integer> entry : this.integerList.entrySet()) {
                NBTTag.setInt(entry.getKey(), entry.getValue());
            }

            // SAVE LONG
            for (Map.Entry<String, Long> entry : this.longList.entrySet()) {
                NBTTag.setLong(entry.getKey(), entry.getValue());
            }

            // SAVE DOUBLE
            for (Map.Entry<String, Double> entry : this.doubleList.entrySet()) {
                NBTTag.setDouble(entry.getKey(), entry.getValue());
            }

            // SAVE FLOAT
            for (Map.Entry<String, Float> entry : this.floatList.entrySet()) {
                NBTTag.setFloat(entry.getKey(), entry.getValue());
            }

            // SAVE BYTE
            for (Map.Entry<String, Byte> entry : this.byteList.entrySet()) {
                NBTTag.setByte(entry.getKey(), entry.getValue());
            }

            // SAVE BOOLEAN
            for (Map.Entry<String, Boolean> entry : this.booleanList.entrySet()) {
                NBTTag.setBoolean(entry.getKey(), entry.getValue());
            }

            // SAVE LOCATIONS
            for (Map.Entry<String, String> entry : this.locationList.entrySet()) {
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
    private NBTTagCompound getSubTag(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing subtag '" + key + "'.");
            return null;
        } else {
            return NBTTag.getCompound(key);
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

    private byte loadValueAsByte(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getByte(key);
        }
    }

    private long loadValueAsLong(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getLong(key);
        }
    }

    private int loadValueAsInteger(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getInt(key);
        }
    }

    private float loadValueAsFloat(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getFloat(key);
        }
    }

    private double loadValueAsDouble(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getDouble(key);
        }
    }

    private short loadValueAsShort(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getShort(key);
        }
    }

    private boolean loadValueAsBoolean(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(Core.pluginName, "File is missing value '" + key + "'.");
            return false;
        } else {
            return NBTTag.getBoolean(key);
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

                // LOAD STRINGS
                String s_value;
                NBTTagCompound subTag = this.getSubTag(NBTTag, "strings");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        s_value = this.loadValueAsString(NBTTag, thisTag.getName());
                        if (s_value != null)
                            this.stringList.put(thisTag.getName(), s_value);
                    }
                }

                // LOAD LOCATIONS
                String loc_value;
                subTag = this.getSubTag(NBTTag, "locations");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        loc_value = this.loadValueAsString(NBTTag, thisTag.getName());
                        if (loc_value != null)
                            this.locationList.put(thisTag.getName(), loc_value);
                    }
                }

                // LOAD BOOLEAN
                boolean b_value;
                subTag = this.getSubTag(NBTTag, "boolean");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        b_value = this.loadValueAsBoolean(NBTTag, thisTag.getName());
                        this.booleanList.put(thisTag.getName(), b_value);
                    }
                }

                // LOAD BYTE
                byte byte_value;
                subTag = this.getSubTag(NBTTag, "byte");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        byte_value = this.loadValueAsByte(NBTTag, thisTag.getName());
                        this.byteList.put(thisTag.getName(), byte_value);
                    }
                }

                // LOAD INTEGER
                int i_value;
                subTag = this.getSubTag(NBTTag, "integer");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        i_value = this.loadValueAsInteger(NBTTag, thisTag.getName());
                        this.integerList.put(thisTag.getName(), i_value);
                    }
                }

                // LOAD SHORT
                short short_value;
                subTag = this.getSubTag(NBTTag, "short");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        short_value = this.loadValueAsShort(NBTTag, thisTag.getName());
                        this.shortList.put(thisTag.getName(), short_value);
                    }
                }

                // LOAD DOUBLE
                double d_value;
                subTag = this.getSubTag(NBTTag, "double");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        d_value = this.loadValueAsDouble(NBTTag, thisTag.getName());
                        this.doubleList.put(thisTag.getName(), d_value);
                    }
                }

                // LOAD FLOAT
                float f_value;
                subTag = this.getSubTag(NBTTag, "float");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        f_value = this.loadValueAsFloat(NBTTag, thisTag.getName());
                        this.floatList.put(thisTag.getName(), f_value);
                    }
                }

                // LOAD LONG
                long long_value;
                subTag = this.getSubTag(NBTTag, "long");
                for (Object base : subTag.d()) {
                    if (base instanceof NBTBase) {
                        NBTBase thisTag = (NBTBase) base;
                        long_value = this.loadValueAsLong(NBTTag, thisTag.getName());
                        this.longList.put(thisTag.getName(), long_value);
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
