package org.rb.objectserialization.model;

import java.util.UUID;

public class Person implements IObjectSerializable {

    private UUID id;
    private String name;
    private String address;
    private int age;

    public Person() {
    }

    public Person(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Method for packing as CSV string.
     * Useful to pass object from activity to other activity/dialogFragment.
     * Other way is to implement Parcelable interface.
     * For large object we can use external Json library to stringify object.
     * @return csv string
     */
    public String csv(){

        return id+","+name+","+address+","+age;
    }

    public static Person fromCSV(String personCSV){
        Person person = new Person();
        person.copyCSVFields(personCSV);
        return person;
    }
    public void copyCSVFields(String personCSV){
        String[] fields = personCSV.split(",");
        this.id = UUID.fromString(fields[0]);
        this.name = fields[1];
        this.address = fields[2];
        this.age =Integer.parseInt(fields[3]);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
