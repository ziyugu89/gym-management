/*
 Navicat Premium Data Transfer

 Source Server         : 57
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : db_gym

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 28/07/2025 07:42:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `adminNo` int(11) NOT NULL AUTO_INCREMENT,
  `adminAccount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `adminPassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`adminNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '');

-- ----------------------------
-- Table structure for checkin
-- ----------------------------
DROP TABLE IF EXISTS `checkin`;
CREATE TABLE `checkin`  (
  `checkNo` int(11) NOT NULL AUTO_INCREMENT,
  `memberNo` int(11) NULL DEFAULT NULL,
  `checkDate` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`checkNo`) USING BTREE,
  INDEX `memberNo`(`memberNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkin
-- ----------------------------
INSERT INTO `checkin` VALUES (1, 1, '2023-06-20 00:00:00');
INSERT INTO `checkin` VALUES (2, NULL, '2023-06-29 00:00:00');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `courseNo` int(11) NOT NULL AUTO_INCREMENT,
  `courseName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `courseTime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `courseDuration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `coursePrice` decimal(10, 2) NULL DEFAULT NULL,
  `courseDesc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `courseIntegral` int(11) NULL DEFAULT NULL,
  `employeeNo` int(11) NULL DEFAULT NULL COMMENT '教练员工编号',
  `managerNo` int(11) NULL DEFAULT NULL COMMENT '经理编号',
  PRIMARY KEY (`courseNo`) USING BTREE,
  INDEX `employeeNo`(`employeeNo`) USING BTREE,
  INDEX `managerNo`(`managerNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (3, '瑜伽', '2024-07-27 21:21:11', '10', 1000.00, '瑜伽课', 200, 1, 2);
INSERT INTO `course` VALUES (6, '跑步训练', '2025-07-17 06:44:48', '900', 200.00, '跑步', 2000, 1, 2);
INSERT INTO `course` VALUES (9, '拳击', '2025-07-16 16:00:00', '244', 388.00, '拳击', 3880, 1, 2);
INSERT INTO `course` VALUES (10, '舞蹈', '2025-07-16 16:00:00', '66', 888.00, '舞蹈', 8880, 1, 2);
INSERT INTO `course` VALUES (11, '增肌训练', '2025-07-17 16:00:00', '320', 888.00, '增肌训练', 8880, 1, 2);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `employeeNo` int(11) NOT NULL AUTO_INCREMENT,
  `employeeAge` int(11) NULL DEFAULT NULL,
  `employeeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `employeeGender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `employeePhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `employeeTime` datetime(0) NULL DEFAULT NULL,
  `employeeMessage` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `employeeJob` int(11) NULL DEFAULT NULL COMMENT '1 教练 2前台 3保洁 4经理',
  PRIMARY KEY (`employeeNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 35, '橙灰', 'M', '15798645769', '2023-06-20 12:18:29', '冠军教练', 1);
INSERT INTO `employee` VALUES (2, 40, '小黄', 'M', '13568974568', '2023-06-29 09:38:48', '项目经理', 4);
INSERT INTO `employee` VALUES (3, 24, '小胡', 'F', '13598756985', '2023-06-29 10:09:15', NULL, 2);

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`  (
  `equipmentNo` int(11) NOT NULL AUTO_INCREMENT,
  `equipmentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `equipmentLocation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `equipmentState` int(11) NULL DEFAULT NULL,
  `equipmentMessage` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `employeeJob` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`equipmentNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipment
-- ----------------------------
INSERT INTO `equipment` VALUES (1, '跑步机', '大厅', 1, '无', NULL);
INSERT INTO `equipment` VALUES (2, '瑜伽垫', '大厅', 1, '无', NULL);
INSERT INTO `equipment` VALUES (3, '11', '11', 1, '111', NULL);
INSERT INTO `equipment` VALUES (4, '跑步机', '大厅', 0, '无', NULL);
INSERT INTO `equipment` VALUES (5, '111', '111', 1, '111', NULL);
INSERT INTO `equipment` VALUES (6, '11111111', '111111', 1, '1111', NULL);

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `managerNo` int(11) NOT NULL AUTO_INCREMENT,
  `employeeNo` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`managerNo`) USING BTREE,
  INDEX `employeeNo`(`employeeNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES (1, 2);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `memberNo` int(11) NOT NULL AUTO_INCREMENT,
  `memberUsername` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `memberPassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `memberName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `memberAge` int(11) NULL DEFAULT NULL,
  `memberGender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `memberPhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `cardTime` datetime(0) NULL DEFAULT NULL,
  `memberHeight` double NULL DEFAULT NULL,
  `memberWeight` double NULL DEFAULT NULL,
  `cardClass` int(11) NULL DEFAULT NULL,
  `cardNextClass` int(11) NULL DEFAULT NULL,
  `memberIntegral` int(11) NULL DEFAULT NULL,
  `memberChange` int(11) NULL DEFAULT NULL,
  `personalizedSignature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `memberPower` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`memberNo`) USING BTREE,
  UNIQUE INDEX `memberPhone`(`memberPhone`) USING BTREE COMMENT '手机号唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, 'admin', 'admin', 'admin', 18, 'F', '18470558967', '2023-06-20 12:12:01', 175, 60, 200, 150, 47, 39, 'hhh', '1', NULL);
INSERT INTO `member` VALUES (2, '0031023684', '123456', '北京小熊', 20, 'M', '13333333333', '2023-06-20 12:16:16', 180, 70, 50, 50, 112644, 8824, '1', '4', NULL);
INSERT INTO `member` VALUES (3, '0000', '', '232', 18, 'F', '122323', '2023-06-20 12:12:01', 175, 60, 200, 150, 47, 39, NULL, '1', NULL);

-- ----------------------------
-- Table structure for recharge
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge`  (
  `rechargeNo` int(11) NOT NULL AUTO_INCREMENT,
  `rechargeDate` datetime(0) NULL DEFAULT NULL,
  `rechargeMethod` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `rechargeStatus` int(11) NULL DEFAULT NULL,
  `rechargeMoney` double NULL DEFAULT NULL,
  `memberNo` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`rechargeNo`) USING BTREE,
  INDEX `memberNo`(`memberNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recharge
-- ----------------------------
INSERT INTO `recharge` VALUES (1, '2025-07-27 00:00:00', '在线充值', 1, 100, 2);
INSERT INTO `recharge` VALUES (2, '2025-07-27 00:00:00', '在线充值', 1, 100, 2);
INSERT INTO `recharge` VALUES (3, '2025-07-27 00:00:00', '在线充值', 1, 10000, 2);
INSERT INTO `recharge` VALUES (4, '2025-07-27 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (5, '2025-07-27 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (6, '2025-07-27 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (7, '2025-07-27 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (8, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (9, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (10, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (11, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (12, '2025-07-28 00:00:00', '在线充值', 1, 100, 2);
INSERT INTO `recharge` VALUES (13, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);
INSERT INTO `recharge` VALUES (14, '2025-07-28 00:00:00', '在线充值', 1, 200, 2);

-- ----------------------------
-- Table structure for register
-- ----------------------------
DROP TABLE IF EXISTS `register`;
CREATE TABLE `register`  (
  `registerNo` int(11) NOT NULL AUTO_INCREMENT,
  `courseNo` int(11) NULL DEFAULT NULL,
  `memberNo` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`registerNo`) USING BTREE,
  INDEX `courseNo`(`courseNo`) USING BTREE,
  INDEX `memberNo`(`memberNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of register
-- ----------------------------
INSERT INTO `register` VALUES (6, 5, 2);
INSERT INTO `register` VALUES (8, 4, 2);
INSERT INTO `register` VALUES (9, 7, 2);
INSERT INTO `register` VALUES (10, 8, 2);
INSERT INTO `register` VALUES (12, 10, 2);

SET FOREIGN_KEY_CHECKS = 1;
