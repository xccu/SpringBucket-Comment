create table t_coffee (
    id bigint not null auto_increment, --设置自增
    name varchar(255),
    price bigint not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)       --设置主键
);