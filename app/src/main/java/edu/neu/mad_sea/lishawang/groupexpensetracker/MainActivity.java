package edu.neu.mad_sea.lishawang.groupexpensetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Expense;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

public class MainActivity extends AppCompatActivity {
    private Button addGroupBtn;
    private ListView groupList;
    private ArrayList<Group> groups;
    private TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Group Expense Tracker");

        groups = (ArrayList<Group>) getIntent().getSerializableExtra("groups");
        if (groups == null) {
            groups = new ArrayList<>();
        }
        //createDemo();
        addGroupBtn = (Button) findViewById(R.id.add_group_button);
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add_group = new Intent(MainActivity.this, AddGroupActivity.class);
                intent_add_group.putExtra("groups", groups);
                startActivity(intent_add_group);
            }
        });

        groupList = (ListView) findViewById(R.id.group_list);
        final List<HashMap<String, String>> listItems = new ArrayList<>();
        final SimpleAdapter groupListAdapter = new SimpleAdapter(this, listItems,
                R.layout.group_list_item, new String[] {"Title", "Description"}, new int[]{R.id.group_list_title, R.id.group_list_des});
        for (Group group : groups) {
            HashMap<String, String> pairs = new HashMap<>();
            pairs.put("Title", group.getGroupTitle());
            pairs.put("Description", group.getGroupDescription());
            listItems.add(pairs);
        }
        groupList.setAdapter(groupListAdapter);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int selectedItem = position;
                String groupName = listItems.get(position).get("Title");
                Group myGroup = null;
                for (int i = 0; i < groups.size(); i++) {
                    if (groups.get(i).getGroupTitle().equals(groupName)) {
                        myGroup = groups.get(i);
                    }
                }
                Intent intent_click_group = new Intent(MainActivity.this, GroupActivity.class);
                intent_click_group.putExtra("myGroup", myGroup);
                startActivity(intent_click_group);
            }
        });

        groupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int selectedItem = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("This group and all the associated expenses will be deleted")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String groupName = listItems.get(selectedItem).get("Title");
                                for (int i = 0; i < groups.size(); i++) {
                                    if (groups.get(i).getGroupTitle().equals(groupName)) {
                                        groups.remove(i);
                                    }
                                }
                                listItems.remove(selectedItem);
                                groupListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    public void createDemo() {
        String groupTile = "Apartment 111(Demo Group)";
        String groupDescription = "water, electricity, rent, etc";
        List<String> groupMembers = Arrays.asList("Lisa", "Jane");
        List<Expense> expenses = new ArrayList<>();

        String itemName1 = "Gas";
        String owner1 = "Lisa";
        Double amount1 = 20.00;
        HashMap<String, Double> splitMap1 = new HashMap<>();
        splitMap1.put("Lisa", 10.00);
        splitMap1.put("Jane", 10.00);
        Expense expense1 = new Expense(itemName1, owner1, amount1, splitMap1);
        expenses.add(expense1);

        String itemName2 = "Target";
        String owner2 = "Jane";
        Double amount2 = 30.00;
        HashMap<String, Double> splitMap2 = new HashMap<>();
        splitMap1.put("Lisa", 10.00);
        splitMap1.put("Jane", 20.00);
        Expense expense2 = new Expense(itemName2, owner2, amount2, splitMap2);
        expenses.add(expense2);

        groups.add(new Group(groupTile, groupDescription, groupMembers, expenses));
    }
}