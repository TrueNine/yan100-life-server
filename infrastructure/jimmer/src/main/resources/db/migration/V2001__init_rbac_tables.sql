-- RBAC 权限表
create
    table
        if not exists permission(
            code varchar(128) not null unique,
            name varchar(128) not null,
            description varchar(255)
        );

select
    add_base_struct('permission');

-- RBAC 角色表
create
    table
        if not exists role(
            code varchar(128) not null unique,
            name varchar(128) not null,
            description varchar(255)
        );

select
    add_base_struct('role');

-- RBAC 角色组表
create
    table
        if not exists role_group(
            code varchar(128) not null unique,
            name varchar(128) not null,
            description varchar(255)
        );

select
    add_base_struct('role_group');

-- 用户账号表（如未建，补充基础结构）
create
    table
        if not exists user_account(
            phone varchar(32),
            avatar_url varchar(255),
            wechat_wxpa_open_id varchar(128),
            account varchar(128) not null unique,
            password_enc varchar(255) not null,
            nick_name varchar(128) not null
        );

select
    add_base_struct('user_account');

-- 角色-权限多对多中间表
create
    table
        if not exists role_permission(
            role_id bigint not null references role(id) on
            delete
                cascade,
                permission_id bigint not null references permission(id) on
                delete
                    cascade
        );

-- 角色组-角色多对多中间表
create
    table
        if not exists role_group_role(
            role_group_id bigint not null references role_group(id) on
            delete
                cascade,
                role_id bigint not null references role(id) on
                delete
                    cascade
        );

-- 用户账号-角色组多对多中间表
create
    table
        if not exists user_account_role_group(
            user_account_id bigint not null references user_account(id) on
            delete
                cascade,
                role_group_id bigint not null references role_group(id) on
                delete
                    cascade
        );
