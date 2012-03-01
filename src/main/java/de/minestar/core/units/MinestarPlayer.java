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

import de.minestar.core.MinestarCore;
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
        this.setString("listName", listName);
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
        File file = new File(MinestarCore.dataFolder, "\\playerdata\\" + this.playerName + ".dat");
        try {
            // CREAT TEMP-FILE
            File tmpFile = new File(MinestarCore.dataFolder, "\\playerdata\\tmp_" + this.playerName + ".dat");
            FileOutputStream fileoutputstream = new FileOutputStream(tmpFile);

            NBTTagCompound NBTTag = new NBTTagCompound();

            // SAVE STRINGS
            NBTTagCompound stringTag = new NBTTagCompound();
            for (Map.Entry<String, String> entry : this.stringList.entrySet()) {
                stringTag.setString(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("strings", stringTag);

            // SAVE SHORT
            NBTTagCompound shortTag = new NBTTagCompound();
            for (Map.Entry<String, Short> entry : this.shortList.entrySet()) {
                shortTag.setShort(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("short", shortTag);

            // SAVE INTEGER
            NBTTagCompound intTag = new NBTTagCompound();
            for (Map.Entry<String, Integer> entry : this.integerList.entrySet()) {
                intTag.setInt(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("integer", intTag);

            // SAVE LONG
            NBTTagCompound longTag = new NBTTagCompound();
            for (Map.Entry<String, Long> entry : this.longList.entrySet()) {
                longTag.setLong(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("long", longTag);

            // SAVE DOUBLE
            NBTTagCompound doubleTag = new NBTTagCompound();
            for (Map.Entry<String, Double> entry : this.doubleList.entrySet()) {
                doubleTag.setDouble(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("double", doubleTag);

            // SAVE FLOAT
            NBTTagCompound floatTag = new NBTTagCompound();
            for (Map.Entry<String, Float> entry : this.floatList.entrySet()) {
                floatTag.setFloat(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("float", floatTag);

            // SAVE BYTE
            NBTTagCompound byteTag = new NBTTagCompound();
            for (Map.Entry<String, Byte> entry : this.byteList.entrySet()) {
                byteTag.setByte(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("byte", byteTag);

            // SAVE BOOLEAN
            NBTTagCompound boolTag = new NBTTagCompound();
            for (Map.Entry<String, Boolean> entry : this.booleanList.entrySet()) {
                boolTag.setBoolean(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("boolean", boolTag);

            // SAVE LOCATIONS
            NBTTagCompound locTag = new NBTTagCompound();
            for (Map.Entry<String, String> entry : this.locationList.entrySet()) {
                locTag.setString(entry.getKey(), entry.getValue());
            }
            NBTTag.setCompound("locations", locTag);

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
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing subtag '" + key + "'.");
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
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return null;
        } else {
            return NBTTag.getString(key);
        }
    }

    private byte loadValueAsByte(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getByte(key);
        }
    }

    private long loadValueAsLong(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getLong(key);
        }
    }

    private int loadValueAsInteger(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getInt(key);
        }
    }

    private float loadValueAsFloat(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getFloat(key);
        }
    }

    private double loadValueAsDouble(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getDouble(key);
        }
    }

    private short loadValueAsShort(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return 0;
        } else {
            return NBTTag.getShort(key);
        }
    }

    private boolean loadValueAsBoolean(NBTTagCompound NBTTag, String key) {
        if (!NBTTag.hasKey(key)) {
            ConsoleUtils.printError(MinestarCore.pluginName, "File is missing value '" + key + "'.");
            return false;
        } else {
            return NBTTag.getBoolean(key);
        }
    }

    /**
     * This method will load all the playerdata from the file
     */
    private void loadData() {
        File file = new File(MinestarCore.dataFolder, "\\playerdata\\" + this.playerName + ".dat");
        if (file != null && file.exists()) {
            try {
                // OPEN STREAM
                FileInputStream FIS = new FileInputStream(file);
                NBTTagCompound NBTTag = CompressedStreamTools.loadGzippedCompoundFromOutputStream(FIS);

                // LOAD STRINGS
                String s_value;
                NBTTagCompound subTag = this.getSubTag(NBTTag, "strings");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            s_value = this.loadValueAsString(subTag, thisTag.getName());
                            if (s_value != null)
                                this.stringList.put(thisTag.getName(), s_value);
                            System.out.println(thisTag.getName() + " = " + this.stringList.get(thisTag.getName()));
                        }
                    }

                // LOAD LOCATIONS
                String loc_value;
                subTag = this.getSubTag(NBTTag, "locations");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            loc_value = this.loadValueAsString(subTag, thisTag.getName());
                            if (loc_value != null)
                                this.locationList.put(thisTag.getName(), loc_value);
                        }
                    }

                // LOAD BOOLEAN
                boolean b_value;
                subTag = this.getSubTag(NBTTag, "boolean");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            b_value = this.loadValueAsBoolean(subTag, thisTag.getName());
                            this.booleanList.put(thisTag.getName(), b_value);
                        }
                    }

                // LOAD BYTE
                byte byte_value;
                subTag = this.getSubTag(NBTTag, "byte");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            byte_value = this.loadValueAsByte(subTag, thisTag.getName());
                            this.byteList.put(thisTag.getName(), byte_value);
                        }
                    }

                // LOAD INTEGER
                int i_value;
                subTag = this.getSubTag(NBTTag, "integer");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            i_value = this.loadValueAsInteger(subTag, thisTag.getName());
                            this.integerList.put(thisTag.getName(), i_value);
                        }
                    }

                // LOAD SHORT
                short short_value;
                subTag = this.getSubTag(NBTTag, "short");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            short_value = this.loadValueAsShort(subTag, thisTag.getName());
                            this.shortList.put(thisTag.getName(), short_value);
                        }
                    }

                // LOAD DOUBLE
                double d_value;
                subTag = this.getSubTag(NBTTag, "double");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            d_value = this.loadValueAsDouble(subTag, thisTag.getName());
                            this.doubleList.put(thisTag.getName(), d_value);
                        }
                    }

                // LOAD FLOAT
                float f_value;
                subTag = this.getSubTag(NBTTag, "float");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            f_value = this.loadValueAsFloat(subTag, thisTag.getName());
                            this.floatList.put(thisTag.getName(), f_value);
                        }
                    }

                // LOAD LONG
                long long_value;
                subTag = this.getSubTag(NBTTag, "long");
                if (subTag != null)
                    for (Object base : subTag.d()) {
                        if (base instanceof NBTBase) {
                            NBTBase thisTag = (NBTBase) base;
                            long_value = this.loadValueAsLong(subTag, thisTag.getName());
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
