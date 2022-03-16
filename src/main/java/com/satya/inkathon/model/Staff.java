package com.satya.inkathon.model;

import java.util.ArrayList;
import java.util.List;

public class Staff {
    private String name;
    private int age;
    private List<String> skills = new ArrayList<String>();
    private int salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Staff [name = " + name + ", skills = " + skills + ", salary = " + salary
                + ", age = " + age + "]";
    }
}
