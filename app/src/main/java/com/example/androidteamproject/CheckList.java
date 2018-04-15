package com.example.androidteamproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class CheckList extends AppCompatActivity {

    String[] arrPhonenumber = new String[100];
    String[] arrName = new String[100];
    String[] arrNamePhone = new String[100];

    List<String> listPhonenumber = new ArrayList<String>(); // 폰번호 리스트
    List<String> listName = new ArrayList<String>();        //이름 리스트
    List<String> listNamePhone = new ArrayList<String>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        LoadData();

        ListView listView = (ListView)findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, simple_list_item_1, listNamePhone);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                listName.remove(arg2);
                listPhonenumber.remove(arg2);
                listNamePhone.remove(arg2);
                DeleteData();
                adapter.notifyDataSetChanged();


                return false;
            }
        });
    }

    public void LoadData()
    {
        try {
            /**
             *  파일 불러오기
             */
            String outputimsi1 = new String();
            String outputimsi2 = new String();

            FileInputStream inputNum = openFileInput("phonenumberData.txt");
            FileInputStream inputName = openFileInput("nameData.txt");

            byte[] txtNum = new byte[inputNum.available()];
            byte[] txtName = new byte[inputName.available()];

            while(inputNum.read(txtNum)!=-1){;}
            while(inputName.read(txtName)!=-1){;}

            String numberData = new String(txtNum);
            String nameData = new String(txtName);

            /**
             * 불러온 파일 내의 각 항목을 분리 (Split)
             */
            arrName=nameData.split("\\;");
            arrPhonenumber=numberData.split("\\;");
            arrNamePhone=nameData.split("\\;");

            for(int i=0;i<arrName.length;i++)
            {
                arrNamePhone[i] = arrName[i] + " "+ arrPhonenumber[i];
            }

            /**
             * 배열을 List 로 변환
             */
            listName = new ArrayList<String>(Arrays.asList(arrName));
            listPhonenumber = new ArrayList<String>(Arrays.asList(arrPhonenumber));
            listNamePhone = new ArrayList<String>(Arrays.asList(arrNamePhone));
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(),"★읽기 실패 : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteData()
    {
        String thisname;
        String thisnum;

        /**
         * 배열을 String 형태로 변환하기
         */
        String imsiNum = new String();
        String imsiName = new String();
        for(int i=0;i<listName.size();i++) {
            imsiName += (listName.get(i) + ";");
            imsiNum += (listPhonenumber.get(i) + ";");
        }

        try {
            /**
             * 파일 제거하기
             */
            FileOutputStream outNum = openFileOutput("phonenumberData.txt", Context.MODE_PRIVATE);
            outNum.write(imsiNum.getBytes());
            outNum.close();

            FileOutputStream outName = openFileOutput("nameData.txt", Context.MODE_PRIVATE);
            outName.write(imsiName.getBytes());
            outName.close();

            Toast.makeText(getApplicationContext(), "연락망에서 제거되었습니다.", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            Toast.makeText(getApplicationContext(),"★저장 실패 : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
