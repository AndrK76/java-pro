package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    static final Logger log = LoggerFactory.getLogger(AppConfig2.class);

    public AppConfig2(AppConfig1 config1) {
        log.debug("Config created");
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        log.debug("Creating  IOService");
        return new IOServiceStreams(System.out, System.in);
    }

}
