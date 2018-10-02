package org.rb.objectserialization.service;

import android.content.Context;
import org.rb.objectserialization.model.IListViewModel;
import org.rb.objectserialization.model.IObjectSerializable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Generic class
 *
 * @param <T>
 */
public final class WriteReadObj_G<T extends IObjectSerializable> {

    private static final String DATA_BIN = "data.bin";

    //Android: for openFileInput/ openFileOutput
    private final Context context;
    private  IListViewModel viewModelItf=null;

    private ArrayList<T> items = new ArrayList<>();

    private WriteReadObj_G(Context context) {

          this.context = context;
    }
    private WriteReadObj_G(Context context, IListViewModel viewModelItf) {
        this.viewModelItf = viewModelItf;
        this.context = context;
    }


    public static WriteReadObj_G instanciate(Context context) throws IOException, ClassNotFoundException {
        WriteReadObj_G instance = new WriteReadObj_G(context);
        instance.read();
        return instance;
    }
    public static WriteReadObj_G instanciate(Context context, IListViewModel viewModelItf) throws IOException, ClassNotFoundException {
        WriteReadObj_G instance = new WriteReadObj_G(context, viewModelItf);
        instance.read();
        return instance;
    }


    public void add(T item) throws IOException {
        item.setId(UUID.randomUUID());
        items.add(item);
        write();
    }
    @SuppressWarnings("unchecked")
    public T getByIdx(int idx) throws CloneNotSupportedException {
        T item = items.get(idx);

        return (T)item.clone();

    }

    public void edit(T item) throws IOException {
        int idx = -1;
        for(int i = 0; i< items.size(); i++){
            if(items.get(i).getId().equals(item.getId())){
                idx = i;
                break;
            }
        }
        //item.setId(items.get(idx).getId());
        items.set(idx,item);
        write();
    }
    public void remove(UUID id) throws IOException {
        for (T elm: items) {
            if(elm.getId().equals(id)){
                items.remove(elm);
                write();
                return;
            }
        }
    }
    public void removeAll() throws IOException {
        items.clear();
        write();
    }

    public List<?> findAll() {
        if(viewModelItf == null) {
            return Collections.unmodifiableList(items);
        }
        //noinspection unchecked
        return Collections.unmodifiableList(viewModelItf.viewModel(items));
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
            oos.writeObject(items);
        }finally {
            if(oos != null) oos.close();
            if(os != null) os.close();
        }

    }


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
            //noinspection unchecked
            items = (ArrayList<T>) ois.readObject();
        }catch (FileNotFoundException ex){
            return;
        }finally {
           if(ois != null) ois.close();
           if(is != null) is.close();
        }

    }
}
