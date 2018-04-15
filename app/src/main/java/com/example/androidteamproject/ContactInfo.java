package com.example.androidteamproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactInfo extends AppCompatActivity {
    EditText inputPhonenumber;          // 폰번호 입력칸
    EditText inputName;                 // 이름 입력칸

    TextView textviewPhonenumber;       // 폰번호 리스트 출력칸
    TextView textviewName;              // 이름 리스트 출력칸

    Button saveBtn;                     // 저장 버튼

    List<String> listPhonenumber = new ArrayList<String>(); // 폰번호 리스트
    List<String> listName = new ArrayList<String>();        //이름 리스트

    String[] arrName = new String[100];
    String[] arrPhonenumber = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        inputPhonenumber = (EditText)findViewById(R.id.inputPhonenumber);
        inputName = (EditText)findViewById(R.id.inputName);
        textviewPhonenumber = (TextView) findViewById(R.id.textviewPhonenumber);
        textviewName = (TextView)findViewById(R.id.textviewName);
        saveBtn = (Button) findViewById(R.id.saveBtn);

        saveBtn.setText("긴급 연락망 등록하기");

        LoadData();

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String thisname;
                String thisnum;

                /**
                 * 입력값 받아오기
                 */
                thisname = inputName.getText().toString();
                thisnum = inputPhonenumber.getText().toString();

                /**
                 * 입력값을 기존 배열에 추가하기
                 */
                listName.add(thisname);
                listPhonenumber.add(thisnum);

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
                     * 파일 저장하기
                     */
                    FileOutputStream outNum = openFileOutput("phonenumberData.txt", Context.MODE_PRIVATE);
                    outNum.write(imsiNum.getBytes());
                    outNum.close();

                    FileOutputStream outName = openFileOutput("nameData.txt", Context.MODE_PRIVATE);
                    outName.write(imsiName.getBytes());
                    outName.close();

                    Toast.makeText(getApplicationContext(), "연락망에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    inputName.setText("");
                    inputPhonenumber.setText("");
                }
                catch(IOException e)
                {
                    Toast.makeText(getApplicationContext(),"★저장 실패 : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void LoadData()
    {
        try {
            /**
             *  파일 불러오기
             */


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



            /**
             * 배열을 List 로 변환
             */
            listName = new ArrayList<String>(Arrays.asList(arrName));
            listPhonenumber = new ArrayList<String>(Arrays.asList(arrPhonenumber));
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(),"★읽기 실패 : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
