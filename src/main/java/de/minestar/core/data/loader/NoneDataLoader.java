package de.minestar.core.data.loader;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import de.minestar.core.data.GenericValue;

public class NoneDataLoader implements IDataLoader {

    private ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue<?>>> valueMap;

    public NoneDataLoader() {
        this.initVars();
    }

    /**
     * This method will initialize all needed var-fields
     */
    private void initVars() {
        // INIT THE MAP
        this.valueMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue<?>>>();

        // INIT ALL OBJECTS THAT ARE CURRENTLY USED
        // TODO: WE WANT TO MAKE THIS AUTOMATICLY
        this.initObject(Boolean.class);
        this.initObject(Byte.class);
        this.initObject(Byte[].class);
        this.initObject(Double.class);
        this.initObject(Float.class);
        this.initObject(Integer.class);
        this.initObject(Long.class);
        this.initObject(Short.class);
        this.initObject(String.class);
        this.initObject(Location.class);
        this.initObject(ItemStack.class);
        this.initObject(ItemStack[].class);
    }

    private void initObject(Class<?> clazz) {
        this.valueMap.put(clazz.getName(), new ConcurrentHashMap<String, GenericValue<?>>());
    }

    public void setValue(String key, Object value) {
        ConcurrentHashMap<String, GenericValue<?>> thisValues = this.valueMap.get(value.getClass().getName());
        if (thisValues == null) {
            throw new RuntimeException(value.getClass().getName() + " IS CURRENTLY NOT SUPPORTED!");
        }

        GenericValue<Object> thisV = new GenericValue<Object>(value);
        thisValues.put(key, thisV);
    }

    public <T> GenericValue<?> getValue(String key, Class<T> clazz) {
        ConcurrentHashMap<String, GenericValue<?>> thisValues = this.valueMap.get(clazz.getName());
        if (thisValues != null) {
            return thisValues.get(key);
        }
        return null;
    }

    @Override
    public void load() {
        throw new RuntimeException("LOADING is currently not supported!");
    }

    @Override
    public void save() {
        throw new RuntimeException("SAVING is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadBoolean() {
        throw new RuntimeException("Boolean is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadByte() {
        throw new RuntimeException("Byte is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadByteArray() {
        throw new RuntimeException("Byte[] is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadDouble() {
        throw new RuntimeException("Double is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadFloat() {
        throw new RuntimeException("Float is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadInteger() {
        throw new RuntimeException("Integer is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadLong() {
        throw new RuntimeException("Long is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadShort() {
        throw new RuntimeException("Short is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadString() {
        throw new RuntimeException("String is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadItemStack() {
        throw new RuntimeException("ItemStack is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadItemStackArray() {
        throw new RuntimeException("ItemStack[] is currently not supported!");
    }

    @Override
    public Collection<GenericValue<?>> loadLocation() {
        throw new RuntimeException("Location is currently not supported!");
    }
}
