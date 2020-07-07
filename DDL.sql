create schema usuarios;
use usuarios;


CREATE USER 'lucas'@'localhost' IDENTIFIED BY 'lucas';
GRANT ALL PRIVILEGES ON * . * TO 'lucas'@'localhost';
FLUSH PRIVILEGES;


CREATE TABLE `usuarios`.`user` (
  `id_user` BIGINT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(255) NOT NULL,
  `user_email` VARCHAR(255) NOT NULL unique,
  `user_pass` VARCHAR(255) NOT NULL,
  `user_role` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_user`));

INSERT INTO`user`
(
`user_name`,
`user_email`,
`user_pass`,
`user_role`)
VALUES
(
"lucas",
"lucas@gmail.com",
"12345",
"Admin");
