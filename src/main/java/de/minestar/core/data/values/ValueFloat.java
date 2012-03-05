package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public class ValueFloat implements IValue {

    private static final String name = "FLOAT";
    private ConcurrentHashMap<String, Float> valueList = new ConcurrentHashMap<String, Float>();

    @Override
    public Object getValue(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Float) value);
    }

    @Override
    public void load(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Float>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getFloat(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Float> entry : this.valueList.entrySet()) {
            thisTag.setFloat(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
