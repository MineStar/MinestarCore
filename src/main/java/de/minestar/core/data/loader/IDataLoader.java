package de.minestar.core.data.loader;

import java.util.Collection;

import de.minestar.core.data.GenericValue;

public interface IDataLoader {

    public abstract void load();

    public abstract void save();

    // /////////////////////////////////////////
    // NATIVE DATA-TYPES
    // /////////////////////////////////////////

    public abstract Collection<GenericValue<?>> loadBoolean();

    public abstract Collection<GenericValue<?>> loadByte();

    public abstract Collection<GenericValue<?>> loadByteArray();

    public abstract Collection<GenericValue<?>> loadDouble();

    public abstract Collection<GenericValue<?>> loadFloat();

    public abstract Collection<GenericValue<?>> loadInteger();

    public abstract Collection<GenericValue<?>> loadLong();

    public abstract Collection<GenericValue<?>> loadShort();

    public abstract Collection<GenericValue<?>> loadString();

    // /////////////////////////////////////////
    // EXTENDED DATA-TYPES
    // /////////////////////////////////////////

    public abstract Collection<GenericValue<?>> loadItemStack();

    public abstract Collection<GenericValue<?>> loadItemStackArray();

    public abstract Collection<GenericValue<?>> loadLocation();

}
