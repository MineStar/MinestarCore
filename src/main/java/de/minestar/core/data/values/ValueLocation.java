package de.minestar.core.data.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import com.bukkit.gemo.utils.BlockUtils;

import de.minestar.core.data.Returnable;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public class ValueLocation extends Returnable implements IValue {

    private static final String name = "LOCATION";
    private ConcurrentHashMap<String, String> valueList = new ConcurrentHashMap<String, String>();

    @Override
    public Location getLocation(String name) {
        return BlockUtils.LocationFromString(this.valueList.get(name));
    }

    @Override
    public void setValue(String name, Object value) {
        this.valueList.put(name, BlockUtils.LocationToString((Location) value));
    }

    @Override
    public void load(NBTTagCompound NBTTag) {
        valueList = new ConcurrentHashMap<String, String>();
        if (NBTTag.hasKey(name)) {
            NBTTagCompound thisCompound = NBTTag.getCompound(name);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    this.valueList.put(thisTag.getName(), thisCompound.getString(thisTag.getName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound NBTTag) {
        NBTTagCompound thisTag = new NBTTagCompound();
        for (Map.Entry<String, String> entry : this.valueList.entrySet()) {
            thisTag.setString(entry.getKey(), entry.getValue());
        }
        NBTTag.setCompound(name, thisTag);
    }

}
