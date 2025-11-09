create table orders
(
    id          bigint auto_increment
        primary key,
    customer_id bigint                     not null,
    status      varchar(20)                not null,
    created_at  datetime default CURRENT_TIMESTAMP not null,
    total_price double(10, 2)              not null
);

