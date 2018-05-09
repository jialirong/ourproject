/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.0.96-community-nt : Database - db_stockmanage
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_stockmanage` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_stockmanage`;

/*Table structure for table `t_export` */

DROP TABLE IF EXISTS `t_export`;

CREATE TABLE `t_export` (
  `id` int(11) NOT NULL auto_increment,
  `goodsId` int(20) default NULL,
  `expoPrice` varchar(20) default NULL,
  `expoDate` datetime default NULL,
  `expoNum` varchar(20) default NULL,
  `expoDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_t_export` (`goodsId`),
  CONSTRAINT `FK_t_export` FOREIGN KEY (`goodsId`) REFERENCES `t_goods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `t_export` */

insert  into `t_export`(`id`,`goodsId`,`expoPrice`,`expoDate`,`expoNum`,`expoDesc`) values (2,2,'1001','2014-04-01 12:12:12','100','java01'),(3,3,'1002','2014-04-02 12:12:12','200','java02'),(4,4,'1003','2014-04-03 12:12:12','300','java03'),(5,5,'1004','2014-04-04 12:12:12','400','java04'),(6,6,'1005','2014-04-05 12:12:12','500','java05'),(7,7,'1006','2014-04-06 12:12:12','600','java06'),(8,8,'1007','2014-04-07 12:12:12','120','java07'),(9,9,'1008','2014-04-08 12:12:12','130','java08'),(10,12,'10091','2014-04-10 00:00:00','10091','java091');

/*Table structure for table `t_goods` */

DROP TABLE IF EXISTS `t_goods`;

CREATE TABLE `t_goods` (
  `id` int(11) NOT NULL auto_increment,
  `goodsId` int(20) default NULL,
  `goodsName` varchar(20) default NULL,
  `proId` int(20) default NULL,
  `typeId` int(20) default NULL,
  `goodsDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_t_goods` (`typeId`),
  KEY `FK_t1_goods` (`proId`),
  CONSTRAINT `FK_t1_goods` FOREIGN KEY (`proId`) REFERENCES `t_provider` (`id`),
  CONSTRAINT `FK_t_goods` FOREIGN KEY (`typeId`) REFERENCES `t_goodstype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `t_goods` */

insert  into `t_goods`(`id`,`goodsId`,`goodsName`,`proId`,`typeId`,`goodsDesc`) values (1,22,'java01',22,1,'java01'),(2,23,'java02',23,2,'java02'),(3,24,'java03',24,3,'java03'),(4,25,'java04',25,4,'java04'),(5,26,'java05',26,5,'java05'),(6,27,'java06',27,1,'java06'),(7,28,'java07',28,2,'java07'),(8,29,'java08',29,3,'java08'),(9,30,'java09',22,4,'java09'),(10,31,'java10',23,5,'java10'),(12,101,'c#',29,3,'c#'),(13,1,'vb1',23,1,'java0121111'),(14,101,'vb2',24,2,'java02222'),(16,922,'n你懂嘛',22,1,'收到收到'),(18,108,'108.0',22,1,'108.0');

/*Table structure for table `t_goodstype` */

DROP TABLE IF EXISTS `t_goodstype`;

CREATE TABLE `t_goodstype` (
  `id` int(11) NOT NULL auto_increment,
  `typeName` varchar(20) default NULL,
  `typeDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

/*Data for the table `t_goodstype` */

insert  into `t_goodstype`(`id`,`typeName`,`typeDesc`) values (1,'计算机书籍1','关于计算机1'),(2,'计算机书籍2','关于计算机22'),(3,'计算机书籍3','关于计算机3'),(4,'计算机书籍4','关于计算机4'),(5,'计算机书籍5','关于计算机5'),(7,'计算机书籍6','关于计算机6'),(8,'计算机书籍7','关于计算机7'),(9,'计算机书籍8','关于计算机8'),(10,'计算机书籍9','关于计算机9'),(11,'计算机书籍10','关于计算机10'),(12,'计算机书籍11','关于计算机11'),(13,'计算机书籍12','关于计算机12'),(14,'计算机书籍13','关于计算机13'),(15,'计算机书籍14','关于计算机14'),(16,'计算机书籍15','关于计算机15'),(17,'计算机书籍16','关于计算机16'),(18,'计算机书籍17','关于计算机17'),(20,'Java书籍','关于java'),(21,'C++书籍1','关于C++1'),(22,'C++书籍123','1123');

/*Table structure for table `t_import` */

DROP TABLE IF EXISTS `t_import`;

CREATE TABLE `t_import` (
  `id` int(11) NOT NULL auto_increment,
  `goodsId` int(20) default NULL,
  `impoPrice` varchar(20) default NULL,
  `impoDate` datetime default NULL,
  `impoNum` varchar(20) default NULL,
  `impoDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_t_import` (`goodsId`),
  CONSTRAINT `FK_t_import` FOREIGN KEY (`goodsId`) REFERENCES `t_goods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `t_import` */

insert  into `t_import`(`id`,`goodsId`,`impoPrice`,`impoDate`,`impoNum`,`impoDesc`) values (1,1,'100','2014-03-01 12:12:12','1001','java01'),(2,2,'200','2014-03-02 12:13:13','1002','java02'),(3,3,'300','2014-03-03 12:14:12','1003','java03'),(4,4,'400','2014-03-04 12:15:12','1004','java04'),(5,5,'500','2014-03-06 12:16:12','1005','java05'),(6,6,'600','2014-03-07 12:17:12','1006','java06'),(7,7,'700','2014-03-08 12:18:12','1030','java07'),(8,8,'800','2014-03-09 12:19:12','1040','java08'),(9,9,'900','2014-03-10 12:10:12','1050','java09'),(19,1,'911','2014-05-29 00:00:00','911','as123'),(20,9,'11','2014-05-06 00:00:00','11','a'),(21,1,'111233','2014-05-16 00:00:00','111233','asdfddee'),(22,1,'911.0','2014-12-22 00:00:00','911.0','900.0'),(23,2,'912.0','2014-11-12 00:00:00','912.0','902.0'),(25,1,'1007781.0','2014-03-01 00:00:00','7782.0','7781.0'),(26,3,'7781.0','2014-03-01 00:00:00','8177.0','1877.0');

/*Table structure for table `t_provider` */

DROP TABLE IF EXISTS `t_provider`;

CREATE TABLE `t_provider` (
  `id` int(11) NOT NULL auto_increment,
  `proId` int(20) default NULL,
  `proName` varchar(20) default NULL,
  `linkman` varchar(20) default NULL,
  `proPhone` varchar(20) default NULL,
  `proDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Data for the table `t_provider` */

insert  into `t_provider`(`id`,`proId`,`proName`,`linkman`,`proPhone`,`proDesc`) values (22,103,'公司3','103@qq.com','103@qq.com','常用3'),(23,104,'公司4','104@qq.com','104@qq.com','常用4'),(24,105,'公司5','105@qq.com','105@qq.com','常用5'),(25,106,'公司6','106@qq.com','106@qq.com','常用6'),(26,107,'公司7','107@qq.com','107@qq.com','常用7'),(27,108,'公司8','108@qq.com','108@qq.com','常用8'),(28,109,'公司9','109@qq.com','109@qq.com','常用9'),(29,110,'公司10','110@qq.com','110@qq.com','常用10'),(30,111,'AB公司','123','11111','AB1'),(31,9111,'上11','阿斯顿1','1231','阿斯顿1');

/*Table structure for table `t_stock` */

DROP TABLE IF EXISTS `t_stock`;

CREATE TABLE `t_stock` (
  `id` int(11) NOT NULL auto_increment,
  `goodsId` int(20) default NULL,
  `stockNum` varchar(20) default NULL,
  `impoPrice` varchar(20) default NULL,
  `expoPrice` varchar(20) default NULL,
  `stockDesc` varchar(1000) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_t_stock` (`goodsId`),
  CONSTRAINT `FK_t_stock` FOREIGN KEY (`goodsId`) REFERENCES `t_goods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

/*Data for the table `t_stock` */

insert  into `t_stock`(`id`,`goodsId`,`stockNum`,`impoPrice`,`expoPrice`,`stockDesc`) values (2,1,'1001','1001','1011','java01'),(3,2,'1002','1002','1021','java02'),(5,4,'1004','1004','1041','java04'),(6,5,'1005','1005','1051','java05'),(7,6,'1006','1006','1061','java06'),(8,7,'1007','1007','1071','java07'),(9,8,'1008','1008','1081','java08'),(10,9,'1009','1009','1091','java09'),(11,10,'1010','1010','1111','java10'),(13,NULL,NULL,'4321','4321','a'),(14,NULL,NULL,'12','12','a'),(15,NULL,'12321','121','1212','s'),(23,12,'25','3333','23','qwer');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL auto_increment,
  `userName` varchar(20) default NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`userName`,`password`) values (1,'java','123');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
