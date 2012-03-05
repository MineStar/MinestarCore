package de.minestar.core.data;

import org.bukkit.Location;

public interface IReturnable {

    public boolean getBoolean(String name);

    public byte getByte(String name);

    public byte[] getByteArray(String name);

    public double getDouble(String name);

    public float getFloat(String name);

    public int getInteger(String name);

    public Location getLocation(String name);

    public long getLong(String name);

    public short getShort(String name);

    public String getString(String name);
}
