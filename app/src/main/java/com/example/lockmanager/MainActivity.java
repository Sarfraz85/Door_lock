package com.example.lockmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText text;
    ImageView img;
    BitmapDrawable share;
    Spinner spin;
    int pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.editTextTextPersonName);
        img=findViewById(R.id.imageView);
        spin = (Spinner) findViewById(R.id.spinner2);
        String[] str={"Master","Guest"};
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,str);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }
    public void btn(View view) throws WriterException {
        SecureRandom random = new SecureRandom();
        byte bt[] = new byte[64];
        random.nextBytes(bt);
        String str=bt.toString();
        QRCodeWriter qr=new QRCodeWriter();
        BitMatrix bit=qr.encode(str, BarcodeFormat.QR_CODE, 200,200);
        Bitmap map=Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
        for(int i=0;i<200;i++)
            for(int j=0;j<200;j++)
            {
                map.setPixel(i,j,bit.get(i,j)? Color.BLACK:Color.WHITE);
            }
        img.setImageBitmap(map);
        Random r=new Random();
        pin=r.nextInt(999999);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((str+"%$*").getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection con = connectionHelper.connectionclass();
            if (con != null) {
                Statement st = con.createStatement();
                st.execute("insert into users values('"+sb.toString()+"',"+pin+",'"+spin.getSelectedItem().toString()+"','"+text.getText().toString()+"')");
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Error occured"+ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.e("Error",ex.getMessage().toString());
        }

    }
    public void sharebtn(View view)
    {
        share=(BitmapDrawable) img.getDrawable();
        Bitmap map=share.getBitmap();
        String path= MediaStore.Images.Media.insertImage(getContentResolver(),map,"title",null);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        i.putExtra(Intent.EXTRA_TEXT, String.valueOf(pin).concat(" is the security key"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(i,"Share via: "));
    }
}