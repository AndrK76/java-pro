package ru.otus.andrk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.cachehw.HwCache;
import ru.otus.andrk.cachehw.MyCache;
import ru.otus.andrk.service.DBServiceClientWithCacheImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final int SAMPLE_SIZE = 500;

    public static void main(String[] args) throws InterruptedException {
        new Main().run();
    }

    private void run() throws InterruptedException {
        var sessionFactory = getSessionFactory();
        var dbServiceClientNoCache = getDbServiceClient(sessionFactory);

        HwCache<String,Client> hwCache = new MyCache<>();
        var dbServiceClientWithCache = new DBServiceClientWithCacheImpl(dbServiceClientNoCache,hwCache);

        log.info("******    Start   work ******");
        log.info("Start init data");
        initData(dbServiceClientNoCache);
        log.info("Start get data via without cache service");
        log.info("end get data, time={} ms",getTimeForGetData(dbServiceClientNoCache));
        log.info("Start get data via cache service without cache (cache size={})",hwCache.getCacheSize());
        log.info("end get data, time={} ms",getTimeForGetData(dbServiceClientWithCache));
        log.info("Start get1 data via cache service with cache (cache size={})",hwCache.getCacheSize());
        log.info("end get data, time={} ms",getTimeForGetData(dbServiceClientWithCache));
        log.info("Start get2 data via cache service with cache (cache size={})",hwCache.getCacheSize());
        log.info("end get data, time={} ms",getTimeForGetData(dbServiceClientWithCache));

        log.info("Call GC");
        System.gc();
        Thread.sleep(1000);

        log.info("Start get data via cache service after clear cache (cache size={})",hwCache.getCacheSize());
        log.info("end get data, time={} ms",getTimeForGetData(dbServiceClientWithCache));

    }


    private SessionFactory getSessionFactory() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        return HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
    }


    private DBServiceClient getDbServiceClient(SessionFactory sessionFactory) {
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        //в DbServiceClientImpl.getClient() добавлено для "чистоты эксперимента" - session.clear()
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private void initData(DBServiceClient dbServiceClient) {
        for (int i = 1; i <= SAMPLE_SIZE; i++) {
            dbServiceClient.saveClient(new Client("client num " + (i + 1)));
        }
    }

    private long getTimeForGetData(DBServiceClient dbServiceClient){
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= SAMPLE_SIZE; i++) {
            var client = dbServiceClient.getClient(i);
        }
        long endTime = System.currentTimeMillis();
        return endTime-startTime;
    }


}