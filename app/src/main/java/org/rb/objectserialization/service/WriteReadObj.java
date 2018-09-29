package org.rb.objectserialization.service;

import android.content.Context;
import org.rb.objectserialization.model.Person;

import java.io.*;
import java.util.*;

public final class WriteReadObj {

    private static final String DATA_BIN = "data.bin";

    //Android: for openFileInput/ openFileOutput
    private final Context context;

    private ArrayList<Person> persons = new ArrayList<>();

    private WriteReadObj(Context context) {

          this.context = context;
    }

    public static WriteReadObj instanciate(Context context) throws IOException, ClassNotFoundException {
        WriteReadObj instance = new WriteReadObj(context);
        instance.read();
        return instance;
    }


    public void add(Person person) throws IOException {
        person.setId(UUID.randomUUID());
        persons.add(person);
        write();
    }
    public Person getByIdx(int idx) throws CloneNotSupportedException {
        Person person = persons.get(idx);
        /**
        Person eperson = new Person(person.getName(), person.getAddress(), person.getAge());
        eperson.setId(person.getId());
         **/
        Person eperson = (Person)person.clone();
        return eperson;

    }

    public void edit(Person person) throws IOException {
        int idx = -1;
        for(int i=0; i< persons.size(); i++){
            if(persons.get(i).getId().equals(person.getId())){
                idx = i;
                break;
            }
        }
        //person.setId(persons.get(idx).getId());
        persons.set(idx,person);
        write();
    }
    public void remove(UUID id) throws IOException {
        for (Person elm: persons ) {
            if(elm.getId().equals(id)){
                persons.remove(elm);
                write();
                return;
            }
        }
    }
    public void removeAll() throws IOException {
        persons.clear();
        write();
    }

    public List<Person> findAll() {
        //read();
        return Collections.unmodifiableList(persons);
    }

    public void readData() throws IOException, ClassNotFoundException {
        read();
    }


    private void write() throws IOException {
        FileOutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = context.openFileOutput(DATA_BIN, Context.MODE_PRIVATE); //Android specific
            oos = new ObjectOutputStream(os);
            oos.writeObject(persons);
        }finally {
            if(oos != null) oos.close();
            if(os != null) os.close();
        }

    }

    @SuppressWarnings("unchecked")
    private void read() throws IOException, ClassNotFoundException {
        /*
        Correct way to use File.exists
         */
        /**
        if( !new File(context.getFilesDir(),DATA_BIN).exists() )
           return;
         **/
       /*
        Wrong way to use File.exists
        */
       /**
        File fi = new File(DATA_BIN); !!
        if (!fi.exists()) return;  !!
        **/
        FileInputStream is = null;
        ObjectInputStream ois = null;
        try {
            is = context.openFileInput(DATA_BIN); //Android specific
            ois = new ObjectInputStream(is);
            persons = (ArrayList<Person>) ois.readObject();
        }catch (FileNotFoundException ex){
            return;
        }finally {
           if(ois != null) ois.close();
           if(is != null) is.close();
        }

    }
}
