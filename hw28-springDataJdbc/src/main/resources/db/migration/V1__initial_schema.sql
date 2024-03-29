
--create sequence client_SEQ start with 1 increment by 1;

create table client
(
    client_id   bigserial not null primary key,
    name varchar(50)
);

--create sequence address_seq start with 1 increment by 1;
create table address
(
    address_id   bigserial not null primary key,
    street varchar(100)
);

--create sequence phone_seq start with 1 increment by 1;
create table phone
(
    phone_id   bigserial not null primary key,
    number varchar(50)
);

/*
TODO:(!!!)Для не получившегося варианта с хранением ссылки на адрес в клиенте
alter table client add column address_id bigint;

alter table client add constraint client_address_fk
    foreign key (address_id) references address(address_id);
*/
--/*
alter table address add column client_id bigint;

alter table address add constraint address_client_fk
    foreign key (client_id) references client(client_id);
--*/



alter table phone add column client_id bigint;

alter table phone add constraint phone_client_fk
    foreign key (client_id) references client(client_id);


--/*
--TODO:(!!!)Стартовая инициализация
insert into client(name) values ('Клиент 1');
insert into address(street,client_id) values ('Улица для 1-го клиента', 1);
insert into phone (number,client_id) values ('123-456',1);

insert into client(name) values ('Клиент 2');
insert into address(street,client_id) values ('Улица для 2-го клиента', 2);

insert into client(name) values ('Клиент 3');
insert into phone (number,client_id) values ('345-678',3);
insert into phone (number,client_id) values ('этот номер в web-интерфейсе не увидим',3);
--*/

/*
--Стартовая инициализация
insert into address(street) values ('Улица для 1-го клиента');
insert into client(name,address_id) values ('Клиент 1',1);
insert into phone (number,client_id) values ('123-456',1);

insert into address(street) values ('Улица для 2-го клиента');
insert into client(name,address_id) values ('Клиент 2', 2);

insert into client(name) values ('Клиент 3');
insert into phone (number,client_id) values ('345-678',3);
*/