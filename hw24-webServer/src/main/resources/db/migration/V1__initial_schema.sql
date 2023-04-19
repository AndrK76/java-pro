
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

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

alter table client add column address_id bigint;

alter table client add constraint client_address_fk
    foreign key (address_id) references address(id);

alter table phone add column client_id bigint;


alter table phone add constraint phone_client_fk
    foreign key (client_id) references client(id);
