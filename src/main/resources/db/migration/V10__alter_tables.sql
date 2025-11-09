alter table orders
    add constraint orders_users_id_fk
        foreign key (customer_id) references users (id);

