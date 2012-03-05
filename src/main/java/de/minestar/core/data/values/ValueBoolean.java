package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import de.minestar.core.data.Returnable;

public class ValueBoolean extends Returnable implements IValue {

    private static final String name = "BOOLEAN";
    private ConcurrentHashMap<String, Boolean> valueList = new ConcurrentHashMap<String, Boolean>();

    @Override
    public boolean getBoolean(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Boolean) value);
    }

    @Override
    public void loadNBT(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Boolean>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getBoolean(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void saveNBT(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Boolean> entry : this.valueList.entrySet()) {
            thisTag.setBoolean(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }
}
