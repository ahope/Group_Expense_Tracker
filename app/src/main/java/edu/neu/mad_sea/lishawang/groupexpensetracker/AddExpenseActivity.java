package edu.neu.mad_sea.lishawang.groupexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Expense;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

public class AddExpenseActivity extends AppCompatActivity {
    private Group myGroup;
    private Spinner spinner;
    private ChipGroup chipGroup;
    private Button goSplitBtn;
    private EditText expenseName, expenseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        myGroup = (Group) getIntent().getSerializableExtra("myGroup");

        setTitle(myGroup.getGroupTitle());

        expenseName = (EditText) findViewById(R.id.expense_name);
        expenseAmount = (EditText) findViewById(R.id.expense_amount);
        spinner = (Spinner) findViewById(R.id.expense_payer);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddExpenseActivity.this,
                android.R.layout.simple_list_item_1, myGroup.getGroupMembers());
        spinner.setAdapter(spinnerAdapter);

        chipGroup = (ChipGroup) findViewById(R.id.chip_group);
        for (String member : myGroup.getGroupMembers()) {
            Chip chip = new Chip(this);
            chip.setText(member);
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }

        goSplitBtn = (Button) findViewById(R.id.go_split_btn);
        goSplitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExpense();
            }
        });
    }

    public void memberNotSelected() {
        Toast.makeText(this, "Please select at least one member",
                Toast.LENGTH_SHORT).show();
    }

    public void createExpense() {
        HashMap<String, Double> splitMap= new HashMap<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip)chipGroup.getChildAt(i);
            if (chip.isChecked()){
                splitMap.put(chip.getText().toString(), 0.00);
            }
        }
        if (splitMap.size() == 0) {
            memberNotSelected();
            return;
        }

        String nameStr = expenseName.getText().toString();
        if (nameStr.equals("")) {
            Toast.makeText(this, "Please enter item name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String amountStr = expenseAmount.getText().toString();
        if (amountStr.equals("")) {
            Toast.makeText(this, "Please enter amount paid",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Double val = Double.parseDouble(amountStr);
        String payer = spinner.getSelectedItem().toString();

        Expense myExpense = new Expense(nameStr, payer, val, splitMap);
        Intent go_split_intent = new Intent(AddExpenseActivity.this, SplitExpenseActivity.class);
        go_split_intent.putExtra("myExpense", myExpense);
        go_split_intent.putExtra("myGroup", myGroup);
        startActivity(go_split_intent);
    }
}