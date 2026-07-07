SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alembic_version
-- ----------------------------
DROP TABLE IF EXISTS `alembic_version`;
CREATE TABLE `alembic_version`  (
  `version_num` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`version_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alembic_version
-- ----------------------------
INSERT INTO `alembic_version` VALUES ('abdc9ede147d');

-- ----------------------------
-- Table structure for cdr
-- ----------------------------
DROP TABLE IF EXISTS `cdr`;
CREATE TABLE `cdr`  (
  `accountcode` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `src` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dst` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dcontext` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `clid` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `channel` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dstchannel` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lastapp` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lastdata` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start` datetime(0) NULL DEFAULT NULL,
  `answer` datetime(0) NULL DEFAULT NULL,
  `end` datetime(0) NULL DEFAULT NULL,
  `calldate` datetime(0) NULL DEFAULT NULL,
  `duration` int(0) NULL DEFAULT NULL,
  `billsec` int(0) NULL DEFAULT NULL,
  `disposition` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amaflags` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `userfield` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uniqueid` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `linkedid` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `peeraccount` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sequence` int(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cdr
-- ----------------------------
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000001', '', 'StopMixMonitor', '0x7fcec4005d10', NULL, NULL, NULL, '2026-01-26 08:43:28', 12, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-01-26 08:43:53', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000003', '', 'StopMixMonitor', '0x7fcd94003200', NULL, NULL, NULL, '2026-01-26 09:03:29', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', '', 'StopMixMonitor', '0x7f26b800fdb0', NULL, NULL, NULL, '2026-01-27 18:44:10', 4, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', '', 'StopMixMonitor', '0x7fd51800d1e0', NULL, NULL, NULL, '2026-01-27 18:51:42', 4, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6004', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', '', 'Hangup', '', NULL, NULL, NULL, '2026-01-30 17:26:07', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6004', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000001', '', 'Hangup', '', NULL, NULL, NULL, '2026-01-30 17:27:29', 12, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6004', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', '', 'StopMixMonitor', '0x7f69b800d2d0', NULL, NULL, NULL, '2026-01-30 17:29:13', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', 'PJSIP/6002-00000001', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 09:08:25', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', 'PJSIP/6002-00000003', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 09:10:23', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000004', 'PJSIP/6002-00000005', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 09:14:31', 10, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000006', 'PJSIP/6002-00000007', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 09:15:23', 11, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', 'PJSIP/6002-00000001', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 16:20:33', 5, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', 'PJSIP/6002-00000003', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 16:23:20', 7, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000004', 'PJSIP/6002-00000005', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 16:29:56', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000006', 'PJSIP/6002-00000007', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-03 16:30:19', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '07553331845', '6010', 'outbound-dialer', '\"\" <07553331845>', 'PJSIP/6003-0000000a', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-03 17:13:49', 6, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', 'PJSIP/6002-00000001', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-04 15:49:36', 8, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', 'PJSIP/6002-00000003', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-04 16:01:27', 5, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000004', 'PJSIP/6002-00000005', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-04 16:03:14', 9, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000006', 'PJSIP/6002-00000007', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-04 16:04:22', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000008', 'PJSIP/6002-00000009', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-04 16:05:43', 14, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000a', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-04 16:31:00', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 10:24:47', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000001', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 10:29:07', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 10:31:32', 1, 1, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000003', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 10:43:34', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000004', '', 'StopMixMonitor', '0x7f415400d620', NULL, NULL, NULL, '2026-02-06 10:51:24', 22, 22, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000005', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 10:59:50', 25, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000006', 'PJSIP/6002-00000007', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-06 11:00:26', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000008', 'PJSIP/6002-00000009', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-06 11:13:42', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6002', '6010', 'from-internal', '\"6002\" <6002>', 'PJSIP/6002-0000000a', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/6002/recording_20260206111437.wav', NULL, NULL, NULL, '2026-02-06 11:14:37', 46, 46, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6002', '6010', 'from-internal', '\"6002\" <6002>', 'PJSIP/6002-0000000b', '', 'StopMixMonitor', '0x7f415c017d80', NULL, NULL, NULL, '2026-02-06 11:16:56', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000c', 'PJSIP/6002-0000000d', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-06 11:18:42', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000e', 'PJSIP/6002-0000000f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-06 11:19:35', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000010', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-06 11:20:06', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000011', 'PJSIP/6002-00000012', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-06 11:20:26', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000013', 'PJSIP/6002-00000014', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-06 11:22:53', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000015', '', 'StopMixMonitor', '0x7f416c019ed0', NULL, NULL, NULL, '2026-02-06 11:46:18', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000016', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/6003/recording_20260206191426.wav', NULL, NULL, NULL, '2026-02-06 19:14:26', 6, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6010', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000000', '', 'StopMixMonitor', '0x7fe440014c30', NULL, NULL, NULL, '2026-02-11 15:42:29', 10, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000000', 'PJSIP/6003-00000001', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-14 10:47:47', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000002', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260214104945.', NULL, NULL, NULL, '2026-02-14 10:49:45', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000003', '', 'StopMixMonitor', '0x7fc3e800d370', NULL, NULL, NULL, '2026-02-14 10:52:58', 5, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000004', '', 'StopMixMonitor', '0x7fc400010bb0', NULL, NULL, NULL, '2026-02-14 10:54:36', 12, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000005', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260214105533.', NULL, NULL, NULL, '2026-02-14 10:55:33', 5, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000006', '', 'StopMixMonitor', '0x7fc3f400ca90', NULL, NULL, NULL, '2026-02-14 10:57:25', 5, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000007', '', 'StopMixMonitor', '0x7fc3f401c300', NULL, NULL, NULL, '2026-02-14 11:00:06', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000000', '', 'StopMixMonitor', '0x7f1f3c00d840', NULL, NULL, NULL, '2026-02-14 11:05:06', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000001', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-14 11:06:46', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000002', 'PJSIP/6002-00000003', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:08:54', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000004', '', 'StopMixMonitor', '0x7f1fbc00fda0', NULL, NULL, NULL, '2026-02-14 11:10:21', 20, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000005', '', 'StopMixMonitor', '0x7f1fb0005b90', NULL, NULL, NULL, '2026-02-14 11:25:55', 12, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000006', 'PJSIP/6002-00000007', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:33:58', 7, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000008', 'PJSIP/6002-00000009', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:34:19', 4, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000a', 'PJSIP/6002-0000000b', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:34:46', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000c', 'PJSIP/6002-0000000d', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:37:39', 18, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000000e', 'PJSIP/6002-0000000f', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:38:42', 7, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000010', 'PJSIP/6002-00000011', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:40:36', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000012', '', 'StopMixMonitor', '0x7f1fb00116f0', NULL, NULL, NULL, '2026-02-14 11:41:33', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000013', 'PJSIP/6002-00000014', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:42:59', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000015', '', 'StopMixMonitor', '0x7f1f54004900', NULL, NULL, NULL, '2026-02-14 11:46:12', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000016', 'PJSIP/6002-00000017', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:46:52', 16, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000018', '', 'StopMixMonitor', '0x7f1fa00112c0', NULL, NULL, NULL, '2026-02-14 11:47:32', 22, 22, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000019', 'PJSIP/6002-0000001a', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:54:10', 26, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001b', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260214115703.', NULL, NULL, NULL, '2026-02-14 11:57:03', 24, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000001c', 'PJSIP/6002-0000001d', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 11:58:45', 80, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000001e', 'PJSIP/6002-0000001f', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 12:05:08', 46, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000020', 'PJSIP/6002-00000021', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 12:05:58', 21, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000022', '', 'StopMixMonitor', '0x7f1e9400e170', NULL, NULL, NULL, '2026-02-14 12:07:08', 12, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000023', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260214121357.', NULL, NULL, NULL, '2026-02-14 12:13:57', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000024', '', 'StopMixMonitor', '0x7f1fa80064f0', NULL, NULL, NULL, '2026-02-14 12:14:25', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000025', '', 'StopMixMonitor', '0x7f1f440215b0', NULL, NULL, NULL, '2026-02-14 12:47:35', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000026', 'PJSIP/6002-00000027', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:13:03', 50, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000028', 'PJSIP/6002-00000029', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:15:40', 77, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000002a', 'PJSIP/6002-0000002b', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:36:18', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000002c', 'PJSIP/6002-0000002d', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:37:51', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-0000002e', 'PJSIP/6002-0000002f', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:40:42', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000030', 'PJSIP/6002-00000031', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:41:03', 2, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6003', '6002', 'from-internal', '\"6003\" <6003>', 'PJSIP/6003-00000032', 'PJSIP/6002-00000033', 'Dial', 'PJSIP/6002', NULL, NULL, NULL, '2026-02-14 13:41:28', 44, 42, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000034', '', 'StopMixMonitor', '0x7f1f44008f10', NULL, NULL, NULL, '2026-02-14 13:42:45', 20, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000035', '', 'StopMixMonitor', '0x7f1f5400a390', NULL, NULL, NULL, '2026-02-14 13:46:02', 20, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000036', '', 'StopMixMonitor', '0x7f1f4c010d00', NULL, NULL, NULL, '2026-02-14 13:49:43', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000037', '', 'StopMixMonitor', '0x7f1e94017500', NULL, NULL, NULL, '2026-02-14 13:50:54', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000038', '', 'StopMixMonitor', '0x7f1e88005bd0', NULL, NULL, NULL, '2026-02-14 13:52:13', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000039', '', 'StopMixMonitor', '0x7f1f54006410', NULL, NULL, NULL, '2026-02-14 13:54:03', 7, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000003a', '', 'StopMixMonitor', '0x7f1f4c016230', NULL, NULL, NULL, '2026-02-14 14:00:33', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000000', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260224091550.', NULL, NULL, NULL, '2026-02-24 09:15:50', 21, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000002', 'PJSIP/6003-00000003', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-24 09:35:44', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000001', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260224093046.', NULL, NULL, NULL, '2026-02-24 09:30:46', 677, 677, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000004', 'PJSIP/6003-00000005', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-24 11:07:25', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000000', 'PJSIP/6002-00000001', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 09:49:37', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000002', 'PJSIP/6003-00000003', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 09:50:21', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000004', 'PJSIP/6002-00000005', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 09:50:48', 36, 36, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000004', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 09:51:24', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000006', '', 'StopMixMonitor', '0x7f413800ddb0', NULL, NULL, NULL, '2026-02-25 09:52:20', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000007', 'PJSIP/6003-00000008', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 09:53:44', 7, 7, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000009', '', 'StopMixMonitor', '0x7f414000bb80', NULL, NULL, NULL, '2026-02-25 12:05:01', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000a', '', 'StopMixMonitor', '0x7f414802f850', NULL, NULL, NULL, '2026-02-25 12:06:44', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000b', 'PJSIP/6002-0000000c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 12:07:15', 8, 8, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000d', '', 'StopMixMonitor', '0x7f41a80062d0', NULL, NULL, NULL, '2026-02-25 14:41:03', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000e', '', 'StopMixMonitor', '0x7f41bc010630', NULL, NULL, NULL, '2026-02-25 14:41:51', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000f', '', 'StopMixMonitor', '0x7f413800c520', NULL, NULL, NULL, '2026-02-25 14:42:36', 20, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000010', 'PJSIP/6002-00000011', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 14:43:26', 25, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000010', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 14:43:52', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000012', '', 'StopMixMonitor', '0x7f4148006750', NULL, NULL, NULL, '2026-02-25 14:45:20', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000013', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 14:49:16', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000014', 'PJSIP/6002-00000015', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 14:49:47', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000014', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 14:50:02', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000016', '', 'StopMixMonitor', '0x7f414800f670', NULL, NULL, NULL, '2026-02-25 14:50:15', 24, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000017', 'PJSIP/6002-00000018', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:19:39', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000017', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 15:19:59', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000019', 'PJSIP/6002-0000001a', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:22:21', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001b', 'PJSIP/6002-0000001c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:23:58', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001d', '', 'StopMixMonitor', '0x7f41a8027780', NULL, NULL, NULL, '2026-02-25 15:24:47', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001e', 'PJSIP/6002-0000001f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:25:13', 16, 16, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000020', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 15:26:23', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000021', 'PJSIP/6002-00000022', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:26:48', 10, 10, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000023', 'PJSIP/6002-00000024', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:28:24', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000025', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 15:29:04', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000026', 'PJSIP/6002-00000027', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:29:27', 14, 14, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000028', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 15:31:02', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000029', 'PJSIP/6002-0000002a', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:35:48', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002b', 'PJSIP/6002-0000002c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:39:10', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002d', 'PJSIP/6002-0000002e', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:47:50', 16, 16, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002f', 'PJSIP/6002-00000030', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 15:49:16', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000031', 'PJSIP/6002-00000032', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 16:34:32', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000033', '', 'StopMixMonitor', '0x7f407800bee0', NULL, NULL, NULL, '2026-02-25 16:34:58', 4, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000034', '', 'StopMixMonitor', '0x7f414000db50', NULL, NULL, NULL, '2026-02-25 16:35:59', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000035', '', 'StopMixMonitor', '0x7f413c032780', NULL, NULL, NULL, '2026-02-25 17:21:07', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000036', 'PJSIP/6002-00000037', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:23:02', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', 'Unknown', '6010', 'from-internal', '\"Unknown\" <Unknown>', 'PJSIP/fxogateway-00000038', 'PJSIP/6002-00000039', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:27:18', 13, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', 'Unknown', '6010', 'from-internal', '\"Unknown\" <Unknown>', 'PJSIP/fxogateway-00000038', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:27:32', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003a', 'PJSIP/6002-0000003b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:28:11', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003a', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:28:29', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003c', 'PJSIP/6002-0000003d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:33:43', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003e', 'PJSIP/6002-0000003f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:35:27', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000040', 'PJSIP/6002-00000041', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:36:59', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000042', 'PJSIP/6002-00000043', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:38:43', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000044', 'PJSIP/6002-00000045', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:39:43', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000046', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:41:38', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000047', 'PJSIP/6002-00000048', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:42:32', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000049', 'PJSIP/6003-0000004a', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:43:28', 5, 5, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004b', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:43:50', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004c', 'PJSIP/6002-0000004d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:45:02', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000004e', 'PJSIP/6003-0000004f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:45:33', 43, 43, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000004e', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:46:17', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000050', 'PJSIP/6002-00000051', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:48:43', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000052', 'PJSIP/6003-00000053', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:49:16', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000054', 'PJSIP/6002-00000055', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:50:27', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000056', 'PJSIP/6002-00000057', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:51:20', 14, 14, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000058', 'PJSIP/6002-00000059', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:55:52', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000058', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 17:56:10', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000005a', '', 'StopMixMonitor', '0x7f41a8001d40', NULL, NULL, NULL, '2026-02-25 17:59:09', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000005b', 'PJSIP/6002-0000005c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 17:59:24', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000005d', 'PJSIP/6002-0000005e', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:00:14', 11, 11, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000005f', 'PJSIP/6002-00000060', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:07:32', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000061', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260225181017.', NULL, NULL, NULL, '2026-02-25 18:10:17', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000062', 'PJSIP/6002-00000063', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:12:56', 23, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000062', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:13:20', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000064', 'PJSIP/6002-00000065', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:17:00', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000064', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:17:16', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000066', 'PJSIP/6003-00000067', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:18:04', 26, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000066', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:18:31', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000068', 'PJSIP/6002-00000069', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:20:35', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000068', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:20:51', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-0000006a', '', 'StopMixMonitor', '0x7f4058001150', NULL, NULL, NULL, '2026-02-25 18:20:55', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006b', 'PJSIP/6002-0000006c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:23:31', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006d', 'PJSIP/6002-0000006e', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:24:22', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006d', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:24:41', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-0000006f', 'PJSIP/6002-00000070', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-25 18:25:40', 41, 41, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-0000006f', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-25 18:26:21', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000000', '', 'StopMixMonitor', '0x7fb96400f930', NULL, NULL, NULL, '2026-02-26 09:05:57', 21, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000001', 'PJSIP/6002-00000002', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:06:32', 22, 22, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000003', 'PJSIP/6002-00000004', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:12:27', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000005', 'PJSIP/6002-00000006', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:16:43', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000007', 'PJSIP/6002-00000008', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:20:48', 17, 17, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000009', 'PJSIP/6002-0000000a', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:26:03', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000b', '', 'StopMixMonitor', '0x7fb9d8013600', NULL, NULL, NULL, '2026-02-26 09:34:24', 20, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000c', 'PJSIP/6002-0000000d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:35:06', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000c', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 09:35:24', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000000e', 'PJSIP/6002-0000000f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:42:00', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000010', 'PJSIP/6002-00000011', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 09:55:41', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000012', 'PJSIP/6002-00000013', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:01:26', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000014', 'PJSIP/6002-00000015', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:02:34', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000016', 'PJSIP/6002-00000017', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:05:00', 9, 9, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000018', 'PJSIP/6002-00000019', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:10:01', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001a', 'PJSIP/6002-0000001b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:11:14', 24, 24, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001c', 'PJSIP/6002-0000001d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:12:34', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001c', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:12:53', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001e', '', 'StopMixMonitor', '0x7fb974008430', NULL, NULL, NULL, '2026-02-26 10:13:44', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000001f', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:14:12', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000020', 'PJSIP/6002-00000021', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:14:46', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000022', 'PJSIP/6002-00000023', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:16:04', 15, 15, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000024', 'PJSIP/6002-00000025', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:18:09', 19, 19, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000026', 'PJSIP/6002-00000027', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:19:58', 14, 14, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000028', 'PJSIP/6002-00000029', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:21:32', 15, 15, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002a', 'PJSIP/6002-0000002b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:24:17', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002c', 'PJSIP/6003-0000002d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:25:07', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002e', 'PJSIP/6002-0000002f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:25:42', 16, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000002e', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:25:58', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000030', 'PJSIP/6002-00000031', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:26:27', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000032', 'PJSIP/6002-00000033', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:26:53', 10, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000032', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:27:04', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000034', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/Incoming/recording_20260226104503.', NULL, NULL, NULL, '2026-02-26 10:45:03', 5, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000035', 'PJSIP/6002-00000036', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:45:38', 15, 15, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000037', 'PJSIP/6002-00000038', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:46:44', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000039', 'PJSIP/6002-0000003a', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:47:25', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003b', 'PJSIP/6002-0000003c', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:57:06', 13, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003b', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:57:20', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003d', 'PJSIP/6002-0000003e', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 10:58:40', 7, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003d', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 10:58:48', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003f', 'PJSIP/6002-00000040', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:07:51', 13, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000003f', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:08:04', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000041', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:08:19', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000042', 'PJSIP/6002-00000043', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:08:42', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000042', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:08:50', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000044', 'PJSIP/6002-00000045', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:29:20', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000044', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:29:38', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000046', 'PJSIP/6002-00000047', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:33:09', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000048', 'PJSIP/6002-00000049', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:34:32', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004a', 'PJSIP/6002-0000004b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:35:04', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004a', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:35:16', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000004c', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:37:03', 25, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004d', 'PJSIP/6002-0000004e', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:37:40', 13, 13, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000004f', 'PJSIP/6002-00000050', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:42:50', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000051', 'PJSIP/6002-00000052', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 11:51:13', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000051', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 11:51:30', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000053', '', 'StopMixMonitor', '0x7fb8a8011520', NULL, NULL, NULL, '2026-02-26 12:06:47', 14, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000054', 'PJSIP/6002-00000055', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:07:52', 22, 22, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000054', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 12:08:14', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000056', 'PJSIP/6002-00000057', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:08:32', 14, 14, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000058', 'PJSIP/6002-00000059', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:09:11', 15, 15, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000005a', 'PJSIP/6002-0000005b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:11:17', 10, 10, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000005c', 'PJSIP/6002-0000005d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:12:05', 6, 6, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-0000005e', 'PJSIP/6002-0000005f', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:12:42', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13347160164', '6010', 'from-internal', '\"Incoming\" <13347160164>', 'PJSIP/fxogateway-00000060', 'PJSIP/6002-00000061', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:15:01', 6, 6, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000062', 'PJSIP/6002-00000063', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 12:20:01', 7, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000062', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 12:20:08', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000064', 'PJSIP/6002-00000065', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 14:39:06', 8, 8, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000066', 'PJSIP/6003-00000067', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 14:40:55', 18, 18, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000068', 'PJSIP/6002-00000069', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 14:42:21', 45, 45, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-0000006a', 'PJSIP/6002-0000006b', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 14:50:59', 45, 45, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006c', 'PJSIP/6002-0000006d', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 15:06:00', 18, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006c', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:06:18', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006e', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:07:39', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-0000006f', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:08:05', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000070', 'PJSIP/6002-00000071', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 15:08:30', 26, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000070', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:08:56', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000072', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:11:17', 15, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000073', 'PJSIP/6002-00000074', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 15:11:44', 19, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15754922923', '6010', 'from-internal', '\"Incoming\" <15754922923>', 'PJSIP/fxogateway-00000073', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-26 15:12:03', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000075', 'PJSIP/6002-00000076', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-26 15:22:14', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '6002', '6010', 'from-internal', '\"6002\" <6002>', 'PJSIP/6002-00000077', '', 'AGI', 'agi://localhost/callin.agi?type=1&path=/data/6002/recording_20260227104621.wav', NULL, NULL, NULL, '2026-02-27 10:46:20', 6, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000000', 'PJSIP/6002-00000001', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-27 11:15:52', 21, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000000', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-27 11:16:13', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000002', 'PJSIP/6002-00000003', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-27 11:25:26', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15540960623', '6010', 'from-internal', '\"Incoming\" <15540960623>', 'PJSIP/fxogateway-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-02-27 11:25:44', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '013299593554', '6010', 'from-internal', '\"Incoming\" <013299593554>', 'PJSIP/fxogateway-00000004', 'PJSIP/6002-00000005', 'Queue', 'supporttest01,cn,,,10', NULL, NULL, NULL, '2026-02-27 12:20:10', 12, 12, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000000', '', 'StopMixMonitor', '0x740368007090', NULL, NULL, NULL, '2026-03-15 00:29:50', 13, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000001', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 00:32:46', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000002', '', 'StopMixMonitor', '0x7402ec00a150', NULL, NULL, NULL, '2026-03-15 00:35:11', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000003', '', 'StopMixMonitor', '0x7403680148f0', NULL, NULL, NULL, '2026-03-15 02:04:30', 17, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000000', '', 'StopMixMonitor', '0x7364a8006ae0', NULL, NULL, NULL, '2026-03-15 15:05:34', 51, 50, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000001', '', 'StopMixMonitor', '0x736444036fa0', NULL, NULL, NULL, '2026-03-15 15:11:35', 78, 77, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000002', '', 'StopMixMonitor', '0x736440006a10', NULL, NULL, NULL, '2026-03-15 15:42:22', 67, 66, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000003', '', 'StopMixMonitor', '0x7364a8016180', NULL, NULL, NULL, '2026-03-15 15:43:59', 45, 45, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000004', '', 'MixMonitor', '/data/88888888/voice/2033117465925521408.wav,r(/data/88888888/voice/user_203311', NULL, NULL, NULL, '2026-03-15 17:45:48', 1374, 1373, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '99999', 'outbound-dialer', '\"12345678\" <12345678>', 'PJSIP/12345678-00000005', '', 'MixMonitor', '/data/12345678/voice/2033118355273158656.wav,r(/data/12345678/voice/user_203311', NULL, NULL, NULL, '2026-03-15 17:49:18', 1164, 1164, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000006', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 18:09:12', 5, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000008', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 18:10:29', 1, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '99999', 'outbound-dialer', '\"12345678\" <12345678>', 'PJSIP/12345678-00000007', '', 'MixMonitor', '/data/12345678/voice/2033123598614577152.wav,r(/data/12345678/voice/user_203312', NULL, NULL, NULL, '2026-03-15 18:10:08', 1000, 999, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000009', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 18:27:10', 3, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000000a', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 18:34:51', 2, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000000d', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-15 18:38:39', 4, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000000f', '', 'StopMixMonitor', '0x73644402a960', NULL, NULL, NULL, '2026-03-15 18:40:57', 10, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000000e', '', 'StopMixMonitor', '0x73643c0014b0', NULL, NULL, NULL, '2026-03-15 18:40:31', 388, 384, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000010', '', 'StopMixMonitor', '0x736450024090', NULL, NULL, NULL, '2026-03-15 18:48:27', 14, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000011', '', 'StopMixMonitor', '0x73645001a960', NULL, NULL, NULL, '2026-03-15 18:52:06', 12, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000012', '', 'StopMixMonitor', '0x73645001a930', NULL, NULL, NULL, '2026-03-15 18:52:51', 10, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000013', '', 'StopMixMonitor', '0x7364bc02e680', NULL, NULL, NULL, '2026-03-15 18:53:32', 84, 82, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000014', '', 'StopMixMonitor', '0x73645002aac0', NULL, NULL, NULL, '2026-03-15 18:59:28', 39, 37, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000015', '', 'StopMixMonitor', '0x7364400332f0', NULL, NULL, NULL, '2026-03-15 19:18:12', 13, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000016', '', 'StopMixMonitor', '0x73644000b480', NULL, NULL, NULL, '2026-03-15 19:19:46', 6, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000017', '', 'StopMixMonitor', '0x73644002d780', NULL, NULL, NULL, '2026-03-15 19:20:33', 17, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-00000019', '', 'StopMixMonitor', '0x7364400332c0', NULL, NULL, NULL, '2026-03-15 19:22:02', 14, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000001a', '', 'StopMixMonitor', '0x7364400012c0', NULL, NULL, NULL, '2026-03-15 19:23:09', 12, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', 'robot', 'outbound-dialer', '\"\" <12345678>', 'PJSIP/88888888-0000001b', '', 'StopMixMonitor', '0x73643c05e8f0', NULL, NULL, NULL, '2026-03-15 19:31:03', 34, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001c', '', 'StopMixMonitor', '0x73644402a8c0', NULL, NULL, NULL, '2026-03-15 19:51:55', 9, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001d', '', 'StopMixMonitor', '0x73644402bf90', NULL, NULL, NULL, '2026-03-15 20:01:11', 17, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001e', '', 'StopMixMonitor', '0x73643c00fe30', NULL, NULL, NULL, '2026-03-15 20:03:41', 27, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001f', '', 'StopMixMonitor', '0x73645001f7e0', NULL, NULL, NULL, '2026-03-16 09:06:43', 14, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000020', '', 'StopMixMonitor', '0x73644000ac20', NULL, NULL, NULL, '2026-03-16 09:23:34', 12, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000021', '', 'StopMixMonitor', '0x7364bc0226e0', NULL, NULL, NULL, '2026-03-16 09:25:34', 8, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000022', '', 'AGI', 'agi://localhost/call.agi?type=2', NULL, NULL, NULL, '2026-03-16 09:27:54', 98, 92, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000023', '', 'AGI', 'agi://localhost/call.agi?type=2', NULL, NULL, NULL, '2026-03-16 09:30:14', 285, 282, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000024', '', 'StopMixMonitor', '0x73644002f6a0', NULL, NULL, NULL, '2026-03-16 09:59:34', 183, 180, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000025', '', 'StopMixMonitor', '0x7364ac0020d0', NULL, NULL, NULL, '2026-03-16 11:07:44', 4, 3, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000026', '', 'MixMonitor', '/data/88888888/voice/2033393362364674048.wav,r(/data/88888888/voice/user_203339', NULL, NULL, NULL, '2026-03-16 12:02:02', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000001', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-16 20:39:48', 6, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000004', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 09:18:18', 4, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000006', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 09:52:12', 3, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000008', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 09:58:43', 3, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000009', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 10:02:33', 5, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-0000000a', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 10:34:13', 10, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000000c', '', 'StopMixMonitor', '0x790110006250', NULL, NULL, NULL, '2026-03-17 10:34:23', 18, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-0000000d', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 10:36:29', 19, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13146611668', 'robot', 'outbound-dialer', '\"\" <13146611668>', 'PJSIP/88888888-0000000e', '', 'StopMixMonitor', '0x79010c004890', NULL, NULL, NULL, '2026-03-17 10:36:49', 4, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-0000000f', '', 'StopMixMonitor', '0x790118039c20', NULL, NULL, NULL, '2026-03-17 10:40:59', 13, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000010', '', 'StopMixMonitor', '0x7900a0000dc0', NULL, NULL, NULL, '2026-03-17 10:42:07', 15, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000011', '', 'StopMixMonitor', '0x7900ac008eb0', NULL, NULL, NULL, '2026-03-17 10:44:27', 18, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000012', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 11:00:16', 19, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000013', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 11:11:54', 13, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000014', '', 'MixMonitor', '/data/robot/voice/2033744800513155072.wav,r(/data/robot/voice/user_203374480051', NULL, NULL, NULL, '2026-03-17 11:17:19', 80, 77, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000000', '', 'AGI', 'agi://wf784925.natappfree.cc:4551/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 11:32:30', 36, 32, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000000', '', 'MixMonitor', '/data/robot/voice/2033751728135995392.wav,r(/data/robot/voice/user_203375172813', NULL, NULL, NULL, '2026-03-17 11:45:07', 15393, 15389, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000001', '', 'MixMonitor', '/data/robot/voice/2033816455910428672.wav,r(/data/robot/voice/user_203381645591', NULL, NULL, NULL, '2026-03-17 16:02:20', 830, 827, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 16:16:41', 22, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000003', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 16:18:00', 24, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x7605ec01e990', NULL, NULL, NULL, '2026-03-17 16:49:41', 69, 66, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7605e8009470', NULL, NULL, NULL, '2026-03-17 16:56:21', 20, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x7605e80095e0', NULL, NULL, NULL, '2026-03-17 17:07:32', 45, 41, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000008', '', 'MixMonitor', '/data/robot/voice/2033835994178084864.wav,r(/data/robot/voice/user_203383599417', NULL, NULL, NULL, '2026-03-17 17:20:18', 170, 167, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/88888888-00000009', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-17 17:21:38', 95, 91, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/88888888-0000000b', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 17:25:09', 67, 58, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000000a', '', 'MixMonitor', '/data/robot/voice/2033836849224642560.wav,r(/data/robot/voice/user_203383684922', NULL, NULL, NULL, '2026-03-17 17:23:49', 565, 562, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/88888888-0000000e', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 17:35:04', 50, 46, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000000f', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 17:37:04', 155, 152, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000010', '', 'StopMixMonitor', '0x7605c4011b30', NULL, NULL, NULL, '2026-03-17 17:44:44', 18, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/99999999-00000012', '', 'StopMixMonitor', '0x7605740097f0', NULL, NULL, NULL, '2026-03-17 18:17:05', 26, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000000d', '', 'MixMonitor', '/data/robot/voice/2033839498598699008.wav,r(/data/robot/voice/user_203383949859', NULL, NULL, NULL, '2026-03-17 17:33:54', 15543, 15540, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000013', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-17 23:15:46', 71, 63, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000014', '', 'StopMixMonitor', '0x7605f4007110', NULL, NULL, NULL, '2026-03-17 23:23:26', 30, 27, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000016', '', 'StopMixMonitor', '0x7605f4009e70', NULL, NULL, NULL, '2026-03-17 23:25:56', 34, 32, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000015', '', 'MixMonitor', '/data/robot/voice/2033927694464475136.wav,r(/data/robot/voice/user_203392769446', NULL, NULL, NULL, '2026-03-17 23:24:56', 232, 229, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000017', '', 'StopMixMonitor', '0x760578007090', NULL, NULL, NULL, '2026-03-17 23:29:17', 37, 34, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000018', '', 'StopMixMonitor', '0x76057802b160', NULL, NULL, NULL, '2026-03-17 23:30:47', 40, 37, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000019', '', 'StopMixMonitor', '0x7605e0017040', NULL, NULL, NULL, '2026-03-17 23:31:47', 28, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001d', '', 'StopMixMonitor', '0x760554010dd0', NULL, NULL, NULL, '2026-03-18 00:25:18', 70, 67, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001b', '', 'MixMonitor', '/data/robot/voice/2033932582812536832.wav,r(/data/robot/voice/user_203393258281', NULL, NULL, NULL, '2026-03-17 23:43:57', 36894, 36889, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001c', '', 'MixMonitor', '/data/robot/voice/2033933585213444096.wav,r(/data/robot/voice/user_203393358521', NULL, NULL, NULL, '2026-03-17 23:47:47', 36664, 36661, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001a', '', 'MixMonitor', '/data/robot/voice/2033929572216623104.wav,r(/data/robot/voice/user_203392957221', NULL, NULL, NULL, '2026-03-17 23:32:27', 37584, 37582, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001f', '', 'MixMonitor', '/data/robot/voice/2034096917308403712.wav,r(/data/robot/voice/user_203409691730', NULL, NULL, NULL, '2026-03-18 10:36:27', 297, 293, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000021', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-18 10:42:34', 25, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000022', '', 'MixMonitor', '/data/robot/voice/2034100936055648256.wav,r(/data/robot/voice/user_203410093605', NULL, NULL, NULL, '2026-03-18 10:53:07', 177, 174, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000023', '', 'MixMonitor', '/data/robot/voice/2034101827512979456.wav,r(/data/robot/voice/user_203410182751', NULL, NULL, NULL, '2026-03-18 10:56:35', 463, 461, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000024', '', 'StopMixMonitor', '0x76057803d8e0', NULL, NULL, NULL, '2026-03-18 11:04:53', 22, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000025', '', 'StopMixMonitor', '0x7605e8001430', NULL, NULL, NULL, '2026-03-18 11:06:13', 134, 131, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000026', '', 'MixMonitor', '/data/robot/voice/2034106506263302144.wav,r(/data/robot/voice/user_203410650626', NULL, NULL, NULL, '2026-03-18 11:15:25', 1334, 1331, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000027', '', 'StopMixMonitor', '0x760584026b20', NULL, NULL, NULL, '2026-03-18 11:51:13', 21, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000028', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-18 11:52:23', 21, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000002b', '', 'MixMonitor', '/data/robot/voice/2034165786026872832.wav,r(/data/robot/voice/user_203416578602', NULL, NULL, NULL, '2026-03-18 15:11:14', 16, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000029', '', 'MixMonitor', '/data/robot/voice/2034165017219338240.wav,r(/data/robot/voice/user_203416501721', NULL, NULL, NULL, '2026-03-18 15:08:06', 20, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000002a', '', 'MixMonitor', '/data/robot/voice/2034165593000808448.wav,r(/data/robot/voice/user_203416559300', NULL, NULL, NULL, '2026-03-18 15:10:06', 38, 35, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000000', '', 'MixMonitor', '/data/robot/recording_20260318152244.wav,i(mixMonitorId)', NULL, NULL, NULL, '2026-03-18 15:21:06', 98, 96, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-18 15:28:14', 3, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000003', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-18 15:31:03', 2, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7ca804005d00', NULL, NULL, NULL, '2026-03-18 15:36:35', 34, 31, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000001', '', 'MixMonitor', '/data/15011071226/voice/2034173838679515136.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 15:42:59', 62, 59, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7ca87400aeb0', NULL, NULL, NULL, '2026-03-18 15:44:31', 78, 70, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x7ca804006300', NULL, NULL, NULL, '2026-03-18 15:51:51', 57, 52, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000004', '', 'MixMonitor', '/data/15011071226/voice/2034181062634184704.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 16:11:42', 221, 218, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7ca8040109f0', NULL, NULL, NULL, '2026-03-18 16:15:53', 26, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000006', '', 'StopMixMonitor', '0x7ca804007a10', NULL, NULL, NULL, '2026-03-18 16:59:34', 30, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15701670617', 'robot', 'outbound-dialer', '\"\" <15701670617>', 'PJSIP/88888888-00000007', '', 'StopMixMonitor', '0x7ca874008710', NULL, NULL, NULL, '2026-03-18 17:02:25', 41, 38, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000008', '', 'AGI', 'agi://localhost/call.agi?type=1', NULL, NULL, NULL, '2026-03-18 17:21:20', 49, 49, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/99999999-0000000c', '', 'StopMixMonitor', '0x7ca7f8015780', NULL, NULL, NULL, '2026-03-18 17:31:35', 32, 29, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '11111', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000d', '', 'MixMonitor', '/data/99999999/voice/2034205097346641920.wav,r(/data/99999999/voice/user_203420', NULL, NULL, NULL, '2026-03-18 17:47:11', 501, 501, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000009', '', 'MixMonitor', '/data/88888888/voice/2034199231708966912.wav,r(/data/88888888/voice/user_203419', NULL, NULL, NULL, '2026-03-18 17:24:12', 1881, 1881, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '0855554', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000f', '', 'MixMonitor', '/data/99999999/voice/2034206817824976896.wav,r(/data/99999999/voice/user_203420', NULL, NULL, NULL, '2026-03-18 17:53:53', 100, 99, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '158', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000e', '', 'MixMonitor', '/data/99999999/voice/2034206464052211712.wav,r(/data/99999999/voice/user_203420', NULL, NULL, NULL, '2026-03-18 17:51:47', 226, 225, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '889444', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000012', '', 'StopMixMonitor', '0x652a08d3e1d0', NULL, NULL, NULL, '2026-03-18 18:01:44', 61, 61, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000013', '', 'StopMixMonitor', '0x7ca82000c990', NULL, NULL, NULL, '2026-03-18 18:18:14', 31, 31, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '8888', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000018', '', 'AGI', 'agi://localhost/call.agi?type=1', NULL, NULL, NULL, '2026-03-18 18:23:54', 53, 53, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001a', '', 'MixMonitor', '/data/15011071226/voice/2034226420793942016.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:12:05', 461, 457, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000016', '', 'MixMonitor', '/data/88888888/voice/2034213495702233088.wav,r(/data/88888888/voice/user_203421', NULL, NULL, NULL, '2026-03-18 18:19:57', 3590, 3589, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000015', '', 'MixMonitor', '/data/88888888/voice/2034213209759752192.wav,r(/data/88888888/voice/user_203421', NULL, NULL, NULL, '2026-03-18 18:19:47', 3599, 3598, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '8888', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000017', '', 'MixMonitor', '/data/88888888/voice/2034214192795557888.wav,r(/data/88888888/voice/user_203421', NULL, NULL, NULL, '2026-03-18 18:23:19', 3387, 3387, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '779794', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000011', '', 'MixMonitor', '/data/99999999/voice/2034207942275964928.wav,r(/data/99999999/voice/user_203420', NULL, NULL, NULL, '2026-03-18 17:58:22', 4884, 4884, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '55558', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000010', '', 'MixMonitor', '/data/99999999/voice/2034207453492748288.wav,r(/data/99999999/voice/user_203420', NULL, NULL, NULL, '2026-03-18 17:56:00', 5027, 5026, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '99999999', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000014', '', 'MixMonitor', '/data/88888888/voice/2034213067681898496.wav,r(/data/88888888/voice/user_203421', NULL, NULL, NULL, '2026-03-18 18:18:54', 3652, 3652, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001c', '', 'MixMonitor', '/data/15011071226/voice/2034228549503836160.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:20:46', 423, 421, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001d', '', 'MixMonitor', '/data/15011071226/voice/2034230518419218432.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:28:30', 266, 262, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000021', '', 'MixMonitor', '/data/15011071226/voice/2034232342605873152.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:35:46', 392, 389, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000001f', '', 'MixMonitor', '/data/15011071226/voice/2034231899997749248.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:34:06', 492, 490, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000024', '', 'StopMixMonitor', '0x7ca7ac013c90', NULL, NULL, NULL, '2026-03-18 19:44:19', 14, 12, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000025', '', 'StopMixMonitor', '0x7ca7c8028680', NULL, NULL, NULL, '2026-03-18 19:47:49', 22, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/99999999-00000026', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-18 19:48:09', 20, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000027', '', 'StopMixMonitor', '0x7ca7ec036be0', NULL, NULL, NULL, '2026-03-18 19:51:09', 23, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/99999999-00000028', '', 'StopMixMonitor', '0x7ca804032ad0', NULL, NULL, NULL, '2026-03-18 20:22:00', 28, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/99999999-00000029', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-18 20:44:10', 25, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/99999999-0000002a', '', 'StopMixMonitor', '0x7ca83c009290', NULL, NULL, NULL, '2026-03-18 20:48:10', 18, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000002b', '', 'StopMixMonitor', '0x7ca804031f00', NULL, NULL, NULL, '2026-03-18 20:49:40', 23, 20, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011112222', 'robot', 'outbound-dialer', '\"\" <15011112222>', 'PJSIP/99999999-0000002c', '', 'StopMixMonitor', '0x7ca86000e6b0', NULL, NULL, NULL, '2026-03-18 20:57:00', 50, 44, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011113333', 'robot', 'outbound-dialer', '\"\" <15011113333>', 'PJSIP/99999999-0000002d', '', 'StopMixMonitor', '0x7ca81402fd80', NULL, NULL, NULL, '2026-03-18 20:59:00', 30, 27, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011114444', 'robot', 'outbound-dialer', '\"\" <15011114444>', 'PJSIP/99999999-0000002e', '', 'StopMixMonitor', '0x7ca880017060', NULL, NULL, NULL, '2026-03-18 21:00:50', 38, 34, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011115555', 'robot', 'outbound-dialer', '\"\" <15011115555>', 'PJSIP/99999999-0000002f', '', 'StopMixMonitor', '0x7ca87800dfe0', NULL, NULL, NULL, '2026-03-18 21:02:30', 39, 36, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/99999999-00000030', '', 'StopMixMonitor', '0x652a0894c3e0', NULL, NULL, NULL, '2026-03-18 21:07:11', 33, 29, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000022', '', 'MixMonitor', '/data/15011071226/voice/2034234111947870208.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-18 19:42:49', 6149, 6144, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/99999999-00000031', '', 'MixMonitor', '/data/15164719046/voice/2034257173426638848.wav,r(/data/15164719046/voice/user_', NULL, NULL, NULL, '2026-03-18 21:14:21', 657, 650, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719077', 'robot', 'outbound-dialer', '\"\" <15164719077>', 'PJSIP/99999999-00000035', '', 'MixMonitor', '/data/15164719077/voice/2034258334196076544.wav,r(/data/15164719077/voice/user_', NULL, NULL, NULL, '2026-03-18 21:19:01', 377, 372, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719077', 'robot', 'outbound-dialer', '\"\" <15164719077>', 'PJSIP/99999999-00000034', '', 'MixMonitor', '/data/15164719077/voice/2034258225827844096.wav,r(/data/15164719077/voice/user_', NULL, NULL, NULL, '2026-03-18 21:18:41', 397, 393, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13911111111', 'robot', 'outbound-dialer', '\"\" <13911111111>', 'PJSIP/99999999-00000036', '', 'StopMixMonitor', '0x7ca874013c00', NULL, NULL, NULL, '2026-03-18 21:25:48', 57, 53, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011110000', 'robot', 'outbound-dialer', '\"\" <15011110000>', 'PJSIP/99999999-00000038', '', 'StopMixMonitor', '0x7ca87404a1c0', NULL, NULL, NULL, '2026-03-18 21:26:46', 34, 27, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15164719046', 'robot', 'outbound-dialer', '\"\" <15164719046>', 'PJSIP/99999999-00000039', '', 'StopMixMonitor', '0x7ca8040305a0', NULL, NULL, NULL, '2026-03-18 21:31:58', 45, 41, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13911110000', 'robot', 'outbound-dialer', '\"\" <13911110000>', 'PJSIP/99999999-0000003a', '', 'StopMixMonitor', '0x7ca81401f920', NULL, NULL, NULL, '2026-03-18 21:35:28', 35, 31, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13911111234', 'robot', 'outbound-dialer', '\"\" <13911111234>', 'PJSIP/99999999-0000003b', '', 'StopMixMonitor', '0x7ca874045860', NULL, NULL, NULL, '2026-03-18 21:36:48', 27, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000042', '', 'StopMixMonitor', '0x7ca7e40079c0', NULL, NULL, NULL, '2026-03-18 21:45:18', 58, 50, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000043', '', 'StopMixMonitor', '0x7ca7e401af30', NULL, NULL, NULL, '2026-03-18 21:47:08', 32, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000044', '', 'StopMixMonitor', '0x7ca884004930', NULL, NULL, NULL, '2026-03-18 21:56:39', 39, 36, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '13112341234', 'robot', 'outbound-dialer', '\"\" <13112341234>', 'PJSIP/99999999-00000045', '', 'StopMixMonitor', '0x7ca878011eb0', NULL, NULL, NULL, '2026-03-18 22:31:54', 38, 35, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011992211', 'robot', 'outbound-dialer', '\"\" <15011992211>', 'PJSIP/99999999-00000046', '', 'StopMixMonitor', '0x7ca87400e930', NULL, NULL, NULL, '2026-03-18 22:33:14', 25, 21, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000004b', '', 'StopMixMonitor', '0x7ca87404b8e0', NULL, NULL, NULL, '2026-03-19 11:57:17', 40, 36, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-0000004e', '', 'StopMixMonitor', '0x7ca798001b50', NULL, NULL, NULL, '2026-03-19 12:00:37', 108, 105, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15848476874', 'robot', 'outbound-dialer', '\"\" <15848476874>', 'PJSIP/99999999-0000004a', '', 'MixMonitor', '/data/15848476874/voice/2034464330638049280.wav,r(/data/15848476874/voice/user_', NULL, NULL, NULL, '2026-03-19 10:57:27', 3908, 3901, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000047', '', 'MixMonitor', '/data/15011071226/voice/2034464269095026688.wav,r(/data/15011071226/voice/user_', NULL, NULL, NULL, '2026-03-19 10:57:06', 3929, 3925, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000004f', '', 'MixMonitor', '/data/99999999/voice/2034538839902699520.wav,r(/data/99999999/voice/user_203453', NULL, NULL, NULL, '2026-03-19 15:53:47', 88065, 88064, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000051', 'PJSIP/88888888-00000052', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-21 21:27:26', 15, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000053', 'PJSIP/88888888-00000054', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-21 21:30:41', 11, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000055', 'PJSIP/88888888-00000056', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-21 21:49:20', 5, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000057', 'PJSIP/88888888-00000058', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-21 21:49:35', 27, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000059', '', 'MixMonitor', '/data/1001/voice/2035353483571560448.wav,r(/data/1001/voice/user_20353534835715', NULL, NULL, NULL, '2026-03-21 21:50:52', 413, 413, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '58888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000050', '', 'MixMonitor', '/data/99999999/voice/2034912497020108800.wav,r(/data/99999999/voice/user_203491', NULL, NULL, NULL, '2026-03-20 16:38:30', 105555, 105554, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000005a', '', 'MixMonitor', '/data/1001/voice/2035355475723313152.wav,r(/data/1001/voice/user_20353554757233', NULL, NULL, NULL, '2026-03-21 21:58:40', 464, 463, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000005b', '', 'StopMixMonitor', '0x7ca87c00c850', NULL, NULL, NULL, '2026-03-21 22:07:59', 4, 3, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000000', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-22 21:24:29', 91, 87, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000001', '', 'StopMixMonitor', '0x773c28004980', NULL, NULL, NULL, '2026-03-22 21:37:39', 48, 42, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000002', '', 'StopMixMonitor', '0x773bbc020c00', NULL, NULL, NULL, '2026-03-22 21:38:59', 35, 31, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000003', '', 'StopMixMonitor', '0x773bb80072a0', NULL, NULL, NULL, '2026-03-22 21:40:39', 41, 39, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000004', '', 'StopMixMonitor', '0x773c1c008e80', NULL, NULL, NULL, '2026-03-22 21:47:30', 57, 55, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x773bb0008000', NULL, NULL, NULL, '2026-03-22 21:49:50', 33, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x773bb00063f0', NULL, NULL, NULL, '2026-03-22 21:50:40', 11, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000007', '', 'StopMixMonitor', '0x773c1800a3d0', NULL, NULL, NULL, '2026-03-22 21:57:50', 16, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000008', '', 'StopMixMonitor', '0x773c1800cd60', NULL, NULL, NULL, '2026-03-22 21:58:20', 56, 52, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '18612206161', 'robot', 'outbound-dialer', '\"\" <18612206161>', 'PJSIP/88888888-00000009', '', 'StopMixMonitor', '0x773bb4001e40', NULL, NULL, NULL, '2026-03-22 22:09:49', 32, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000010', '', 'StopMixMonitor', '0x773bb4010190', NULL, NULL, NULL, '2026-03-22 22:16:59', 16, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000012', '', 'StopMixMonitor', '0x773bbc024fe0', NULL, NULL, NULL, '2026-03-23 12:13:12', 61, 57, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000013', '', 'StopMixMonitor', '0x773c1c016350', NULL, NULL, NULL, '2026-03-23 12:17:32', 31, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000014', '', 'StopMixMonitor', '0x773bb400e4c0', NULL, NULL, NULL, '2026-03-23 12:20:22', 42, 37, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000015', '', 'StopMixMonitor', '0x773bb4005ec0', NULL, NULL, NULL, '2026-03-23 12:22:02', 18, 15, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000016', '', 'StopMixMonitor', '0x773bb4054a70', NULL, NULL, NULL, '2026-03-23 12:22:52', 43, 34, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000017', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-23 12:24:02', 35, 33, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000018', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-23 12:24:52', 31, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-00000019', '', 'StopMixMonitor', '0x773bb400d9b0', NULL, NULL, NULL, '2026-03-23 12:25:32', 16, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001a', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-23 12:26:22', 70, 65, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001b', '', 'StopMixMonitor', '0x773bb4058000', NULL, NULL, NULL, '2026-03-23 12:31:32', 19, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001c', '', 'StopMixMonitor', '0x773bb800b5c0', NULL, NULL, NULL, '2026-03-23 12:32:02', 19, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/88888888-0000001d', '', 'StopMixMonitor', '0x773c180157f0', NULL, NULL, NULL, '2026-03-23 12:32:42', 32, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000001e', 'PJSIP/88888888-0000001f', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 00:16:22', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000020', 'PJSIP/88888888-00000021', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 00:16:28', 0, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011081227', 'robot', 'outbound-dialer', '\"\" <15011081227>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x775b64012cc0', NULL, NULL, NULL, '2026-03-26 16:29:42', 37, 34, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011081227', 'robot', 'outbound-dialer', '\"\" <15011081227>', 'PJSIP/99999999-00000002', '', 'AGI', 'agi://127.0.0.1/call.agi?type=2', NULL, NULL, NULL, '2026-03-26 16:31:32', 22, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011081227', 'robot', 'outbound-dialer', '\"\" <15011081227>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x775b58006120', NULL, NULL, NULL, '2026-03-26 16:33:02', 41, 35, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x775b6c0081c0', NULL, NULL, NULL, '2026-03-26 16:43:52', 34, 30, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', 'robot', 'outbound-dialer', '\"\" <15011071226>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x775bd8013980', NULL, NULL, NULL, '2026-03-26 16:44:52', 59, 56, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000006', '', 'StopMixMonitor', '0x775b6c0011b0', NULL, NULL, NULL, '2026-03-26 23:30:13', 1, 1, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000007', 'PJSIP/88888888-00000008', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 23:30:16', 9, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000009', 'PJSIP/88888888-0000000a', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 23:30:34', 7, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000000b', '', 'StopMixMonitor', '0x775b64002f20', NULL, NULL, NULL, '2026-03-26 23:30:42', 3, 3, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000000c', 'PJSIP/88888888-0000000d', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 23:30:48', 4, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8888', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000000e', 'PJSIP/88888888-0000000f', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-26 23:31:33', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000010', '', 'StopMixMonitor', '0x775b60001ed0', NULL, NULL, NULL, '2026-03-26 23:31:39', 3, 3, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000011', '', 'StopMixMonitor', '0x775b6c004e50', NULL, NULL, NULL, '2026-03-26 23:41:48', 2, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000012', '', 'StopMixMonitor', '0x775b6c00aff0', NULL, NULL, NULL, '2026-03-26 23:42:16', 3, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000013', '', 'StopMixMonitor', '0x775b6c006180', NULL, NULL, NULL, '2026-03-27 00:38:09', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000014', '', 'StopMixMonitor', '0x775bd00303d0', NULL, NULL, NULL, '2026-03-27 00:38:18', 2, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000015', '', 'StopMixMonitor', '0x775b68012980', NULL, NULL, NULL, '2026-03-27 00:39:10', 7, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000016', '', 'StopMixMonitor', '0x775bd0010740', NULL, NULL, NULL, '2026-03-27 00:39:27', 3, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000017', '', 'StopMixMonitor', '0x775bd000f490', NULL, NULL, NULL, '2026-03-27 23:46:39', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000018', '', 'StopMixMonitor', '0x775b6002c3e0', NULL, NULL, NULL, '2026-03-27 23:48:11', 3, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '99999', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000019', '', 'StopMixMonitor', '0x775bd000da50', NULL, NULL, NULL, '2026-03-27 23:56:13', 1, 1, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000000', 'PJSIP/1001-00000001', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 00:39:23', 6, 0, '4', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000000', 'PJSIP/1001-00000001', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 00:55:08', 9, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000000', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 00:55:17', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000002', 'PJSIP/88888888-00000003', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 00:55:43', 10, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000002', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 00:55:54', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000004', 'PJSIP/1001-00000005', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 00:56:49', 4, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000006', 'PJSIP/1001-00000007', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:02:24', 12, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000006', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:02:36', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000008', 'PJSIP/88888888-00000009', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:02:44', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '100', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000000a', '', 'StopMixMonitor', '0x79bc24001710', NULL, NULL, NULL, '2026-03-28 01:03:44', 3, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000000b', 'PJSIP/1001-0000000c', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:03:54', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000000d', 'PJSIP/1001-0000000e', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:10:36', 14, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000000d', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:10:50', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000000f', 'PJSIP/88888888-00000010', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:11:04', 8, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000011', 'PJSIP/88888888-00000012', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:11:16', 10, 3, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000011', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:11:26', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000013', 'PJSIP/1001-00000014', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:11:34', 8, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000013', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:11:43', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000015', 'PJSIP/88888888-00000016', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:11:45', 5, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000017', 'PJSIP/88888888-00000018', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:12:15', 2, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000019', 'PJSIP/1001-0000001a', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:17:03', 11, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000019', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:17:15', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000001b', 'PJSIP/88888888-0000001c', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:17:18', 9, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000001d', 'PJSIP/88888888-0000001e', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:17:35', 7, 2, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000001f', 'PJSIP/1001-00000020', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:17:57', 10, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000001f', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:18:07', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000021', 'PJSIP/88888888-00000022', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:18:11', 2, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000023', 'PJSIP/1001-00000024', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:18:53', 10, 0, '4', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000025', 'PJSIP/88888888-00000026', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:19:08', 0, 0, '4', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000027', 'PJSIP/88888888-00000028', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:19:19', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-00000029', 'PJSIP/1001-0000002a', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:23:37', 2, 0, '4', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000002b', 'PJSIP/1001-0000002c', 'Dial', 'PJSIP/1001,30,g', NULL, NULL, NULL, '2026-03-28 01:24:11', 10, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '88888888', '1001', 'inbound-dialer', '\"\" <88888888>', 'PJSIP/88888888-0000002b', '', 'Hangup', '', NULL, NULL, NULL, '2026-03-28 01:24:22', 0, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000002d', 'PJSIP/88888888-0000002e', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:24:26', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000002f', 'PJSIP/88888888-00000030', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:24:37', 16, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '8855', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000031', 'PJSIP/88888888-00000032', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:48:56', 6, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '56655', '56655', 'outbound-dialer', '\"test\" <56655>', 'PJSIP/1001-00000033', 'PJSIP/88888888-00000034', 'Dial', 'PJSIP/56655@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:50:26', 12, 4, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', '15011071226', 'outbound-dialer', '\"test\" <15011071226>', 'PJSIP/1001-00000035', 'PJSIP/88888888-00000036', 'Dial', 'PJSIP/15011071226@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:52:00', 9, 4, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011071226', '15011071226', 'outbound-dialer', '\"test\" <15011071226>', 'PJSIP/1001-00000039', 'PJSIP/88888888-0000003a', 'Dial', 'PJSIP/15011071226@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:53:00', 6, 3, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '15011071226', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000003c', 'PJSIP/88888888-0000003d', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:53:32', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999', '99999', 'outbound-dialer', '\"test\" <99999>', 'PJSIP/1001-0000003e', '', 'StopMixMonitor', '0x79bc24003fd0', NULL, NULL, NULL, '2026-03-28 01:54:23', 6, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '123456', '123456', 'outbound-dialer', '\"test\" <123456>', 'PJSIP/1001-0000003f', 'PJSIP/99999999-00000040', 'Dial', 'PJSIP/123456@99999999,30,g', NULL, NULL, NULL, '2026-03-28 01:55:17', 15, 9, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000041', 'PJSIP/88888888-00000042', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:55:54', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1258', '1258', 'outbound-dialer', '\"test\" <1258>', 'PJSIP/1001-00000043', 'PJSIP/99999999-00000044', 'Dial', 'PJSIP/1258@99999999,30,g', NULL, NULL, NULL, '2026-03-28 01:56:42', 4, 2, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000045', 'PJSIP/88888888-00000046', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:56:53', 7, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '15011071226', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000047', 'PJSIP/88888888-00000048', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:57:03', 14, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '15011071226', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000004a', 'PJSIP/88888888-0000004b', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:57:29', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '15011071226', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000004c', 'PJSIP/88888888-0000004d', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:57:32', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000004e', 'PJSIP/88888888-0000004f', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 01:59:37', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '123456', '123456', 'outbound-dialer', '\"test\" <123456>', 'PJSIP/1001-00000050', 'PJSIP/99999999-00000051', 'Dial', 'PJSIP/123456@99999999,30,g', NULL, NULL, NULL, '2026-03-28 01:59:47', 18, 15, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-00000052', 'PJSIP/88888888-00000053', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:00:19', 8, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '123456', '123456', 'outbound-dialer', '\"test\" <123456>', 'PJSIP/1001-00000054', 'PJSIP/99999999-00000055', 'Dial', 'PJSIP/123456@99999999,30,g', NULL, NULL, NULL, '2026-03-28 02:00:45', 7, 5, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '123456', '123456', 'outbound-dialer', '\"test\" <123456>', 'PJSIP/1001-00000056', 'PJSIP/99999999-00000057', 'Dial', 'PJSIP/123456@99999999,30,g', NULL, NULL, NULL, '2026-03-28 02:01:00', 5, 3, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '123456', '123456', 'outbound-dialer', '\"test\" <123456>', 'PJSIP/1001-00000058', 'PJSIP/99999999-00000059', 'Dial', 'PJSIP/123456@99999999,30,g', NULL, NULL, NULL, '2026-03-28 02:03:36', 8, 2, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '123456', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000005a', 'PJSIP/88888888-0000005b', 'Dial', 'PJSIP/1001@88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:04:57', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1001', '12345678', 'outbound-dialer', '\"1001\" <1001>', 'PJSIP/1001-0000005c', 'PJSIP/88888888-0000005d', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:05:33', 1, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-0000005e', 'PJSIP/88888888-0000005f', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:05:55', 13, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-00000060', 'PJSIP/88888888-00000061', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:30:58', 15, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-00000065', 'PJSIP/88888888-00000066', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:32:59', 12, 9, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-00000067', 'PJSIP/88888888-00000068', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:34:18', 3, 1, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-00000069', 'PJSIP/88888888-0000006a', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:34:52', 5, 2, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '12345678', '12345678', 'outbound-dialer', '\"test\" <12345678>', 'PJSIP/1001-0000006b', 'PJSIP/88888888-0000006c', 'Dial', 'PJSIP/88888888,30,g', NULL, NULL, NULL, '2026-03-28 02:35:30', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '15082200708', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000007b', 'PJSIP/88888888-0000007c', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-01 03:43:44', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '019218332191', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000007d', 'PJSIP/88888888-0000007e', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-01 03:43:55', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0015082200708', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000000', 'PJSIP/88888888-00000001', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-10 23:10:46', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '008615082200708', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000002', 'PJSIP/88888888-00000003', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-10 23:11:22', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '17713958362', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000004', 'PJSIP/88888888-00000005', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-10 23:11:58', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '017713958362', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000006', 'PJSIP/88888888-00000007', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-10 23:12:37', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '07740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000008', 'PJSIP/88888888-00000009', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-11 06:09:59', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000000a', 'PJSIP/88888888-0000000b', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-11 06:10:00', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '07740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000000c', 'PJSIP/88888888-0000000d', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-11 06:11:26', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000000e', 'PJSIP/88888888-0000000f', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-11 06:11:26', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000010', 'PJSIP/88888888-00000011', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-11 06:11:27', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '1085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000012', 'PJSIP/88888888-00000013', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:16:19', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '01085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000014', 'PJSIP/88888888-00000015', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:17:04', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000016', 'PJSIP/88888888-00000017', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:17:47', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000018', 'PJSIP/88888888-00000019', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:18:33', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '00001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000001a', 'PJSIP/88888888-0000001b', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:19:17', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '000001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000001c', 'PJSIP/88888888-0000001d', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:20:02', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0000001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000001e', 'PJSIP/88888888-0000001f', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:20:47', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '00000001085316555', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000020', 'PJSIP/88888888-00000021', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-16 07:21:32', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '17740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000022', 'PJSIP/88888888-00000023', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-19 11:40:01', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000024', 'PJSIP/88888888-00000025', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-19 11:40:39', 13, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000026', 'PJSIP/88888888-00000027', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-19 11:40:58', 3, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '17740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000028', 'PJSIP/88888888-00000029', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-04-19 20:31:06', 13, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '17740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000002a', 'PJSIP/88888888-0000002b', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-03 04:39:43', 14, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000002c', 'PJSIP/88888888-0000002d', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-03 04:40:00', 17, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '17740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-0000002e', 'PJSIP/88888888-0000002f', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-04 02:20:38', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000030', 'PJSIP/88888888-00000031', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-04 02:20:45', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '9017740928873', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000032', 'PJSIP/88888888-00000033', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-04 02:20:50', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '1002', '0983384533', 'outbound-dialer', '\"\" <1002>', 'PJSIP/1002-00000034', 'PJSIP/88888888-00000035', 'Dial', 'PJSIP/1002@88888888,30,g', NULL, NULL, NULL, '2026-05-04 02:20:50', 30, 0, '0', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x70fa18006490', NULL, NULL, NULL, '2026-06-11 10:37:02', 40, 39, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x70fa20016030', NULL, NULL, NULL, '2026-06-11 10:41:06', 18, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x70fa18005a90', NULL, NULL, NULL, '2026-06-11 10:44:13', 17, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x70fa90013af0', NULL, NULL, NULL, '2026-06-11 11:14:31', 39, 39, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x70fa10000e60', NULL, NULL, NULL, '2026-06-11 11:15:36', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x70fa180037a0', NULL, NULL, NULL, '2026-06-11 11:15:59', 8, 8, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x70fa200056c0', NULL, NULL, NULL, '2026-06-11 11:17:20', 5, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7355d0013e00', NULL, NULL, NULL, '2026-06-12 11:35:03', 38, 37, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-12 11:40:15', 17, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7355d0004970', NULL, NULL, NULL, '2026-06-12 14:45:38', 25, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x7355d0014b60', NULL, NULL, NULL, '2026-06-12 14:59:45', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '15011221212', 'robot', 'outbound-dialer', '\"\" <15011221212>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x735560029b60', NULL, NULL, NULL, '2026-06-16 15:10:09', 17, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000021', '', 'Hangup', '', NULL, NULL, NULL, '2026-06-22 23:30:30', 7, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000022', '', 'Hangup', '', NULL, NULL, NULL, '2026-06-22 23:37:33', 7, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000023', '', 'StopMixMonitor', '0x73556c035900', NULL, NULL, NULL, '2026-06-22 23:56:08', 63, 62, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000024', '', 'StopMixMonitor', '0x735574002980', NULL, NULL, NULL, '2026-06-23 00:03:21', 78, 78, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000025', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 00:26:50', 31, 30, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000026', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 00:44:49', 26, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000027', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 09:01:51', 33, 32, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000028', '', 'ControlPlayback', '', NULL, NULL, NULL, '2026-06-23 09:05:13', 81, 80, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000029', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 09:06:54', 84, 84, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002a', '', 'StopMixMonitor', '0x73556c004430', NULL, NULL, NULL, '2026-06-23 09:10:35', 86, 86, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002b', '', 'StopMixMonitor', '0x7355e8018f40', NULL, NULL, NULL, '2026-06-23 09:26:08', 52, 51, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002c', '', 'StopMixMonitor', '0x73557000e5b0', NULL, NULL, NULL, '2026-06-23 10:00:34', 98, 98, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002d', '', 'StopMixMonitor', '0x73556c010a30', NULL, NULL, NULL, '2026-06-23 10:08:59', 9, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002e', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 10:10:01', 164, 164, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000002f', '', 'StopMixMonitor', '0x7355d0002800', NULL, NULL, NULL, '2026-06-23 11:03:35', 47, 46, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000030', '', 'StopMixMonitor', '0x7355e8022490', NULL, NULL, NULL, '2026-06-23 11:06:34', 83, 82, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000031', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 11:08:41', 40, 40, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000032', '', 'StopMixMonitor', '0x73557000d450', NULL, NULL, NULL, '2026-06-23 11:09:31', 22, 22, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000033', '', 'StopMixMonitor', '0x7355e8016020', NULL, NULL, NULL, '2026-06-23 11:10:36', 23, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000034', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 11:13:32', 46, 46, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000035', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 11:19:05', 79, 79, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000036', '', 'StopMixMonitor', '0x7355700020d0', NULL, NULL, NULL, '2026-06-23 11:25:00', 96, 95, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000037', '', 'StopMixMonitor', '0x7355e8025530', NULL, NULL, NULL, '2026-06-23 15:46:00', 19, 18, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000038', '', 'StopMixMonitor', '0x73557c0344e0', NULL, NULL, NULL, '2026-06-23 15:47:49', 24, 24, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000039', '', 'ControlPlayback', '/data/99999999/voice/AliTtsClient_d2dc331a4348af3af1584f126a5572df', NULL, NULL, NULL, '2026-06-23 15:48:23', 30, 30, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000003a', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 16:12:09', 47, 47, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000003b', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 16:13:06', 30, 30, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000003c', '', 'StopMixMonitor', '0x735560001a50', NULL, NULL, NULL, '2026-06-23 16:13:46', 72, 71, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000003d', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-23 17:11:09', 79, 78, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000003e', '', 'StopMixMonitor', '0x735574002570', NULL, NULL, NULL, '2026-06-23 17:12:56', 80, 80, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7353fc0094c0', NULL, NULL, NULL, '2026-06-25 10:10:14', 17, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x735398006da0', NULL, NULL, NULL, '2026-06-25 10:11:38', 51, 50, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7353640068a0', NULL, NULL, NULL, '2026-06-25 10:22:29', 14, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x735368001a30', NULL, NULL, NULL, '2026-06-25 10:28:28', 29, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x73537c003f00', NULL, NULL, NULL, '2026-06-25 10:32:14', 14, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7353800050e0', NULL, NULL, NULL, '2026-06-25 10:34:25', 7, 6, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x73539c01a350', NULL, NULL, NULL, '2026-06-25 10:34:39', 47, 47, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000007', '', 'StopMixMonitor', '0x7353a8014320', NULL, NULL, NULL, '2026-06-25 11:10:25', 63, 62, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000008', '', 'StopMixMonitor', '0x7353c000c2e0', NULL, NULL, NULL, '2026-06-25 15:55:19', 10, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000009', '', 'StopMixMonitor', '0x7353a4004b10', NULL, NULL, NULL, '2026-06-25 15:56:01', 66, 66, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000a', '', 'StopMixMonitor', '0x7353a40089a0', NULL, NULL, NULL, '2026-06-25 15:57:26', 46, 46, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000b', '', 'StopMixMonitor', '0x7353e0004650', NULL, NULL, NULL, '2026-06-25 16:00:03', 52, 51, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000c', '', 'StopMixMonitor', '0x73540c0338e0', NULL, NULL, NULL, '2026-06-25 16:01:10', 90, 89, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000d', '', 'ControlPlayback', '/data/99999999/voice/DashScopeTtsClient_f6e682455b7c1fddb1e2b84d8d91fdaa', NULL, NULL, NULL, '2026-06-25 16:32:18', 242, 242, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000e', '', 'ControlPlayback', '/data/99999999/voice/DashScopeTtsClient_9b5707db4a5521a24e4a4ac413345451', NULL, NULL, NULL, '2026-06-25 17:01:09', 158, 158, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000f', '', 'StopMixMonitor', '0x7353a4016370', NULL, NULL, NULL, '2026-06-25 17:11:32', 333, 333, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000010', '', 'StopMixMonitor', '0x735408002650', NULL, NULL, NULL, '2026-06-25 23:12:15', 25, 25, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000011', '', 'StopMixMonitor', '0x7353a4020820', NULL, NULL, NULL, '2026-06-25 23:13:14', 63, 63, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000012', '', 'StopMixMonitor', '0x73539c0183f0', NULL, NULL, NULL, '2026-06-25 23:14:27', 90, 90, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000016', '', 'StopMixMonitor', '0x73539c003050', NULL, NULL, NULL, '2026-06-25 23:52:30', 158, 157, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000017', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-26 00:09:42', 171, 170, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000018', '', 'StopMixMonitor', '0x7353e0006760', NULL, NULL, NULL, '2026-06-26 00:23:19', 186, 186, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000019', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-26 00:26:35', 336, 336, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000001a', '', 'StopMixMonitor', '0x73540c034c70', NULL, NULL, NULL, '2026-06-26 00:37:14', 26, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000001b', '', 'AGI', 'agi://127.0.0.1:4571/call.agi?type=1', NULL, NULL, NULL, '2026-06-26 00:37:49', 182, 182, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000001c', '', 'StopMixMonitor', '0x735330009af0', NULL, NULL, NULL, '2026-06-28 19:02:20', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000001d', '', 'StopMixMonitor', '0x7353a0016070', NULL, NULL, NULL, '2026-06-28 19:04:03', 167, 167, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000001e', '', 'StopMixMonitor', '0x735348003710', NULL, NULL, NULL, '2026-06-28 19:09:26', 361, 360, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '555555', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7ac088008120', NULL, NULL, NULL, '2026-07-03 18:43:58', 15, 14, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '5666', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x7ac088000d20', NULL, NULL, NULL, '2026-07-03 18:44:31', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '99998', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7ac0100363d0', NULL, NULL, NULL, '2026-07-03 18:45:50', 93, 92, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888794', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x7f55a80018f0', NULL, NULL, NULL, '2026-07-06 10:09:12', 5, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '7997', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7f55ec004b20', NULL, NULL, NULL, '2026-07-06 10:09:58', 52, 51, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '444558', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x7f55b4008510', NULL, NULL, NULL, '2026-07-06 10:13:12', 42, 42, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '98854', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000007', '', 'StopMixMonitor', '0x7f562c00edf0', NULL, NULL, NULL, '2026-07-06 10:14:33', 61, 61, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'AGI', 'agi://localhost:4573/call.agi?type=1', NULL, NULL, NULL, '2026-07-07 01:18:42', 24, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '88888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x7fe4c80132f0', NULL, NULL, NULL, '2026-07-07 01:47:37', 14, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7fe4fc0023a0', NULL, NULL, NULL, '2026-07-07 02:38:44', 56, 55, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8558', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x7fe4a80024b0', NULL, NULL, NULL, '2026-07-07 02:50:55', 75, 74, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '996868', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x7fe52c002230', NULL, NULL, NULL, '2026-07-07 02:57:03', 74, 73, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '55555', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7fe46c003720', NULL, NULL, NULL, '2026-07-07 03:17:01', 24, 23, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000006', '', 'StopMixMonitor', '0x7fe528003220', NULL, NULL, NULL, '2026-07-07 03:19:47', 10, 9, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '55558', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000007', '', 'StopMixMonitor', '0x7fe550006e60', NULL, NULL, NULL, '2026-07-07 03:20:09', 32, 31, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8554', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000008', '', 'StopMixMonitor', '0x7fe4b0001de0', NULL, NULL, NULL, '2026-07-07 03:21:38', 62, 61, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '85588', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7fe810013ed0', NULL, NULL, NULL, '2026-07-07 06:56:52', 29, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '885', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x7fe8640030c0', NULL, NULL, NULL, '2026-07-07 06:58:07', 8, 7, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7fe860009380', NULL, NULL, NULL, '2026-07-07 07:07:02', 5, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000000', '', 'StopMixMonitor', '0x7f23580014f0', NULL, NULL, NULL, '2026-07-07 07:24:30', 36, 35, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000001', '', 'StopMixMonitor', '0x7f23ac005b00', NULL, NULL, NULL, '2026-07-07 07:25:30', 5, 4, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '85558', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000002', '', 'StopMixMonitor', '0x7f2380010670', NULL, NULL, NULL, '2026-07-07 07:52:00', 27, 26, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '85452', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000003', '', 'StopMixMonitor', '0x55c3546d0080', NULL, NULL, NULL, '2026-07-07 07:53:39', 87, 86, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8548', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000004', '', 'StopMixMonitor', '0x7f23ac00a0d0', NULL, NULL, NULL, '2026-07-07 07:56:52', 69, 69, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8855', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000005', '', 'StopMixMonitor', '0x7f23b4015250', NULL, NULL, NULL, '2026-07-07 08:47:20', 20, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000006', '', 'Hangup', '', NULL, NULL, NULL, '2026-07-07 09:06:32', 11, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000007', '', 'Hangup', '', NULL, NULL, NULL, '2026-07-07 09:34:58', 1, 0, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8588', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000008', '', 'StopMixMonitor', '0x7f23ec003900', NULL, NULL, NULL, '2026-07-07 09:38:45', 23, 22, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '79948', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000009', '', 'StopMixMonitor', '0x7f2300002bc0', NULL, NULL, NULL, '2026-07-07 09:57:20', 18, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '444', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000a', '', 'StopMixMonitor', '0x7f238c00aa40', NULL, NULL, NULL, '2026-07-07 09:59:10', 20, 19, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '5587', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000b', '', 'StopMixMonitor', '0x7f23b8001ab0', NULL, NULL, NULL, '2026-07-07 10:04:05', 14, 13, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8585', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000c', '', 'ControlPlayback', '', NULL, NULL, NULL, '2026-07-07 10:18:43', 18, 17, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8547', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000d', '', 'StopMixMonitor', '0x7f2368004a70', NULL, NULL, NULL, '2026-07-07 10:20:47', 29, 28, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000e', '', 'StopMixMonitor', '0x7f22e8004d30', NULL, NULL, NULL, '2026-07-07 13:35:20', 17, 16, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8585', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-0000000f', '', 'StopMixMonitor', '0x7f2374002f60', NULL, NULL, NULL, '2026-07-07 13:37:15', 283, 283, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '555', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000010', '', 'StopMixMonitor', '0x7f23740089f0', NULL, NULL, NULL, '2026-07-07 13:50:02', 85, 84, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000011', '', 'StopMixMonitor', '0x7f23980043d0', NULL, NULL, NULL, '2026-07-07 14:03:52', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '8888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000012', '', 'StopMixMonitor', '0x7f23c40015e0', NULL, NULL, NULL, '2026-07-07 14:04:36', 56, 56, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '88888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000013', '', 'StopMixMonitor', '0x7f23280068d0', NULL, NULL, NULL, '2026-07-07 14:06:03', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000014', '', 'StopMixMonitor', '0x7f2358005820', NULL, NULL, NULL, '2026-07-07 14:06:36', 11, 10, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '9999', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000015', '', 'StopMixMonitor', '0x7f2380008ab0', NULL, NULL, NULL, '2026-07-07 14:07:02', 37, 36, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '55665', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000016', '', 'StopMixMonitor', '0x7f23d80134a0', NULL, NULL, NULL, '2026-07-07 14:08:04', 12, 11, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '888', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000017', '', 'StopMixMonitor', '0x7f22f8002140', NULL, NULL, NULL, '2026-07-07 14:08:35', 6, 5, '8', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `cdr` VALUES ('', '99999999', '777', 'inbound-dialer', '\"\" <99999999>', 'PJSIP/99999999-00000018', '', 'StopMixMonitor', '0x7f2320002530', NULL, NULL, NULL, '2026-07-07 14:08:55', 65, 65, '8', '3', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for extensions
-- ----------------------------
DROP TABLE IF EXISTS `extensions`;
CREATE TABLE `extensions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `exten` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `priority` int(0) NOT NULL,
  `app` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appdata` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `context`(`context`, `exten`, `priority`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for iaxfriends
-- ----------------------------
DROP TABLE IF EXISTS `iaxfriends`;
CREATE TABLE `iaxfriends`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` enum('friend','user','peer') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailbox` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `secret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dbsecret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regcontext` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `host` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ipaddr` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` int(0) NULL DEFAULT NULL,
  `defaultip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sourceaddress` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mask` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regexten` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regseconds` int(0) NULL DEFAULT NULL,
  `accountcode` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mohinterpret` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mohsuggest` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `inkeys` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outkeys` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callerid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cid_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sendani` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fullname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trunk` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `auth` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maxauthreq` int(0) NULL DEFAULT NULL,
  `requirecalltoken` enum('yes','no','auto') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `encryption` enum('yes','no','aes128') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transfer` enum('yes','no','mediaonly') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `jitterbuffer` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `forcejitterbuffer` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disallow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codecpriority` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualifysmoothing` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualifyfreqok` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualifyfreqnotok` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timezone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `adsi` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amaflags` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setvar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  INDEX `iaxfriends_name`(`name`) USING BTREE,
  INDEX `iaxfriends_name_host`(`name`, `host`) USING BTREE,
  INDEX `iaxfriends_name_ipaddr_port`(`name`, `ipaddr`, `port`) USING BTREE,
  INDEX `iaxfriends_ipaddr_port`(`ipaddr`, `port`) USING BTREE,
  INDEX `iaxfriends_host_port`(`host`, `port`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for meetme
-- ----------------------------
DROP TABLE IF EXISTS `meetme`;
CREATE TABLE `meetme`  (
  `bookid` int(0) NOT NULL AUTO_INCREMENT,
  `confno` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `starttime` datetime(0) NULL DEFAULT NULL,
  `endtime` datetime(0) NULL DEFAULT NULL,
  `pin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `adminpin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `opts` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `adminopts` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `recordingfilename` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `recordingformat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maxusers` int(0) NULL DEFAULT NULL,
  `members` int(0) NOT NULL,
  PRIMARY KEY (`bookid`) USING BTREE,
  INDEX `meetme_confno_start_end`(`confno`, `starttime`, `endtime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for musiconhold
-- ----------------------------
DROP TABLE IF EXISTS `musiconhold`;
CREATE TABLE `musiconhold`  (
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mode` enum('custom','files','mp3nb','quietmp3nb','quietmp3','playlist') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `directory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `application` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `digit` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `format` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stamp` datetime(0) NULL DEFAULT NULL,
  `loop_last` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for musiconhold_entry
-- ----------------------------
DROP TABLE IF EXISTS `musiconhold_entry`;
CREATE TABLE `musiconhold_entry`  (
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `position` int(0) NOT NULL,
  `entry` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`name`, `position`) USING BTREE,
  CONSTRAINT `fk_musiconhold_entry_name_musiconhold` FOREIGN KEY (`name`) REFERENCES `musiconhold` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_aors
-- ----------------------------
DROP TABLE IF EXISTS `ps_aors`;
CREATE TABLE `ps_aors`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `default_expiration` int(0) NULL DEFAULT NULL,
  `mailboxes` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_contacts` int(0) NULL DEFAULT NULL,
  `minimum_expiration` int(0) NULL DEFAULT NULL,
  `remove_existing` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify_frequency` int(0) NULL DEFAULT NULL,
  `authenticate_qualify` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maximum_expiration` int(0) NULL DEFAULT NULL,
  `outbound_proxy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `support_path` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify_timeout` float NULL DEFAULT NULL,
  `voicemail_extension` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remove_unavailable` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify_2xx_only` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_aors_id`(`id`) USING BTREE,
  INDEX `ps_aors_qualifyfreq_contact`(`qualify_frequency`, `contact`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_aors
-- ----------------------------
INSERT INTO `ps_aors` VALUES ('1001', NULL, NULL, NULL, 1, 300, 'yes', 20, NULL, 1800, NULL, NULL, 2, NULL, '0', NULL, NULL, 1, '2026-05-19 11:45:05', 1, '2026-05-19 11:45:06', 0);
INSERT INTO `ps_aors` VALUES ('1002', NULL, NULL, NULL, 1, 300, '0', 20, NULL, 1800, NULL, NULL, 2, NULL, '0', NULL, NULL, 1, '2026-05-19 11:45:05', 1, '2026-05-19 11:45:06', 0);
INSERT INTO `ps_aors` VALUES ('88888888', 'sip:121.57.170.8:5060', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:05', 1, '2026-05-19 11:45:06', 0);
INSERT INTO `ps_aors` VALUES ('99999999', 'sip:121.57.170.8:5060', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:05', 1, '2026-05-19 11:45:06', 0);

-- ----------------------------
-- Table structure for ps_asterisk_publications
-- ----------------------------
DROP TABLE IF EXISTS `ps_asterisk_publications`;
CREATE TABLE `ps_asterisk_publications`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `devicestate_publish` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailboxstate_publish` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_state` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_state_filter` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailbox_state` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailbox_state_filter` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_asterisk_publications_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_auths
-- ----------------------------
DROP TABLE IF EXISTS `ps_auths`;
CREATE TABLE `ps_auths`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `auth_type` enum('md5','userpass','google_oauth') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nonce_lifetime` int(0) NULL DEFAULT NULL,
  `md5_cred` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `realm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oauth_clientid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oauth_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password_digest` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supported_algorithms_uas` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supported_algorithms_uac` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_auths_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_auths
-- ----------------------------
INSERT INTO `ps_auths` VALUES ('1001', 'userpass', NULL, NULL, 'test123', NULL, 'test', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:32', 1, '2026-05-19 11:45:32', 0);
INSERT INTO `ps_auths` VALUES ('1002', 'userpass', NULL, NULL, 'abc123', NULL, 'qwead', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:32', 1, '2026-05-19 11:45:32', 0);
INSERT INTO `ps_auths` VALUES ('2033110455926525952', 'userpass', NULL, NULL, '123test', NULL, 'test', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:32', 1, '2026-05-19 11:45:32', 0);
INSERT INTO `ps_auths` VALUES ('2033721037100830720', 'userpass', NULL, NULL, 'test123', NULL, 'test', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-05-19 11:45:32', 1, '2026-05-19 11:45:32', 0);

-- ----------------------------
-- Table structure for ps_contacts
-- ----------------------------
DROP TABLE IF EXISTS `ps_contacts`;
CREATE TABLE `ps_contacts`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expiration_time` bigint(0) NULL DEFAULT NULL,
  `qualify_frequency` int(0) NULL DEFAULT NULL,
  `outbound_proxy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify_timeout` float NULL DEFAULT NULL,
  `reg_server` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `authenticate_qualify` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `via_addr` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `via_port` int(0) NULL DEFAULT NULL,
  `call_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `prune_on_boot` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify_2xx_only` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  UNIQUE INDEX `ps_contacts_uq`(`id`, `reg_server`) USING BTREE,
  INDEX `ps_contacts_id`(`id`) USING BTREE,
  INDEX `ps_contacts_qualifyfreq_exp`(`qualify_frequency`, `expiration_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_contacts
-- ----------------------------
INSERT INTO `ps_contacts` VALUES ('99999999^3B@19ed32c9bc21a896a79fda6a045c61d8', 'sip:99999999@121.57.170.98:9368', 1783441232, 0, '', '', 'PortSIP UC Client iOS - v12.4.1', 3, '', 'no', '192.168.2.24', 5960, 'hSRFfP1MWxeTn4ZMhOCqXw..', '99999999', 'no', 'false', NULL, 1, '2026-07-07 21:49:59', 1, '2026-07-08 00:19:02', 0);

-- ----------------------------
-- Table structure for ps_domain_aliases
-- ----------------------------
DROP TABLE IF EXISTS `ps_domain_aliases`;
CREATE TABLE `ps_domain_aliases`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_domain_aliases_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_endpoint_id_ips
-- ----------------------------
DROP TABLE IF EXISTS `ps_endpoint_id_ips`;
CREATE TABLE `ps_endpoint_id_ips`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `match` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `srv_lookups` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `match_header` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `match_request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_endpoint_id_ips_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_endpoint_id_ips
-- ----------------------------
INSERT INTO `ps_endpoint_id_ips` VALUES ('88888888', '88888888', '121.57.170.8', 'no', NULL, NULL, NULL, 1, '2026-05-19 11:46:07', 1, '2026-05-19 11:46:08', 0);
INSERT INTO `ps_endpoint_id_ips` VALUES ('99999999', '99999999', '121.57.170.8', 'no', NULL, NULL, NULL, 1, '2026-05-19 11:46:07', 1, '2026-05-19 11:46:08', 0);

-- ----------------------------
-- Table structure for ps_endpoints
-- ----------------------------
DROP TABLE IF EXISTS `ps_endpoints`;
CREATE TABLE `ps_endpoints`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `business_type` enum('attend','line') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '端点类型',
  `transport` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `aors` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disallow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direct_media` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `connected_line_method` enum('invite','reinvite','update') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direct_media_method` enum('invite','reinvite','update') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direct_media_glare_mitigation` enum('none','outgoing','incoming') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disable_direct_media_on_nat` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtmf_mode` enum('rfc4733','inband','info','auto','auto_info') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `external_media_address` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `force_rport` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ice_support` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `identify_by` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailboxes` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `moh_suggest` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outbound_auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outbound_proxy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rewrite_contact` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtp_ipv6` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtp_symmetric` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_diversion` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_pai` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_rpid` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timers_min_se` int(0) NULL DEFAULT NULL,
  `timers` enum('forced','no','required','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timers_sess_expires` int(0) NULL DEFAULT NULL,
  `callerid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callerid_privacy` enum('allowed_not_screened','allowed_passed_screened','allowed_failed_screened','allowed','prohib_not_screened','prohib_passed_screened','prohib_failed_screened','prohib','unavailable') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callerid_tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `100rel` enum('no','required','peer_supported','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `aggregate_mwi` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trust_id_inbound` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trust_id_outbound` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `use_ptime` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `use_avpf` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `media_encryption` enum('no','sdes','dtls') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `inband_progress` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `call_group` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pickup_group` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `named_call_group` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `named_pickup_group` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_state_busy_at` int(0) NULL DEFAULT NULL,
  `fax_detect` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38_udptl` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38_udptl_ec` enum('none','fec','redundancy') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38_udptl_maxdatagram` int(0) NULL DEFAULT NULL,
  `t38_udptl_nat` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38_udptl_ipv6` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tone_zone` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `language` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `one_touch_recording` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `record_on_feature` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `record_off_feature` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtp_engine` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_transfer` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_subscribe` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sdp_owner` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sdp_session` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tos_audio` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tos_video` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sub_min_expiry` int(0) NULL DEFAULT NULL,
  `from_domain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `from_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mwi_from_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_verify` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_rekey` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_cert_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_private_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_cipher` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_ca_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_ca_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_setup` enum('active','passive','actpass') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `srtp_tag_32` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `media_address` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `redirect_method` enum('user','uri_core','uri_pjsip') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `set_var` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `cos_audio` int(0) NULL DEFAULT NULL,
  `cos_video` int(0) NULL DEFAULT NULL,
  `message_context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `force_avp` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `media_use_received_transport` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `accountcode` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_eq_phone` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `moh_passthrough` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `media_encryption_optimistic` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rpid_immediate` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `g726_non_standard` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtp_keepalive` int(0) NULL DEFAULT NULL,
  `rtp_timeout` int(0) NULL DEFAULT NULL,
  `rtp_timeout_hold` int(0) NULL DEFAULT NULL,
  `bind_rtp_to_media_address` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `voicemail_extension` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mwi_subscribe_replaces_unsolicited` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deny` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `permit` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `acl` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_deny` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_permit` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_acl` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `subscribe_context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fax_detect_timeout` int(0) NULL DEFAULT NULL,
  `contact_user` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `preferred_codec_only` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `asymmetric_rtp_codec` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtcp_mux` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_overlap` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refer_blind_progress` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `notify_early_inuse_ringing` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_audio_streams` int(0) NULL DEFAULT NULL,
  `max_video_streams` int(0) NULL DEFAULT NULL,
  `webrtc` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_fingerprint` enum('SHA-1','SHA-256') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `incoming_mwi_mailbox` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bundle` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtls_auto_generate_cert` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `follow_early_media_fork` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `accept_multiple_sdp_answers` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `suppress_q850_reason_headers` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trust_connected_line` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_connected_line` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ignore_183_without_sdp` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codec_prefs_incoming_offer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codec_prefs_outgoing_offer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codec_prefs_incoming_answer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codec_prefs_outgoing_answer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stir_shaken` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_history_info` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_unauthenticated_options` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38_bind_udptl_to_media_address` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `geoloc_incoming_call_profile` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `geoloc_outgoing_call_profile` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `incoming_call_offer_pref` enum('local','local_first','remote','remote_first') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outgoing_call_offer_pref` enum('local','local_merge','local_first','remote','remote_merge','remote_first') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stir_shaken_profile` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `security_negotiation` enum('no','mediasec') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `security_mechanisms` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_aoc` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `overlap_context` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenantid` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000',
  `suppress_moh_on_sendonly` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `add_zero` tinyint(1) NULL DEFAULT 0 COMMENT '外地号是否需要加0',
  `prefix` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '呼叫前缀',
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_endpoints_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_endpoints
-- ----------------------------
INSERT INTO `ps_endpoints` VALUES ('1001', 'attend', 'transport-udp', '1001', '1001', 'outbound-dialer', 'all', 'ulaw,alaw,g729,gsm', 'no', NULL, NULL, NULL, NULL, 'rfc4733', NULL, 'yes', 'no', NULL, NULL, NULL, NULL, NULL, 'yes', NULL, 'yes', NULL, NULL, NULL, NULL, NULL, NULL, 'test', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'no', 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '000000', NULL, '2026-03-14 23:47:48', '2026-06-09 11:37:52', 0, NULL, NULL, 1, 1, 0);
INSERT INTO `ps_endpoints` VALUES ('1002', 'attend', 'transport-udp', '1002', '1002', 'outbound-dialer', 'all', 'ulaw,alaw,g729,gsm', 'no', NULL, NULL, NULL, NULL, 'rfc4733', NULL, 'yes', 'no', NULL, NULL, NULL, NULL, NULL, 'yes', NULL, 'yes', NULL, NULL, NULL, NULL, 'no', NULL, '1002', NULL, NULL, NULL, NULL, 'no', 'no', 'no', 'no', 'no', 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '000000', 'no', '2026-03-21 21:20:49', '2026-06-09 11:37:52', 0, NULL, NULL, 1, 1, 0);
INSERT INTO `ps_endpoints` VALUES ('88888888', 'line', 'transport-udp', '88888888', '2033110455926525952', 'inbound-dialer', 'all', 'ulaw,alaw,g729,gsm', 'no', NULL, NULL, NULL, NULL, 'rfc4733', NULL, 'yes', 'no', NULL, NULL, NULL, NULL, NULL, 'yes', NULL, 'yes', NULL, NULL, NULL, NULL, 'no', NULL, '88888888', NULL, NULL, NULL, NULL, 'yes', 'yes', 'no', 'no', 'no', 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '000000', 'yes', '2026-03-15 17:18:01', '2026-06-09 11:37:52', 0, '', NULL, 1, 1, 0);
INSERT INTO `ps_endpoints` VALUES ('99999999', 'line', 'transport-udp', '99999999', '2033721037100830720', 'inbound-dialer', 'all', 'ulaw,alaw,g729,gsm', 'no', NULL, NULL, NULL, NULL, 'rfc4733', NULL, 'yes', 'no', NULL, NULL, NULL, NULL, NULL, 'yes', NULL, 'yes', NULL, NULL, NULL, NULL, 'no', NULL, '99999999', NULL, NULL, NULL, NULL, 'yes', 'yes', 'no', 'no', 'no', 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'routeId=2019597485876838401', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '000000', 'yes', '2026-03-17 09:44:15', '2026-06-09 11:37:52', 0, '', NULL, 1, 1, 0);

-- ----------------------------
-- Table structure for ps_globals
-- ----------------------------
DROP TABLE IF EXISTS `ps_globals`;
CREATE TABLE `ps_globals`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `max_forwards` int(0) NULL DEFAULT NULL,
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `default_outbound_endpoint` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `debug` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `endpoint_identifier_order` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_initial_qualify_time` int(0) NULL DEFAULT NULL,
  `default_from_user` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keep_alive_interval` int(0) NULL DEFAULT NULL,
  `regcontext` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_expiration_check_interval` int(0) NULL DEFAULT NULL,
  `default_voicemail_extension` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disable_multi_domain` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unidentified_request_count` int(0) NULL DEFAULT NULL,
  `unidentified_request_period` int(0) NULL DEFAULT NULL,
  `unidentified_request_prune_interval` int(0) NULL DEFAULT NULL,
  `default_realm` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mwi_tps_queue_high` int(0) NULL DEFAULT NULL,
  `mwi_tps_queue_low` int(0) NULL DEFAULT NULL,
  `mwi_disable_initial_unsolicited` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ignore_uri_user_options` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `use_callerid_contact` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_contact_status_on_update_registration` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `taskprocessor_overload_trigger` enum('none','global','pjsip_only') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `norefersub` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_sending_180_after_183` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `all_codecs_on_empty_reinvite` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `default_auth_algorithms_uas` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `default_auth_algorithms_uac` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_globals_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ps_globals
-- ----------------------------
INSERT INTO `ps_globals` VALUES ('global', NULL, NULL, NULL, NULL, 'auth_username,ip,username', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ps_inbound_publications
-- ----------------------------
DROP TABLE IF EXISTS `ps_inbound_publications`;
CREATE TABLE `ps_inbound_publications`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event_asterisk-devicestate` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event_asterisk-mwi` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_inbound_publications_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_outbound_publishes
-- ----------------------------
DROP TABLE IF EXISTS `ps_outbound_publishes`;
CREATE TABLE `ps_outbound_publishes`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `expiration` int(0) NULL DEFAULT NULL,
  `outbound_auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outbound_proxy` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `server_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `from_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_auth_attempts` int(0) NULL DEFAULT NULL,
  `transport` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `multi_user` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `@body` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `@context` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `@exten` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_outbound_publishes_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_registrations
-- ----------------------------
DROP TABLE IF EXISTS `ps_registrations`;
CREATE TABLE `ps_registrations`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `auth_rejection_permanent` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expiration` int(0) NULL DEFAULT NULL,
  `max_retries` int(0) NULL DEFAULT NULL,
  `outbound_auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outbound_proxy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `retry_interval` int(0) NULL DEFAULT NULL,
  `forbidden_retry_interval` int(0) NULL DEFAULT NULL,
  `server_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transport` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `support_path` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fatal_retry_interval` int(0) NULL DEFAULT NULL,
  `line` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `support_outbound` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_header_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `max_random_initial_delay` int(0) NULL DEFAULT NULL,
  `security_negotiation` enum('no','mediasec') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `security_mechanisms` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_registrations_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_resource_list
-- ----------------------------
DROP TABLE IF EXISTS `ps_resource_list`;
CREATE TABLE `ps_resource_list`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `list_item` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `full_state` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `notification_batch_interval` int(0) NULL DEFAULT NULL,
  `resource_display_name` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_resource_list_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_subscription_persistence
-- ----------------------------
DROP TABLE IF EXISTS `ps_subscription_persistence`;
CREATE TABLE `ps_subscription_persistence`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `packet` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `src_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `src_port` int(0) NULL DEFAULT NULL,
  `transport_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `local_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `local_port` int(0) NULL DEFAULT NULL,
  `cseq` int(0) NULL DEFAULT NULL,
  `tag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `endpoint` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expires` int(0) NULL DEFAULT NULL,
  `contact_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `prune_on_boot` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `generator_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_subscription_persistence_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_systems
-- ----------------------------
DROP TABLE IF EXISTS `ps_systems`;
CREATE TABLE `ps_systems`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `timer_t1` int(0) NULL DEFAULT NULL,
  `timer_b` int(0) NULL DEFAULT NULL,
  `compact_headers` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `threadpool_initial_size` int(0) NULL DEFAULT NULL,
  `threadpool_auto_increment` int(0) NULL DEFAULT NULL,
  `threadpool_idle_timeout` int(0) NULL DEFAULT NULL,
  `threadpool_max_size` int(0) NULL DEFAULT NULL,
  `disable_tcp_switch` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `follow_early_media_fork` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `accept_multiple_sdp_answers` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disable_rport` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_systems_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ps_transports
-- ----------------------------
DROP TABLE IF EXISTS `ps_transports`;
CREATE TABLE `ps_transports`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `async_operations` int(0) NULL DEFAULT NULL,
  `bind` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ca_list_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cert_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cipher` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `domain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `external_media_address` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `external_signaling_address` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `external_signaling_port` int(0) NULL DEFAULT NULL,
  `method` enum('default','unspecified','tlsv1','tlsv1_1','tlsv1_2','tlsv1_3','sslv2','sslv23','sslv3') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `local_net` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `priv_key_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `protocol` enum('udp','tcp','tls','ws','wss','flow') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `require_client_cert` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `verify_client` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `verify_server` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tos` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cos` int(0) NULL DEFAULT NULL,
  `allow_reload` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `symmetric_transport` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow_wildcard_certs` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tcp_keepalive_enable` tinyint(1) NULL DEFAULT NULL,
  `tcp_keepalive_idle_time` int(0) NULL DEFAULT NULL,
  `tcp_keepalive_interval_time` int(0) NULL DEFAULT NULL,
  `tcp_keepalive_probe_count` int(0) NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `ps_transports_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for queue_log
-- ----------------------------
DROP TABLE IF EXISTS `queue_log`;
CREATE TABLE `queue_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `time` datetime(0) NULL DEFAULT NULL,
  `callid` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queuename` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `agent` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data4` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for queue_members
-- ----------------------------
DROP TABLE IF EXISTS `queue_members`;
CREATE TABLE `queue_members`  (
  `queue_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `interface` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `membername` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state_interface` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `penalty` int(0) NULL DEFAULT NULL,
  `paused` int(0) NULL DEFAULT NULL,
  `uniqueid` int(0) NOT NULL AUTO_INCREMENT,
  `wrapuptime` int(0) NULL DEFAULT NULL,
  `ringinuse` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `reason_paused` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`queue_name`, `interface`) USING BTREE,
  UNIQUE INDEX `uniqueid`(`uniqueid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of queue_members
-- ----------------------------
INSERT INTO `queue_members` VALUES ('supporttest01', 'PJSIP/1001', '1001', NULL, NULL, 1, 23, NULL, NULL, NULL, NULL, 1, '2026-05-31 03:28:34', 1, '2026-06-14 23:25:51', 0);
INSERT INTO `queue_members` VALUES ('supporttest01', 'PJSIP/1002', '1002', NULL, NULL, 0, 26, NULL, NULL, NULL, NULL, 1, '2026-07-03 19:40:33', 1, '2026-07-03 19:40:50', 0);
INSERT INTO `queue_members` VALUES ('supporttest02', 'PJSIP/1002', '1002', NULL, NULL, 1, 25, NULL, NULL, NULL, NULL, 1, '2026-05-31 03:36:34', 1, '2026-05-31 03:36:48', 0);

-- ----------------------------
-- Table structure for queue_rules
-- ----------------------------
DROP TABLE IF EXISTS `queue_rules`;
CREATE TABLE `queue_rules`  (
  `rule_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `min_penalty` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `max_penalty` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for queues
-- ----------------------------
DROP TABLE IF EXISTS `queues`;
CREATE TABLE `queues`  (
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `show_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `musiconhold` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `announce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timeout` int(0) NULL DEFAULT NULL,
  `ringinuse` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setinterfacevar` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setqueuevar` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setqueueentryvar` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `monitor_format` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `membermacro` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `membergosub` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_youarenext` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_thereare` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_callswaiting` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_quantity1` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_quantity2` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_holdtime` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_minutes` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_minute` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_seconds` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_thankyou` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_callerannounce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `queue_reporthold` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `announce_frequency` int(0) NULL DEFAULT NULL,
  `announce_to_first_user` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `min_announce_frequency` int(0) NULL DEFAULT NULL,
  `announce_round_seconds` int(0) NULL DEFAULT NULL,
  `announce_holdtime` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `announce_position` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `announce_position_limit` int(0) NULL DEFAULT NULL,
  `periodic_announce` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `periodic_announce_frequency` int(0) NULL DEFAULT NULL,
  `relative_periodic_announce` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `random_periodic_announce` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `retry` int(0) NULL DEFAULT NULL,
  `wrapuptime` int(0) NULL DEFAULT NULL,
  `penaltymemberslimit` int(0) NULL DEFAULT NULL,
  `autofill` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `monitor_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autopause` enum('yes','no','all') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autopausedelay` int(0) NULL DEFAULT NULL,
  `autopausebusy` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autopauseunavail` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maxlen` int(0) NULL DEFAULT NULL,
  `servicelevel` int(0) NULL DEFAULT NULL,
  `strategy` enum('ringall','leastrecent','fewestcalls','random','rrmemory','linear','wrandom','rrordered') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `joinempty` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `leavewhenempty` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `reportholdtime` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `memberdelay` int(0) NULL DEFAULT NULL,
  `weight` int(0) NULL DEFAULT NULL,
  `timeoutrestart` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `defaultrule` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timeoutpriority` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_restricted_caller_id` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_org` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_user` bigint(0) NOT NULL DEFAULT 1 COMMENT '创建人',
  `update_user` int(0) NOT NULL DEFAULT 1 COMMENT '更新人',
  `delete_time` bigint(0) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '所属租户',
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of queues
-- ----------------------------
INSERT INTO `queues` VALUES ('supporttest01', '测试1', NULL, NULL, 'default', 10, 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, -1, 0, NULL, 'yes', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'rrmemory', NULL, NULL, NULL, NULL, NULL, 'no', NULL, NULL, NULL, '2026-01-27 09:18:49', '2026-05-26 15:46:36', NULL, 1, 1, 0, '000000');
INSERT INTO `queues` VALUES ('supporttest02', '测试2', NULL, NULL, NULL, 10, 'no', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, -1, NULL, NULL, 'yes', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'rrmemory', NULL, NULL, NULL, NULL, NULL, 'no', NULL, NULL, NULL, '2026-01-27 09:18:49', '2026-05-26 15:46:36', NULL, 1, 1, 0, '000000');

-- ----------------------------
-- Table structure for sippeers
-- ----------------------------
DROP TABLE IF EXISTS `sippeers`;
CREATE TABLE `sippeers`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ipaddr` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` int(0) NULL DEFAULT NULL,
  `regseconds` int(0) NULL DEFAULT NULL,
  `defaultuser` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fullcontact` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regserver` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `useragent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lastms` int(0) NULL DEFAULT NULL,
  `host` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` enum('friend','user','peer') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `context` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `permit` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deny` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `secret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `md5secret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remotesecret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transport` enum('udp','tcp','tls','ws','wss','udp,tcp','tcp,udp') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dtmfmode` enum('rfc2833','info','shortinfo','inband','auto') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `directmedia` enum('yes','no','nonat','update','outgoing') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nat` varchar(29) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callgroup` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pickupgroup` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `language` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disallow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allow` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `insecure` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trustrpid` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `progressinband` enum('yes','no','never') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `promiscredir` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `useclientcode` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `accountcode` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setvar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callerid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amaflags` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callcounter` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `busylevel` int(0) NULL DEFAULT NULL,
  `allowoverlap` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allowsubscribe` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `videosupport` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maxcallbitrate` int(0) NULL DEFAULT NULL,
  `rfc2833compensate` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailbox` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `session-timers` enum('accept','refuse','originate') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `session-expires` int(0) NULL DEFAULT NULL,
  `session-minse` int(0) NULL DEFAULT NULL,
  `session-refresher` enum('uac','uas') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `t38pt_usertpsource` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regexten` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fromdomain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fromuser` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qualify` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `defaultip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtptimeout` int(0) NULL DEFAULT NULL,
  `rtpholdtimeout` int(0) NULL DEFAULT NULL,
  `sendrpid` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outboundproxy` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callbackextension` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timert1` int(0) NULL DEFAULT NULL,
  `timerb` int(0) NULL DEFAULT NULL,
  `qualifyfreq` int(0) NULL DEFAULT NULL,
  `constantssrc` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contactpermit` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contactdeny` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `usereqphone` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `textsupport` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `faxdetect` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `buggymwi` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `auth` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fullname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trunkname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cid_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callingpres` enum('allowed_not_screened','allowed_passed_screen','allowed_failed_screen','allowed','prohib_not_screened','prohib_passed_screen','prohib_failed_screen','prohib') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mohinterpret` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mohsuggest` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parkinglot` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `hasvoicemail` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `subscribemwi` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vmexten` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autoframing` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rtpkeepalive` int(0) NULL DEFAULT NULL,
  `call-limit` int(0) NULL DEFAULT NULL,
  `g726nonstandard` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ignoresdpversion` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allowtransfer` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dynamic` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supportpath` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  INDEX `sippeers_name`(`name`) USING BTREE,
  INDEX `sippeers_name_host`(`name`, `host`) USING BTREE,
  INDEX `sippeers_ipaddr_port`(`ipaddr`, `port`) USING BTREE,
  INDEX `sippeers_host_port`(`host`, `port`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for stir_tn
-- ----------------------------
DROP TABLE IF EXISTS `stir_tn`;
CREATE TABLE `stir_tn`  (
  `id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `private_key_file` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `public_cert_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attest_level` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_mky` enum('0','1','off','on','false','true','no','yes') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for voicemail
-- ----------------------------
DROP TABLE IF EXISTS `voicemail`;
CREATE TABLE `voicemail`  (
  `uniqueid` int(0) NOT NULL AUTO_INCREMENT,
  `context` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mailbox` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fullname` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `alias` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pager` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attach` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attachfmt` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `serveremail` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tz` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deletevoicemail` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `saycid` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sendvoicemail` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `review` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tempgreetwarn` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `envelope` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sayduration` int(0) NULL DEFAULT NULL,
  `forcename` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `forcegreetings` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callback` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dialout` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `exitcontext` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `maxmsg` int(0) NULL DEFAULT NULL,
  `volgain` decimal(5, 2) NULL DEFAULT NULL,
  `imapuser` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `imappassword` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `imapserver` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `imapport` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `imapflags` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stamp` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`uniqueid`) USING BTREE,
  INDEX `voicemail_mailbox`(`mailbox`) USING BTREE,
  INDEX `voicemail_context`(`context`) USING BTREE,
  INDEX `voicemail_mailbox_context`(`mailbox`, `context`) USING BTREE,
  INDEX `voicemail_imapuser`(`imapuser`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for voicemail_messages
-- ----------------------------
DROP TABLE IF EXISTS `voicemail_messages`;
CREATE TABLE `voicemail_messages`  (
  `dir` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `msgnum` int(0) NOT NULL,
  `context` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `callerid` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `origtime` int(0) NULL DEFAULT NULL,
  `duration` int(0) NULL DEFAULT NULL,
  `recording` longblob NULL,
  `flag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `category` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailboxuser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mailboxcontext` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `msg_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`dir`, `msgnum`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
