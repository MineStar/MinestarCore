package de.minestar.core.data.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import de.minestar.core.data.Data;
import de.minestar.core.data.tools.CompressedStreamTools;

import net.minecraft.server.NBTTagCompound;

public class DataLoaderNBT implements IDataLoader {
    private File folder, file;
    private final String fileName;

    private final Data data;

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
        if (this.file != null && this.file.exists()) {
            try {
                // OPEN STREAM
                FileInputStream FIS = new FileInputStream(this.file);
                NBTTagCompound rootTag = CompressedStreamTools.loadGzippedCompoundFromOutputStream(FIS);

                // LOAD DATA
                if (rootTag != null) {
                    this.data.getDataBool().load(rootTag);
                    this.data.getDataByte().load(rootTag);
                    this.data.getDataByte().load(rootTag);
                    this.data.getDataDouble().load(rootTag);
                    this.data.getDataFloat().load(rootTag);
                    this.data.getDataInt().load(rootTag);
                    this.data.getDataLong().load(rootTag);
                    this.data.getDataShort().load(rootTag);
                    this.data.getDataString().load(rootTag);
                    this.data.getDataLocation().load(rootTag);
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
            this.data.getDataBool().save(rootTag);
            this.data.getDataByte().save(rootTag);
            this.data.getDataByteArray().save(rootTag);
            this.data.getDataDouble().save(rootTag);
            this.data.getDataFloat().save(rootTag);
            this.data.getDataInt().save(rootTag);
            this.data.getDataLong().save(rootTag);
            this.data.getDataShort().save(rootTag);
            this.data.getDataString().save(rootTag);
            this.data.getDataLocation().save(rootTag);

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
}
