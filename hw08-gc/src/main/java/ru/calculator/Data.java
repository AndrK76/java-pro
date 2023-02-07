package ru.calculator;

public class Data {

    private final static Data instance = new Data(0);

    private int value;

    public Data(int value) {
        this.value = value;
    }

    //Понимаю что при многопоточном выполнении так нельзя
    public static Data create(int value){
        instance.value = value;
        return instance;
    }

    public int getValue() {
        return value;
    }
}
