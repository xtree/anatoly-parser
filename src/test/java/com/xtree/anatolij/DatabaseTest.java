package com.xtree.anatolij;

import com.xtree.anatolij.dao.Inbound;
import com.xtree.anatolij.dao.Outbound;
import com.xtree.anatolij.data.Event;
import com.xtree.anatolij.data.Ritual;
import com.xtree.anatolij.data.Sucking;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class DatabaseTest {

    @Test
    @Ignore
    public void storesMessage() throws Exception {
        try (Database db = new Database()){
            //act
            db.insertOutboundSms("000111222","testmessage");

            //assert
            Connection connection = db.getDbConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"smslib_out\" WHERE \"address\"=\'000111222\'");
            ResultSet results = statement.executeQuery();
                List<Outbound> smsList = new ArrayList<>();
            while( results.next()){
                smsList.add(Outbound.getFromResult(results));
            }
            assertThat(smsList.size(),greaterThan(0));
        }

        //cleanup
        try (Database db = new Database()){
            Connection connection = db.getDbConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"smslib_out\" WHERE \"address\"='000111222'");
            statement.execute();
            connection.commit();
        }


    }


}