package com.elijahgill.todotutorial;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.elijahgill.todotutorial.com.elijahgill.todotutorial.contentprovider.MyTodoContentProvider;


public class TodoMainActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_main);

        getSupportFragmentManager().beginTransaction().setTransition(
                FragmentTransaction.TRANSIT_ENTER_MASK)
                .replace(R.id.fragmentPlaceholder,
                        new TodosOverviewFragment(),"TODO_LIST").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TodosOverviewFragment frag = (TodosOverviewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentPlaceholder);
        switch (item.getItemId()){
            case R.id.insert:
                frag.createTodo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
