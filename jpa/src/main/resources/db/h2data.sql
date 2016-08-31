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
INSERT INTO authorities (authority_id, authority, is_locked) VALUES (3, 'ROLE_POSTS', true);

------------------------------------------------------------
-- User_Authorities
------------------------------------------------------------

INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 3);
INSERT INTO user_authorities (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 3);
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

------------------------------------------------------------
-- Posts
------------------------------------------------------------

-- INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (1, 1, 'Post One Title', 'post-one-title', 'http://nixmash.com/something', '2016-05-31 13:27:47', '2016-05-31 13:28:01', 'LINK', 'LINK', 1, 'Post One Content', 'nixmash.com', null, 0, 0, 0, 0);
-- INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (2, 1, 'Post Two Title', 'post-two-title', 'http://stackoverflow.com/something', '2016-05-31 14:30:45', '2016-05-31 14:30:47', 'LINK', 'LINK', 1, 'Post Two Content', 'stackoverflow.com', null, 0, 0, 0, 0);

INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (-1, 3, 'Not Yet Selected', 'not-yet-selected', null, '2016-03-28 17:40:18', '2016-03-28 17:40:18', 'POST', 'SINGLEPHOTO_POST', 1, 'This is a placemarker post', 'NA', null, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (1, 3, 'JavaScript · Bootstrap', 'javascript-bootstrap', 'http://getbootstrap.com/javascript/#carousel', '2016-06-06 15:30:34', '2016-06-06 15:30:34', 'LINK', 'LINK', 1, 'Bootstrap, a sleek, intuitive, and powerful mobile first front-end framework...', 'getbootstrap.com', null, 0, 2, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (2, 3, 'A Java collection of value pairs? (tuples?)', 'a-java-collection-of-value-pairs-tuples', 'http://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples', '2016-06-05 15:31:01', '2016-06-05 15:31:01', 'LINK', 'LINK_SUMMARY', 1, 'I like how Java has a Map...', 'stackoverflow.com', '/images/posts/stackoverflow.png', 0, 2, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (3, 3, 'Jsoup Parsing and Traversing Document and URL - JAVATIPS.INFO', 'jsoup-parsing-and-traversing-document-and-url-javatips-info', 'http://javatips.info/jsoup-parsing-and-traversing-document-and-url.html', '2016-06-04 15:31:38', '2016-06-04 15:31:38', 'LINK', 'LINK', 1, 'Prerequisites Development environment...', 'javatips.info', null, 0, 1, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (4, 3, 'Content Negotiation using Spring MVC', 'content-negotiation-using-spring-mvc', 'https://spring.io/blog/2013/05/`/content-negotiation-using-spring-mvc', '2016-06-03 15:32:28', '2016-06-03 15:32:28', 'LINK', 'LINK_SUMMARY', 1, '<p>There are two ways to generate output using Spring MVC...</p>', 'spring.io', '/images/posts/spring.png', 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (5, 3, 'Variations on JSON Key-Value Pairs in Spring MVC', 'variations-on-json-key-value-pairs-in-spring-mvc', 'http://nixmash.com/java/variations-on-json-key-value-pairs-in-spring-mvc/', '2016-06-02 15:34:09', '2016-06-02 15:34:09', 'LINK', 'NIXMASH_POST', 1, 'The topic of this post is pretty lightweight. A bit of a lark, really...', 'nixmash.com', 'http://nixmash.com/x/blog/2016/jsonpair0528c.png', 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (6, 3, 'Freestanding Note Post', 'freestanding-note-post', null, '2016-06-01 17:40:18', '2016-06-01 17:40:18', 'POST', 'POST', 1, 'The freestanding Note is a marvelous thing', 'NA', null, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (7, 3, '100 Ways To Title Something', '100-ways-to-title-something', null, '2016-05-30 17:40:18', '2016-05-30 17:40:18', 'POST', 'POST', 1, 'This post title begins with 100', 'NA', null, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (8, 3, '200 Ways To Title Something', '200-ways-to-title-something', null, '2016-05-29 17:40:18', '2016-05-29 17:40:18', 'POST', 'POST', 1, 'This post title begins with 200', 'NA', null, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (9, 3, '1000 Ways To Title Something', '1000-ways-to-title-something', null, '2016-05-28 17:40:18', '2016-05-28 17:40:18', 'POST', 'POST', 1, 'This post title begins with 1000', 'NA', null, 0, 0, 0, 0);

INSERT INTO tags (tag_id, tag_value) VALUES (1, 'h2tagone');
INSERT INTO tags (tag_id, tag_value) VALUES (2, 'h2tagtwo');
INSERT INTO tags (tag_id, tag_value) VALUES (3, 'h2tagthree');
INSERT INTO tags (tag_id, tag_value) VALUES (4, 'h2tagfour');
INSERT INTO tags (tag_id, tag_value) VALUES (5, 'h2 Tag With Spaces');
INSERT INTO tags (tag_id, tag_value) VALUES (6, 'h2tagsix');

INSERT INTO post_tag_ids (post_id, tag_id) VALUES (1, 1);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (1, 2);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (1, 3);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (2, 4);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (2, 1);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (3, 1);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (4, 1);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (5, 1);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (5, 5);
INSERT INTO post_tag_ids (post_id, tag_id) VALUES (6, 2);

INSERT INTO user_likes (user_id, item_id, content_type_id) VALUES (3, 1, 1);
INSERT INTO user_likes (user_id, item_id, content_type_id) VALUES (3, 2, 1);
INSERT INTO user_likes (user_id, item_id, content_type_id) VALUES (3, 3, 1);
INSERT INTO user_likes (user_id, item_id, content_type_id) VALUES (2, 1, 1);
INSERT INTO user_likes (user_id, item_id, content_type_id) VALUES (2, 2, 1);

INSERT INTO post_images (post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (1, 'WP_000993.jpg', 'f6dea6b8-87bf-42eb-a4b6-4e4c751f0d70-thumbnail.png', 'f6dea6b8-87bf-42eb-a4b6-4e4c751f0d70.jpg', 'image/jpeg', 1065071, 53726, '2016-08-03 13:49:09');
INSERT INTO post_images (post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (1, 'WP_000624.jpg', '49e5d232-56a9-4bf9-a9df-916508a4f540-thumbnail.png', '49e5d232-56a9-4bf9-a9df-916508a4f540.jpg', 'image/jpeg', 580112, 40909, '2016-08-03 13:49:09');
INSERT INTO post_images (post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (2, 'WP_000931.jpg', '000e045d-781b-4979-9a00-37692f8d33cf-thumbnail.png', '000e045d-781b-4979-9a00-37692f8d33cf.jpg', 'image/jpeg', 908532, 51092, '2016-08-03 14:58:45');

INSERT INTO flashcard_categories (category) VALUES ('category one');
INSERT INTO flashcard_categories (category) VALUES ('category two');

INSERT INTO flashcard_slides (category_id, slide_image, slide_content, datetime_created) VALUES (1, 'slideone.jpg', 'slide one', '2016-08-11 15:01:46');
INSERT INTO flashcard_slides (category_id, slide_image, slide_content, datetime_created) VALUES (1, 'slidetwo.jpg', 'slide two', '2016-08-11 15:02:11');
INSERT INTO flashcard_slides (category_id, slide_image, slide_content, datetime_created) VALUES (2, 'slidethree.jpg', 'slide three', '2016-08-11 15:02:30');
