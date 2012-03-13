package de.minestar.core.data;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import de.minestar.core.data.loader.DataLoaderNBT;
import de.minestar.core.data.loader.IDataLoader;
import de.minestar.core.data.values.IValue;
import de.minestar.core.data.values.ValueBoolean;
import de.minestar.core.data.values.ValueByte;
import de.minestar.core.data.values.ValueByteArray;
import de.minestar.core.data.values.ValueDouble;
import de.minestar.core.data.values.ValueFloat;
import de.minestar.core.data.values.ValueInteger;
import de.minestar.core.data.values.ValueLocation;
import de.minestar.core.data.values.ValueLong;
import de.minestar.core.data.values.ValueShort;
import de.minestar.core.data.values.ValueString;

@SuppressWarnings("rawtypes")
public class Data {

    // VARS
    private IDataLoader dataLoader;
    private IValue dataBool, dataByte, dataByteArray, dataDouble, dataFloat, dataInt, dataLong, dataShort, dataString, dataLocation;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue>> valueMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue>>();;

    public Data(String fileName, DataType type) {
        this(new File("/"), fileName, type);
    }

    public Data(File dataFolder, String fileName, DataType type) {
        switch (type) {
            case NBT : {
                this.dataLoader = new DataLoaderNBT(this, dataFolder, fileName + type.getEnding());
                break;
            }
            default : {
                this.dataLoader = null;
                throw new RuntimeException("TYPE '" + type.getName() + "' is not supported yet!");
            }
        }

        // INIT VARS
        this.initVars();
    }

    /**
     * This method will initialize all needed var-fields
     */
    private void initVars() {
        this.dataBool = new ValueBoolean();
        this.dataByte = new ValueByte();
        this.dataByteArray = new ValueByteArray();
        this.dataDouble = new ValueDouble();
        this.dataFloat = new ValueFloat();
        this.dataInt = new ValueInteger();
        this.dataLong = new ValueLong();
        this.dataShort = new ValueShort();
        this.dataString = new ValueString();
        this.dataLocation = new ValueLocation();

        // INIT ALL OBJECTS THAT ARE CURRENTLY USED
        // TODO: WE WANT TO MAKE THIS AUTOMATICLY

        this.initObject(Boolean.class);
        this.initObject(Byte.class);
        this.initObject(Byte[].class);
        this.initObject(Double.class);
        this.initObject(Float.class);
        this.initObject(Integer.class);
        this.initObject(Long.class);
        this.initObject(Short.class);
        this.initObject(String.class);
        this.initObject(Location.class);
        this.initObject(ItemStack.class);
        this.initObject(ItemStack[].class);
    }

    private void initObject(Class clazz) {
        this.valueMap.put(clazz.getName(), new ConcurrentHashMap<String, GenericValue>());
    }

    /**
     * This method will save the file
     */
    public void save() {
        if (this.dataLoader != null) {
            this.dataLoader.save();
        } else {
            throw new RuntimeException("Error while saving Data: DataLoader is NULL!");
        }
    }

    /**
     * This method will load all the data from the file
     */
    public void load() {
        if (this.dataLoader != null) {
            this.dataLoader.load();
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
        ConcurrentHashMap<String, GenericValue> thisValues = this.valueMap.get(value.getClass().getName());
        if (thisValues == null) {
            throw new RuntimeException(value.getClass().getName() + " IS CURRENTLY NOT SUPPORTED!");
        }

        GenericValue thisV = new GenericValue<Object>(value);
        thisValues.put(key, thisV);
    }

    @SuppressWarnings("unchecked")
    public <T> GenericValue<T> getValue(String key, Class<T> clazz) {
        ConcurrentHashMap<String, GenericValue> thisValues = this.valueMap.get(clazz.getName());
        if (thisValues != null) {
            return thisValues.get(key);
        }
        return null;
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

    /**
     * @return the dataBool
     */
    public IValue getDataBool() {
        return dataBool;
    }

    /**
     * @return the dataByte
     */
    public IValue getDataByte() {
        return dataByte;
    }

    /**
     * @return the dataByteArray
     */
    public IValue getDataByteArray() {
        return dataByteArray;
    }

    /**
     * @return the dataDouble
     */
    public IValue getDataDouble() {
        return dataDouble;
    }

    /**
     * @return the dataFloat
     */
    public IValue getDataFloat() {
        return dataFloat;
    }

    /**
     * @return the dataInt
     */
    public IValue getDataInt() {
        return dataInt;
    }

    /**
     * @return the dataLong
     */
    public IValue getDataLong() {
        return dataLong;
    }

    /**
     * @return the dataShort
     */
    public IValue getDataShort() {
        return dataShort;
    }

    /**
     * @return the dataString
     */
    public IValue getDataString() {
        return dataString;
    }

    /**
     * @return the dataLocation
     */
    public IValue getDataLocation() {
        return dataLocation;
    }
}
