package my.testapp.model;

import java.io.Serializable;

public class Person implements Serializable {
    public String name,
    sname,
    gender,
    job;
    public int age;

    public Person(String name, String sname, String gender, int age, String job)
    {
        this.age=age;
        this.name=name;
        this.sname=sname;
        this.gender=gender;
        this.job=job;
    }
}
