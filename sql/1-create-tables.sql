create table item (id bigint not null, name varchar(255), price integer not null, stock integer not null, primary key (id)) engine=InnoDB;
create table item_seq (next_val bigint) engine=InnoDB;
insert into item_seq values ( 1 );

create table customer (id bigint not null,
country varchar(255), postal_address varchar(255),
street varchar(255), zip_code varchar(255),
email varchar(255), full_name varchar(255),
ssn varchar(255), primary key (id)) engine=InnoDB;
create table customer_seq (next_val bigint) engine=InnoDB;
insert into customer_seq values ( 1 );
create table customer_wishlist (customer_id bigint not null, itemid bigint) engine=InnoDB;