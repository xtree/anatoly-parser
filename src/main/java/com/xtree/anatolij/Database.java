package com.xtree.anatolij;

import com.xtree.anatolij.dao.Inbound;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class Database implements Closeable {

    //jdbc:postgresql://10.4.0.16/sms_gw?autoReconnect=true -driver com.mysql.jdbc.Driver -username postgres -password sergtsop -profile sms_gw
    public static final String CONNECTION_STRING = "jdbc:postgresql://10.4.0.16/sms_lib?autoReconnect=true";
    public static final String DB_USERNAME = "sms";
    public static final String DB_PASSWORD = "smslib";
    private final Connection connection;

    public Database() throws Exception {
        connection = getDbConnection();
    }

    public List<Inbound> readNew() {
        List<Inbound> smsList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"smslib_in\" WHERE \"status\"=\'0\'")) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                smsList.add(Inbound.getFromResult(results));
            }
        } catch (SQLException e) {
            log.error("error while processing sms", e);
        }
        return smsList;
    }

    public boolean insertOutboundSms(String number, String message) throws SQLException {
        try(PreparedStatement insert = connection.prepareStatement("insert into \"smslib_out\" (address, text, message_id) values (?,?,?)")) {
            //insert.setString(1, sender);
            insert.setString(1, number);
            insert.setString(2, message);
            insert.setString(3, UUID.randomUUID().toString());
            boolean retVal = insert.execute();
            connection.commit();
            return retVal;
        }
    }

    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public Connection getDbConnection() throws Exception {
        Connection db = null;
        while (db == null) {
            try {
                // Class.forName(this.dbDriver);
                db = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
                db.setAutoCommit(false);
            } catch (SQLException e) {
                log.warn("DB error, retrying...", e);
                Thread.sleep(5000);
            }
        }
        return db;
    }

    public boolean markAsProcessed(int id) throws SQLException {
        /*UPDATE table_name
          SET column1=value1,column2=value2,...
          WHERE some_column=some_value;
        */
        try (PreparedStatement update = connection.prepareStatement("update \"smslib_in\" set \"status\"='1' where \"id\"= ?")) {
            update.setInt(1,id);
            boolean retVal = update.execute();
            connection.commit();
            return retVal;
        }

    }
}

