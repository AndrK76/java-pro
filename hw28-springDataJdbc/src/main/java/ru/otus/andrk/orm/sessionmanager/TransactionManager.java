package ru.otus.andrk.orm.sessionmanager;

import ru.otus.andrk.orm.sessionmanager.TransactionAction;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
