package ru.otus.andrk;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class HelloOtus {
    public static void main(String... args) {
        exWithTable();
    }

    private static void exWithTable() {
        System.out.println("***Эксперимент с com.google.common.collect.Table***");
        List<String> rowQuarterIncome
                = Lists.newArrayList("Иванов", "Петров");
        List<String> columnQuarterIncome
                = Lists.newArrayList("I", "II", "III", "IV");
        Table<String, String, BigDecimal> tableQuarterIncome
                = ArrayTable.create(rowQuarterIncome, columnQuarterIncome);
        tableQuarterIncome.put("Иванов", "I", BigDecimal.valueOf(456_568.85));
        tableQuarterIncome.put("Иванов", "III", BigDecimal.valueOf(422_456.50));
        tableQuarterIncome.put("Иванов", "IV", BigDecimal.valueOf(500_236.21));
        tableQuarterIncome.put("Петров", "I", BigDecimal.valueOf(656_236.45));
        tableQuarterIncome.put("Петров", "II", BigDecimal.valueOf(550_23.02));
        tableQuarterIncome.put("Петров", "III", BigDecimal.valueOf(99_236.12));
        System.out.printf("Содержимое таблицы:%n\t%s%n", tableQuarterIncome.toString());

        System.out.println("Доходы во 2 квартале:");
        Map<String, BigDecimal> secondQuarterIncome = tableQuarterIncome.column("II");
        for (var person : secondQuarterIncome.keySet()) {
            System.out.printf("\t%s\t%10s%n", person, secondQuarterIncome.get(person) );
        }
    }
}
