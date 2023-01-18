package ru.otus.andrk.hw04;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class CustomerService {
    static class CustomerScoresComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return Long.signum(o1.getScores() - o2.getScores());
        }
    }

    final TreeMap<Customer, String> map = new TreeMap<>(new CustomerScoresComparator());

    //TODO: Как вариант была идея реализовать свой класс реализующий Map.Entry и в нём
    //возвращать копию Customer, но кода писать больше
    private Map.Entry<Customer, String> getCloneEntry(Map.Entry<Customer, String> item) {
        if (item == null) return null;
        var ret = new TreeMap<Customer, String>(new CustomerScoresComparator());
        ret.put(item.getKey().clone(), item.getValue());
        return ret.firstEntry();
    }

    public Map.Entry<Customer, String> getSmallest() {
        return getCloneEntry(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getCloneEntry(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
