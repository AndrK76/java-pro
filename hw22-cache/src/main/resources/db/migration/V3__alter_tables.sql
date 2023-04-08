alter table client add column address_id bigint;

alter table client add constraint client_address_fk
    foreign key (address_id) references address(id);

alter table phone add column client_id bigint;


alter table phone add constraint phone_client_fk
    foreign key (client_id) references client(id);
