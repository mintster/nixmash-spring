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


insert into contact (first_name, last_name, birth_date, email, version) values ('Summer', 'Glass', '1981-05-03', 'chris@email.com', 0);
insert into contact (first_name, last_name, birth_date, email, version) values ('Scott', 'Tiger', '1990-11-02', 'scott@email.com', 0);
insert into contact (first_name, last_name, birth_date, email, version) values ('John', 'Smith', '1964-02-28', 'john@email.com', 0);

insert into contact_tel_detail (contact_id, tel_type, tel_number, version) values (1, 'Mobile', '1234567890', 0);
insert into contact_tel_detail (contact_id, tel_type, tel_number, version) values (1, 'Home', '1234567890', 0);
insert into contact_tel_detail (contact_id, tel_type, tel_number, version) values (2, 'Home', '1234567890', 0);

insert into hobby (hobby_id) values ('Swimming');
insert into hobby (hobby_id) values ('Jogging');
insert into hobby (hobby_id) values ('Programming');
insert into hobby (hobby_id) values ('Movies');
insert into hobby (hobby_id) values ('Reading');

insert into contact_hobby_detail(contact_id, hobby_id) values (1, 'Swimming');
insert into contact_hobby_detail(contact_id, hobby_id) values (1, 'Movies');
insert into contact_hobby_detail(contact_id, hobby_id) values (2, 'Swimming');

