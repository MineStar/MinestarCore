package de.minestar.core.data.values;

import net.minecraft.server.NBTTagCompound;
import de.minestar.core.data.IReturnable;

public interface IValue extends IReturnable {

    public void setValue(String name, Object value);

    public void loadNBT(NBTTagCompound NBTTag);

    public void saveNBT(NBTTagCompound NBTTag);
}
