package com.example.androidteamproject;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 육문수 on 2017-07-07.
 */

public class ContactUtil {
    public static String myPhoneNumber(Context context, boolean isIDD)
    {
        TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (isIDD)
            return getIDD(phone.getLine1Number());
        else
            return phone.getLine1Number();
    }
    /**
     * 내 전화번호 가져오기
     * @param context
     * @return 전화번호
     */
    public static String myPhoneNumber(Context context)
    {
        return myPhoneNumber(context, false);
    }


    /**
     * 주소록에 있는 전화번호 목록 가져오기
     * @param context
     * @param isIDD 국제전화 규격 적용 여부
     * @return 주소록의 전화번호
     */
    public static ArrayList<String> contactPhoneNumber(Context context, boolean isIDD)
    {
        ArrayList<String> phone = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String s = cursor.getString(index);

            if (isIDD)
                phone.add(getIDD(s));
            else
                phone.add(s);
        }

        return phone;
    }
    /**
     * 주소록에 있는 전화번호 목록 가져오기
     * @param context
     * @return 주소록의 전화번호
     */
    public static ArrayList<String> contactPhoneNumber(Context context)
    {
        return contactPhoneNumber(context, false);
    }


    /**
     * 주소록의 이름, 전화번호 맵을 가져온다
     * @param context
     * @param isIDD 국제전화 규격 적용 여부
     * @return 이름, 전화번호 map
     */
    public static Map<String, String> getAddressBook(Context context, boolean isIDD)
    {
        Map<String, String> result = new HashMap<String, String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext())
        {
            int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String phone = cursor.getString(phone_idx);
            String name = cursor.getString(name_idx);

            if (isIDD)
                result.put(getIDD(phone), name);
            else
                result.put(phone, name);

//          Log.e("####getAddressBook", name + " : "+phone);
        }

        return result;
    }
    /**
     * 주소록의 이름, 전화번호 맵을 가져온다
     * @param context
     * @return 이름, 전화번호 map
     */
    public static Map<String, String> getAddressBook(Context context)
    {
        return getAddressBook(context, false);
    }

    /**
     * 국제전화 형식으로 변경한다.
     * @param phone
     * @return 국제전화번호 규격
     */
    public static String getIDD(String phone)
    {
        String result = phone;

        try {
            result = "82" + Long.parseLong(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
