-- 帖子内容表
create
    table
        if not exists post_content(
            pub_user_account_id bigint,
            type varchar(32) not null,
            title varchar(255) not null,
            content text not null,
            status varchar(32) not null
        );

select
    add_base_struct('post_content');

-- 审核表
create
    table
        if not exists audit(
            post_content_id bigint not null references post_content(id) on
            delete
                cascade,
                auditor_user_account_id bigint not null references user_account(id) on
                delete
                    cascade,
                    status varchar(32) not null,
                    reason varchar(255)
        );

select
    add_base_struct('audit');

-- 消息表
create
    table
        if not exists message(
            from_user_account_id bigint references user_account(id) on
            delete
            set
                null,
                to_user_account_id bigint references user_account(id) on
                delete
                set
                    null,
                    post_content_id bigint references post_content(id) on
                    delete
                    set
                        null,
                        content text not null,
                        read boolean not null default false
        );

select
    add_base_struct('message');
