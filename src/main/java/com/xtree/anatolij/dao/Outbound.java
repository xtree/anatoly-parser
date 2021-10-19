package com.xtree.anatolij.dao;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Outbound {
    int id;//	INT	Autonumber/identity field	The table's primary key.
    int parent_id;
    String message_id;
    long date;
    String sender_address;
    String address;
    //String type;//	CHAR(1)	NOT NULL	The message type. This should be "O" for normal outbound messages, or "W" for wap si messages.
//    String recipient;//	CHAR(16)	NOT NULL	The recipient's number to whom the message should be sent.
    String text;//	CHAR(xxx)	NOT NULL	The message text.
    String encoding;//	CHAR(1)	NOT NULL, Default value: '7'	"7" for 7bit, "8" for 8bit and "U" for Unicode/UCS2.
    int priority;//	INT	NOT NULL, Default value: 0	Lower (or negative) values mean lower priority than higher (or positive) values. By convention, a priority of a value 0 (zero) is considered the normal priority. High priority messages get sent first than others.
    //    String wap_url;//	CHAR(xxx)	NOT NULL for WAP SI messages!	The WAP SI URL address.
    int request_delivery_report;
    int flash_sms;//	INT	NOT NULL, Default value: 0	Set to 1 if you require your message to be sent as a flash message.
    String sent_status;//	CHAR(1)	NOT NULL, Default value "U"	"U" : unsent, "Q" : queued, "S" : sent, "F" : failed. This field is updated by SMSServer when it sends your message. If set in the configuration file, this field takes extra values depending on the received status report message: "D" : delivered, "P" : pending, "A" : aborted.
    long sent_date;//	DATETIME	NULL	The sent date. This field is updated by SMSServer when it sends your message.
    String gateway_id;//	CHAR(64)	NOT NULL, Default value is the star character	Set it to the star character if you want to leave to SMSServer the decision as to which gateway to use to send your message. Set it to a specific Gateway ID to force SMSServer to send your message via this gateway.
    String operator_message_id;
    String delivery_status;
    String delivery_date;

//    long wap_expiry_date;//	DATETIME	NULL	The WAP SI expiry date. If no value is given, SMSServer will calculate a 7 day ahead expiry date.
//    String wap_signal;//	CHAR(1)	NULL	The WAP SI signal. Use "N" for NONE, "L" for LOW, "M" for MEDIUM, "H" for HIGH, "D" for DELETE. If no value/invalid value is given, the NONE signal will be used.
//    Long create_date;//	DATETIME	NOT NULL, Default value: current date/time	The datetime when this record was created.
//    String originator;//	CHAR(16)	NOT NULL, Default value: ''	The originator. Normally you should leave this blank.
//    int status_report;//	INT	NOT NULL, Default value: 0	Set to 1 if you require a status report message to be generated.
//    int src_port;//	INT	NOT NULL, Default value: -1	Set to source port (for midlets)
//    int dst_port;//	INT	NOT NULL, Default value: -1	Set to destination port (for midlets)
//    String ref_no;//	CHAR(64)	NULL	The Reference ID of your message. This field is updated by SMSServer when it sends your message.
//    int errors;//	INT	NOT NULL, Default value: 0	The number of retries SMSServer did to send your message. This field is updated by SMSServer.

    public static Outbound getFromResult(ResultSet set) {
        Outbound sms = new Outbound();

        try {
            sms.id= set.getInt("id");

            sms.message_id = set.getString("message_id");
            sms.address = set.getString("address");
            sms.sender_address = set.getString("sender_address");
            sms.encoding = set.getString("encoding");
            sms.text = set.getString("text");
            sms.gateway_id = set.getString("gateway_id");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return sms;
    }
}
