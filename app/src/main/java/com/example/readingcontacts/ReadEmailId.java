package com.example.readingcontacts;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.example.readingcontacts.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ReadEmailId extends AppCompatActivity {
    ActivityMainBinding binding;
    ContentResolver resolver;
    ArrayList<String> al;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getReadContactPermission();
        resolver = getContentResolver();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri emailUri= ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String emailCol[] = {
                  ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                  ContactsContract.CommonDataKinds.Email.DATA1
                };
                // select id,name from table_name
                al = new ArrayList<>();
                Cursor c = resolver.query(emailUri,emailCol,null,null,null);
                while(c.moveToNext()){
                   String id =  c.getString(0);
                   String email = c.getString(1);
                   al.add(id+"\n"+email);
                }
                adapter = new ArrayAdapter<>(ReadEmailId.this, android.R.layout.simple_list_item_1,al);
                binding.lv.setAdapter(adapter);
            }
        });
    }
    private void getReadContactPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},23);
        }
    }
}