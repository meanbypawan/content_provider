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

public class ReadAllContactsDetails extends AppCompatActivity {
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
              al = new ArrayList<>();
              Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
              String contactCol[] = {
                   ContactsContract.Contacts._ID,
                   ContactsContract.Contacts.DISPLAY_NAME
              };
              Cursor c1 = resolver.query(contactsUri,contactCol,null,null,null);
              while(c1.moveToNext()){
                  String id = "";
                  String name = "";
                  String phone = "";
                  String email = "";
                  id = c1.getString(0);
                  name = c1.getString(1);
                  String phoneCol[] = {
                          ContactsContract.CommonDataKinds.Phone.NUMBER
                  };
                  Cursor c2 = resolver.query(
                          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                          phoneCol,
                          ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                          new String[]{id},null);
                 if(c2.moveToNext())
                     phone = c2.getString(0);

                 Cursor c3 = resolver.query(
                         ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                         new String[]{ContactsContract.CommonDataKinds.Email.DATA1},
                         ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",
                         new String[]{id},null);
                 if(c3.moveToNext())
                     email = c3.getString(0);
                 al.add(id+"\n"+name+"\n"+phone+"\n"+email);
              }
              adapter = new ArrayAdapter<>(ReadAllContactsDetails.this, android.R.layout.simple_list_item_1,al);
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