package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTTagCompound;

public class ValueInteger implements IValue {

    private static final String name = "INTEGER";
    private ConcurrentHashMap<String, Integer> valueList = new ConcurrentHashMap<String, Integer>();

    @Override
    public Object getValue(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Integer) value);
    }

    @Override
    public void load(NBTTagCompound NBTTag) {
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTTagCompound) {
                    NBTTagCompound thisTag = (NBTTagCompound) base;
                    this.valueList.put(thisTag.getName(), thisTag.getInt(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Integer> entry : this.valueList.entrySet()) {
            thisTag.setInt(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
