/*
Navicat MariaDB Data Transfer

Source Server         : mariadb
Source Server Version : 100017
Source Host           : localhost:3306
Source Database       : dev_hibernate

Target Server Type    : MariaDB
Target Server Version : 100017
File Encoding         : 65001

Date: 2015-04-21 17:49:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(60) NOT NULL,
  `last_name` varchar(40) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_contact_1` (`first_name`,`last_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact
-- ----------------------------
INSERT INTO `contact` VALUES ('1', 'Summer', 'Glass', '1968-08-05', 'vitae@egestasadui.net', '0');
INSERT INTO `contact` VALUES ('2', 'Mikayla', 'Church', '1975-04-03', 'lobortis.Class@aliquam.org', '0');
INSERT INTO `contact` VALUES ('3', 'Shaine', 'Brooks', '1971-08-24', 'vel.pede@metusVivamuseuismod.edu', '0');
INSERT INTO `contact` VALUES ('4', 'Robin', 'Sullivan', '1961-09-09', 'purus.gravida@necleo.edu', '0');
INSERT INTO `contact` VALUES ('5', 'Xantha', 'Kim', '1960-08-25', 'risus.Duis.a@velnisl.ca', '0');
INSERT INTO `contact` VALUES ('6', 'Barry', 'Kirk', '1982-03-27', 'blandit.at@Maurisblanditenim.com', '0');
INSERT INTO `contact` VALUES ('7', 'Tad', 'Grant', '1972-08-08', 'In.lorem.Donec@Vivamusnisi.org', '0');
INSERT INTO `contact` VALUES ('8', 'Finn', 'Browning', '1974-05-27', 'aliquet@ornare.net', '0');
INSERT INTO `contact` VALUES ('9', 'Ali', 'Calhoun', '1976-11-30', 'fermentum@nulla.co.uk', '0');
INSERT INTO `contact` VALUES ('10', 'Alexandra', 'Hendricks', '1973-07-05', 'at.auctor@pellentesquemassalobortis.edu', '0');

-- ----------------------------
-- Table structure for contact_hobby_detail
-- ----------------------------
DROP TABLE IF EXISTS `contact_hobby_detail`;
CREATE TABLE `contact_hobby_detail` (
  `contact_id` int(11) NOT NULL,
  `hobby_id` varchar(20) NOT NULL,
  PRIMARY KEY (`contact_id`,`hobby_id`),
  KEY `fk_contact_hobby_detail_2` (`hobby_id`),
  CONSTRAINT `fk_contact_hobby_detail_1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_contact_hobby_detail_2` FOREIGN KEY (`hobby_id`) REFERENCES `hobby` (`hobby_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact_hobby_detail
-- ----------------------------
INSERT INTO `contact_hobby_detail` VALUES ('1', 'Jogging');
INSERT INTO `contact_hobby_detail` VALUES ('1', 'Movies');
INSERT INTO `contact_hobby_detail` VALUES ('2', 'Programming');
INSERT INTO `contact_hobby_detail` VALUES ('2', 'Reading');
INSERT INTO `contact_hobby_detail` VALUES ('3', 'Jogging');
INSERT INTO `contact_hobby_detail` VALUES ('3', 'Swimming');
INSERT INTO `contact_hobby_detail` VALUES ('4', 'Movies');
INSERT INTO `contact_hobby_detail` VALUES ('4', 'Programming');
INSERT INTO `contact_hobby_detail` VALUES ('5', 'Reading');
INSERT INTO `contact_hobby_detail` VALUES ('5', 'Swimming');
INSERT INTO `contact_hobby_detail` VALUES ('6', 'Jogging');
INSERT INTO `contact_hobby_detail` VALUES ('6', 'Movies');
INSERT INTO `contact_hobby_detail` VALUES ('7', 'Programming');
INSERT INTO `contact_hobby_detail` VALUES ('7', 'Reading');
INSERT INTO `contact_hobby_detail` VALUES ('8', 'Jogging');
INSERT INTO `contact_hobby_detail` VALUES ('8', 'Swimming');
INSERT INTO `contact_hobby_detail` VALUES ('9', 'Movies');
INSERT INTO `contact_hobby_detail` VALUES ('9', 'Programming');
INSERT INTO `contact_hobby_detail` VALUES ('10', 'Reading');
INSERT INTO `contact_hobby_detail` VALUES ('10', 'Swimming');

-- ----------------------------
-- Table structure for contact_tel_detail
-- ----------------------------
DROP TABLE IF EXISTS `contact_tel_detail`;
CREATE TABLE `contact_tel_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_id` int(11) NOT NULL,
  `tel_type` varchar(20) NOT NULL,
  `tel_number` varchar(20) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_contact_tel_detail_1` (`contact_id`,`tel_type`),
  CONSTRAINT `fk_contact_tel_detail_1` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contact_tel_detail
-- ----------------------------
INSERT INTO `contact_tel_detail` VALUES ('4', '1', 'Mobile', '1-113-753-8020', '0');
INSERT INTO `contact_tel_detail` VALUES ('5', '1', 'Home', '1-996-507-0853', '0');
INSERT INTO `contact_tel_detail` VALUES ('6', '2', 'Mobile', '1-407-100-1341', '0');
INSERT INTO `contact_tel_detail` VALUES ('7', '2', 'Home', '1-285-981-2510', '0');
INSERT INTO `contact_tel_detail` VALUES ('8', '3', 'Mobile', '1-274-311-9291', '0');
INSERT INTO `contact_tel_detail` VALUES ('9', '3', 'Home', '1-499-112-9185', '0');
INSERT INTO `contact_tel_detail` VALUES ('10', '4', 'Mobile', '1-234-628-6511', '0');
INSERT INTO `contact_tel_detail` VALUES ('11', '4', 'Home', '1-560-178-3273', '0');
INSERT INTO `contact_tel_detail` VALUES ('12', '5', 'Mobile', '1-430-941-9233', '0');
INSERT INTO `contact_tel_detail` VALUES ('13', '5', 'Home', '1-271-831-8886', '0');
INSERT INTO `contact_tel_detail` VALUES ('14', '6', 'Mobile', '1-255-105-0103', '0');
INSERT INTO `contact_tel_detail` VALUES ('15', '6', 'Home', '1-481-652-4155', '0');
INSERT INTO `contact_tel_detail` VALUES ('16', '7', 'Mobile', '1-917-917-8478', '0');
INSERT INTO `contact_tel_detail` VALUES ('17', '7', 'Home', '1-766-831-2271', '0');
INSERT INTO `contact_tel_detail` VALUES ('18', '8', 'Mobile', '1-863-515-3218', '0');
INSERT INTO `contact_tel_detail` VALUES ('19', '8', 'Home', '1-930-909-9849', '0');
INSERT INTO `contact_tel_detail` VALUES ('20', '9', 'Mobile', '1-423-399-6903', '0');
INSERT INTO `contact_tel_detail` VALUES ('21', '9', 'Home', '1-294-840-1996', '0');
INSERT INTO `contact_tel_detail` VALUES ('22', '10', 'Mobile', '1-661-300-3848', '0');
INSERT INTO `contact_tel_detail` VALUES ('23', '10', 'Home', '1-972-479-8970', '0');

-- ----------------------------
-- Table structure for hobby
-- ----------------------------
DROP TABLE IF EXISTS `hobby`;
CREATE TABLE `hobby` (
  `hobby_id` varchar(20) NOT NULL,
  PRIMARY KEY (`hobby_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of hobby
-- ----------------------------
INSERT INTO `hobby` VALUES ('Jogging');
INSERT INTO `hobby` VALUES ('Movies');
INSERT INTO `hobby` VALUES ('Programming');
INSERT INTO `hobby` VALUES ('Reading');
INSERT INTO `hobby` VALUES ('Swimming');
