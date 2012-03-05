package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.minestar.core.data.Returnable;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public class ValueDouble extends Returnable implements IValue {

    private static final String name = "DOUBLE";
    private ConcurrentHashMap<String, Double> valueList = new ConcurrentHashMap<String, Double>();

    @Override
    public double getDouble(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Double) value);
    }

    @Override
    public void load(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Double>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getDouble(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Double> entry : this.valueList.entrySet()) {
            thisTag.setDouble(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
