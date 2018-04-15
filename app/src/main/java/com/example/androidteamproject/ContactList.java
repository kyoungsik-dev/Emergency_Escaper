package com.example.androidteamproject;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.androidteamproject.R.layout.activity_contact_list;

public class ContactList extends Activity {
    ListView listPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_contact_list);

        listPerson = (ListView)findViewById(R.id.listView);
        getList();
    }

    public void getList(){

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        String[] selectionArgs = null;

        //정렬
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        //조회해서 가져온다
        Cursor contactCursor = managedQuery(uri, projection, null, selectionArgs, sortOrder);

        //정보를 담을 array 설정
        ArrayList persons = new ArrayList();

        if(contactCursor.moveToFirst()){
            do{
                persons.add(contactCursor.getString(1) + "/" + contactCursor.getString(0));
            }while(contactCursor.moveToNext());
        }

        //리스트에 연결할 adapter 설정
        ArrayAdapter adp = new ArrayAdapter(ContactList.this, android.R.layout.simple_list_item_1, persons);

        //리스트뷰에 표시
        listPerson.setAdapter(adp);
    }
}
