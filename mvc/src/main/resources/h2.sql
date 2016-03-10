create table UserConnection (
  userId varchar(255) not null,
  providerId varchar(255) not null,
  providerUserId varchar(255),
  rank int not null,
  displayName varchar(255),
  profileUrl varchar(512),
  imageUrl varchar(512),
  accessToken varchar(1024) not null,
  secret varchar(255),
  refreshToken varchar(255),
  expireTime bigint,
  primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);


INSERT INTO `userconnection` VALUES ('yeyeye', 'facebook', '103695sdsds', '1', 'sdsds', 'https:///103695193351811/', 'https://graph.facebook.com/v2.5/103695193351811/picture', 'CAACoujK6ltwBAGjAjtkUyABVriHAq8VyKKPuXgWn0RBbkZBMCdHyrJZB6IUlm84tDSCodVuUynVF9D8O9m2gNu8xvWIuNFL0vGyi1QLmPBFfpsBfF3D0UGv0itcl5KUfoiVexzzAG7051ZArHUXMNWWqPF7oeuQZCpQt4LtvtZBpg7C6VD8ZBLtGusNgHqUZBYZD', null, null, '1462733317625');

