package org.rb.objectserialization;

import org.junit.Test;
import org.rb.objectserialization.model.Person;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PersonUnitTest {

    @Test
    public void testCSV() {

        Person person = new Person("Name_1", "Address_1", 22);
        person.setId(UUID.randomUUID());
       String personCSV = person.csv();
        Person person_1 = Person.fromCSV(personCSV);
        assertTrue(person_1.toString().equals(person.toString()));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        Person person = new Person("Name_1", "Address_1", 22);
        person.setId(UUID.randomUUID());
        Person person_1 = (Person) person.clone();
        System.out.println("> " +person_1);
        assertTrue(person_1.toString().equals(person.toString()));
        person_1.setId(UUID.randomUUID());
        person_1.setName("Name_2");
        System.out.println("person: "+person);
        System.out.println("person_1: "+person_1);
    }

}