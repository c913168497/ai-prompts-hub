CREATE TABLE `final_prompt` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `project_id` bigint(20) NOT NULL,
                                `name` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `created_at` datetime NOT NULL,
                                `updated_at` datetime NOT NULL,
                                `uuid_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `final_prompt_item` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `final_prompt_id` bigint(20) NOT NULL,
                                     `prompt_id` bigint(20) NOT NULL,
                                     `created_at` datetime NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `idx_final_prompt_id` (`final_prompt_id`),
                                     KEY `idx_prompt_id` (`prompt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `final_prompt_skeleton` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `final_prompt_id` bigint(20) NOT NULL,
                                         `category_id` bigint(20) NOT NULL,
                                         `sort_order` int(11) NOT NULL,
                                         `created_at` datetime NOT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `global_category` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `name` varchar(100) NOT NULL COMMENT '分类名称',
                                   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COMMENT='全局分类定义表';


CREATE TABLE `project` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `name` varchar(200) NOT NULL COMMENT '项目名称',
                           `git_name` varchar(255) DEFAULT NULL,
                           `git_repo` varchar(500) DEFAULT NULL COMMENT 'Git 仓库地址',
                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_git` (`git_repo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='项目表';


CREATE TABLE `project_skeleton` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
                                    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
                                    `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序序号',
                                    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`id`),
                                    KEY `FK79y4kwovcemif3xv8mtw7rt7` (`category_id`),
                                    KEY `FKfmdppbiu0qcgem25v55bk709i` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COMMENT='项目骨架配置表';


CREATE TABLE `prompt` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `project_id` bigint(20) NOT NULL COMMENT '所属项目ID',
                          `category_id` bigint(20) NOT NULL COMMENT '所属分类ID',
                          `title` varchar(200) NOT NULL COMMENT '提示词标题',
                          `content` text NOT NULL COMMENT '提示词内容',
                          `is_selected` tinyint(1) DEFAULT '0' COMMENT '是否被选中',
                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `FK914jc4sqbfgrwamjp67bqsltn` (`category_id`),
                          KEY `FK596hug6yhk8qd0ba6pqvpgdj6` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='提示词表';