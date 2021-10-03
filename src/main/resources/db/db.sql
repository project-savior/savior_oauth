
# 1.第三方平台表
drop table if exists `ums_third_platform`;
create table `ums_third_platform`
(
    id                 bigint(20)   not null auto_increment comment '自增id',
    logo               varchar(100) not null comment '第三方平台的logo',
    platform_name      varchar(50)  not null comment '平台类型名，建议英文',
    platform_login_url varchar(197) not null comment '平台登录地址',
    params             varchar(197) not null default '' comment '登录参数',
    handler            varchar(197) not null comment '处理器全限定类名',
    doc_url            varchar(197) not null comment '第三方平台官方文档地址',
    status             int(1)       not null default 1 comment '平台接入状态，0->不可用；1->可用',
    create_time        datetime     not null comment '接入时间',
    primary key (id),
    unique key `uni_login_url` (platform_login_url)
) engine = innodb
  default charset = utf8mb4 comment ='第三方平台信息表';

# 2.用户表
drop table if exists `ums_user`;
create table `ums_user`
(
    `id`            bigint(20) not null auto_increment,
    `username`      varchar(64) comment '用户名',
    `password`      varchar(64) comment '密码',
    `nickname`      varchar(64) comment '昵称',
    `phone`         varchar(64) comment '手机号码',
    `status`        int(1)       default 1 comment '帐号启用状态:0->禁用；1->启用',
    `name_verified` int(2)       default 0 comment '是否实名认证: 0->否；1->是；2->已过期',
    `email`         varchar(197) comment '邮箱',
    `real_name`     varchar(64) comment '真实姓名',
    `id_card`       varchar(30) comment '身份证号码',
    `student_id`    varchar(50) comment '学号字符串',
    `avatar`        varchar(197) comment '头像',
    `gender`        int(1) comment '性别：0->未知；1->男；2->女',
    `birthday`      date comment '生日',
    `city`          varchar(64)  default '未知' comment '所在城市',
    `motto`         varchar(197) default '这个人很懒，还没有个性签名' comment '个性签名',
    `exp`           int(11) comment '成就值',
    `create_time`   datetime   not null comment '注册时间',
    `update_time`   datetime comment '更新资料时间',
    primary key (`id`),
    unique key `uni_username` (`username`),
    unique key `uni_phone` (`phone`)
) engine = innodb
  default charset = utf8mb4 comment = '用户表';

# 3.用户-第三方平台绑定关系表
drop table if exists `ums_user_platform_relation`;
create table `ums_user_platform_relation`
(
    id          bigint(20) not null auto_increment comment '自增id',
    user_id     bigint(20) not null comment '用户id',
    platform_id bigint(20) not null comment '第三方平台id',
    status      int(1)     not null default 1 comment '绑定状态，0->已删除；1->已绑定',
    sort        int(10)             default 0 comment '排序规则',
    create_time datetime   not null comment '绑定时间',
    update_time datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='用户-第三方平台绑定关系';

# 5.权限表
drop table if exists `ums_permission`;
create table `ums_permission`
(
    id              bigint(20)  not null auto_increment comment '自增id',
    permission_name varchar(50) not null comment '权限名',
    description     varchar(197) default '' comment '描述',
    status          int(1)       default 1 comment '权限状态; 0-->异常，1-->正常',
    sort            int(10)      default 0 comment '排序规则',
    create_time     datetime    not null comment '创建时间',
    update_time     datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='权限表';

# 6.权限命名空间表
drop table if exists `ums_permission_namespace`;
create table `ums_permission_namespace`
(
    id                  bigint(20)  not null auto_increment comment '自增id',
    namespace           varchar(50) not null comment '命名空间',
    parent_namespace_id bigint(20) comment '父命名空间ID',
    description         varchar(197)         default '' comment '描述',
    status              int(1)      not null default 1 comment '状态，0->异常；1->正常',
    sort                int(10)              default 0 comment '排序规则',
    create_time         datetime    not null comment '绑定时间',
    update_time         datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='权限命名空间表';


# 7.命名空间-权限关系表
drop table if exists `ums_namespace_permission_relation`;
create table `ums_namespace_permission_relation`
(
    id            bigint(20) not null auto_increment comment '自增id',
    namespace_id  bigint(20) not null comment '命名空间ID',
    permission_id bigint(20) not null comment '权限ID',
    status        int(1)     not null default 1 comment '状态，0->异常；1->正常',
    sort          int(10)             default 0 comment '排序规则',
    create_time   datetime   not null comment '绑定时间',
    update_time   datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='命名空间-权限关系表';

# 8.api表
drop table if exists `ums_api`;
create table `ums_api`
(
    id            bigint(20)   not null auto_increment comment '自增id',
    name          varchar(50)  not null comment '资源名称',
    url           varchar(197) not null comment '资源路径',
    method        varchar(20)  not null comment '请求方式',
    permission_id bigint(20) comment '权限ID',
    status        int(1)       default 1 comment 'api状态；0-->异常，1-->正常',
    description   varchar(197) default '' comment '资源描述',
    sort          int(10)      default 0 comment '排序规则',
    create_time   datetime     not null comment '创建时间',
    update_time   datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='资源表';


# 9.组件表
drop table if exists `ums_component`;
create table `ums_component`
(
    id             bigint(20)  not null auto_increment comment '自增id',
    component_name varchar(50) not null comment '组件识别名',
    page           varchar(50) default '' comment '组件所属页面',
    permission_id  bigint(20) comment '权限ID',
    description    varchar(197) comment '描述',
    status         int(1)      default 1 comment '组件状态；0-->异常，1-->正常',
    sort           int(10)     default 0 comment '排序规则',
    create_time    datetime    not null comment '创建时间',
    update_time    datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='组件表';


# 10.用户-权限表
drop table if exists `ums_user_permission_relation`;
create table `ums_user_permission_relation`
(
    id            bigint(20) not null auto_increment comment '自增id',
    user_id       bigint(20) not null comment '用户ID',
    permission_id bigint(20) not null comment '权限ID',
    status        int(1)  default 1 comment '关系状态；0-->异常，1-->正常',
    sort          int(10) default 0 comment '排序规则',
    create_time   datetime   not null comment '创建时间',
    update_time   datetime comment '更改时间',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='用户-权限关系';











