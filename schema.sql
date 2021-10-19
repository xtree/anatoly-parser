-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.24-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE="NO_AUTO_VALUE_ON_ZERO" */;

CREATE OR REPLACE FUNCTION update_modified_timestamp() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF (NEW != OLD) THEN
        NEW."date" = CURRENT_TIMESTAMP;
        RETURN NEW;
    END IF;
    RETURN OLD;
END;
$$;

-- Dumping structure for table smslib.smslib_calls
CREATE TABLE IF NOT EXISTS "smslib_calls" (
  "id" SERIAL,
  "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  "address" varchar(16) NOT NULL,
  "gateway_id" varchar(32) NOT NULL,
  PRIMARY KEY ("id")
); 

CREATE TRIGGER "smslib_calls_timestamp"
  BEFORE UPDATE
  ON "smslib_calls"
  FOR EACH ROW
  EXECUTE PROCEDURE update_modified_timestamp();

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_gateways
CREATE TABLE IF NOT EXISTS "smslib_gateways" (
  "id" SERIAL,
  "class" varchar(64) NOT NULL,
  "gateway_id" varchar(32) NOT NULL UNIQUE,
  "p0" varchar(32) DEFAULT NULL,
  "p1" varchar(32) DEFAULT NULL,
  "p2" varchar(32) DEFAULT NULL,
  "p3" varchar(32) DEFAULT NULL,
  "p4" varchar(32) DEFAULT NULL,
  "p5" varchar(32) DEFAULT NULL,
  "sender_address" varchar(16) DEFAULT NULL,
  "priority" integer NOT NULL DEFAULT '0',
  "max_message_parts" integer NOT NULL DEFAULT '2',
  "delivery_reports" integer NOT NULL DEFAULT '0',
  "profile" varchar(32) NOT NULL DEFAULT '*',
  "enabled" smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ("id")
) ;

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_groups
CREATE TABLE IF NOT EXISTS "smslib_groups" (
  "id" SERIAL,
  "group_name" varchar(32) NOT NULL,
  "group_description" varchar(100) NOT NULL,
  "enabled" smallint NOT NULL DEFAULT '0',
  "profile" varchar(32) NOT NULL,
  PRIMARY KEY ("id")
) ;
CREATE INDEX "group_name_index"
ON "smslib_groups" ("group_name");

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_group_recipients
CREATE TABLE IF NOT EXISTS "smslib_group_recipients" (
  "id" SERIAL,
  "group_id" integer NOT NULL,
  "address" varchar(16) NOT NULL,
  "enabled" smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ("id")
) ;

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_in
CREATE TABLE IF NOT EXISTS "smslib_in" (
  "id" SERIAL,
  "address" varchar(16) NOT NULL,
  "encoding" varchar(1) NOT NULL,
  "text" varchar(4096) NOT NULL,
  "message_date" timestamp NOT NULL,
  "receive_date" timestamp NOT NULL,
  "gateway_id" varchar(32) NOT NULL,
  "status" integer NOT NULL DEFAULT 0,
  PRIMARY KEY ("id")
) ;

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_number_routes
CREATE TABLE IF NOT EXISTS "smslib_number_routes" (
  "id" SERIAL,
  "address_regex" varchar(128) NOT NULL,
  "gateway_id" varchar(32) NOT NULL,
  "enabled" smallint NOT NULL DEFAULT '0',
  "profile" varchar(32) NOT NULL,
  PRIMARY KEY ("id")
) ;

-- Data exporting was unselected.


-- Dumping structure for table smslib.smslib_out
CREATE TABLE IF NOT EXISTS "smslib_out" (
  "id" SERIAL,
  "parent_id" integer NOT NULL DEFAULT '0',
  "message_id" varchar(128) NOT NULL UNIQUE,
  "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "sender_address" varchar(16) NOT NULL DEFAULT '',
  "address" varchar(16) NOT NULL,
  "text" varchar(1024) NOT NULL,
  "encoding" varchar(1) NOT NULL DEFAULT '7',
  "priority" integer NOT NULL DEFAULT '0',
  "request_delivery_report" integer NOT NULL DEFAULT '0',
  "flash_sms" integer NOT NULL DEFAULT '0',
  "sent_status" varchar(1) NOT NULL DEFAULT 'U',
  "sent_date" timestamp NOT NULL DEFAULT TIMESTAMP '1970-01-01 00:00:00',
  "gateway_id" varchar(32) NOT NULL DEFAULT '',
  "operator_message_id" varchar(128) NOT NULL DEFAULT '',
  "delivery_status" varchar(1) NOT NULL DEFAULT '',
  "delivery_date" timestamp NOT NULL DEFAULT TIMESTAMP '1970-01-01 00:00:00',
  PRIMARY KEY ("id")
) ;

CREATE INDEX "sent_status_index"
ON "smslib_out" ("sent_status");

CREATE TRIGGER "smslib_out_timestamp"
  BEFORE UPDATE
  ON "smslib_calls"
  FOR EACH ROW
  EXECUTE PROCEDURE update_modified_timestamp();

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, "") */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;