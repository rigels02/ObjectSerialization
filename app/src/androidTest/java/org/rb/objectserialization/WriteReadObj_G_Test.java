package org.rb.objectserialization;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rb.objectserialization.model.IListViewModel;
import org.rb.objectserialization.model.Person;
import org.rb.objectserialization.model.PersonListViewModel;
import org.rb.objectserialization.service.WriteReadObj_G;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class WriteReadObj_G_Test {



    IListViewModel<PersonListViewModel, Person> listViewModel =
            new IListViewModel<PersonListViewModel, Person>() {
                @Override
                public List<PersonListViewModel> viewModel(List<Person> items) {
                    ArrayList<PersonListViewModel> viewPersons = new ArrayList<PersonListViewModel>();
                    for(Person elm: items){
                        PersonListViewModel pview = new PersonListViewModel(elm);
                        viewPersons.add(pview);
                    }
                    return viewPersons;
                }
            };

    @SuppressWarnings("unchecked")
    @Test
    public void test_writeObj() throws IOException, ClassNotFoundException {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        WriteReadObj_G<Person> service = WriteReadObj_G.instanciate(appContext);
        service.removeAll();
        service.add(new Person("Name_1","Address_1",11));
        Person person2;
        service.add(person2 = new Person("Name_2","Address_2",22));
        service.add(new Person("Name_3","Address_3",33));
        List<Person> persons = (List<Person>) service.findAll();
        assertTrue(persons.size() == 3);
        assertTrue(persons.get(1).toString().equals(person2.toString()));
        System.out.println("test_writeObj....OK");
        service.removeAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_writeObjWithView() throws IOException, ClassNotFoundException {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        WriteReadObj_G<Person> service = WriteReadObj_G.instanciate(appContext, listViewModel);
        service.add(new Person("Name_1","Address_1",11));
        Person person2;
        service.add(person2 = new Person("Name_2","Address_2",22));
        service.add(new Person("Name_3","Address_3",33));
        List<PersonListViewModel> persons = (List<PersonListViewModel>) service.findAll();
        assertTrue(persons.size() == 3);
        PersonListViewModel pview = persons.get(1);
        assertTrue(pview.getName().equals(person2.getName()));
        assertTrue(pview.getId().equals(person2.getId()));
        assertTrue(pview.getAddress().equals(person2.getAddress()));
        System.out.println(pview.toString());
        System.out.println("test_writeObjWithView....OK");
        service.removeAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_writeReadEditDeleteObj() throws IOException, ClassNotFoundException, CloneNotSupportedException {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        WriteReadObj_G<Person> service = WriteReadObj_G.instanciate(appContext);
        service.removeAll();

        //Write 1
        service.add(new Person("Name_1","Address_1",11));
        Person person2;
        //write 2
        service.add(person2 = new Person("Name_2","Address_2",22));
        //Read by idx
        Person personIdx = service.getByIdx(1);
        assertTrue(person2.getId().equals(personIdx.getId()));
        //write 3
        service.add(new Person("Name_3","Address_3",33));
        //Read all
        List<Person> persons = (List<Person>) service.findAll();
        assertTrue(persons.size() == 3);
        String name = "Changed";
        person2.setName(name);
        //Edit
        service.edit(person2);
        persons = (List<Person>) service.findAll();
        assertTrue(persons.size() == 3);
        assertTrue(persons.get(1).toString().equals(person2.toString()));
        assertTrue(persons.get(1).getName().equals(name));

        service.remove(person2.getId());
        persons = (List<Person>) service.findAll();
        assertTrue(persons.size() == 2);
        service.removeAll();
        persons = (List<Person>) service.findAll();
        assertTrue(persons.size() == 0);
        System.out.println("test_writeReadEditDeleteObj....OK");

    }
}
