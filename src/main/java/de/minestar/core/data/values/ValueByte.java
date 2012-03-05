package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import de.minestar.core.data.Returnable;

public class ValueByte extends Returnable implements IValue {

    private static final String name = "BYTE";
    private ConcurrentHashMap<String, Byte> valueList = new ConcurrentHashMap<String, Byte>();

    @Override
    public byte getByte(String name) {
        return this.valueList.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, (Byte) value);
    }

    @Override
    public void loadNBT(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, Byte>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getByte(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void saveNBT(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, Byte> entry : this.valueList.entrySet()) {
            thisTag.setByte(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
