package de.minestar.core.data.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import de.minestar.core.data.Data;
import de.minestar.core.data.GenericValue;
import de.minestar.core.data.tools.CompressedStreamTools;

public class DataLoaderNBT extends NoneDataLoader {
    private File folder, file;
    private final String fileName;

    private final Data data;

    private NBTTagCompound rootTag;

    public DataLoaderNBT(Data data, String fileName) {
        this(data, new File("/"), fileName);
    }

    public DataLoaderNBT(Data data, File dataFolder, String fileName) {
        this.data = data;
        this.folder = dataFolder;
        this.file = new File(folder, fileName);
        this.fileName = fileName;
    }

    @Override
    public void load() {
        ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue>> valueMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, GenericValue>>();
        if (this.file != null && this.file.exists()) {
            try {
                // OPEN STREAM
                FileInputStream FIS = new FileInputStream(this.file);
                this.rootTag = CompressedStreamTools.loadGzippedCompoundFromOutputStream(FIS);

                // LOAD DATA
                if (this.rootTag != null) {
                    this.data.getDataBool().loadNBT(rootTag);
                    this.data.getDataByte().loadNBT(rootTag);
                    this.data.getDataByte().loadNBT(rootTag);
                    this.data.getDataDouble().loadNBT(rootTag);
                    this.data.getDataFloat().loadNBT(rootTag);
                    this.data.getDataInt().loadNBT(rootTag);
                    this.data.getDataLong().loadNBT(rootTag);
                    this.data.getDataShort().loadNBT(rootTag);
                    this.data.getDataString().loadNBT(rootTag);
                    this.data.getDataLocation().loadNBT(rootTag);
                }

                // CLOSE STREAM
                FIS.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        try {
            // CREAT TEMP-FILE
            File tmpFile = new File(this.folder, this.fileName + ".tmp");
            FileOutputStream FOS = new FileOutputStream(tmpFile);

            NBTTagCompound rootTag = new NBTTagCompound();
            this.data.getDataBool().saveNBT(rootTag);
            this.data.getDataByte().saveNBT(rootTag);
            this.data.getDataByteArray().saveNBT(rootTag);
            this.data.getDataDouble().saveNBT(rootTag);
            this.data.getDataFloat().saveNBT(rootTag);
            this.data.getDataInt().saveNBT(rootTag);
            this.data.getDataLong().saveNBT(rootTag);
            this.data.getDataShort().saveNBT(rootTag);
            this.data.getDataString().saveNBT(rootTag);
            this.data.getDataLocation().saveNBT(rootTag);

            // GZIP THE TEMP-FILE
            CompressedStreamTools.writeGzippedCompoundToOutputStream(rootTag, FOS);

            // DELETE OLD FILE
            FOS.close();
            if (file.exists()) {
                file.delete();
            }

            // RENAME THE TMP FILE
            tmpFile.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<GenericValue<?>> loadBoolean() {
        ArrayList<GenericValue<?>> valueList = new ArrayList<GenericValue<?>>();

        final String keyName = "BOOLEAN";

        if (rootTag.hasKey(keyName)) {
            NBTTagCompound thisCompound = rootTag.getCompound(keyName);
            for (Object base : thisCompound.d()) {
                if (base instanceof NBTBase) {
                    NBTBase thisTag = (NBTBase) base;
                    valueList.add(new GenericValue<Boolean>(thisCompound.getBoolean(thisTag.getName())));
                }
            }
        }

        return valueList;
    }

}
