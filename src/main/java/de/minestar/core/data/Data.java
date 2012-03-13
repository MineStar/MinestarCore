package de.minestar.core.data;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import de.minestar.core.data.container.DataContainerNBT;
import de.minestar.core.data.container.DataContainerNone;
import de.minestar.core.data.container.IDataContainer;

public class Data {

    // VARS
    private IDataContainer dataContainer;
    private File file;

    public Data() {
        this.dataContainer = new DataContainerNone();
    }

    public Data(String fileName, DataType type) {
        this(new File("/"), fileName, type);
    }

    public Data(File dataFolder, String fileName, DataType type) {
        this.file = new File(dataFolder, fileName + type.getEnding());
        switch (type) {
            case NBT : {
                this.dataContainer = new DataContainerNBT();
                break;
            }
            default : {
                this.dataContainer = new DataContainerNone();
                break;
            }
        }
    }

    /**
     * This method will save the file
     */
    public void save() {
        if (this.dataContainer != null) {
            this.dataContainer.save(this.file);
        } else {
            throw new RuntimeException("Error while saving Data: DataLoader is NULL!");
        }
    }

    /**
     * This method will load all the data from the file
     */
    public void load() {
        if (this.dataContainer != null) {
            this.dataContainer.load(this.file);
        } else {
            throw new RuntimeException("Error while loading Data: DataLoader is NULL!");
        }
    }

    // ///////////////////////////////////////////////
    //
    // SET VALUES
    //
    // ///////////////////////////////////////////////

    public void setValue(String key, Object value) {
        this.dataContainer.setValue(key, value);
    }

    public <T> GenericValue<T> getValue(String key, Class<T> clazz) {
        return this.dataContainer.getValue(key, clazz);
    }

    public void setBoolean(String key, boolean value) {
        this.setValue(key, value);
    }

    public void setByte(String key, byte value) {
        this.setValue(key, value);
    }

    public void setByteArray(String key, byte[] value) {
        this.setValue(key, value);
    }

    public void setDouble(String key, double value) {
        this.setValue(key, value);
    }

    public void setFloat(String key, float value) {
        this.setValue(key, value);
    }

    public void setInteger(String key, int value) {
        this.setValue(key, value);
    }

    public void setLong(String key, long value) {
        this.setValue(key, value);
    }

    public void setShort(String key, short value) {
        this.setValue(key, value);
    }

    public void setString(String key, String value) {
        this.setValue(key, value);
    }

    public void setLocation(String key, Location value) {
        this.setValue(key, value);
    }

    public void setItemStack(String key, ItemStack value) {
        this.setValue(key, value);
    }

    public void setItemStackArray(String key, ItemStack[] value) {
        this.setValue(key, value);
    }

    // ///////////////////////////////////////////////
    //
    // GET VALUES
    //
    // ///////////////////////////////////////////////

    public boolean getBoolean(String key) {
        return this.getValue(key, Boolean.class).getValue();
    }

    public byte getByte(String key) {
        return this.getValue(key, byte.class).getValue();
    }

    public byte[] getByteArray(String key) {
        return this.getValue(key, byte[].class).getValue();
    }

    public double getDouble(String key) {
        return this.getValue(key, double.class).getValue();
    }

    public float getFloat(String key) {
        return this.getValue(key, float.class).getValue();
    }

    public int getInteger(String key) {
        return this.getValue(key, int.class).getValue();
    }

    public long getLong(String key) {
        return this.getValue(key, long.class).getValue();
    }

    public short getShort(String key) {
        return this.getValue(key, short.class).getValue();
    }

    public String getString(String key) {
        return this.getValue(key, String.class).getValue();
    }

    public Location getLocation(String key) {
        return this.getValue(key, Location.class).getValue();
    }

    public ItemStack getItemStack(String key) {
        return this.getValue(key, ItemStack.class).getValue();
    }

    public ItemStack[] getItemStackArray(String key) {
        return this.getValue(key, ItemStack[].class).getValue();
    }
}
