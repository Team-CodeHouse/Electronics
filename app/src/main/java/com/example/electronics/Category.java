package com.example.electronics;

public class Category {

    public static final int Communications = 1;
    public static final int Control_Systems = 2;
    public static final int Digital_Electronics = 3;
    public static final int Select_All_Categories = 4;


    private int id;
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
