package ru.otus.andrk.orm.sessionmanager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.orm.sessionmanager.TransactionAction;
import ru.otus.andrk.orm.sessionmanager.TransactionManager;

@Component
public class TransactionManagerSpring implements TransactionManager {


    @Override
    @Transactional
    public <T> T doInTransaction(TransactionAction<T> action) {
        return action.get();
    }
}
