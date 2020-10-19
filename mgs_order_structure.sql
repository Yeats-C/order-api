/*
 Navicat MySQL Data Transfer

 Source Server         : new -test-order
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : rm-2zedpm9s6b931qhe2yo.mysql.rds.aliyuncs.com:3306
 Source Schema         : mgs_order

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 19/10/2020 15:41:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动id',
  `activity_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `activity_type` tinyint(1) NULL DEFAULT 1 COMMENT '活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单',
  `activity_status` tinyint(1) NULL DEFAULT 0 COMMENT '活动状态是否手动关闭0否1是',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '活动终止时间',
  `publishing_organization` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布组织',
  `activity_brief` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动简介',
  `activity_pic_pc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'PC端展示图片',
  `activity_pic_app` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机端展示图片',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `active_store_range` tinyint(1) NULL DEFAULT NULL COMMENT '活动门店范围 1全部 2部分',
  `activity_scope` tinyint(1) NULL DEFAULT NULL COMMENT '活动范围：1.按单品设置2.按品类设置3.按品牌设置4.按单品排除',
  `multiple_give` tinyint(1) NULL DEFAULT 0 COMMENT '满足赠送条件多次赠送 :0赠一次1赠多次',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'to b 促销活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_gift
-- ----------------------------
DROP TABLE IF EXISTS `activity_gift`;
CREATE TABLE `activity_gift`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则序号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编码(满赠规则使用)',
  `product_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称(满赠规则使用)',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码(满赠规则使用)',
  `numbers` int(11) NULL DEFAULT NULL COMMENT '数量(满赠规则使用)',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 242 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠方式（规则）表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_product
-- ----------------------------
DROP TABLE IF EXISTS `activity_product`;
CREATE TABLE `activity_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku_code',
  `product_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `product_brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `product_brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `product_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `price_tax` decimal(20, 4) NULL DEFAULT NULL COMMENT '原价(分销价格 ) ',
  `discount` decimal(20, 4) NULL DEFAULT NULL COMMENT '折扣',
  `reduce` decimal(20, 4) NULL DEFAULT NULL COMMENT '减价金额，促销价格',
  `actual_price` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际价格',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0为启用，1为禁用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `activity_scope` tinyint(1) NULL DEFAULT NULL COMMENT '活动范围：1.按单品设置2.按品类设置3.按品牌设置4.按单品排除',
  `level` tinyint(1) NULL DEFAULT NULL COMMENT '品类级别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动商品范围明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_rule
-- ----------------------------
DROP TABLE IF EXISTS `activity_rule`;
CREATE TABLE `activity_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `rule_unit` tinyint(1) NULL DEFAULT 1 COMMENT '优惠单位：1.按数量（件）2.按金额（元）0.无条件',
  `meeting_conditions` bigint(20) NULL DEFAULT NULL COMMENT '满足条件（满多少惨加活动）',
  `preferential_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '优惠金额、优惠件数、折扣点数（百分比）',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `activity_type` tinyint(1) NULL DEFAULT 1 COMMENT '活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单',
  `rule_num` bigint(20) NULL DEFAULT NULL COMMENT '规则序号',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  `rule_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则id唯一标识',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku_code',
  `threshold` decimal(20, 0) NULL DEFAULT NULL COMMENT '买赠活动sku门槛',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 501 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠方式（规则）表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_store
-- ----------------------------
DROP TABLE IF EXISTS `activity_store`;
CREATE TABLE `activity_store`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `province_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在省',
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在省份',
  `city_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在市',
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在市',
  `district_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在区',
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店所在区',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4915 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动-门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for app_version_info
-- ----------------------------
DROP TABLE IF EXISTS `app_version_info`;
CREATE TABLE `app_version_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `system_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '系统类型，1 安卓，2 苹果',
  `app_type` tinyint(1) NULL DEFAULT NULL COMMENT '类型，1 POS收银App，2 爱掌柜app',
  `app_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统版本号',
  `app_build` int(255) NOT NULL COMMENT '内部系统版本号',
  `update_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下载地址',
  `update_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人ID',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(1) UNSIGNED NOT NULL COMMENT '状态：0：已删除，1：进行中，2：已结束',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app版本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_info
-- ----------------------------
DROP TABLE IF EXISTS `batch_info`;
CREATE TABLE `batch_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `basic_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联商品id',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号【展示字段】',
  `batch_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次时间',
  `warehouse_type_code` tinyint(1) NULL DEFAULT 1 COMMENT '传入库房编码:1:销售库，2:特卖库',
  `batch_type` tinyint(1) NULL DEFAULT NULL COMMENT '批次类型  0：月份批次  1：非月份批次',
  `batch_info_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号【供应链唯一标识】',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '商品数量',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `sku_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `batch_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '批次价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批次信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_customer_flow_daily
-- ----------------------------
DROP TABLE IF EXISTS `bi_customer_flow_daily`;
CREATE TABLE `bi_customer_flow_daily`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月日',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `stat_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `total_customer_flow` bigint(20) NULL DEFAULT NULL COMMENT '客流人流量',
  `goal_num` bigint(20) NULL DEFAULT NULL COMMENT '目标人流量',
  `different_num` bigint(20) NULL DEFAULT NULL COMMENT '客流差值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店客流量(按日)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_customer_flow_monthly
-- ----------------------------
DROP TABLE IF EXISTS `bi_customer_flow_monthly`;
CREATE TABLE `bi_customer_flow_monthly`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `total_customer_flow` bigint(20) NULL DEFAULT NULL COMMENT '客流人流量',
  `goal_num` bigint(20) NULL DEFAULT NULL COMMENT '目标人流量',
  `different_num` bigint(20) NULL DEFAULT NULL COMMENT '客流差值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店客流量(按月)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_month_18a_brand_sale_month
-- ----------------------------
DROP TABLE IF EXISTS `bi_month_18a_brand_sale_month`;
CREATE TABLE `bi_month_18a_brand_sale_month`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `brand_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编号',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额',
  `sale_cost` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售成本',
  `sale_num` bigint(20) NULL DEFAULT NULL COMMENT '销售数量',
  `sale_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '毛利额',
  `goal_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '18a品牌销售目标额',
  `different_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '18a品牌销售额差值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '18A品牌销售额（年月）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_month_18a_sale_margin
-- ----------------------------
DROP TABLE IF EXISTS `bi_month_18a_sale_margin`;
CREATE TABLE `bi_month_18a_sale_margin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `sale_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '毛利额',
  `last_month_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '上月毛利额',
  `last_year_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '上年毛利额',
  `chain_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '环比增长率',
  `year_on_year_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '同比增长率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_date_index`(`store_id`, `stat_year`, `stat_month`) USING BTREE COMMENT '门店,年,月唯一索引',
  INDEX `year_month_index`(`stat_year_month`) USING BTREE COMMENT '年月索引'
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '18a销售毛利' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_month_18a_total_sale_month
-- ----------------------------
DROP TABLE IF EXISTS `bi_month_18a_total_sale_month`;
CREATE TABLE `bi_month_18a_total_sale_month`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `goal_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售目标额',
  `sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额',
  `sale_cost` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售成本',
  `sale_num` bigint(20) NULL DEFAULT NULL COMMENT '销售数量',
  `sale_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '毛利额',
  `different_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额差值',
  `is_satisfy_code` tinyint(1) NOT NULL COMMENT '是否达标状态（0不达标，1达标）',
  `is_satisfy` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否达标',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_date_index`(`store_id`, `stat_year`, `stat_month`) USING BTREE COMMENT '门店,年,月唯一索引',
  INDEX `year_month_index`(`stat_year_month`) USING BTREE COMMENT '年月索引'
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '18A总销售额（年月）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_month_sale_margin
-- ----------------------------
DROP TABLE IF EXISTS `bi_month_sale_margin`;
CREATE TABLE `bi_month_sale_margin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `sale_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '毛利额',
  `last_month_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '上月毛利额',
  `last_year_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '上年毛利额',
  `chain_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '环比增长率',
  `year_on_year_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '同比增长率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_date_index`(`store_id`, `stat_year`, `stat_month`) USING BTREE COMMENT '门店,年,月唯一索引',
  INDEX `year_month_index`(`stat_year_month`) USING BTREE COMMENT '年月索引'
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售毛利' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_order_overview
-- ----------------------------
DROP TABLE IF EXISTS `bi_order_overview`;
CREATE TABLE `bi_order_overview`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `order_num` bigint(20) NULL DEFAULT NULL COMMENT '订单数',
  `sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额(实销金额)',
  `last_month_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '上月销售额',
  `last_year_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '上年销售额',
  `chain_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '环比增长率',
  `year_on_year_growth` decimal(20, 4) NULL DEFAULT NULL COMMENT '同比增长率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单概览' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_order_payment_month
-- ----------------------------
DROP TABLE IF EXISTS `bi_order_payment_month`;
CREATE TABLE `bi_order_payment_month`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` int(10) NULL DEFAULT NULL COMMENT '年',
  `stat_month` int(10) NULL DEFAULT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店code',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `pay_money` decimal(20, 4) NULL DEFAULT NULL COMMENT '实付金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店_订单的实际支付金额_月(后台)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_product_overview
-- ----------------------------
DROP TABLE IF EXISTS `bi_product_overview`;
CREATE TABLE `bi_product_overview`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month_day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月日',
  `store_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `sku_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编码',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类id',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `sale_margin` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '毛利额',
  `saleout_dgree` decimal(20, 4) NULL DEFAULT NULL COMMENT '畅销度',
  `product_label_code` int(10) NULL DEFAULT NULL COMMENT '商品标签编码(0,畅销 1,普通商品 3,滞销',
  `product_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标签',
  `spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `color_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `model_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1383 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页商品概览' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_store_transfor_rate_daily
-- ----------------------------
DROP TABLE IF EXISTS `bi_store_transfor_rate_daily`;
CREATE TABLE `bi_store_transfor_rate_daily`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月日',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `stat_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `total_customer_flow` bigint(20) NULL DEFAULT NULL COMMENT '总客流数',
  `order_num` bigint(20) NULL DEFAULT NULL COMMENT '总订单数',
  `goal_rate` decimal(20, 4) NULL DEFAULT NULL COMMENT '目标转化率',
  `actual_transfor_rate` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际转化率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店转化率（年日）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_store_transfor_rate_monthly
-- ----------------------------
DROP TABLE IF EXISTS `bi_store_transfor_rate_monthly`;
CREATE TABLE `bi_store_transfor_rate_monthly`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `total_customer_flow` bigint(20) NULL DEFAULT NULL COMMENT '总客流数',
  `order_num` bigint(20) NULL DEFAULT NULL COMMENT '总订单数',
  `goal_rate` decimal(20, 4) NULL DEFAULT NULL COMMENT '目标转化率',
  `actual_transfor_rate` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际转化率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店转化率（年月）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_total_sales_achieved_day
-- ----------------------------
DROP TABLE IF EXISTS `bi_total_sales_achieved_day`;
CREATE TABLE `bi_total_sales_achieved_day`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月日',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `stat_day` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `goal_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售目标额',
  `sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额',
  `different_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额差值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_date_index`(`store_id`, `stat_year`, `stat_month`, `stat_day`) USING BTREE COMMENT '门店,年,月,日唯一索引',
  INDEX `year_month_day_index`(`stat_year_month_day`) USING BTREE COMMENT '年月日索引'
) ENGINE = InnoDB AUTO_INCREMENT = 376 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总销售额（年月日）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bi_total_sales_achieved_month
-- ----------------------------
DROP TABLE IF EXISTS `bi_total_sales_achieved_month`;
CREATE TABLE `bi_total_sales_achieved_month`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_year_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月',
  `stat_year` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年',
  `stat_month` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `goal_sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售目标额',
  `sale_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额',
  `sale_cost` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售成本',
  `sale_num` bigint(20) NULL DEFAULT NULL COMMENT '销售数量',
  `sale_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '毛利额',
  `different_amt` decimal(20, 4) NULL DEFAULT NULL COMMENT '销售额差值',
  `is_satisfy_code` tinyint(1) NOT NULL COMMENT '是否达标状态（0不达标，1达标）',
  `is_satisfy` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否达标',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_date_index`(`store_id`, `stat_year`, `stat_month`) USING BTREE COMMENT '门店,年,月唯一索引',
  INDEX `year_month_index`(`stat_year_month`) USING BTREE COMMENT '年月索引'
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总销售达成情况--月' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销机构名称',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品id',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'skucode',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `logo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'logo图片',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `price` bigint(20) NOT NULL COMMENT '价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cashier_shift_schedule
-- ----------------------------
DROP TABLE IF EXISTS `cashier_shift_schedule`;
CREATE TABLE `cashier_shift_schedule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `end_time` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交接时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收银员交接班时间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_status
-- ----------------------------
DROP TABLE IF EXISTS `check_status`;
CREATE TABLE `check_status`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_status_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allinpay_member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户ID',
  `member_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `return_result` tinyint(1) NULL DEFAULT NULL COMMENT '反馈结果',
  `memo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '反馈时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '检查状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for complimentary_warehouse_correspondence
-- ----------------------------
DROP TABLE IF EXISTS `complimentary_warehouse_correspondence`;
CREATE TABLE `complimentary_warehouse_correspondence`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku_code',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兑换赠品与仓库对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for copartner_area_info
-- ----------------------------
DROP TABLE IF EXISTS `copartner_area_info`;
CREATE TABLE `copartner_area_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域ID',
  `copartner_area_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域名称',
  `copartner_area_level` tinyint(1) NULL DEFAULT 1 COMMENT '管理层级 1:一级 2：二级',
  `copartner_area_company` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人公司',
  `company_person_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司负责人ID',
  `company_person_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司负责人名称',
  `copartner_area_id_up` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '上级经营区域ID',
  `copartner_area_name_up` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '总部' COMMENT '上级经营区域名称',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `memo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 390 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '经营区域信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for copartner_area_role
-- ----------------------------
DROP TABLE IF EXISTS `copartner_area_role`;
CREATE TABLE `copartner_area_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域ID',
  `person_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号',
  `person_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `role_code` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限编码',
  `role_name` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `role_type` tinyint(1) NULL DEFAULT NULL COMMENT '组织权限 1：本公司管理权限,2：下级公司管理权限',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `memo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `role_store_code` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店权限编码',
  `role_store_name` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店权限名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1188 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '经营区域权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for copartner_area_store
-- ----------------------------
DROP TABLE IF EXISTS `copartner_area_store`;
CREATE TABLE `copartner_area_store`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域ID',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `memo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23193 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '经营区域关联门店' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_approval_detail
-- ----------------------------
DROP TABLE IF EXISTS `coupon_approval_detail`;
CREATE TABLE `coupon_approval_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `form_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批编码',
  `coupon_type` int(11) NULL DEFAULT NULL COMMENT '优惠券种类： 1-服纺券 2-A品券',
  `total_money` decimal(20, 4) NULL DEFAULT NULL COMMENT '优惠券金额总额度',
  `store_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放门店id',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放门店名称',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期结束时间',
  `creator` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `franchisee_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商ID',
  `order_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号（例如：退货单号）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'A品券审批详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_approval_info
-- ----------------------------
DROP TABLE IF EXISTS `coupon_approval_info`;
CREATE TABLE `coupon_approval_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `form_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批编号',
  `process_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批名称',
  `process_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批类型',
  `status` int(11) NULL DEFAULT 0 COMMENT '审批状态',
  `statu_str` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态描述',
  `applier_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人姓名',
  `applier_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人ID',
  `create_time_str` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `pre_node_opt_user` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `pre_node_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券审批表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_info
-- ----------------------------
DROP TABLE IF EXISTS `coupon_info`;
CREATE TABLE `coupon_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单ID',
  `franchisee_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商ID',
  `coupon_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券编码',
  `coupon_type` int(11) NULL DEFAULT NULL COMMENT '优惠券类型0-物流券 1-服纺券 2-A品券',
  `coupon_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `nominal_value` decimal(20, 4) NULL DEFAULT NULL COMMENT '面值',
  `validity_start_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期开始时间',
  `validity_end_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期结束时间',
  `state` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '状态(0:未录入 1:已录入)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 456 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_rule
-- ----------------------------
DROP TABLE IF EXISTS `coupon_rule`;
CREATE TABLE `coupon_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_type` tinyint(4) NULL DEFAULT NULL COMMENT '优惠券类型 0-物流券 1-服纺券 2-A品券',
  `coupon_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `proportion` decimal(20, 4) NULL DEFAULT NULL COMMENT '优惠比例',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券优惠规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_rule_detail
-- ----------------------------
DROP TABLE IF EXISTS `coupon_rule_detail`;
CREATE TABLE `coupon_rule_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编码',
  `coupon_type` tinyint(4) NULL DEFAULT NULL COMMENT '优惠券类型 0-物流券 1-服纺券 2-A品券',
  `product_property_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 141 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券优惠规则--商品属性关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for encoding_rule
-- ----------------------------
DROP TABLE IF EXISTS `encoding_rule`;
CREATE TABLE `encoding_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `numbering_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '业务编号',
  `numbering_value` bigint(20) NOT NULL DEFAULT 0 COMMENT '编码流水',
  `numbering_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编码流水' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for first_report
-- ----------------------------
DROP TABLE IF EXISTS `first_report`;
CREATE TABLE `first_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `copartner_area_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域',
  `zs_sales_amount` decimal(20, 4) NOT NULL COMMENT '首单直送销售金额',
  `zs_same_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单直送同期',
  `zs_on_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单直送上期',
  `zs_same_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单直送同比',
  `zs_ring_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单直送环比',
  `ps_sales_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单配送销售金额',
  `ps_same_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单配送同期',
  `ps_on_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单配送上期',
  `ps_same_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单配送同比',
  `ps_ring_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单配送环比',
  `hj_sales_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单货架销售金额',
  `hj_same_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单货架同期',
  `hj_on_period` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单货架上期',
  `hj_same_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单货架同比',
  `hj_ring_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单货架环比',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `report_time` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计月份',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 537 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首单销售报表表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for front_end_sales_statistics
-- ----------------------------
DROP TABLE IF EXISTS `front_end_sales_statistics`;
CREATE TABLE `front_end_sales_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sale_statistics_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售统计id',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单明细id',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku码',
  `sku_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名',
  `month` int(10) NULL DEFAULT NULL COMMENT '统计月份 eg: 202001',
  `category_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型id',
  `category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型名称',
  `sale_total_count` bigint(45) NULL DEFAULT 0 COMMENT '销售数量',
  `sale_total_amount` bigint(45) NULL DEFAULT 0 COMMENT '销售总金额(销售金额)',
  `price_unit` tinyint(1) NULL DEFAULT 0 COMMENT '0 : 分 1： 元',
  `sku_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'eg: 听  包 双等等',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_by` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `statistics_id_index`(`sale_statistics_id`) USING BTREE,
  INDEX `month_index`(`month`) USING BTREE COMMENT '统计月份索引',
  INDEX `store_id_index`(`store_id`) USING BTREE COMMENT '门店id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '前台销售统计(front_end_sales_statistics)-精确到月\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for front_end_sales_statistics_detail
-- ----------------------------
DROP TABLE IF EXISTS `front_end_sales_statistics_detail`;
CREATE TABLE `front_end_sales_statistics_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sale_statistics_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售统计id',
  `sale_statistics_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售统计明细id',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单明细id',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku码',
  `sku_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名',
  `day` int(10) NULL DEFAULT NULL COMMENT '统计天 eg : 20200101',
  `category_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型id',
  `category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型名称',
  `sale_total_count` bigint(45) NULL DEFAULT 0 COMMENT '销售数量',
  `sale_total_amount` bigint(45) NULL DEFAULT 0 COMMENT '销售总金额(销售金额)',
  `price_unit` tinyint(1) NULL DEFAULT 0 COMMENT '0 : 分 1： 元',
  `sku_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'eg: 听  包 双等等',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `statistics_id_index`(`sale_statistics_id`) USING BTREE,
  UNIQUE INDEX `statistics_detail_id_index`(`sale_statistics_detail_id`) USING BTREE,
  INDEX `store_id_index`(`store_id`) USING BTREE COMMENT '门店id索引',
  INDEX `day_index`(`day`) USING BTREE COMMENT '统计日期索引'
) ENGINE = InnoDB AUTO_INCREMENT = 908 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '前台销售统计明细(front_end_sales_statistics_detail)--精确到日\r\n\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for frozen
-- ----------------------------
DROP TABLE IF EXISTS `frozen`;
CREATE TABLE `frozen`  (
  `frozen_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '挂单Id',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销机构名称',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `sale_byid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售员id',
  `sale_byname` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售员名称',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品id',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku code（商品编码）',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `orig_price` bigint(20) NOT NULL COMMENT '原价',
  `curr_price` bigint(20) NOT NULL COMMENT '现价',
  `amount` int(11) NOT NULL COMMENT '数量',
  `price` bigint(20) NOT NULL COMMENT '金额',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '挂起人',
  `create_time` datetime(0) NOT NULL COMMENT '挂起时间',
  `memo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`create_by`, `distributor_id`, `frozen_id`, `sku_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '挂单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gift_pool
-- ----------------------------
DROP TABLE IF EXISTS `gift_pool`;
CREATE TABLE `gift_pool`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku编码',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku名称',
  `spu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu编码',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu名称',
  `price_tax` decimal(20, 4) NULL DEFAULT NULL COMMENT '原价(分销价格 ) ',
  `manufacturer_guide_price` decimal(20, 4) NULL DEFAULT NULL COMMENT '厂商指导价',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0为启用，1为禁用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兑换赠品池表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gift_quotas_use_detail
-- ----------------------------
DROP TABLE IF EXISTS `gift_quotas_use_detail`;
CREATE TABLE `gift_quotas_use_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `change_in_gift_quota` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赠品额度变化',
  `bill_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单code',
  `bill_link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关单据链接【预留字段】',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '类型 1 赠品划单 2订单使用 3订单赠送 4过期',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间【预留字段】',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '终止时间【预留字段】',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兑换赠品积分账户使用明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logistics_relief_info
-- ----------------------------
DROP TABLE IF EXISTS `logistics_relief_info`;
CREATE TABLE `logistics_relief_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rult_type` tinyint(1) NULL DEFAULT NULL COMMENT '规则类型：0单品购买数量 1单品购买金额 2累计购买数量  3累计购买金额 10(区分新旧规则)',
  `rult_type_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则类型名称',
  `rult_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则唯一编码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `logistics_explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流减免说明',
  `amount_required` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额要求',
  `logistics_proportion` decimal(20, 4) NULL DEFAULT NULL COMMENT '物流费比例',
  `logistics_order_type` tinyint(1) NULL DEFAULT NULL COMMENT '物流订单类型 1按品类 2按品牌 3按商品属性',
  `reduce_scope` tinyint(1) NULL DEFAULT NULL COMMENT '减免范围 1全部  2部分',
  `dev` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `effective_status` tinyint(1) NULL DEFAULT NULL COMMENT '生效状态： 0未开启 1开启',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 1是 2否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物流减免规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logistics_relief_product
-- ----------------------------
DROP TABLE IF EXISTS `logistics_relief_product`;
CREATE TABLE `logistics_relief_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rult_type` tinyint(1) NULL DEFAULT NULL COMMENT '规则类型: 0单品购买数量 1单品购买金额 2累计购买数量 3累计购买金额',
  `rult_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则唯一编码',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `product_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sales_standard` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批发销售规格',
  `brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `brand` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `category` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类',
  `types` tinyint(1) NULL DEFAULT NULL COMMENT '类型： 0件 1元',
  `fare_sill` decimal(20, 4) NULL DEFAULT NULL COMMENT '免运费门槛',
  `effective_status` tinyint(1) NULL DEFAULT 0 COMMENT '生效状态： 0未开启 1开启',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `spu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'spu编码',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'spu名称',
  `product_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性 ',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `rult_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流减免商品唯一id',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 1是 2否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 288 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物流减免商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `operation_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '来源编码',
  `operation_type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '日志类型 0 .新增 1.修改 2.删除 3.下载 ',
  `source_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '来源类型 0.销售 1.采购 2.退货  3.退供',
  `operation_status` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '关联单据状态',
  `operation_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志内容',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `use_status` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0. 启用 1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_operation_code`(`operation_code`) USING BTREE,
  INDEX `ind_source_type`(`source_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93919 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_after_sale
-- ----------------------------
DROP TABLE IF EXISTS `order_after_sale`;
CREATE TABLE `order_after_sale`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `after_sale_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '售后id',
  `after_sale_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '售后编号',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `member_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员手机号',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销机构名称',
  `return_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '退款金额',
  `after_sale_status` tinyint(1) NOT NULL COMMENT '退货状态',
  `after_sale_content` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '售后备注信息',
  `after_sale_type` tinyint(1) NOT NULL COMMENT '售后类型',
  `process_type` tinyint(1) NOT NULL COMMENT '处理方式',
  `receive_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receive_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人手机号',
  `pay_type` tinyint(1) NULL DEFAULT NULL COMMENT '订单支付方式',
  `origin_type` tinyint(1) NOT NULL COMMENT '订单来源类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员名称',
  `order_type` tinyint(1) NULL DEFAULT NULL COMMENT '订单类型 1：TOC订单 2: TOB订单 3：服务商品',
  `return_money_type` tinyint(1) NOT NULL DEFAULT 3 COMMENT '退款方式0.现金 1.储值卡',
  `refund_status` tinyint(1) NULL DEFAULT 0 COMMENT '退款状态，0退款中，1已完成',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_after_sale_2`(`order_code`, `member_phone`, `pay_type`, `after_sale_type`, `create_time`) USING BTREE,
  INDEX `idx_order_after_sale_1`(`order_type`, `distributor_id`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2085 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '售后订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_after_sale_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_after_sale_detail`;
CREATE TABLE `order_after_sale_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `after_sale_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货明细id',
  `after_sale_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku码',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `price` int(11) NOT NULL DEFAULT 0 COMMENT '单价',
  `member_price` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '会员价',
  `return_amount` int(11) NOT NULL DEFAULT 0 COMMENT '退货数量',
  `return_price` int(11) NOT NULL DEFAULT 0 COMMENT '退货金额',
  `return_reason_content` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因详情',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '商品类别id',
  `type_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类别名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `return_reason_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货原因名称',
  `return_reason_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货原因编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员名称',
  `cost_points` int(11) NOT NULL DEFAULT 0 COMMENT '退货返还加盟商的积分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_after_detail_1`(`after_sale_id`, `order_code`, `order_detail_id`) USING BTREE,
  INDEX `idx_order_after_detail_2`(`sku_code`, `bar_code`, `type_id`, `create_time`, `return_reason_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2324 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '售后订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_consumer_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_consumer_detail`;
CREATE TABLE `order_consumer_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品id，不在使用',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码 ,不在使用',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `list_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku_code',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名字',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_code',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格/说明书',
  `unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `retail_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '零售价格',
  `member_price` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '会员价',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际价格',
  `cost_price` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品成本价',
  `amount` int(11) NOT NULL DEFAULT 0 COMMENT '购买数量',
  `product_status` tinyint(1) NULL DEFAULT NULL COMMENT '商品状态',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表图',
  `type_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型id',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型名称',
  `gift_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否为赠品',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '退货状态',
  `return_amount` int(11) NULL DEFAULT 0 COMMENT '已退货数量',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动id',
  `activity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动名称',
  `coupon_discount` bigint(20) NULL DEFAULT 0 COMMENT '优惠券优惠金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `package_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐包ID',
  `total_price` int(11) NULL DEFAULT 0 COMMENT '行总金额',
  `shop_product_money` bigint(20) NULL DEFAULT NULL COMMENT '店长优惠后该商品总金额。没有优惠为null',
  `cost_points` bigint(20) NULL DEFAULT 0 COMMENT '消耗的积分',
  `receive_points` bigint(20) NULL DEFAULT 0 COMMENT '获取的积分',
  `product_property_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性',
  `brand_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_detail_2`(`retail_price`, `actual_price`, `amount`, `create_time`) USING BTREE,
  INDEX `idx_order_detail_1`(`order_id`, `sku_code`, `create_time`, `bar_code`, `gift_status`, `order_detail_id`) USING BTREE,
  INDEX `idx_order_detail_3`(`product_code`, `product_name`(191), `type_id`, `amount`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12977 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'toc订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_consumer_detail_pre
-- ----------------------------
DROP TABLE IF EXISTS `order_consumer_detail_pre`;
CREATE TABLE `order_consumer_detail_pre`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品id，不在使用',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品编码 ,不在使用',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `list_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku_code',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名字',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_code',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格/说明书',
  `unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `retail_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '零售价格',
  `member_price` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '会员价',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际价格',
  `cost_price` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品成本价',
  `amount` int(11) NOT NULL DEFAULT 0 COMMENT '购买数量',
  `product_status` tinyint(1) NULL DEFAULT NULL COMMENT '商品状态',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表图',
  `type_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型id',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型名称',
  `gift_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否为赠品',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '退货状态',
  `return_amount` int(11) NULL DEFAULT 0 COMMENT '已退货数量',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动id',
  `activity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动名称',
  `coupon_discount` bigint(20) NULL DEFAULT 0 COMMENT '优惠券优惠金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `package_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐包ID',
  `total_price` int(11) NULL DEFAULT 0 COMMENT '行总金额',
  `shop_product_money` bigint(20) NULL DEFAULT NULL COMMENT '店长优惠后该商品总金额。没有优惠为null',
  `cost_points` bigint(20) NULL DEFAULT 0 COMMENT '消耗的积分',
  `receive_points` bigint(20) NULL DEFAULT 0 COMMENT '获取的积分',
  `product_property_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性',
  `brand_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 508 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'toc订单明细表草稿' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_consumer_frozen
-- ----------------------------
DROP TABLE IF EXISTS `order_consumer_frozen`;
CREATE TABLE `order_consumer_frozen`  (
  `frozen_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '挂单Id',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销机构名称',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `sale_byid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售员id',
  `sale_byname` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售员名称',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品id',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku code（商品编码）',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `orig_price` bigint(20) NOT NULL COMMENT '原价',
  `curr_price` bigint(20) NOT NULL COMMENT '现价',
  `amount` int(11) NOT NULL COMMENT '数量',
  `price` bigint(20) NOT NULL COMMENT '金额',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '挂起人',
  `create_time` datetime(0) NOT NULL COMMENT '挂起时间',
  `memo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_prestorage` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否预存订单，1：是 ，2 否',
  PRIMARY KEY (`create_by`, `distributor_id`, `frozen_id`, `sku_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店挂单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_consumer_info
-- ----------------------------
DROP TABLE IF EXISTS `order_consumer_info`;
CREATE TABLE `order_consumer_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `member_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员手机号',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构名称',
  `origin_type` tinyint(1) NOT NULL COMMENT '来源类型',
  `receive_type` tinyint(1) NOT NULL COMMENT '收货方式',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态0 未付款，1:待发货，2:带提货，3:已发货，4:已取消，5:已完成',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否发生退货',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态 0 未支付 1已支付',
  `pay_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式，0在线支付-微信，1在线支付-支付宝，2在线支付-银行卡，3到店支付-现金，4到店支付-微信，5到店支付-支付宝，6到店支付-银行卡，7储值卡支付',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际金额',
  `total_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '应付金额',
  `custom_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户备注',
  `business_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家备注',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `cashier_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员名称',
  `guide_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购id',
  `guide_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购名称',
  `create_time` datetime(0) NOT NULL COMMENT '下单时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `receive_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货码',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '提货码生成时间',
  `order_type` tinyint(1) NULL DEFAULT 1 COMMENT '订单类型 1：TOC订单 2: TOB订单 3：服务商品，4 预存订单',
  `suk_return` tinyint(1) NULL DEFAULT NULL COMMENT '销量是否被统计1:已统计',
  `shop_order_preferential` bigint(20) NULL DEFAULT 0 COMMENT '店长整单优惠金额',
  `brand_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_info_2`(`order_code`, `member_phone`, `order_status`, `origin_type`, `create_time`, `pay_status`) USING BTREE,
  INDEX `idx_order_info_1`(`order_type`, `distributor_id`, `create_time`, `member_id`, `order_id`) USING BTREE,
  INDEX `order_info__index_statistical`(`distributor_id`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9985 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Toc订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_consumer_info_pre
-- ----------------------------
DROP TABLE IF EXISTS `order_consumer_info_pre`;
CREATE TABLE `order_consumer_info_pre`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `member_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员手机号',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构名称',
  `origin_type` tinyint(1) NOT NULL COMMENT '来源类型',
  `receive_type` tinyint(1) NOT NULL COMMENT '收货方式',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态0 未付款，1:待发货，2:带提货，3:已发货，4:已取消，5:已完成',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否发生退货',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态 0 未支付 1已支付',
  `pay_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式，0在线支付-微信，1在线支付-支付宝，2在线支付-银行卡，3到店支付-现金，4到店支付-微信，5到店支付-支付宝，6到店支付-银行卡，7储值卡支付',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际金额',
  `total_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '应付金额',
  `custom_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户备注',
  `business_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家备注',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `cashier_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员名称',
  `guide_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购id',
  `guide_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购名称',
  `create_time` datetime(0) NOT NULL COMMENT '下单时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `receive_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货码',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '提货码生成时间',
  `order_type` tinyint(1) NULL DEFAULT 1 COMMENT '订单类型 1：TOC订单 2: TOB订单 3：服务商品，4 预存订单',
  `suk_return` tinyint(1) NULL DEFAULT NULL COMMENT '销量是否被统计1:已统计',
  `shop_order_preferential` bigint(20) NULL DEFAULT 0 COMMENT '店长整单优惠金额',
  `brand_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 238 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Toc订单表草稿' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品id',
  `product_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品编码',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `list_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku_code',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_code',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格/说明书',
  `unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `retail_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '零售价格',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际价格',
  `amount` int(11) NOT NULL DEFAULT 0 COMMENT '购买数量',
  `product_status` tinyint(1) NULL DEFAULT NULL COMMENT '商品状态',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列表图',
  `type_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型id',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类类型名称',
  `gift_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否为赠品',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '退货状态',
  `return_amount` int(11) NULL DEFAULT 0 COMMENT '已退货数量',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动id',
  `activity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营销管理创建活动名称',
  `coupon_discount` bigint(20) NULL DEFAULT 0 COMMENT '优惠券优惠金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_detail_2`(`retail_price`, `actual_price`, `amount`, `create_time`) USING BTREE,
  INDEX `idx_order_detail_1`(`order_id`, `sku_code`, `create_time`, `bar_code`, `gift_status`, `order_detail_id`) USING BTREE,
  INDEX `idx_order_detail_3`(`product_code`, `product_name`(191), `type_id`, `amount`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7456 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_give_approval
-- ----------------------------
DROP TABLE IF EXISTS `order_give_approval`;
CREATE TABLE `order_give_approval`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `form_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批编码',
  `fanchisee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商名称',
  `fanchisee_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商id',
  `store_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `market_value` decimal(20, 4) NULL DEFAULT NULL COMMENT '首单赠送市值',
  `actual_market_value` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际赠送市值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `status` int(1) NULL DEFAULT 0 COMMENT '审批状态 0:待审批 1:审批完成',
  `process_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批名称',
  `process_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批类型',
  `statu_str` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态描述',
  `applier_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人姓名',
  `applier_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人ID',
  `pre_node_opt_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `pre_node_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `order_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首单赠送超额审批' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_give_fee
-- ----------------------------
DROP TABLE IF EXISTS `order_give_fee`;
CREATE TABLE `order_give_fee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `store_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '扣减金额',
  `sub_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额差值',
  `status` int(1) NULL DEFAULT 0 COMMENT '核对状态 0:未核对 1:已核对',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首单赠送--赠送市值核对表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `member_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员手机号',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构名称',
  `origin_type` tinyint(1) NOT NULL COMMENT '来源类型',
  `receive_type` tinyint(1) NOT NULL COMMENT '收货方式',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态',
  `return_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否发生退货',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态 0 未支付 1已支付',
  `pay_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式，1.现金、2微信  3.支付宝、4银联 5储值卡',
  `actual_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际金额',
  `total_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '应付金额',
  `custom_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户备注',
  `business_note` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家备注',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `cashier_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员名称',
  `guide_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购id',
  `guide_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购名称',
  `create_time` datetime(0) NOT NULL COMMENT '下单时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `receive_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提货码',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '提货码生成时间',
  `order_type` tinyint(1) NULL DEFAULT 1 COMMENT '订单类型 1：TOC订单 2: TOB订单 3：服务商品，4 预存订单',
  `suk_return` tinyint(1) NULL DEFAULT NULL COMMENT '销量是否被统计1:已统计',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_info_2`(`order_code`, `member_phone`, `order_status`, `origin_type`, `create_time`, `pay_status`) USING BTREE,
  INDEX `idx_order_info_1`(`order_type`, `distributor_id`, `create_time`, `member_id`, `order_id`) USING BTREE,
  INDEX `order_info__index_statistical`(`distributor_id`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4903 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Toc订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_list
-- ----------------------------
DROP TABLE IF EXISTS `order_list`;
CREATE TABLE `order_list`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型(1:配送补货、2:直送补货、3:首单、4:首单赠送)',
  `order_status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '订单状态',
  `payment_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '支付状态(0:未支付 1:已支付 2:已退款)',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付日期',
  `payment_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式描述',
  `payment_type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式编码(支付类型(1.微信，2支付宝，3网联，4 转账))',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店code',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `store_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店类型',
  `total_orders` bigint(20) NOT NULL COMMENT '订单总额',
  `actual_amount_paid` bigint(20) NOT NULL COMMENT '实付金额',
  `activity_amount` bigint(20) NOT NULL COMMENT '活动金额',
  `preferential_quota` bigint(20) NOT NULL COMMENT '优惠额度',
  `logistics_remission_ratio` int(11) NULL DEFAULT NULL COMMENT '物流减免比例',
  `logistics_remission_type` tinyint(1) NULL DEFAULT NULL COMMENT '物流减免类型(0:送货上门,1:送货市级市,2:无减免)',
  `place_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下单人编码',
  `place_order_time` datetime(0) NOT NULL COMMENT '下单时间',
  `account_balance` bigint(20) NULL DEFAULT NULL COMMENT '账户余额',
  `line_credit` bigint(20) NULL DEFAULT NULL COMMENT '授信额度',
  `receiving_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '到货时间',
  `consignee` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `consignee_phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属公司',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `original` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原单标识code',
  `province_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省编码',
  `province_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省名称',
  `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市编码',
  `city_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市名称',
  `district_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区编码',
  `district_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区名称',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `invoice_type` tinyint(1) NULL DEFAULT NULL COMMENT '发票抬头类型 0不开、1增普、2增专',
  `order_original` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单来源',
  `product_num` int(11) NULL DEFAULT NULL COMMENT '订单商品数量',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `transport_center_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流中心编码',
  `transport_center_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流中心名称',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `weight` int(11) NULL DEFAULT NULL COMMENT '重量',
  `zip_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `remake` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `application_gifts` tinyint(1) NULL DEFAULT NULL COMMENT '是否申请赠品(0是1否)',
  `apply_coupons` tinyint(1) NULL DEFAULT NULL COMMENT '是否申请优惠券(0是1否)',
  `order_list_activity_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单参与活动编号',
  `order_list_activity_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单参与活动名称',
  `order_list_activity_type` tinyint(2) NULL DEFAULT NULL COMMENT '订单参与活动类型 (6-条件类型满减，7-条件类型满赠，8-条件类型折扣，9-条件类型特价)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_code_UNIQUE`(`order_code`) USING BTREE,
  INDEX `order_list_order_status_store_id_create_time_index`(`order_status`, `store_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_list_logistics
-- ----------------------------
DROP TABLE IF EXISTS `order_list_logistics`;
CREATE TABLE `order_list_logistics`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `invoice_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发货单号',
  `logistics_centre_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流中心标示',
  `logistics_centre_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流中心名称',
  `implement_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行人',
  `implement_time` datetime(0) NOT NULL COMMENT '执行时间',
  `implement_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行内容',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单物流从表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_list_product
-- ----------------------------
DROP TABLE IF EXISTS `order_list_product`;
CREATE TABLE `order_list_product`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单商品ID',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品sku码',
  `sku_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `product_number` int(11) NOT NULL COMMENT '商品数量',
  `activity_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参与活动编号',
  `activity_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参与活动名称',
  `activity_type` tinyint(2) NULL DEFAULT NULL COMMENT '订单参与活动类型 (6-条件类型满减，7-条件类型满赠，8-条件类型折扣，9-条件类型特价)\n',
  `gift` tinyint(1) NOT NULL COMMENT '是否赠品(0:不是赠品 1:是赠品)',
  `original_product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赠送赠品商品ID',
  `specifications` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `color_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色code',
  `color_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色名称',
  `model_number` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `actual_deliver_num` int(11) NULL DEFAULT 0 COMMENT '实发数量',
  `product_price` bigint(20) NOT NULL COMMENT '商品价格（单品合计成交价)',
  `original_product_price` bigint(20) NULL DEFAULT NULL COMMENT '商品单价',
  `discount_money` bigint(20) NULL DEFAULT 0 COMMENT '优惠额度抵扣金额（单品合计）',
  `amount` int(11) NULL DEFAULT NULL COMMENT '总价',
  `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `preferential_allocation` int(11) NULL DEFAULT 0 COMMENT '优惠分摊',
  `return_num` int(11) NULL DEFAULT 0 COMMENT '退货数量',
  `supply_company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应单位编码',
  `supply_company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应单位名称',
  `use_discount_amount` tinyint(1) NULL DEFAULT NULL COMMENT '是否使用优惠额度(是否使用优惠额度,1-使用了优惠额度，0-没使用)',
  `discount_amount_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠额度信息(json)',
  `product_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型，0-正常品，1-效期品',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单商品从表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `order_operation_log`;
CREATE TABLE `order_operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志id',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单id',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `status_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态码',
  `status_content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `status_type` int(11) NULL DEFAULT NULL COMMENT '状态类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31751 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_pay
-- ----------------------------
DROP TABLE IF EXISTS `order_pay`;
CREATE TABLE `order_pay`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付id',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `pay_type` tinyint(1) NOT NULL COMMENT '支付类型',
  `pay_way` tinyint(1) NOT NULL COMMENT '支付方式',
  `pay_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付描述',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态',
  `pay_price` bigint(20) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_pay_1`(`order_id`, `pay_type`, `pay_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7520 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单支付' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_relation_coupon
-- ----------------------------
DROP TABLE IF EXISTS `order_relation_coupon`;
CREATE TABLE `order_relation_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ordercoupon_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键ID',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单明细id',
  `coupon_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券id',
  `coupon_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_discount` int(11) NOT NULL DEFAULT 0 COMMENT '优惠券优惠金额',
  `coupon_status` tinyint(1) NULL DEFAULT 0 COMMENT '优惠券状态',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `order_relation_coupon_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单明细id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_coupon_1`(`order_id`, `order_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单优惠券关系表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_splits_num
-- ----------------------------
DROP TABLE IF EXISTS `order_splits_num`;
CREATE TABLE `order_splits_num`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单编码',
  `num` int(11) NULL DEFAULT NULL COMMENT '拆单数量',
  `status` int(11) NULL DEFAULT 0 COMMENT '是否发货完成 0:未完成 1:已完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 868 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '子单拆单数量表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_status
-- ----------------------------
DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `order_status_code` int(10) NULL DEFAULT NULL COMMENT '订单状态code',
  `payment_status` tinyint(1) NULL DEFAULT NULL COMMENT '支付状态(0:未支付 1:已支付 2:已退款)',
  `reception_order_status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前台订单状态(显示)',
  `backstage_order_status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后台订单状态(显示)',
  `explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `standard_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准描述',
  `order_display` tinyint(1) NULL DEFAULT NULL COMMENT '订单是否显示(0显示1不显示)',
  `follow_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后续状态',
  `Stop_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请取消、终止订单状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_cart
-- ----------------------------
DROP TABLE IF EXISTS `order_store_cart`;
CREATE TABLE `order_store_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cart_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '购物车id',
  `product_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品id',
  `franchisee_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商id',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `coupon_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券id',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `logo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'logo图片',
  `price` decimal(11, 4) NOT NULL COMMENT '商品价格',
  `color` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品颜色',
  `product_size` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品规格',
  `activity_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku码',
  `spu_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu码',
  `product_type` tinyint(45) NOT NULL COMMENT '商品类型 直送/配送/货架',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_source` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品添加来源 1:门店 2:erp',
  `product_gift` tinyint(1) NULL DEFAULT NULL COMMENT '本品或赠品',
  `gift_parent_cart_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赠品关联本品行cart_id',
  `line_check_status` tinyint(1) NULL DEFAULT 1 COMMENT '行是否选中',
  `stock_num` bigint(20) NULL DEFAULT NULL COMMENT '库存数量',
  `zero_removalCoefficient` bigint(20) NULL DEFAULT NULL COMMENT '交易倍数',
  `spec` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `product_property_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性码',
  `product_property_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性名称',
  `product_brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `product_brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `product_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 866 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运营ERP购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_cart_info
-- ----------------------------
DROP TABLE IF EXISTS `order_store_cart_info`;
CREATE TABLE `order_store_cart_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cart_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '购物车id',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `product_type` tinyint(45) NOT NULL COMMENT '商品类型 1直送 2配送',
  `create_source` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品添加来源 1:门店 2:erp',
  `product_gift` tinyint(1) NOT NULL COMMENT '本品或赠品 0本品 1赠品 2兑换赠品',
  `spu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'spu编码',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'spu名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku编码',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku名称',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `price` decimal(11, 4) NOT NULL COMMENT '商品价格（分销价）',
  `line_check_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '行是否选中 0未选中 1选中',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `max_order_num` bigint(20) NULL DEFAULT NULL COMMENT '最大订购数量',
  `zero_removal_coefficient` bigint(20) NULL DEFAULT NULL COMMENT '交易倍数',
  `logo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'logo图片',
  `color` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品颜色',
  `spec` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `product_size` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_property_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性名称',
  `product_brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `product_brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `product_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `batch_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次时间',
  `warehouse_type_code` tinyint(1) NULL DEFAULT 1 COMMENT '传入库房编码:1:销售库，2:特卖库',
  `batch_info_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库code',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cart_id`(`cart_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2021 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运营ERP购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_store_detail`;
CREATE TABLE `order_store_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_info_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `order_store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu编码',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `bar_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `picture_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `product_spec` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `color_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色编码',
  `color_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色名称',
  `model_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `zero_disassembly_coefficient` bigint(20) NULL DEFAULT 0 COMMENT '拆零系数',
  `product_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型  0商品 1赠品',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '商品数量',
  `product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品单价',
  `purchase_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '采购价',
  `total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品总价',
  `actual_total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实际商品总价',
  `total_preferential_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '优惠分摊总金额',
  `total_acivity_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '活动优惠总金额',
  `actual_inbound_count` bigint(20) NULL DEFAULT 0 COMMENT '实收数量（门店）',
  `actual_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实际商品数量',
  `return_product_count` bigint(20) NULL DEFAULT 0 COMMENT '退货数量',
  `tax_rate` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '税率',
  `activity_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动编码(多个，隔开）',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `gift_line_code` bigint(20) NULL DEFAULT NULL COMMENT '赠品行号（赠品拆为多个的时候为-1）',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `sign_difference_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签收数量差异原因',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称处',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `product_property_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性名称',
  `supplier_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `preferential_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '均摊后单价',
  `box_gross_weight` decimal(20, 2) NULL DEFAULT NULL COMMENT '单个商品毛重（kg）',
  `box_volume` decimal(20, 2) NULL DEFAULT NULL COMMENT '单个商品体积 （mm³）',
  `is_activity` tinyint(1) NULL DEFAULT 0 COMMENT '是否活动商品0否1是',
  `product_brand_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌编码',
  `product_brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌名称',
  `product_category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类编码',
  `product_category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类名称',
  `activity_price` decimal(20, 4) NULL DEFAULT NULL COMMENT '活动后单价',
  `activity_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `top_coupon_discount_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '仅A品优惠金额',
  `top_coupon_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '仅A品优惠单品金额',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号【展示字段】',
  `batch_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次时间',
  `warehouse_type_code` tinyint(1) NULL DEFAULT 1 COMMENT '传入库房编码:1:销售库，2:特卖库',
  `batch_info_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号【供应链唯一标识】',
  `output_tax_rate` bigint(20) NULL DEFAULT NULL COMMENT '销项税率',
  `transport_center_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `batch_type` tinyint(1) NULL DEFAULT NULL COMMENT '批次类型  0：月份批次  1：非月份批次',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_info_detail_id_UNIQUE`(`order_info_detail_id`) USING BTREE,
  INDEX `ind_order_store_code`(`order_store_code`) USING BTREE,
  INDEX `ind_spu_code`(`spu_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_company_code`(`company_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3653 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_detail_batch
-- ----------------------------
DROP TABLE IF EXISTS `order_store_detail_batch`;
CREATE TABLE `order_store_detail_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_store_detail_batch_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `order_store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次号',
  `product_date` datetime(0) NULL DEFAULT NULL COMMENT '生产日期',
  `batch_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '数量',
  `actual_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实际发货数量',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_info_detail_batch_id_UNIQUE`(`order_store_detail_batch_id`) USING BTREE,
  INDEX `ind_order_store_code`(`order_store_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_batch_code`(`batch_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单商品批次信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_fee
-- ----------------------------
DROP TABLE IF EXISTS `order_store_fee`;
CREATE TABLE `order_store_fee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '费用id',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单id',
  `pay_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付单id',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态',
  `total_money` decimal(20, 4) NOT NULL COMMENT '订单总额',
  `activity_money` decimal(20, 4) NULL DEFAULT NULL COMMENT '活动金额',
  `suit_coupon_money` decimal(20, 4) NULL DEFAULT NULL COMMENT '服纺券优惠金额',
  `top_coupon_money` decimal(20, 4) NULL DEFAULT NULL COMMENT 'A品券优惠金额',
  `pay_money` decimal(20, 4) NOT NULL COMMENT '实付金额',
  `goods_coupon` decimal(20, 4) NULL DEFAULT NULL COMMENT '物流券金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人姓名',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1有效 0删除',
  `top_coupon_codes` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'A品券编码',
  `used_gift_quota` double(20, 4) NULL DEFAULT NULL COMMENT '使用赠品额度',
  `nullify_suit_coupon_money` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '服纺券作废金额',
  `nullify_top_coupon_money` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT 'A品券作废金额',
  `complimentary_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '发放赠品额度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1570 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单费用信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_gift_pool_cart_info
-- ----------------------------
DROP TABLE IF EXISTS `order_store_gift_pool_cart_info`;
CREATE TABLE `order_store_gift_pool_cart_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cart_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '购物车id',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `product_type` tinyint(45) NOT NULL COMMENT '商品类型 1直送 2配送',
  `create_source` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品添加来源 1:门店 2:erp',
  `product_gift` tinyint(1) NOT NULL DEFAULT 2 COMMENT '本品或赠品 0本品 1赠品 2兑换赠品',
  `spu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu编码',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku编码',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku名称',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `price` decimal(11, 4) NOT NULL COMMENT '商品价格（分销价）',
  `line_check_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '行是否选中 0未选中 1选中',
  `activity_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动id',
  `max_order_num` bigint(20) NULL DEFAULT NULL COMMENT '最大订购数量',
  `zero_removal_coefficient` bigint(20) NULL DEFAULT NULL COMMENT '交易倍数',
  `logo` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'logo图片',
  `color` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品颜色',
  `spec` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `product_size` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_property_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性编码',
  `product_property_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品属性名称',
  `product_brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `product_brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `product_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `batch_date` datetime(0) NULL DEFAULT NULL COMMENT '批次时间',
  `warehouse_type_code` tinyint(1) NULL DEFAULT 1 COMMENT '传入库房编码:1:销售库，2:特卖库',
  `batch_info_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cart_id`(`cart_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '爱掌柜使用兑换赠品购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_info
-- ----------------------------
DROP TABLE IF EXISTS `order_store_info`;
CREATE TABLE `order_store_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `order_store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编码',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `order_type_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型编码 1直送 2配送 3辅采直送',
  `order_type_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型名称',
  `order_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类别编码',
  `order_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类别名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `transport_center_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `customer_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编码',
  `customer_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `order_status` tinyint(2) NULL DEFAULT 0 COMMENT '订单状态',
  `order_node_status` tinyint(3) NULL DEFAULT NULL COMMENT '订单流程节点状态',
  `order_lock` tinyint(1) NULL DEFAULT 1 COMMENT '是否锁定(0是1否）',
  `lock_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '锁定原因',
  `order_exception` tinyint(1) NULL DEFAULT 1 COMMENT '是否是异常订单(0是1否)',
  `exception_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常原因',
  `order_delete` tinyint(1) NULL DEFAULT 1 COMMENT '是否删除(0是1否)',
  `payment_status` tinyint(1) NULL DEFAULT 1 COMMENT '支付状态0已支付  1未支付',
  `province_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省编码',
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省',
  `city_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市编码',
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市名称',
  `district_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县编码',
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县',
  `receive_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `distribution_mode_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送方式编码',
  `distribution_mode_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送方式名称',
  `receive_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `receive_mobile` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `zip_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `payment_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式编码',
  `payment_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式名称',
  `deliver_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '运费',
  `total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品总价',
  `actual_total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实际商品总价',
  `actual_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实际发货数量',
  `discount_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '优惠额度',
  `order_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '订单金额',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '付款期',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `transport_time` datetime(0) NULL DEFAULT NULL COMMENT '发运时间',
  `transport_status` tinyint(1) NULL DEFAULT 0 COMMENT '发运状态 0 未发运 1 已发运',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `invoice_type` tinyint(1) NULL DEFAULT 0 COMMENT '发票类型 1不开 2增普 3增专',
  `invoice_title` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `total_volume` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '体积',
  `actual_total_volume` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '实际体积',
  `total_weight` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '重量',
  `actual_total_weight` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '实际重量',
  `main_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单号  如果非子订单 此处存order_code',
  `order_level` tinyint(1) NULL DEFAULT 0 COMMENT '订单级别(0主1子订单)',
  `split_status` tinyint(1) NULL DEFAULT NULL COMMENT '是否被拆分 0是 1否',
  `before_cancel_status` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '申请取消时的状态',
  `remake` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `store_type` tinyint(1) NULL DEFAULT NULL COMMENT '门店类型',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `transport_company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输公司编码',
  `transport_company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输公司名称',
  `transport_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输单号',
  `logistics_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流id',
  `fee_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用id',
  `order_return` tinyint(3) NULL DEFAULT NULL COMMENT '退货状态 1未退货 2部分退货 3退货完成',
  `franchisee_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商id',
  `franchisee_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商编码',
  `franchisee_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商名称',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `source_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源单号',
  `source_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源名称',
  `source_type` tinyint(1) NULL DEFAULT 0 COMMENT '来源类型',
  `order_success` tinyint(1) NULL DEFAULT 0 COMMENT '同步是否成功（创建采购单） 0 不生成采购单 1 待生成采购单 2采购单生成成功',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `scour_sheet_status` tinyint(3) NULL DEFAULT NULL COMMENT '冲减单状态 1不需要生成冲减单 2等待生成冲减单 3生成冲减单完成',
  `order_return_process` tinyint(3) NULL DEFAULT NULL COMMENT '退货流程状态 1无进行中的退货 0正在退货',
  `is_activity` tinyint(1) NULL DEFAULT 0 COMMENT '是否活动商品0否1是',
  `activity_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '整单活动id',
  `first_market_value_gift` tinyint(3) NULL DEFAULT 0 COMMENT '是否为首单市值赠送订单 0:不是 1:是',
  `logistics_fee_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否减免物流费用 0否  1是',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人区域id',
  `copartner_area_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人区域名称',
  `business_form` tinyint(255) NULL DEFAULT NULL COMMENT '业务形式(0门店 ，1批发)',
  `copartner_area_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人区域code(表id)',
  `logistics_cost_reduction_ratio` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '物流费用减免比例',
  `logistics_cost_reduction_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '物流费用减免金额',
  `logistics_amount_sent` tinyint(1) NULL DEFAULT 0 COMMENT '是否需要发放物流减免0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_store_id_UNIQUE`(`order_store_id`) USING BTREE,
  UNIQUE INDEX `order_store_code_UNIQUE`(`order_store_code`) USING BTREE,
  INDEX `ind_order_store_code`(`order_store_code`) USING BTREE,
  INDEX `ind_company_code`(`company_code`) USING BTREE,
  INDEX `ind_order_type_code`(`order_type_code`) USING BTREE,
  INDEX `ind_transport_center_code`(`transport_center_code`) USING BTREE,
  INDEX `ind_supplier_code`(`supplier_code`) USING BTREE,
  INDEX `ind_warehouse_code`(`warehouse_code`) USING BTREE,
  INDEX `ind_payment_code`(`payment_code`) USING BTREE,
  INDEX `ind_store_code`(`store_code`) USING BTREE,
  INDEX `ind_franchisee_code`(`franchisee_code`) USING BTREE,
  INDEX `ind_source_code`(`source_code`) USING BTREE,
  INDEX `ind_use_status`(`use_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1959 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_logistics
-- ----------------------------
DROP TABLE IF EXISTS `order_store_logistics`;
CREATE TABLE `order_store_logistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistics_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发货id',
  `pay_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付id',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态',
  `logistics_centre_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流公司编码',
  `logistics_centre_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流公司名称',
  `logistics_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流单号',
  `send_repertory_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货仓库编码',
  `send_repertory_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货仓库名称',
  `logistics_fee` decimal(20, 4) NOT NULL COMMENT '物流费用',
  `coupon_pay_fee` decimal(20, 4) NULL DEFAULT NULL COMMENT '物流券抵扣金额',
  `balance_pay_fee` decimal(20, 4) NULL DEFAULT NULL COMMENT '余额支付金额',
  `coupon_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流券id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人姓名',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1有效 0删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 503 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单物流信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_pay
-- ----------------------------
DROP TABLE IF EXISTS `order_store_pay`;
CREATE TABLE `order_store_pay`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付id',
  `business_key` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务外键',
  `pay_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付流水号',
  `fee_type` tinyint(1) NOT NULL COMMENT '费用类型',
  `pay_status` tinyint(1) NOT NULL COMMENT '支付状态',
  `pay_way` tinyint(1) NULL DEFAULT NULL COMMENT '支付方式',
  `pay_fee` decimal(20, 4) NULL DEFAULT NULL COMMENT '支付金额',
  `pay_start_time` datetime(0) NULL DEFAULT NULL COMMENT '发起支付时间',
  `pay_end_time` datetime(0) NULL DEFAULT NULL COMMENT '支付结束时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人姓名',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1有效 0删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1613 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单关联支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_store_refund
-- ----------------------------
DROP TABLE IF EXISTS `order_store_refund`;
CREATE TABLE `order_store_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `refund_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款单id',
  `refund_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款单编号',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单id',
  `pay_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联支付单id',
  `refund_fee` decimal(20, 4) NOT NULL COMMENT '退款金额',
  `refund_status` tinyint(3) NOT NULL COMMENT '退款状态',
  `refund_type` tinyint(3) NULL DEFAULT NULL COMMENT '退款类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `update_by_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人姓名',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 0有效 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单退款信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prestorage_order_supply
-- ----------------------------
DROP TABLE IF EXISTS `prestorage_order_supply`;
CREATE TABLE `prestorage_order_supply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prestorage_order_supply_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预存订单提货id',
  `prestorage_order_supply_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预存订单提货编号',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `member_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员id',
  `member_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `member_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员手机号',
  `distributor_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构id',
  `distributor_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销机构编码',
  `distributor_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销机构名称',
  `prestorage_order_supply_status` tinyint(1) NOT NULL COMMENT '提货状态，2待提货，5已完成',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员名称',
  `pay_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式，1.现金、2微信  3.支付宝、4银联 5储值卡',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_order_id`(`order_id`) USING BTREE,
  INDEX `index_distributor_id_member_id`(`distributor_id`, `member_id`) USING BTREE,
  INDEX `index_prestorage_order_supply_id`(`prestorage_order_supply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 333 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预存订单提货主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prestorage_order_supply_detail
-- ----------------------------
DROP TABLE IF EXISTS `prestorage_order_supply_detail`;
CREATE TABLE `prestorage_order_supply_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prestorage_order_supply_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预存订单提货明细id',
  `prestorage_order_supply_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预存订单提货id',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku码',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `amount` int(11) NOT NULL DEFAULT 0 COMMENT '购买数量',
  `return_amount` int(11) NOT NULL DEFAULT 0 COMMENT '已提货的退货数量',
  `return_prestorage_amount` int(11) NOT NULL DEFAULT 0 COMMENT '未提货的退货数量',
  `supply_amount` int(11) NOT NULL DEFAULT 0 COMMENT '提货数量',
  `prestorage_order_supply_status` tinyint(1) NOT NULL COMMENT '提货状态，2待提货，5已完成',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_prestorage_order_supply_detail_id`(`prestorage_order_supply_detail_id`) USING BTREE,
  INDEX `index_prestorage_order_supply_id`(`prestorage_order_supply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 502 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预存订单提货表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prestorage_order_supply_logs
-- ----------------------------
DROP TABLE IF EXISTS `prestorage_order_supply_logs`;
CREATE TABLE `prestorage_order_supply_logs`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `prestorage_order_supply_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预存订单提货id',
  `prestorage_order_supply_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预存订单提货明细id',
  `product_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sku_code',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `bar_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `supply_amount` int(11) NULL DEFAULT 0 COMMENT '提货数量',
  `surplus_amount` int(11) NULL DEFAULT 0 COMMENT '剩余数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '提取时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_prestorage_order_supply_detail_id`(`prestorage_order_supply_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 227 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预存订单取货流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for purchase_batch
-- ----------------------------
DROP TABLE IF EXISTS `purchase_batch`;
CREATE TABLE `purchase_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_oder_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号/wms批次号',
  `batch_info_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号',
  `sku_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编码',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SKU名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `batch_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `product_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产日期',
  `be_overdue_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过期日期',
  `total_count` bigint(20) NULL DEFAULT NULL COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT NULL COMMENT '实际最小单位数量',
  `location_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库位号',
  `line_code` int(11) NULL DEFAULT NULL COMMENT '行号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_batch_code`(`batch_code`) USING BTREE,
  INDEX `index_location_code`(`location_code`(191)) USING BTREE,
  INDEX `index_batch_info_code`(`batch_info_code`(191)) USING BTREE,
  INDEX `index_line_code`(`line_code`) USING BTREE,
  INDEX `index_purchase_oder_code`(`purchase_oder_code`(191)) USING BTREE,
  INDEX `index_sku_code`(`sku_code`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108815 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for purchase_order
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `purchase_order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `purchase_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '采购单号',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `transport_center_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `purchase_group_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组编码',
  `purchase_group_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组名称',
  `settlement_method_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算方式编码',
  `settlement_method_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算方式名称',
  `purchase_order_status` tinyint(1) NULL DEFAULT 0 COMMENT '采购单状态  0.待确认1.完成 2.取消 ',
  `purchase_mode` tinyint(1) NULL DEFAULT 0 COMMENT '采购方式 0. 铺采直送  1.配送',
  `purchase_type` tinyint(1) NULL DEFAULT 0 COMMENT '采购单类型   0手动，1.自动  ',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税总金额',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际最小单数数量',
  `actual_total_tax_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际商品含税总金额',
  `cancel_reason` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消原因',
  `cancel_remark` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消备注',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `charge_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `account_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户编码',
  `account_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `contract_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合同编码',
  `contract_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合同名称',
  `contact_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_mobile` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `pre_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '预计到货时间',
  `valid_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期',
  `delivery_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货地址',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `in_stock_time` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `in_stock_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库地址',
  `pre_purchase_order` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预采购单号',
  `payment_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式编码',
  `payment_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式名称',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '付款期',
  `pre_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '预付付款金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源单号',
  `source_type` tinyint(1) NULL DEFAULT NULL COMMENT '来源类型',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `purchase_order_id_UNIQUE`(`purchase_order_id`) USING BTREE,
  UNIQUE INDEX `purchase_order_code_UNIQUE`(`purchase_order_code`) USING BTREE,
  INDEX `ind_purchase_order_code`(`purchase_order_code`) USING BTREE,
  INDEX `ind_transport_center_code`(`transport_center_code`) USING BTREE,
  INDEX `ind_purchase_group_code`(`purchase_group_code`) USING BTREE,
  INDEX `ind_warehouse_code`(`warehouse_code`) USING BTREE,
  INDEX `ind_supplier_code`(`supplier_code`) USING BTREE,
  INDEX `ind_settlement_method_code`(`settlement_method_code`) USING BTREE,
  INDEX `ind_purchase_order_status`(`purchase_order_status`) USING BTREE,
  INDEX `ind_purchase_mode`(`purchase_mode`) USING BTREE,
  INDEX `ind_account_code`(`account_code`) USING BTREE,
  INDEX `ind_contract_code`(`contract_code`) USING BTREE,
  INDEX `ind_payment_code`(`payment_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66919 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for purchase_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order_detail`;
CREATE TABLE `purchase_order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `purchase_order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `purchase_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '采购单号',
  `spu_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu编码',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `product_spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `color_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色编码',
  `color_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色名称',
  `model_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `product_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型   0商品 1赠品 2实物返回',
  `purchase_whole` bigint(20) NULL DEFAULT 0 COMMENT '采购件数（整数）',
  `purchase_single` bigint(20) NULL DEFAULT NULL COMMENT '采购件数（零数）',
  `base_product_spec` bigint(20) NULL DEFAULT 0,
  `box_gauge` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购包装',
  `tax_rate` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '税率',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `factory_sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂商SKU编码',
  `tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税单价',
  `total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税总价',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际采购数量',
  `actual_total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实际含税总价',
  `use_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `purchase_order_detail_id_UNIQUE`(`purchase_order_detail_id`) USING BTREE,
  INDEX `ind_purchase_order_code`(`purchase_order_code`) USING BTREE,
  INDEX `ind_spu_code`(`spu_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_product_type`(`product_type`) USING BTREE,
  INDEX `ind_line_code`(`line_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111491 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购单商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for purchase_order_detail_batch
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order_detail_batch`;
CREATE TABLE `purchase_order_detail_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `purchase_order_detail_batch_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `purchase_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '采购单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次号',
  `product_date` datetime(0) NULL DEFAULT NULL COMMENT '生产日期',
  `batch_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `purchase_whole` bigint(20) NULL DEFAULT 0 COMMENT '采购件数（整数）',
  `purchase_single` bigint(20) NULL DEFAULT 0 COMMENT '采购件数（零数）',
  `base_product_spec` bigint(20) NULL DEFAULT 0 COMMENT '基商品含量',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际最小单位数量',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `purchase_order_detail_batch_id_UNIQUE`(`purchase_order_detail_batch_id`) USING BTREE,
  INDEX `ind_purchase_order_code`(`purchase_order_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_batch_code`(`batch_code`) USING BTREE,
  INDEX `ind_line_code`(`line_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 579 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购单商品批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `pay_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付流水',
  `order_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额',
  `pay_type` int(11) NULL DEFAULT NULL COMMENT '支付类型 1:付款  2:退款',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态 0:未完成 1:已完成',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9246 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reject_record
-- ----------------------------
DROP TABLE IF EXISTS `reject_record`;
CREATE TABLE `reject_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reject_record_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `reject_record_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退供单号',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `transport_center_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `purchase_group_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组编码',
  `purchase_group_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组名称',
  `settlement_method_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算方式编码',
  `settlement_method_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算方式名称',
  `reject_record_status` tinyint(1) NULL DEFAULT NULL COMMENT '退供单状态0.待确认1.完成 2.取消 ',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税金额',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际最小单数数量',
  `actual_total_tax_amount` decimal(45, 0) NULL DEFAULT 0 COMMENT '实际商品含税金额',
  `charge_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `contact_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_mobile` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `province_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省编码',
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省',
  `city_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市编码',
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市',
  `district_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县',
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县',
  `receive_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `pre_expect_time` datetime(0) NULL DEFAULT NULL COMMENT '预计发货时间',
  `valid_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期',
  `out_stock_time` datetime(0) NULL DEFAULT NULL COMMENT '出库时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发运时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `source_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源单号',
  `source_type` tinyint(1) NULL DEFAULT NULL COMMENT '来源类型',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `reject_record_id_UNIQUE`(`reject_record_id`) USING BTREE,
  UNIQUE INDEX `reject_record_code_UNIQUE`(`reject_record_code`) USING BTREE,
  INDEX `ind_reject_record_code`(`reject_record_code`) USING BTREE,
  INDEX `ind_transport_center_code`(`transport_center_code`) USING BTREE,
  INDEX `ind_warehouse_code`(`warehouse_code`) USING BTREE,
  INDEX `ind_company_code`(`company_code`) USING BTREE,
  INDEX `ind_supplier_code`(`supplier_code`) USING BTREE,
  INDEX `ind_settlement_method_code`(`settlement_method_code`) USING BTREE,
  INDEX `ind_reject_record_status`(`reject_record_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4721 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退供单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reject_record_batch
-- ----------------------------
DROP TABLE IF EXISTS `reject_record_batch`;
CREATE TABLE `reject_record_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reject_record_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退供号',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号/wms批次号',
  `batch_info_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编号',
  `sku_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编码',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SKU名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `batch_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `product_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产日期',
  `be_overdue_date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过期日期',
  `total_count` bigint(20) NULL DEFAULT NULL COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT NULL COMMENT '实际最小单位数量',
  `location_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库位号',
  `line_code` int(11) NULL DEFAULT NULL COMMENT '行号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_batch_code`(`batch_code`) USING BTREE,
  INDEX `index_location_code`(`location_code`(191)) USING BTREE,
  INDEX `index_batch_info_code`(`batch_info_code`(191)) USING BTREE,
  INDEX `index_line_code`(`line_code`) USING BTREE,
  INDEX `index_reject_record_code`(`reject_record_code`(191)) USING BTREE,
  INDEX `index_sku_code`(`sku_code`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退供批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reject_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `reject_record_detail`;
CREATE TABLE `reject_record_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id\n',
  `reject_record_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `reject_record_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退供单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `brand_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌编码',
  `brand_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `product_spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `color_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色编码',
  `color_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色名称',
  `model_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `product_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型   0商品 1赠品 2实物返回',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '商品数量',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `tax_rate` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '税率',
  `line_code` bigint(45) NULL DEFAULT NULL COMMENT '行号',
  `factory_sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂商SKU编码',
  `tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税单价',
  `total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品含税总价',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际最小单数数量',
  `actual_total_tax_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实际含税总价',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `reject_record_detail_id_UNIQUE`(`reject_record_detail_id`) USING BTREE,
  INDEX `ind_reject_record_code`(`reject_record_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_brand_code`(`brand_code`) USING BTREE,
  INDEX `ind_category_code`(`category_code`) USING BTREE,
  INDEX `ind_product_type`(`product_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 712 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退供商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reject_record_detail_batch
-- ----------------------------
DROP TABLE IF EXISTS `reject_record_detail_batch`;
CREATE TABLE `reject_record_detail_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `reject_record_detail_batch_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `reject_record_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退供单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'batch_code',
  `product_date` datetime(0) NULL DEFAULT NULL COMMENT '生产日期',
  `batch_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '商品数量',
  `total_count` bigint(20) NULL DEFAULT 0 COMMENT '最小单位数量',
  `actual_total_count` bigint(20) NULL DEFAULT 0 COMMENT '实际最小单数数量',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `use_status` tinyint(1) NULL DEFAULT NULL COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `reject_record_detail_batch_id_UNIQUE`(`reject_record_detail_batch_id`) USING BTREE,
  INDEX `ind_reject_record_code`(`reject_record_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_batch_code`(`batch_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退供商品批次信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for related_sales
-- ----------------------------
DROP TABLE IF EXISTS `related_sales`;
CREATE TABLE `related_sales`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `salse_category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售品类名称',
  `salse_category_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售品类ID',
  `first_sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优先推荐sku',
  `first_sku_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优先推荐sku名称',
  `secondly_sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其次推荐sku',
  `secondly_sku_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其次推荐sku名称',
  `last_sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最次推荐sku',
  `last_sku_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最次推荐sku名称',
  `status` tinyint(2) NULL DEFAULT 0 COMMENT '生效状态 0:生效 1：失效',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关联销售表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_area_return_situation
-- ----------------------------
DROP TABLE IF EXISTS `report_area_return_situation`;
CREATE TABLE `report_area_return_situation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `province_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省编码',
  `province_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省名称',
  `return_count` bigint(20) NULL DEFAULT NULL COMMENT '退货单数',
  `return_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '退货金额',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型 1:直送退货 2:质量退货 3:一般退货 4:采购直送',
  `reason_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货理由',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7667 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '售后管理--各地区退货情况统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_category_copartner_sale
-- ----------------------------
DROP TABLE IF EXISTS `report_category_copartner_sale`;
CREATE TABLE `report_category_copartner_sale`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域ID',
  `copartner_area_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域名称',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店ID',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `child_category_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子类编码',
  `child_category_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子类名称',
  `total_target` bigint(20) NULL DEFAULT 0 COMMENT '销售目标',
  `total_rate` bigint(20) NULL DEFAULT 0 COMMENT '销售目标达成率',
  `total_amount` bigint(20) NULL DEFAULT 0 COMMENT '销售数量',
  `total_price` bigint(20) NULL DEFAULT 0 COMMENT '含税销售金额',
  `price_rate` bigint(20) NULL DEFAULT 0 COMMENT '销售金额占比',
  `last_rate` bigint(20) NULL DEFAULT 0 COMMENT '上期',
  `same_rate` bigint(20) NULL DEFAULT 0 COMMENT '同比',
  `qoq_rate` bigint(20) NULL DEFAULT 0 COMMENT '环比',
  `report_year` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表年份',
  `report_month` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表月份',
  `report_subtotal_type` tinyint(1) NULL DEFAULT 1 COMMENT '小计类型 1:门店 2:经营区域 3:月份',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品类销售报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_category_goods
-- ----------------------------
DROP TABLE IF EXISTS `report_category_goods`;
CREATE TABLE `report_category_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级品类编码',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级品类名称',
  `amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额',
  `proportion` decimal(20, 4) NULL DEFAULT NULL COMMENT '比例',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型 1:直送退货 2:质量退货 3:一般退货',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 279023 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退货商品分类统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_copartner_sale
-- ----------------------------
DROP TABLE IF EXISTS `report_copartner_sale`;
CREATE TABLE `report_copartner_sale`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域ID',
  `copartner_area_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营区域名称',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店ID',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `total_target` bigint(20) NULL DEFAULT 0 COMMENT '总销售目标值',
  `total_finish` bigint(20) NULL DEFAULT 0 COMMENT '总销售实际完成',
  `eighteen_target` bigint(20) NULL DEFAULT 0 COMMENT '18SA销售目标值',
  `eighteen_finish` bigint(20) NULL DEFAULT 0 COMMENT '18SA销售实际完成',
  `key_target` bigint(20) NULL DEFAULT 0 COMMENT '重点品牌销售目标值',
  `key_finish` bigint(20) NULL DEFAULT 0 COMMENT '重点品牌销售实际值',
  `textile_target` bigint(20) NULL DEFAULT 0 COMMENT '服纺销售目标值',
  `textile_finish` bigint(20) NULL DEFAULT 0 COMMENT '服纺销售实际值',
  `report_year` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表年份',
  `report_month` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表月份',
  `report_subtotal_type` tinyint(1) NULL DEFAULT 1 COMMENT '小计类型 1:门店 2:经营区域 3:月份',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合伙人销售报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_order_store_detail
-- ----------------------------
DROP TABLE IF EXISTS `report_order_store_detail`;
CREATE TABLE `report_order_store_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'skuCode',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `store_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `brand_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名字',
  `num` bigint(20) NULL DEFAULT NULL COMMENT '数量',
  `amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额',
  `count_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 891 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店补货报表明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_store_goods
-- ----------------------------
DROP TABLE IF EXISTS `report_store_goods`;
CREATE TABLE `report_store_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `store_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `store_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `brand_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `num` bigint(20) NULL DEFAULT NULL COMMENT '数量',
  `amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '金额',
  `tong_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '同比',
  `chain_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '环比',
  `count_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计时间',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `total_num` bigint(20) NULL DEFAULT NULL COMMENT '总销量',
  `total_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '总额',
  `total_tong_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '总的同比',
  `total_chain_ratio` decimal(20, 4) NULL DEFAULT NULL COMMENT '总的环比',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 715 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店补货报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for return_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `return_order_detail`;
CREATE TABLE `return_order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `return_order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `return_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `picture_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `product_spec` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `color_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色编码',
  `color_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色名称',
  `model_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `base_product_spec` bigint(20) NULL DEFAULT 0 COMMENT 'base_product_spec',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `product_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型 0商品 1赠品 2兑换赠品',
  `zero_disassembly_coefficient` bigint(20) NULL DEFAULT 0 COMMENT '拆零系数',
  `activity_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动编码(多个，隔开）',
  `product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品单价',
  `total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '商品总价',
  `return_product_count` bigint(20) NULL DEFAULT 0 COMMENT '退货数量',
  `actual_return_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实退数量',
  `actual_total_product_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实退商品总价',
  `product_status` tinyint(4) NULL DEFAULT 0 COMMENT '商品状态0新品1残品',
  `tax_rate` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '税率',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `source_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源单号',
  `source_type` tinyint(1) NULL DEFAULT NULL COMMENT '来源单号',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '问题描述',
  `evidence_url` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多个凭证以逗号隔开',
  `preferential_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '均摊后单价',
  `product_category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌编码',
  `product_category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌名称',
  `bar_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `channel_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '渠道单价',
  `channel_total_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '渠道总价',
  `actual_channel_total_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实退渠道总价',
  `gift_line_code` bigint(20) NULL DEFAULT NULL COMMENT '赠品行号（赠品拆为多个的时候为-1）',
  `top_coupon_discount_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '仅A品优惠金额',
  `top_coupon_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '仅A品优惠单品金额',
  `complimentary_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退还赠品额度',
  `return_clothing_spinning` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '服纺券单品金额',
  `batch_info_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次编码【供应链唯一标识】',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号【展示字段】',
  `batch_date` datetime(0) NULL DEFAULT NULL COMMENT '批次时间',
  `warehouse_type_code` tinyint(1) NULL DEFAULT 1 COMMENT '传入库房编码:1:销售库，2:特卖库',
  `refund_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '退款金额',
  `withdraw_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '退供金额',
  `transport_center_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `quantity_returned_count` bigint(20) NULL DEFAULT NULL COMMENT '已退货数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `return_order_detail_id_UNIQUE`(`return_order_detail_id`) USING BTREE,
  INDEX `ind_return_order_code`(`return_order_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_produuct_type`(`product_type`) USING BTREE,
  INDEX `ind_source_code`(`source_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14499 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退货商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for return_order_detail_batch
-- ----------------------------
DROP TABLE IF EXISTS `return_order_detail_batch`;
CREATE TABLE `return_order_detail_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `return_order_detail_batch_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `return_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货单号',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `batch_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次号',
  `product_date` datetime(0) NULL DEFAULT NULL COMMENT '生产日期',
  `batch_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次备注',
  `unit_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位编码',
  `unit_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '数量',
  `actual_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实际数量',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `return_order_detail_batch_id_UNIQUE`(`return_order_detail_batch_id`) USING BTREE,
  INDEX `ind_return_order_code`(`return_order_code`) USING BTREE,
  INDEX `ind_sku_code`(`sku_code`) USING BTREE,
  INDEX `ind_batch_code`(`batch_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退货商品批次信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for return_order_info
-- ----------------------------
DROP TABLE IF EXISTS `return_order_info`;
CREATE TABLE `return_order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `return_order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务id',
  `return_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退货单号',
  `order_store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `supplier_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `transport_center_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `purchase_group_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组编码',
  `purchase_group_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组名称',
  `return_order_status` tinyint(1) NULL DEFAULT 0 COMMENT '退货单状态:1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消',
  `order_type` tinyint(1) NULL DEFAULT 0 COMMENT '订单类型 1直送 2配送 3货架',
  `return_order_type` tinyint(1) NULL DEFAULT NULL COMMENT '退货类型  0客户退货、1缺货退货、2售后退货、3冲减单 、4客户取消  5赠品划单',
  `customer_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编码',
  `customer_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `return_lock` tinyint(1) NULL DEFAULT 1 COMMENT '是否锁定  0是 1否',
  `return_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '锁定原因',
  `payment_status` tinyint(1) NULL DEFAULT 1 COMMENT '支付状态  0 已支付 1未支付',
  `payment_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式编码',
  `payment_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式名称',
  `treatment_method` tinyint(1) NULL DEFAULT 0 COMMENT '处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款 99--已取消',
  `logistics_company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司编码',
  `logistics_company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司名称',
  `logistics_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `pre_expect_time` datetime(0) NULL DEFAULT NULL COMMENT '预计发货时间',
  `transport_time` datetime(0) NULL DEFAULT NULL COMMENT '发运时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `product_count` bigint(20) NULL DEFAULT 0 COMMENT '商品数量',
  `return_order_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退货金额',
  `actual_product_count` bigint(20) NULL DEFAULT 0 COMMENT '实退商品数量',
  `actual_return_order_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实退商品总金额',
  `distribution_mode_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送方式编码',
  `distribution_mode_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送方式名称',
  `receive_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `receive_mobile` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `deliver_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '运费',
  `zip_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `total_weight` bigint(20) NULL DEFAULT 0 COMMENT '重量',
  `actual_total_weight` bigint(20) NULL DEFAULT 0 COMMENT '实际重量',
  `total_volume` bigint(20) NULL DEFAULT 0 COMMENT '体积',
  `actual_total_volume` bigint(20) NULL DEFAULT 0 COMMENT '实退体积',
  `province_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省编码',
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :省',
  `city_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市编码',
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :市',
  `district_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县编码',
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货区域 :区/县',
  `receive_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `valid_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期',
  `out_stock_time` datetime(0) NULL DEFAULT NULL COMMENT '出库时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `return_reason_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货原因编码',
  `return_reason_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货原因描述',
  `inspection_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验货备注',
  `return_discount_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退回优惠额度',
  `store_balance_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '门店余额',
  `store_discount_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '门店剩余优惠额',
  `store_credit_line` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店剩余授信额度',
  `discount_amount_infos` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退回优惠额度信息',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `nature_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店类型编码',
  `nature_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店类型名称',
  `review_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `review_operator` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `refund_status` tinyint(1) NULL DEFAULT 0 COMMENT '退款状态，0-未退款、1-已退款',
  `process_type` tinyint(1) NULL DEFAULT NULL COMMENT '处理方式，0-整单退、1-部分退',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `return_money_type` tinyint(4) NULL DEFAULT NULL COMMENT '退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户',
  `order_category` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '订单类别',
  `source_type` tinyint(4) NULL DEFAULT NULL COMMENT '来源类型',
  `order_success` tinyint(1) NULL DEFAULT 0 COMMENT '同步是否成功（创建退供单） 0 不生成采购单 1 待生成采购单 2采购单生成成功',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `create_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人编码',
  `create_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `update_by_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人编码',
  `update_by_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `review_operator_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人编码',
  `review_time` datetime(6) NULL DEFAULT NULL COMMENT '审核时间',
  `return_good_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退货金额',
  `actual_return_good_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '实际退货金额',
  `really_return` tinyint(4) NULL DEFAULT 0 COMMENT '是否真的发起退货 0:预生成退货单 1:原始订单全部发货完成生成退货单',
  `complimentary_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退还赠品额度',
  `total_zeng_amount` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '退积分金额',
  `top_coupon_discount_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT 'A品优惠总金额',
  `franchisee_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商编码',
  `franchisee_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加盟商名称',
  `business_form` tinyint(1) NULL DEFAULT NULL COMMENT '业务形式 0门店业务 1批发业务',
  `copartner_area_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人区域id',
  `copartner_area_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合伙人区域名称',
  `whether_invoice` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要发票 0-是 1-否',
  `order_product_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单产品类型 1.B2B 2.B2C',
  `business_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货类型 10.ERP退款 11.爱掌柜补货 12.冲减单',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `return_order_id_UNIQUE`(`return_order_id`) USING BTREE,
  UNIQUE INDEX `return_order_code_UNIQUE`(`return_order_code`) USING BTREE,
  INDEX `ind_return_order_code`(`return_order_code`) USING BTREE,
  INDEX `ind_company_code`(`company_code`) USING BTREE,
  INDEX `ind_supplier_code`(`supplier_code`) USING BTREE,
  INDEX `ind_transport_center_code`(`transport_center_code`) USING BTREE,
  INDEX `ind_warehouse_code`(`warehouse_code`) USING BTREE,
  INDEX `ind_purchase_group_code`(`purchase_group_code`) USING BTREE,
  INDEX `ind_return_order_status`(`return_order_status`) USING BTREE,
  INDEX `ind_order_type`(`order_type`) USING BTREE,
  INDEX `ind_return_order_type`(`return_order_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14152 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退货单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sequence_generator
-- ----------------------------
DROP TABLE IF EXISTS `sequence_generator`;
CREATE TABLE `sequence_generator`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sequence_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sequence_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sequence_val` bigint(10) NULL DEFAULT 1,
  `current_day` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sequence_step` smallint(6) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sequence_name`(`sequence_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生成订单号序列的表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_project_asset
-- ----------------------------
DROP TABLE IF EXISTS `service_project_asset`;
CREATE TABLE `service_project_asset`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `asset_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务商品资产id，会员每购买一个服务商品生成一个',
  `customer_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `customer_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `customer_phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户手机号',
  `customer_type` tinyint(1) NOT NULL COMMENT '用户类型，0为会员，1为非会员',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店编号',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `project_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务项目id',
  `project_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务商品编号',
  `project_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务项目名称',
  `type_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目类别id',
  `begin_time` datetime(0) NOT NULL COMMENT '有效期开始时间',
  `finish_time` datetime(0) NOT NULL COMMENT '有效期截止时间',
  `consumption_pattern` tinyint(1) NOT NULL COMMENT '消费方式，0为限次，1为不限次',
  `limit_count` int(11) NULL DEFAULT NULL COMMENT '限制总次数',
  `remain_count` int(11) NULL DEFAULT NULL COMMENT '剩余次数',
  `actual_amount` bigint(20) NOT NULL COMMENT '实际金额',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `cashier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员名称',
  `guide_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购id',
  `guide_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导购名称',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单编号',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `order_detail_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单明细id',
  `is_direct_custom` tinyint(1) NULL DEFAULT NULL COMMENT '是否直接消费，0为是，1为否',
  `pay_status` tinyint(1) NULL DEFAULT NULL COMMENT '支付状态，0为未支付，1为支付完成',
  `use_status` tinyint(1) NULL DEFAULT NULL COMMENT '资产状态，0为可用，1为不可用，2已退卡，3已过期',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6849 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Toc会员购买的服务项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_project_reduce_detail
-- ----------------------------
DROP TABLE IF EXISTS `service_project_reduce_detail`;
CREATE TABLE `service_project_reduce_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `asset_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务项目资产id',
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扣减id，相当于订单id',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `order_type` tinyint(1) NULL DEFAULT NULL COMMENT '类型，0为扣减,1为购买,2为退次，3赠次，4延期\n',
  `customer_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `customer_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `customer_phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `customer_type` tinyint(1) NOT NULL COMMENT '用户类型，0为会员，1为非会员',
  `store_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店id',
  `store_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店编号',
  `store_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门店名称',
  `project_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务项目id',
  `project_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务商品编号',
  `project_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务项目名称',
  `type_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目类型id',
  `reduce_count` int(11) NOT NULL COMMENT '扣减次数',
  `return_amount` bigint(20) NULL DEFAULT NULL COMMENT '金额',
  `remain_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '剩余次数',
  `cashier_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员id',
  `cashier_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收银员名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1590 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Toc会员购买的服务项目变更记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settlement_info
-- ----------------------------
DROP TABLE IF EXISTS `settlement_info`;
CREATE TABLE `settlement_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `settlement_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算信息ID',
  `product_sum` int(11) NOT NULL DEFAULT 0 COMMENT '商品合计=数量总和',
  `freight` int(11) NULL DEFAULT 0 COMMENT '运费',
  `total_coupons_discount` int(11) NULL DEFAULT 0 COMMENT '优惠券总优惠金额',
  `point_percentage` int(11) NULL DEFAULT 0 COMMENT '积分折扣',
  `activity_discount` int(11) NULL DEFAULT 0 COMMENT '活动优惠',
  `order_sum` int(11) NOT NULL DEFAULT 0 COMMENT '订单合计=订单没有折扣的原始金额',
  `order_receivable` int(11) NOT NULL DEFAULT 0 COMMENT '订单应收=折扣钱的金额',
  `order_actual` int(11) NOT NULL DEFAULT 0 COMMENT '订单实收=入账金额',
  `order_change` int(11) NULL DEFAULT 0 COMMENT '订单找零',
  `order_abandon` int(11) NULL DEFAULT 0 COMMENT '订单舍零',
  `points` int(11) NULL DEFAULT 0 COMMENT '使用积分数',
  `point_discount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '积分抵扣金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改员',
  `lucky_sum` int(11) NULL DEFAULT 0 COMMENT '第N件特价金额',
  `full_sum` int(11) NULL DEFAULT 0 COMMENT '满减金额',
  `shop_order_preferential` int(11) NULL DEFAULT 0 COMMENT '门店整单优惠',
  `receive_points` bigint(20) NULL DEFAULT 0 COMMENT '获取的积分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6867 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_lock_details
-- ----------------------------
DROP TABLE IF EXISTS `store_lock_details`;
CREATE TABLE `store_lock_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `company_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `transport_center_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `transport_center_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房编码',
  `warehouse_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房名称',
  `warehouse_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库房类型',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编码',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `line_code` bigint(20) NULL DEFAULT NULL COMMENT '行号',
  `change_count` int(11) NULL DEFAULT NULL COMMENT '锁定库存数',
  `purchase_group_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组编码',
  `purchase_group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购组名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '锁库明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_resource
-- ----------------------------
DROP TABLE IF EXISTS `system_resource`;
CREATE TABLE `system_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_link` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单链接',
  `resource_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `resource_show_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单显示名称',
  `resource_level` int(11) NULL DEFAULT NULL COMMENT '菜单等级',
  `resource_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `resource_icon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `resource_status` tinyint(1) NULL DEFAULT 0 COMMENT '0启用1禁用',
  `parent_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `system_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属系统',
  `plan_mark` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步批次标识',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `memu_id_UNIQUE`(`resource_code`) USING BTREE,
  INDEX `system_code_idx`(`system_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8859 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wholesale_customers
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_customers`;
CREATE TABLE `wholesale_customers`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批发客户编码（唯一标识）',
  `customer_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批发客户名称',
  `contact_person` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone_number` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `identity_number` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `province_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省编码',
  `province_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市编码',
  `city_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市名称',
  `district_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区/县编码',
  `district_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区/县',
  `street_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '街道地址',
  `company_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `use_status` tinyint(1) NULL DEFAULT 0 COMMENT '0. 启用   1.禁用',
  `affiliated_company` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属合伙公司',
  `invite_people_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓客人员编码',
  `invite_people_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓客人员名称',
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `customer_account` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批发客户账号',
  `category_tag_list` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回显品类name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发客户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wholesale_rule
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_rule`;
CREATE TABLE `wholesale_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批发客户编码（唯一标识）',
  `warehouse_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库code',
  `warehouse_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `sku_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku编号',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `product_category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类编码',
  `product_category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类名称',
  `product_brand_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌编码',
  `product_brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌名称',
  `type` int(1) NULL DEFAULT NULL COMMENT '规则类型：1.仓库  2.品牌  3品类  4 单品',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 500 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发客户商品权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for getSequenceNextVal
-- ----------------------------
DROP PROCEDURE IF EXISTS `getSequenceNextVal`;
delimiter ;;
CREATE PROCEDURE `getSequenceNextVal`(sequenceName varchar(100))
  COMMENT '获取序列值'
BEGIN
  DECLARE curVal bigint(10);
  DECLARE seqCurday varchar(8);
  DECLARE realCurday varchar(8);
  set seqCurday = (select current_day from sequence_generator where sequence_name = sequenceName);
  set realCurday = substr(date_format(sysdate(), '%Y%m%D'), 1, 8);
  if seqCurday != realCurday
  then
    update sequence_generator
    set current_day  = realCurday,
        sequence_val = 1
    where sequence_name = sequenceName;
  end if;
  commit;
  set curVal = ifnull((select sequence_val from sequence_generator where sequence_name = sequenceName), 1);
  update sequence_generator
  set sequence_val = (curVal + ifnull(sequence_step, 1))
  where sequence_name = sequenceName;
  commit;
  select curVal;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
