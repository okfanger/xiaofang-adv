-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.28 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 xiaofang 的数据库结构
CREATE DATABASE IF NOT EXISTS `xiaofang` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `xiaofang`;

-- 导出  表 xiaofang.object_alias 结构
CREATE TABLE IF NOT EXISTS `object_alias` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `origin_name` varchar(10) DEFAULT NULL,
  `alias_name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `origin_name` (`origin_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 正在导出表  xiaofang.object_alias 的数据：~3 rows (大约)
DELETE FROM `object_alias`;
/*!40000 ALTER TABLE `object_alias` DISABLE KEYS */;
INSERT INTO `object_alias` (`id`, `origin_name`, `alias_name`) VALUES
	(1, 'attack', '攻击力'),
	(2, 'hp', '生命值'),
	(3, 'mp', '法力');
/*!40000 ALTER TABLE `object_alias` ENABLE KEYS */;

-- 导出  表 xiaofang.object_property 结构
CREATE TABLE IF NOT EXISTS `object_property` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `effect` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 正在导出表  xiaofang.object_property 的数据：~8 rows (大约)
DELETE FROM `object_property`;
/*!40000 ALTER TABLE `object_property` DISABLE KEYS */;
INSERT INTO `object_property` (`class_id`, `name`, `cost`, `effect`) VALUES
	(1, '匕首', 10, 'attack:+10,hp:+10,mp:-10'),
	(2, '炸弹', 5, 'attack:+20,mp:-10'),
	(3, '高射炮', 100, 'attack:+50,mp:-10'),
	(4, '原子弹', 200, 'attack:+10,mp:-10'),
	(5, '苹果', 20, 'mp:10'),
	(6, '甜甜圈', 30, 'hp:20'),
	(7, '毒药', 0, 'hp:-10,mp:-10'),
	(8, '别墅', 9999, 'hp:-99');
/*!40000 ALTER TABLE `object_property` ENABLE KEYS */;

-- 导出  表 xiaofang.player_box 结构
CREATE TABLE IF NOT EXISTS `player_box` (
  `object_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `class_id` int(11) DEFAULT NULL,
  `amount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`object_id`),
  KEY `player_id` (`owner_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- 正在导出表  xiaofang.player_box 的数据：~8 rows (大约)
DELETE FROM `player_box`;
/*!40000 ALTER TABLE `player_box` DISABLE KEYS */;
INSERT INTO `player_box` (`object_id`, `owner_id`, `class_id`, `amount`) VALUES
	(5, 3, 1, 22),
	(12, 5, 1, 0),
	(13, 5, 2, 0),
	(14, 5, 3, 0),
	(51, 1, 5, 0),
	(52, 1, 6, 0),
	(56, 1, 7, 11),
	(57, 1, 2, 2);
/*!40000 ALTER TABLE `player_box` ENABLE KEYS */;

-- 导出  表 xiaofang.player_condition 结构
CREATE TABLE IF NOT EXISTS `player_condition` (
  `player_id` int(11) NOT NULL,
  `hp` int(11) DEFAULT NULL,
  `mp` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `attack` int(11) DEFAULT NULL,
  UNIQUE KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  xiaofang.player_condition 的数据：~2 rows (大约)
DELETE FROM `player_condition`;
/*!40000 ALTER TABLE `player_condition` DISABLE KEYS */;
INSERT INTO `player_condition` (`player_id`, `hp`, `mp`, `money`, `attack`) VALUES
	(1, 88, 70, 9999, 100),
	(5, 130, 30, 855, 100);
/*!40000 ALTER TABLE `player_condition` ENABLE KEYS */;

-- 导出  表 xiaofang.player_profile 结构
CREATE TABLE IF NOT EXISTS `player_profile` (
  `player_id` int(11) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(10) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `registered_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`) USING BTREE,
  UNIQUE KEY `nick_name` (`nick_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 正在导出表  xiaofang.player_profile 的数据：~1 rows (大约)
DELETE FROM `player_profile`;
/*!40000 ALTER TABLE `player_profile` DISABLE KEYS */;
INSERT INTO `player_profile` (`player_id`, `nick_name`, `password`, `registered_date`) VALUES
	(1, 'admin', 'admin', '2021-12-13 10:40:47'),
	(5, 'user1', 'user1', '2021-12-17 10:33:45');
/*!40000 ALTER TABLE `player_profile` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
