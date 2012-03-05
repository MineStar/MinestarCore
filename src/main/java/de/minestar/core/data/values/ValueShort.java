package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.minestar.core.data.Returnable;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public class ValueShort extends Returnable implements IValue {

    private static final String name = "SHORT";
    private ConcurrentHashMap<String, Short> valueList = new ConcurrentHashMap<String, Short>();

    @Override
    public short getShort(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Short) value);
    }

    @Override
    public void load(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Short>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getShort(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Short> entry : this.valueList.entrySet()) {
            thisTag.setShort(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
