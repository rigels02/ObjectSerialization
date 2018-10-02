package org.rb.objectserialization.model;

import java.util.UUID;

public class PersonListViewModel {

    private UUID id;
    private String name;
    private String address;



    public PersonListViewModel(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.address = person.getAddress();
    }

    public UUID getId() {
        return id;
    }

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

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                ", address='" + address + '\'' ;
    }
}
