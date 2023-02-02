package com.example.lockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class key_data extends AppCompatActivity {

    String mes=null;
    Connection con = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_data);
        TextView txt1=findViewById(R.id.textView);
        TextView txt2=findViewById(R.id.textView2);
        TextView txt3=findViewById(R.id.textView3);
        Intent intent = getIntent();
        mes = intent.getStringExtra("select");
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.connectionclass();
            if (con != null) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select * from users where name = '"+mes+"'");
                rs.first();
                    txt1.setText(rs.getString("name"));
                    txt2.setText(rs.getString("passkey"));
                    txt3.setText(rs.getString("type"));
            }

        } catch (Exception ex) {
            Log.e("error:", ex.getMessage());
        }
    }

    public void Delkey(View view) {
        try {
            Statement st = con.createStatement();
            st.execute("Delete from users where name = '"+mes+"'");
            Toast.makeText(getApplicationContext(),mes+" key deleted successfully",Toast.LENGTH_LONG).show();
        }catch(Exception ex)
        {
            Log.e("Error in del",ex.getMessage());
        }
        Intent in=new Intent(this, home.class);
        startActivity(in);
    }
}