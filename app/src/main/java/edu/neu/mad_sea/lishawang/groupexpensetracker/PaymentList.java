package edu.neu.mad_sea.lishawang.groupexpensetracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import edu.neu.mad_sea.lishawang.groupexpensetracker.Retrofit.RetrofitBuilder;
import edu.neu.mad_sea.lishawang.groupexpensetracker.Retrofit.RetrofitInterface;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Expense;
import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentList extends Fragment {
    private Group myGroup;
    private List<String> memberList;
    private List<Expense> expenseList;
    private HashMap<String, Double> calculatedMap;
    private ListView paymentListView;
    private Double totalExp = 0.0;
    TextView testView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentList.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentList newInstance(String param1, String param2) {
        PaymentList fragment = new PaymentList();
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
        memberList = myGroup.getGroupMembers();
        expenseList = myGroup.getGroupExpenses();
        calculatedMap = new HashMap<>();
        for (String name : memberList) {
            calculatedMap.put(name, 0.00);
        }
        for (Expense expense : expenseList) {
            String payer = expense.getOwner();
            Double amountPaid = expense.getPrice();
            totalExp += amountPaid;

            calculatedMap.put(payer, calculatedMap.get(payer) + amountPaid);
            HashMap<String, Double> splitMap = expense.getSplitMap();
            for (String splitter : splitMap.keySet()) {
                calculatedMap.put(splitter, calculatedMap.get(splitter) - splitMap.get(splitter));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);
        paymentListView = view.findViewById(R.id.payment_listview);
        PaymentListAdapter adapter = new PaymentListAdapter();
        paymentListView.setAdapter(adapter);

        TextView totalAmount = view.findViewById(R.id.payment_total);
        totalAmount.setText(totalExp.toString());

        testView = (TextView) view.findViewById(R.id.currency_text);
        connect();
        return view;
    }

    public void connect() {
        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
        Call<JsonObject> call = retrofitInterface.getExchangeCurrency("EUR");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("response", String.valueOf(response.body()));
                JsonObject res = response.body();
                JsonObject rates = res.getAsJsonObject("conversion_rates");
                double myRate = Double.valueOf(rates.get("EUR").toString());
                testView.setText("Today's exchange rate from USD to EUR is " + myRate);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    class PaymentListAdapter extends BaseAdapter {

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
            convertView = getLayoutInflater().inflate(R.layout.payment_list_item, null);

            TextView member = (TextView) convertView.findViewById(R.id.payment_list_name);
            TextView amount = (TextView) convertView.findViewById(R.id.payment_list_amount);

            String name = memberList.get(position);
            member.setText(name);
            Double val = calculatedMap.get(name);
            amount.setText(val.toString() + " USD");
            if (val < 0) {
                amount.setTextColor(Color.parseColor("#FF0000"));
            }
            return convertView;
        }
    }
}