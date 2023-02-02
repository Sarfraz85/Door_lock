package com.example.lockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    Spinner sp;
    List<String> li = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = (Spinner) findViewById(R.id.spinner);
        Connection con = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.connectionclass();
            if (con != null) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select name from users");
                while (rs.next()) {
                    li.add(rs.getString("name"));
                }
            }
            con.close();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, li);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(dataAdapter);
        } catch (Exception ex) {
            Log.e("error:", ex.getMessage());
        }
    }

    public void lelo(View view) {
        Intent intent= new Intent(this ,key_data.class);
        intent.putExtra("select",sp.getSelectedItem().toString());
        startActivity(intent);
    }

    public void newkey(View view) {
        Intent in=new Intent(this, MainActivity.class);
        startActivity(in);
    }
}