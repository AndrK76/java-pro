package ru.otus.andrk.hw04;


import java.util.AbstractMap;
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

    private Map.Entry<Customer, String> getCloneEntry(Map.Entry<Customer, String> item) {
        if (item == null) return null;
        return new AbstractMap.SimpleEntry<>(item.getKey().clone(), item.getValue());
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
