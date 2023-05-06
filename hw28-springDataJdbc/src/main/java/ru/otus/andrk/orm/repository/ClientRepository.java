package ru.otus.andrk.orm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.andrk.domain.model.Client;


public interface ClientRepository extends CrudRepository<Client, Long> {

    //@Query("select c.* from client c left join address a on c.address_id =a.id")
    //Iterable<Client> findAll();
}
