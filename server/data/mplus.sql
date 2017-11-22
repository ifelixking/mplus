CREATE DATABASE  IF NOT EXISTS `mplus` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mplus`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: mplus
-- ------------------------------------------------------
-- Server version	5.6.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `msg`
--

DROP TABLE IF EXISTS `msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `senderID` bigint(20) DEFAULT NULL,
  `receiverID` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `isSenderDelete` bit(1) DEFAULT b'0',
  `isReceiverDelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `IDX_MSG_CREATETIME` (`createTime`),
  KEY `IDX_MSG_SENDERID` (`senderID`),
  KEY `IDX_MSG_RECEIVERID` (`receiverID`),
  KEY `IDX_MSG_ISSENDERDELETE` (`isSenderDelete`),
  KEY `IDX_MSG_ISRECEIVERDELETE` (`isReceiverDelete`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `msg`
--

LOCK TABLES `msg` WRITE;
/*!40000 ALTER TABLE `msg` DISABLE KEYS */;
INSERT INTO `msg` VALUES (1,'show me',1,2,'2017-11-22 17:42:12','\0','\0'),(2,'show me the money',1,2,'2017-11-22 17:43:28','\0','\0');
/*!40000 ALTER TABLE `msg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rel_user`
--

DROP TABLE IF EXISTS `rel_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rel_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userID1` bigint(20) DEFAULT NULL,
  `userID2` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `idDelete` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `IDX_REL_USER_USER1` (`userID1`),
  KEY `IDX_REL_USER_USER2` (`userID2`),
  KEY `IDX_REL_USER_ISDELETE` (`idDelete`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rel_user`
--

LOCK TABLES `rel_user` WRITE;
/*!40000 ALTER TABLE `rel_user` DISABLE KEYS */;
INSERT INTO `rel_user` VALUES (1,1,4,NULL,'2017-11-18 21:08:07','\0'),(2,1,2,NULL,'2017-11-18 21:08:31','\0');
/*!40000 ALTER TABLE `rel_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(45) DEFAULT NULL,
  `nikeName` varchar(255) DEFAULT NULL,
  `device` varchar(255) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `token` varchar(45) DEFAULT NULL,
  `expire` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_USER_MOBILE` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'18610303648','李玉辉','iPhoneX','nintendo','5896db50-cf59-11e7-946b-b57014a47441','2017-11-23 15:47:10'),(2,'13693600531','李萌','nintendo',NULL,'1c67cdd0-c6d8-11e7-9f22-b7692b0ee844','2017-11-12 20:01:54'),(3,'13886351254','晖妈','nintendo',NULL,'3aa34c00-c6da-11e7-9d83-07dd324d8b88','2017-11-12 20:17:04'),(4,'13886351399','晖爸','nintendo',NULL,'5faa4ab0-c6dc-11e7-872c-0118d2f4952c','2017-11-12 20:32:25'),(5,'地方地方134',NULL,'nintendo',NULL,'77f30710-c6dc-11e7-b438-97c3ef73f42d','2017-11-12 20:33:06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'mplus'
--

--
-- Dumping routines for database 'mplus'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-22 17:47:51
