package ru.otus.andrk.orm.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.andrk.domain.model.Client;


public interface ClientRepository extends CrudRepository<Client, Long> {
//    TODO:(!!!) Выбрать данные когда в таблице клиентов хранится ссылка на адреса
//      получилось, но нужно переписывать save и аннотацией @Query не получится?
//    @Query(value = """
//        SELECT c.client_id, c.name, a.address_id AS address_address_id, a.street AS address_street
//            FROM client as c LEFT OUTER JOIN address a ON a.address_id = c.address_id
//        """)
//    Iterable<Client> findAll();
}
