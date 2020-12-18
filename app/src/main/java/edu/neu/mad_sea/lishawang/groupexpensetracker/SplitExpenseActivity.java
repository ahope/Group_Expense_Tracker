package edu.neu.mad_sea.lishawang.groupexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Expense;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

public class SplitExpenseActivity extends AppCompatActivity {
    Group myGroup;
    Expense myExpense;
    ListView splitList;
    List<String> nameList;
    TextView splitTotalAmount, splitEven;
    Button splitSaveBtn;
    Double totalAmt;

    final SplitListAdapter splitListAdapter = new SplitListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_expense);

        myGroup = (Group) getIntent().getSerializableExtra("myGroup");
        myExpense = (Expense) getIntent().getSerializableExtra("myExpense");

        totalAmt = myExpense.getPrice();

        this.setTitle("Total Amount: " + totalAmt);

        nameList = new ArrayList<>();
        for (String name : myExpense.getSplitMap().keySet()) {
            nameList.add(name);
        }

        splitList = (ListView) findViewById(R.id.split_list);

        splitList.setAdapter(splitListAdapter);

        splitTotalAmount = (TextView) findViewById(R.id.split_total_amount);
        splitEven = (TextView) findViewById(R.id.split_even_btn);

        splitEven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double avgPrice = myExpense.getPrice() / (double)nameList.size();
                for (int i = 0; i < splitList.getCount(); i++) {
                    EditText et =
                            (EditText)splitList.getChildAt(i).findViewById(R.id.split_list_amount);
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    String val = df2.format(avgPrice);
                    et.setText(val);
                }
                splitTotalAmount.setText("Remaining Total: 0.00");
            }
        });

        splitSaveBtn = (Button) findViewById(R.id.split_save_btn);
        splitSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

    }

    public Double calculateTotalAmount() {
        Double total = 0.00;
        for (int i = 0; i < splitList.getCount(); i++) {
            EditText et =
                    (EditText)splitList.getChildAt(i).findViewById(R.id.split_list_amount);
            if (et.getText().toString().equals("")) {
                continue;
            }
            Double amount = Double.valueOf(et.getText().toString());
            total += amount;
        }
        DecimalFormat df2 = new DecimalFormat("#.##");
        String val = df2.format(totalAmt - total);
        splitTotalAmount.setText("Remaining Total: " + val);
        return total;
    }

    public void saveExpense() {
        Double curTotal = calculateTotalAmount();
        Double targetTotal = myExpense.getPrice();
        if (Math.abs(curTotal - targetTotal) > 0.1) {
            notFullySplit();
            return;
        }
        HashMap<String, Double> splitMap = new HashMap<>();
        for (int i = 0; i < splitList.getCount(); i++) {
            EditText et =
                    (EditText)splitList.getChildAt(i).findViewById(R.id.split_list_amount);
            if (et.getText().toString().equals("")) {
                continue;
            }
            Double amount = Double.valueOf(et.getText().toString());
            splitMap.put(nameList.get(i), amount);
        }
        myExpense.setSplitMap(splitMap);
        myGroup.getGroupExpenses().add(myExpense);
        Intent intent_expense_added = new Intent(SplitExpenseActivity.this, GroupActivity.class);
        intent_expense_added.putExtra("myGroup", myGroup);
        startActivity(intent_expense_added);
    }

    public void notFullySplit() {
        Toast.makeText(this, "Expense is not fully settled",
                Toast.LENGTH_SHORT).show();
    }

    class SplitListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int position) { return null; }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.split_list_layout, null);

            TextView name = (TextView) convertView.findViewById(R.id.split_list_name);
            EditText amount = (EditText) convertView.findViewById(R.id.split_list_amount);

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateTotalAmount();
                }
            });

            name.setText(nameList.get(position));

            return convertView;
        }
    }
}