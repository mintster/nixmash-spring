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


------------------------------------------------------------
-- Users
------------------------------------------------------------

INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (1, 'admin@email.com', 'admin', '$2a$10$B9wQFSrr3bfQeUGqxtTDuut1.4YFcA/WFthZaGe1wtb1wgVW./Oiq', 'Admin', 'Jones', true, false, false, false, false, '4L4Hr3skHYYMbjkQ', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (2, 'user@aol.com', 'user', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'User', 'Charlie', true, false, false, false, false, 'v7jXapyD6fbRyZvi', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (3, 'keith@aol.com', 'keith', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Keith', 'Obannon', true, false, false, false, false, 'HuoPByrU0hC87gz8', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (4, 'erwin@aol.com', 'erwin', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Erwin', 'Lapote', true, false, false, false, false, 'kSpaxKbYIL0a5Mma', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (5, 'jeremy@aol.com', 'jeremy', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Jeremy', 'Sloan', true, false, false, false, false, 'xt4e25EoFgjx5CP2', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id) VALUES (6, 'scott@aol.com', 'scott', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Scott', 'Shoenberger', true, false, false, false, false, 'Fx05XbWjPFECJZQP', 'SITE');


------------------------------------------------------------
-- Authorities
------------------------------------------------------------

INSERT INTO authorities (authority_id, authority, is_locked) VALUES (1, 'ROLE_ADMIN', true);
INSERT INTO authorities (authority_id, authority, is_locked) VALUES (2, 'ROLE_USER', true);

------------------------------------------------------------
-- User_Authorities
------------------------------------------------------------

INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authorities (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (4, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (5, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (6, 2);


INSERT INTO contacts (first_name, last_name, birth_date, email, created_by_user, creation_time, modified_by_user, modification_time, version)
VALUES ('Summer', 'Glass', '1968-08-05', 'vitae@egestasadui.net', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Mikayla', 'Church', '1975-04-03', 'lobortis.Class@aliquam.org', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Shaine', 'Brooks', '1971-08-24', 'vel.pede@metusVivamuseuismod.edu', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Robin', 'Sullivan', '1961-09-09', 'purus.gravida@necleo.edu', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Xantha', 'Kim', '1960-08-25', 'risus.Duis.a@velnisl.ca', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Barry', 'Kirk', '1982-03-27', 'blandit.at@Maurisblanditenim.com', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Tad', 'Robellaboy', '1972-08-08', 'In.lorem.Donec@Vivamusnisi.org', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Finn', 'Robertorobo', '1974-05-27', 'aliquet@ornare.net', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Ali', 'Calhoun', '1976-11-30', 'fermentum@nulla.co.uk', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0),
('Alexandra', 'Hendricks', '1973-07-05', 'at.auctor@pellentesquemassalobortis.edu', 'admin', '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0);


INSERT INTO contact_phones (contact_id,phone_type,phone_number) VALUES ('1','Mobile','1-113-753-8020'),('1','Home','1-996-507-0853'),('2','Mobile','1-407-100-1341'),('2','Home','1-285-981-2510'),('3','Mobile','1-274-311-9291'),('3','Home','1-499-112-9185'),('4','Mobile','1-234-628-6511'),('4','Home','1-560-178-3273'),('5','Mobile','1-430-941-9233'),('5','Home','1-271-831-8886');
INSERT INTO contact_phones (contact_id,phone_type,phone_number) VALUES ('6','Mobile','1-255-105-0103'),('6','Home','1-481-652-4155'),('7','Mobile','1-917-917-8478'),('7','Home','1-766-831-2271'),('8','Mobile','1-863-515-3218'),('8','Home','1-930-909-9849'),('9','Mobile','1-423-399-6903'),('9','Home','1-294-840-1996'),('10','Mobile','1-661-300-3848'),('10','Home','1-972-479-8970');

INSERT INTO hobbies (hobby_title) VALUES ('Jogging');
INSERT INTO hobbies (hobby_title) VALUES ('Movies');
INSERT INTO hobbies (hobby_title) VALUES ('Programming');
INSERT INTO hobbies (hobby_title) VALUES ('Reading');
INSERT INTO hobbies (hobby_title) VALUES ('Swimming');

INSERT INTO contact_hobby_ids (contact_id,hobby_id) VALUES ('1','1'),('1','2'),('2','3'),('2','4'),('3','5'),('3','1'),('4','2'),('4','3'),('5','4'),('5','5');
INSERT INTO contact_hobby_ids (contact_id,hobby_id) VALUES ('6','1'),('6','2'),('7','3'),('7','4'),('8','5'),('8','1'),('9','2'),('9','3'),('10','4'),('10','5');

INSERT INTO site_options(option_id, option_name, option_value) VALUES ('1', 'siteName','My Site');
INSERT INTO site_options(option_id, option_name, option_value) VALUES ('2', 'siteDescription','My Site Description');
INSERT INTO site_options(option_id, option_name, option_value) VALUES ('3', 'addGoogleAnalytics','false');
INSERT INTO site_options(option_id, option_name, option_value) VALUES ('4', 'googleAnalyticsTrackingId','UA-XXXXXX-7');
INSERT INTO site_options(option_id, option_name, option_value) VALUES ('5', 'integerProperty','1');
