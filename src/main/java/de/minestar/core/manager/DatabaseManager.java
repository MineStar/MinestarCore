package de.minestar.core.manager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.minestar.core.MinestarCore;
import de.minestar.minestarlibrary.database.AbstractSQLiteHandler;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseManager extends AbstractSQLiteHandler {

    private PreparedStatement getPlayerData, setPlayerData;

    public DatabaseManager(String pluginName, File SQLConfigFile) {
        super(pluginName, SQLConfigFile);
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
        // /////////////////////////////////////////
        //
        // MAIN-PROTECTIONS
        //
        // /////////////////////////////////////////
        StringBuilder builder = new StringBuilder();

        // open statement
        builder.append("CREATE TABLE IF NOT EXISTS `tbl_playerdata` (");

        // Protectionowner
        builder.append("`playerName` TEXT NOT NULL PRIMARY KEY ");
        builder.append(", ");

        // ProtectionType as Integer
        builder.append("`data` BLOB NOT NULL");

        // close statement
        builder.append(");");

        // execute statement
        PreparedStatement statement = con.prepareStatement(builder.toString());
        statement.execute();

        // clear
        statement = null;
        builder.setLength(0);
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
        //@formatter:off;
        this.getPlayerData  = con.prepareStatement("SELECT * FROM `tbl_playerdata` WHERE playerName=?");       
        this.setPlayerData  = con.prepareStatement("REPLACE INTO `tbl_playerdata` (playerName, data) VALUES (?, ?)");
        //@formatter:on;
    }

    public boolean savePlayer(String playerName, byte[] data) {
        try {
            this.setPlayerData.setString(1, playerName);
            this.setPlayerData.setBytes(2, data);
            this.setPlayerData.executeUpdate();
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, MinestarCore.NAME, "Can't save playerdata for '" + playerName + "'!");
            return false;
        }
    }

    public ByteArrayInputStream getPlayer(String playerName) {
        try {
            this.getPlayerData.setString(1, playerName);
            ResultSet results = this.getPlayerData.executeQuery();
            if (results.next()) {
                return new ByteArrayInputStream(results.getBytes("data"));
            }
            return null;
        } catch (Exception e) {
            ConsoleUtils.printException(e, MinestarCore.NAME, "Can't load playerdata for '" + playerName + "'!");
            return null;
        }
    }
}
