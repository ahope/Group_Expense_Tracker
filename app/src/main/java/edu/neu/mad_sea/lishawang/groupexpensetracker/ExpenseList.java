package edu.neu.mad_sea.lishawang.groupexpensetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Expense;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton addExpenseBtn;
    private ListView expenseListView;
    private Group myGroup;
    private List<Expense> expenseList;

    public ExpenseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseList.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseList newInstance(String param1, String param2) {
        ExpenseList fragment = new ExpenseList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myGroup = ((GroupActivity)getActivity()).getMyGroup();
        expenseList = myGroup.getGroupExpenses();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);
        addExpenseBtn = view.findViewById(R.id.add_expense_btn);
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add_expense = new Intent(getActivity(), AddExpenseActivity.class);
                intent_add_expense.putExtra("myGroup", myGroup);
                startActivity(intent_add_expense);
            }
        });

        expenseListView = view.findViewById(R.id.expense_listview);
        ExpenseListAdapter adapter = new ExpenseListAdapter();
        expenseListView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }


    class ExpenseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return expenseList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.expense_list_item, null);

            TextView title = (TextView) convertView.findViewById(R.id.expense_list_title);
            TextView des = (TextView) convertView.findViewById(R.id.expense_list_des);
            TextView amount = (TextView) convertView.findViewById(R.id.expense_list_amount);

            title.setText(expenseList.get(position).getItemName());
            des.setText("Paid by " + expenseList.get(position).getOwner());
            Double val = expenseList.get(position).getPrice();
            amount.setText(val.toString() + " USD");

            return convertView;
        }
    }
}