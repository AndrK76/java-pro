package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        ///
        var client3 = dbServiceClient.saveClient(new Client("dbService3",
                new Address("улица"), null));
        var client3Selected = dbServiceClient.getClient(client3.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client3.getId()));
        log.info("client3Selected:{}", client3Selected);
        //dbServiceClient.saveClient(new Client(client3Selected.getId(), "dbService3Updated"));
        dbServiceClient.saveClient(new Client(client3Selected.getId(), "dbService3Updated",
                client3Selected.getAddress(), client3Selected.getPhones()));
        clientUpdated = dbServiceClient.getClient(client3Selected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client3Selected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        ///
        var client4 = dbServiceClient.saveClient(new Client("dbService4", null,
                List.of(new Phone("+7-12323"), new Phone("+7-91866"))));
        var client4Selected = dbServiceClient.getClient(client4.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client4.getId()));
        log.info("client4Selected:{}", client4Selected);
        dbServiceClient.saveClient(new Client(client4Selected.getId(), "dbService4Updated",
                client4Selected.getAddress(), client4Selected.getPhones()));
        clientUpdated = dbServiceClient.getClient(client4Selected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client4Selected.getId()));
        log.info("clientUpdated:{}", clientUpdated);


        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
