package ru.otus.andrk.hibernate.sessionmanager;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
    <T> T doInReadOnlyTransaction(TransactionAction<T> action);
}
