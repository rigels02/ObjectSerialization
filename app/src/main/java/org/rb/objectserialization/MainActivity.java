package org.rb.objectserialization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.rb.objectserialization.model.Person;
import org.rb.objectserialization.service.WriteReadObj_G;
import org.rb.objectserialization.tools.Dialogs;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements PersonFormDlg.IPersonFormDlgCallBack,
        PersonActionConfirm.IPersonActionConfirm {

    private ListView personsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        personsListView = findViewById(R.id.personsLV);


        updateListView();

        personsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person person = (Person) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this,"Selected: "+person,Toast.LENGTH_SHORT).show();
                PersonActionConfirm confirm = new PersonActionConfirm();
                Bundle argsBundle = new Bundle();
                argsBundle.putString(PersonActionConfirm.MSG,"Delete? "+person);
                argsBundle.putString(PersonActionConfirm.PERSON_ID,person.getId().toString());
                confirm.setArguments(argsBundle);
                confirm.show(getSupportFragmentManager(),"PersonConfirm");
                return false;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ADD", Snackbar.LENGTH_LONG)
                        .setAction("ADD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPersonFormDialog();
                            }
                        }).show();
                    }
        });

        FloatingActionButton fab_edit = findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "EDIT SELECTED", Snackbar.LENGTH_LONG)
                        .setAction("EDIT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               editSelected();
                            }
                        }).show();
            }
        });
    }

    private void editSelected() {

        int pos = personsListView.getCheckedItemPosition();
        Toast.makeText(this,"Selected id= "+pos,Toast.LENGTH_SHORT).show();
        if(pos < 0) return;
        Person person;
        try {

            person = (Person) WriteReadObj_G.instanciate(getApplicationContext()).getByIdx(pos);


        }catch (IOException | ClassNotFoundException | CloneNotSupportedException ex){
            Dialogs.infoDlg(this, Dialogs.InfoType.Error,ex.getMessage());
            return;
        }

        PersonFormDlg dlg = new PersonFormDlg();
        dlg.setArguments(dlg.initArguments(PersonFormDlg.ACTION_EDIT,person));

        dlg.show(getSupportFragmentManager(),"personFormDlg");
    }


    private void confirmToDeleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteReadObj_G<Person> service;
                        try {
                            //noinspection unchecked
                            service = WriteReadObj_G.instanciate(getApplicationContext());
                            service.removeAll();
                        } catch (IOException | ClassNotFoundException e) {

                            Dialogs.infoDlg(MainActivity.this, Dialogs.InfoType.Error,e.getMessage());
                            return;
                        }
                        updateListView(service);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    private void showPersonFormDialog() {
        PersonFormDlg dlg = new PersonFormDlg();

        dlg.show(getSupportFragmentManager(),"personFormDlg");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_del_all) {
            confirmToDeleteAll();
            return true;
        }
        if (id == R.id.action_build_demo) {
            WriteReadObj_G<Person> service;
            try {
                //noinspection unchecked
                service = WriteReadObj_G.instanciate(getApplicationContext());
                service.removeAll();
                for(int i=0; i<=20; i++){
                    service.add(new Person("Name_"+i,"Address_"+i,20+i));
                }
            }catch (IOException | ClassNotFoundException ex) {
                Dialogs.infoDlg(this, Dialogs.InfoType.Error,ex.getMessage());
                return true;
            }
            updateListView(service);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateListView(){
        ArrayAdapter addapter;

        try {

            //noinspection unchecked
            addapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    WriteReadObj_G.instanciate(getApplicationContext()).findAll());

        } catch (IOException | ClassNotFoundException e) {
            Dialogs.infoDlg(this, Dialogs.InfoType.Error,e.getMessage());
            return;
        }

        personsListView.setAdapter(addapter);
    }


    private void updateListView(WriteReadObj_G service){

        @SuppressWarnings("unchecked")
        ArrayAdapter<Person> addapter =
                new ArrayAdapter<>(this,
                                    android.R.layout.simple_list_item_1,
                                    service.findAll()
                        );
        personsListView.setAdapter(addapter);

    }

    @Override
    public void onPersonFormDlgOk(Person person) {
        Toast.makeText(this,person.toString(),Toast.LENGTH_SHORT).show();

        WriteReadObj_G<Person> service;
        try {
            //noinspection unchecked
            service = WriteReadObj_G.instanciate(getApplicationContext());
            service.add(person);
        } catch (IOException | ClassNotFoundException e) {
            Dialogs.infoDlg(this, Dialogs.InfoType.Error,e.getMessage());
            return;
        }

        updateListView(service);

    }

    @Override
    public void onPersonEditOK(Person person) {
        Toast.makeText(this,person.toString(),Toast.LENGTH_SHORT).show();

        WriteReadObj_G<Person> service;
        try {
            //noinspection unchecked
            service = WriteReadObj_G.instanciate(getApplicationContext());
            service.edit(person);
        } catch (IOException | ClassNotFoundException e) {
            Dialogs.infoDlg(this, Dialogs.InfoType.Error,e.getMessage());
            return;
        }

        updateListView(service);
    }

    @Override
    public void onPersonActionConfirmOK(UUID personID) {

        WriteReadObj_G<Person> service;
        try {
            //noinspection unchecked
            service = WriteReadObj_G.instanciate(getApplicationContext());
            service.remove(personID);
        } catch (IOException | ClassNotFoundException e) {
            Dialogs.infoDlg(this, Dialogs.InfoType.Error,e.getMessage());
            return;
        }
        updateListView(service);
    }
}
