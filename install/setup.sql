/*
Navicat MariaDB Data Transfer

Source Server         : mariadb
Source Server Version : 100017
Source Host           : localhost:3306
Source Database       : dev_hibernate

Target Server Type    : MariaDB
Target Server Version : 100017
File Encoding         : 65001

Date: 2015-04-10 18:22:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS 'contact';
CREATE TABLE 'contact' (
  'id' int(11) NOT NULL AUTO_INCREMENT,
  'first_name' varchar(60) NOT NULL,
  'last_name' varchar(40) NOT NULL,
  'birth_date' date DEFAULT NULL,
  'version' int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY ('id'),
  UNIQUE KEY 'uq_contact_1' ('first_name','last_name')
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact
-- ----------------------------
INSERT INTO 'contact' VALUES ('1', 'Clarence', 'Ho', '1980-07-30', '0');
INSERT INTO 'contact' VALUES ('2', 'Scott', 'Tiger', '1990-11-02', '0');
INSERT INTO 'contact' VALUES ('3', 'John', 'Smith', '1964-02-28', '0');

-- ----------------------------
-- Table structure for contact_hobby_detail
-- ----------------------------
DROP TABLE IF EXISTS 'contact_hobby_detail';
CREATE TABLE 'contact_hobby_detail' (
  'contact_id' int(11) NOT NULL,
  'hobby_id' varchar(20) NOT NULL,
  PRIMARY KEY ('contact_id','hobby_id'),
  KEY 'fk_contact_hobby_detail_2' ('hobby_id'),
  CONSTRAINT 'fk_contact_hobby_detail_1' FOREIGN KEY ('contact_id') REFERENCES 'contact' ('id') ON DELETE CASCADE,
  CONSTRAINT 'fk_contact_hobby_detail_2' FOREIGN KEY ('hobby_id') REFERENCES 'hobby' ('hobby_id')
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact_hobby_detail
-- ----------------------------
INSERT INTO 'contact_hobby_detail' VALUES ('1', 'Movies');
INSERT INTO 'contact_hobby_detail' VALUES ('1', 'Swimming');
INSERT INTO 'contact_hobby_detail' VALUES ('2', 'Swimming');

-- ----------------------------
-- Table structure for contact_tel_detail
-- ----------------------------
DROP TABLE IF EXISTS 'contact_tel_detail';
CREATE TABLE 'contact_tel_detail' (
  'id' int(11) NOT NULL AUTO_INCREMENT,
  'contact_id' int(11) NOT NULL,
  'tel_type' varchar(20) NOT NULL,
  'tel_number' varchar(20) NOT NULL,
  'version' int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY ('id'),
  UNIQUE KEY 'uq_contact_tel_detail_1' ('contact_id','tel_type'),
  CONSTRAINT 'fk_contact_tel_detail_1' FOREIGN KEY ('contact_id') REFERENCES 'contact' ('id')
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact_tel_detail
-- ----------------------------
INSERT INTO 'contact_tel_detail' VALUES ('1', '1', 'Mobile', '1234567890', '0');
INSERT INTO 'contact_tel_detail' VALUES ('2', '1', 'Home', '1234567890', '0');
INSERT INTO 'contact_tel_detail' VALUES ('3', '2', 'Home', '1234567890', '0');

-- ----------------------------
-- Table structure for hobby
-- ----------------------------
DROP TABLE IF EXISTS 'hobby';
CREATE TABLE 'hobby' (
  'hobby_id' varchar(20) NOT NULL,
  PRIMARY KEY ('hobby_id')
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of hobby
-- ----------------------------
INSERT INTO 'hobby' VALUES ('Jogging');
INSERT INTO 'hobby' VALUES ('Movies');
INSERT INTO 'hobby' VALUES ('Programming');
INSERT INTO 'hobby' VALUES ('Reading');
INSERT INTO 'hobby' VALUES ('Swimming');
