package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;



@AppComponentsContainerConfig(order = 1)
public class AppConfig1 {

    static final Logger log = LoggerFactory.getLogger(AppConfig1.class);

    public AppConfig1() {
        log.debug("Config created");
    }

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        log.debug("Creating  EquationPreparer");
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        log.debug("Creating  PlayerService");
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        log.debug("Creating  GameProcessor");
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }


}
