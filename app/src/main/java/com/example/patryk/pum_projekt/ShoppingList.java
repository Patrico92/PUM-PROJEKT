package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ShoppingList extends Activity implements View.OnClickListener, ListView.OnItemLongClickListener {

    MyDBHandler myDBHandler;
    ArrayAdapter listAdapter;
    Button deleteShoppingListButton;
    Button addItemButton;
    EditText itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        myDBHandler = new MyDBHandler(this,null,null,0);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,myDBHandler.getShoppingList());
        listAdapter.notifyDataSetChanged();

        ListView listView = (ListView) findViewById(R.id.ShoppingItemList);
        listView.setAdapter(listAdapter);
        listView.setOnItemLongClickListener(this);

        deleteShoppingListButton = (Button) findViewById(R.id.deleteShoppingListButton);
        deleteShoppingListButton.setOnClickListener(this);

        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);

        itemName = (EditText) findViewById(R.id.itemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == deleteShoppingListButton.getId())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Czy chcesz usunąć całą listę zakupów?");

            builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    for (String item : myDBHandler.getShoppingList()) {
                        listAdapter.remove(item);
                    }
                    myDBHandler.deleteShoppingList();

                    listAdapter.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            String item = itemName.getText().toString();

            if (! item.equals(""))
            {
                myDBHandler.addShoppingItem(item);
                listAdapter.add(item);
                listAdapter.notifyDataSetChanged();
                itemName.setText("");
            }
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        String item = String.valueOf(parent.getItemAtPosition(position));

        myDBHandler.deleteRecipe(position);

        listAdapter.remove(item);

        listAdapter.notifyDataSetChanged();

        return true;
    }
}
