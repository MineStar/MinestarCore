package de.minestar.core.data;

import org.bukkit.Location;

public class Returnable implements IReturnable {

    public boolean getBoolean(String name) {
        throw new RuntimeException("This value is not a BOOLEAN!");
    }

    public byte getByte(String name) {
        throw new RuntimeException("This value is not a BYTE!");
    }

    public byte[] getByteArray(String name) {
        throw new RuntimeException("This value is not a BYTEARRAY!");
    }

    public double getDouble(String name) {
        throw new RuntimeException("This value is not a DOUBLE!");
    }

    public float getFloat(String name) {
        throw new RuntimeException("This value is not a FLOAT!");
    }

    public int getInteger(String name) {
        throw new RuntimeException("This value is not an INTEGER!");
    }

    public Location getLocation(String name) {
        throw new RuntimeException("This value is not a LOCATION!");
    }

    public long getLong(String name) {
        throw new RuntimeException("This value is not a LONG!");
    }

    public short getShort(String name) {
        throw new RuntimeException("This value is not a SHORT!");
    }

    public String getString(String name) {
        throw new RuntimeException("This value is not a STRING!");
    }
}
