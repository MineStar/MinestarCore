package de.minestar.core.data;

import java.io.File;

import org.bukkit.Location;

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

public class Data {

    // VARS
    private IDataLoader dataLoader;
    private IValue dataBool, dataByte, dataByteArray, dataDouble, dataFloat, dataInt, dataLong, dataShort, dataString, dataLocation;

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

    public void setBoolean(String key, boolean value) {
        this.dataBool.setValue(key, value);
    }

    public void setByte(String key, byte value) {
        this.dataByte.setValue(key, value);
    }

    public void setByteArray(String key, byte[] value) {
        this.dataByteArray.setValue(key, value);
    }

    public void setDouble(String key, double value) {
        this.dataDouble.setValue(key, value);
    }

    public void setFloat(String key, float value) {
        this.dataFloat.setValue(key, value);
    }

    public void setInteger(String key, int value) {
        this.dataInt.setValue(key, value);
    }

    public void setLong(String key, long value) {
        this.dataLong.setValue(key, value);
    }

    public void setShort(String key, short value) {
        this.dataShort.setValue(key, value);
    }

    public void setString(String key, String value) {
        this.dataString.setValue(key, value);
    }

    public void setLocation(String key, Location value) {
        this.dataLocation.setValue(key, value);
    }

    // ///////////////////////////////////////////////
    //
    // GET VALUES
    //
    // ///////////////////////////////////////////////

    public boolean getBoolean(String key) {
        return this.dataBool.getBoolean(key);
    }

    public byte getByte(String key) {
        return this.dataByte.getByte(key);
    }

    public byte[] getByteArray(String key) {
        return this.dataByte.getByteArray(key);
    }

    public double getDouble(String key) {
        return this.dataDouble.getDouble(key);
    }

    public float getFloat(String key) {
        return this.dataFloat.getFloat(key);
    }

    public int getInteger(String key) {
        return this.dataInt.getInteger(key);
    }

    public long getLong(String key) {
        return this.dataLong.getLong(key);
    }

    public short getShort(String key) {
        return this.dataShort.getShort(key);
    }

    public String getString(String key) {
        return this.dataString.getString(key);
    }

    public Location getLocation(String key) {
        return this.dataLocation.getLocation(key);
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
