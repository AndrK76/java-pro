create sequence address_seq start with 1 increment by 1;
create table address
(
    id   bigint not null primary key,
    street varchar(100)
);

create sequence phone_seq start with 1 increment by 1;
create table phone
(
    id   bigint not null primary key,
    number varchar(50)
);
