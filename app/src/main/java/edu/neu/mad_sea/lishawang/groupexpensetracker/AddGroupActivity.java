package edu.neu.mad_sea.lishawang.groupexpensetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

public class AddGroupActivity extends AppCompatActivity {
    private EditText groupTitleInput, groupDesInput, addMemberName;
    private Button addMemberBtn, createGroupBtn;
    private ListView addMemberList;
    private ArrayList<String> nameList;
    private ArrayAdapter nameListAdapter;
    private ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        groups = (ArrayList<Group>) getIntent().getSerializableExtra("groups");

        addMemberName = (EditText)findViewById(R.id.add_memebr_name);
        groupTitleInput = (EditText)findViewById(R.id.group_title_input);
        groupDesInput = (EditText)findViewById(R.id.group_des_input);
        addMemberBtn = (Button)findViewById(R.id.add_member_btn);
        createGroupBtn = (Button)findViewById(R.id.create_group_btn);
        addMemberList = (ListView)findViewById(R.id.add_memebr_list);

        nameList = new ArrayList<>();
        nameListAdapter = new ArrayAdapter(AddGroupActivity.this,
                android.R.layout.simple_list_item_1, nameList);
        addMemberList.setAdapter(nameListAdapter);

        addMemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int selectedItem = position;

                new AlertDialog.Builder(AddGroupActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("The member will be removed from the group")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nameList.remove(selectedItem);
                                nameListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameList.size() > 15) {
                    exceedMaxMembers();
                    return;
                }
                String name = addMemberName.getText().toString();
                if (name.equals("")) {
                    return;
                }
                nameList.add(name);
                nameListAdapter.notifyDataSetChanged();
                addMemberName.getText().clear();
            }
        });

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupTitle = groupTitleInput.getText().toString();
                String groupDes = groupDesInput.getText().toString();
                if (groupTitle.equals("")) {
                    emptyTitle();
                    return;
                }
                if (nameList.size() == 0) {
                    emptyMembers();
                    return;
                }
                createGroup(groupTitle, groupDes, nameList);
            }
        });
    }

    private void createGroup(String groupTitle, String groupDes, List<String> members) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getGroupTitle().equals(groupTitle)) {
                repeatedTile(groupTitle);
                return;
            }
        }
        Group group = new Group(groupTitle, groupDes, members);
        Intent intent_added_group = new Intent(AddGroupActivity.this, MainActivity.class);
        groups.add(group);
        intent_added_group.putExtra("groups", groups);
        startActivity(intent_added_group);
    }

    private void exceedMaxMembers() {
        Toast.makeText(this, "A group cannot have more than 15 members",
                Toast.LENGTH_SHORT).show();
    }

    private void emptyMembers() {
        Toast.makeText(this, "Please add members to the group",
                Toast.LENGTH_SHORT).show();
    }

    private void emptyTitle() {
        Toast.makeText(this, "Please enter a Group Title",
                Toast.LENGTH_SHORT).show();
    }

    private void repeatedTile(String groupTitle) {
        Toast.makeText(this, "Group Title \"" + groupTitle + "\" already exists",
                Toast.LENGTH_SHORT).show();
    }
}