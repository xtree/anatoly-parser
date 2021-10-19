package com.xtree.anatolij.dao;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Inbound {
    int id;//	Autonumber/identity field	The table's primary key.
    //int process;// When new rows (i.e. messages) are created, SMSServer sets this field to 0. You can use this field for your own purposes.
    //String originator;//	The originator of the received message.

    //non documented fields
    String address;

   // String type;// "I" for inbound message, "S" for status report message.
    String encoding;//"7" for 7bit, "8" for 8bit and "U" for Unicode/UCS2.
    long message_date;// The message date (retrieved by the message headers).
    long receive_date;//The datetime when message was received.
    String text;//The body of the message.
    //String original_ref_no;//Available only for status report messages: refers to the RefNo of the original outbound message.
    //long original_receive_date;//Available only for status report messages: refers to the receive date of the original outbound message.
    String gateway_id;//

    public static Inbound getFromResult(ResultSet set) {
        Inbound sms = new Inbound();

        try {
            sms.id= set.getInt("id");

            sms.address = set.getString("address");
            //sms.process = set.getInt("process");
            //sms.originator = set.getString("originator");
           // sms.type = set.getString("type");
            sms.encoding = set.getString("encoding");
            sms.message_date = set.getTime("message_date").getTime();
            sms.receive_date = set.getTime("receive_date").getTime();
            sms.text = set.getString("text");
            //sms.original_ref_no = set.getString("original_ref_no");
            //sms.original_receive_date = set.getLong("original_ref_no"); // translate datetime
            sms.gateway_id = set.getString("gateway_id");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return sms;
    }
//    @Override
//    public void saveInboundMessage(InboundMessageCallbackEvent event) throws Exception
//    {
//        Connection db = null;
//        PreparedStatement s = null;
//        try
//        {
//            db = getDbConnection();
//            s = db.prepareStatement("insert into smslib_in (address, encoding, text, message_date, receive_date, gateway_id) values (?, ?, ?, ?, ?, ?)");
//            s.setString(1, event.getMessage().getOriginatorAddress().getAddress());
//            s.setString(2, event.getMessage().getEncoding().toShortString());
//            switch (event.getMessage().getEncoding())
//            {
//                case Enc7:
//                case EncUcs2:
//                    s.setString(3, event.getMessage().getPayload().getText());
//                    break;
//                case Enc8:
//                    s.setString(3, Common.bytesToString(event.getMessage().getPayload().getBytes()));
//                    break;
//                case EncCustom:
//                    throw new UnsupportedOperationException();
//            }
//            s.setTimestamp(4, new Timestamp(event.getMessage().getSentDate().getTime()));
//            s.setTimestamp(5, new Timestamp(event.getMessage().getCreationDate().getTime()));
//            s.setString(6, event.getMessage().getGatewayId());
//            s.executeUpdate();
//            db.commit();
//        }
//        catch (Exception e)
//        {
//            if (db != null) db.rollback();
//            logger.error("Error!", e);
//            throw e;
//        }
//        finally
//        {
//            if (s != null) s.close();
//            if (db != null) db.close();
//        }
//    }
}

