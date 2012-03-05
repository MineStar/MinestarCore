package de.minestar.core.data.values;

import net.minecraft.server.NBTTagCompound;

public interface IValue {
    public Object getValue(String name);

    public void setValue(String name, Object value);

    public void load(NBTTagCompound NBTTag);

    public void save(NBTTagCompound NBTTag);
}
