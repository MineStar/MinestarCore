package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.minestar.core.data.Returnable;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public class ValueLong extends Returnable implements IValue {

    private static final String name = "LONG";
    private ConcurrentHashMap<String, Long> valueList = new ConcurrentHashMap<String, Long>();

    @Override
    public long getLong(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Long) value);
    }

    @Override
    public void loadNBT(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Long>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getLong(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void saveNBT(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Long> entry : this.valueList.entrySet()) {
            thisTag.setLong(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
