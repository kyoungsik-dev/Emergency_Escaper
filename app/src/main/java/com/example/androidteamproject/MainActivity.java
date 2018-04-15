package com.example.androidteamproject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String url;

    String[] arrPhonenumber = new String[100];
    String[] arrName = new String[100];

    List<String> listPhonenumber = new ArrayList<String>(); // 폰번호 리스트
    List<String> listName = new ArrayList<String>();        //이름 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetLocation();
        LoadData();

        Button bt1 = (Button)findViewById(R.id.button);
        Button bt2 = (Button)findViewById(R.id.button2);
        Button bt3 = (Button)findViewById(R.id.button3);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Option.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for (int i =0; i<arrPhonenumber.length; i++) {
                        sendSMS(arrPhonenumber[i], url); // 문자 보내기 함수 호출
                    }

                    Toast.makeText(MainActivity.this, "SMS 발송 완료", Toast.LENGTH_LONG).show(); // 확인 토스트

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // MediaPlayer 객체 초기화 , 재생 // 현재 화면의 제어권자
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.police); // 음악파일

                    mp.setLooping(false);
                    mp.start(); // 노래 재생 시작

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    // 문자 보내기 함수
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver(){
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        message = "긴급 상황 발생!!" + "\n" + message;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); // 문자 메세지 보내기
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void GetLocation()
    {
        try{
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastlocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1,mLocationListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,mLocationListener);


            try {
                Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    Double latitude = lastLocation.getLatitude();
                    Double longtitude = lastLocation.getLongitude();
                    url = "https://www.google.co.kr/maps/@"+latitude+","+longtitude+",17z";
                }
            } catch(Exception ex) {
                Toast.makeText(getApplicationContext(),"마지막 위치 찾는데서 오류남!",Toast.LENGTH_SHORT).show();
            }
        }
        catch(SecurityException e)
        {
            Toast.makeText(getApplicationContext(),"★에러 !! "+e.getMessage(),Toast.LENGTH_LONG).show();
            // 위치 정보 수집 권한 없을때 발생
        }
    }

    //위치 리스너!!
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("★","위치 변경됨 : "+location);

            double longtitude =location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            double accuracy = location.getAccuracy();
            String provider = location.getProvider();

            url = "https://www.google.co.kr/maps/@"+latitude+","+longtitude+",17z";
            // 구글 지도 URL
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d("★","Status Changed, status : "+s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("★","Provider Enabled, provider : "+s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d("★","Provider Disabled, provider : "+s);
        }
    };

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
