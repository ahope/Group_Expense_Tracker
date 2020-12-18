package edu.neu.mad_sea.lishawang.groupexpensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.neu.mad_sea.lishawang.groupexpensetracker.models.Group;

public class GroupActivity extends AppCompatActivity {
    private Group myGroup;
    private BottomNavigationView bottomNav = null;
    private NavController controller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //connectToInternet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        myGroup = (Group) getIntent().getSerializableExtra("myGroup");
        this.setTitle(myGroup.getGroupTitle());

        bottomNav = findViewById(R.id.bottomNavigationView);
        controller = Navigation.findNavController(GroupActivity.this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNav, controller);
    }

    public Group getMyGroup() {
        return myGroup;
    }

    public void connectToInternet() {

    }
}