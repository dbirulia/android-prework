package com.example.dbirulia.simpletodoapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dbirulia.simpletodoapp.utils.DBHelper;
import com.example.dbirulia.simpletodoapp.R;
import com.example.dbirulia.simpletodoapp.models.ToDoItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ToDoListActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    DBHelper toDoListDB;
    // Map position in the list to ToDoItem
    HashMap<Integer, ToDoItem> hmToDoList;

    private final int REQUEST_CODE = 20;
    int itemToBeRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        lvItems = (ListView)findViewById(R.id.lvItems);
        // items = new ArrayList<>();
        toDoListDB = new DBHelper(this);
        loadList();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                // set color based on Priority
                ToDoItem todo = hmToDoList.get(position);
                if (todo.priority.equals("high")) {
                    text.setTextColor(Color.parseColor("#b00b1e"));
                }
                if (todo.priority.equals("medium")){
                    text.setTextColor(Color.parseColor("#fbaf2d"));
                }
                if (todo.priority.equals("low")){
                    text.setTextColor(Color.parseColor("#004A00"));
                }

                return view;
            }
        };
        lvItems.setAdapter(itemsAdapter);
        setupToDoListListener();
    }

    public void setupToDoListListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                           View view, int pos, long id){

                        itemToBeRemoved = pos;
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoListActivity.this);
                        alertDialogBuilder.setMessage("Are you sure,You want to delete \"" + items.get(pos) + "\"?");
                        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                removeItem(itemToBeRemoved);
                            }
                        });

                        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemToBeRemoved = 0;
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return true;
                    }
                });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View view, int pos, long id){
                        launchEditView(pos);
                    }
                });
    }

    private void removeItem(int pos){
        items.remove(pos);
        itemsAdapter.notifyDataSetChanged();
        ToDoItem todoItem = hmToDoList.get(pos);
        hmToDoList.remove(pos);
        toDoListDB.deleteToDoItem(todoItem.id);
    }

    public void launchEditView(int pos) {
        Intent i = new Intent(ToDoListActivity.this, EditItemActivity.class);
        ToDoItem item = hmToDoList.get(pos);
        i.putExtra("name", item.name);
        i.putExtra("pos", pos);
        i.putExtra("details", item.details);
        i.putExtra("priority", item.priority);
        i.putExtra("duedate", item.dueDate);
        // brings up the edit item activity
        startActivityForResult(i, REQUEST_CODE);
    }

    // Result from Edit item activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newName = data.getExtras().getString("name");
            String newPriority = data.getExtras().getString("priority");
            String newDescription = data.getExtras().getString("details");
            int pos = data.getExtras().getInt("pos", 0);
            int newDueDate = data.getExtras().getInt("duedate", 0);
            items.set(pos, newName);
            ToDoItem todo = hmToDoList.get(pos);
            todo.priority = newPriority;
            todo.details = newDescription;
            todo.dueDate = newDueDate;
            toDoListDB.updateToDoItem(todo.id, newName, newDescription, newPriority, newDueDate);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemName = etNewItem.getText().toString();
        long newItemId = toDoListDB.insertToDoItem(itemName, "", "", 0);
        hmToDoList.put(itemsAdapter.getCount(), new ToDoItem(newItemId, itemName, "", "low", 0));
        itemsAdapter.add(itemName);
        etNewItem.setText("");
    }

    private void loadList(){

        hmToDoList = new HashMap<>();
        ArrayList<ToDoItem> todos = toDoListDB.getAllToDos();
        Integer p = 0;
        items = new ArrayList<>();
        for (ToDoItem todo: todos) {
            hmToDoList.put(p, todo);
            items.add(todo.name);
            p++;
        }
    }

}
