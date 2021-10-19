#!/bin/bash
# jdbc:postgresql://10.4.0.16/matrixdb?user=postgres&password=sergtsop
#java -jar lib\smsserver-dep-dev-SNAPSHOT-all.jar -url jdbc:mysql://localhost/smslib?autoReconnect=true -driver com.mysql.jdbc.Driver -username root -password root

#		<!--//this dependency needs set java.library.path to rxtx libraries-->
#		<!-- -Djava.library.path=/usr/lib64/rxtx-->
#		<!-- -Dsmslib.serial.polling -->
nohup java -Djava.library.path=/pub/smsserver/rxtx -Dsmslib.deletemessagesaftercallback=true -jar smsserver-dep-dev-SNAPSHOT-all.jar -url jdbc:postgresql://10.4.0.16/sms_gw?autoReconnect=true -driver com.mysql.jdbc.Driver -username postgres -password sergtsop -profile sms_gw >std.out &

sudo java -Djava.library.path=/usr/lib/jni -Dsmslib.serial.polling=true -Dsmslib.deletemessagesaftercallback=true -jar smsserver-dep-dev-SNAPSHOT-all.jar -url "jdbc:postgresql://10.4.0.16/sms_lib?user=sms&password=smslib&autoReconnect=true" -driver org.postgresql.Driver -username sms -password smslib -profile public

v library path jsou .so knihovny pro komunikaci javy s portama

sudo java \
-Djava.library.path=/usr/lib/jni \
-Dsmslib.deletemessagesaftercallback=true \
-jar smsserver-dep-dev-SNAPSHOT-all.jar \
  -url "jdbc:postgresql://10.4.0.16/sms_lib?user=sms&password=smslib&autoReconnect=true" \
  -driver org.postgresql.Driver \
  -username sms \
  -password smslib \
  -profile public

 sudo java -Djava.library.path=/usr/lib/jni -Dsmslib.serial.polling=true -Dsmslib.deletemessagesaftercallback=true -jar smsserver-dep-dev-SNAPSHOT-all.jar -url "jdbc:postgresql://10.4.0.16/sms_lib?user=sms&password=smslib&autoReconnect=true" -driver org.postgresql.Driver -username sms -password smslib -profile public

 java -Djava.library.path=/usr/lib/jni -Dsmslib.serial.polling=true -Dsmslib.deletemessagesaftercallback=true -cp /usr/share/java/RXTXcomm.jar;smsserver-dep-dev-SNAPSHOT-all.jar org.smslib.smsserver.SMSServer "-url" "jdbc:postgresql://10.4.0.16/sms_lib?user=sms&password=smslib&autoReconnect=true" "\-driver" "org.postgresql.Driver" "\-username" "sms" "\-password" "smslib" "\-profile" "public"