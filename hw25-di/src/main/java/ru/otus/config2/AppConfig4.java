package ru.otus.config2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceStreams;

@AppComponentsContainerConfig(order = 2)
public class AppConfig4 {

    static final Logger log = LoggerFactory.getLogger(AppConfig4.class);

    public AppConfig4(AppConfig3 config3) {
        log.debug("Config created");
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        log.debug("Creating  IOService");
        return new IOServiceStreams(System.out, System.in);
    }

}
