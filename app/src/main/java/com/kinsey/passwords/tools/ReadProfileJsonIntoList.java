package com.kinsey.passwords.tools;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.kinsey.passwords.items.Profile;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.format_ymdtime;

public class ReadProfileJsonIntoList {
    private static final String TAG = "ReadProfileJsonIntoList";

    public List<Profile> readProfileJson(String jsonFilename) {

        List<Profile> listAccounts = new ArrayList<Profile>();
        try {

            final JsonReader reader = new JsonReader(new FileReader(jsonFilename));

            reader.beginArray();
            while (reader.hasNext()) {
                Profile account = readMessage(reader);

                if (account == null) {
                    break;
                } else {
                    listAccounts.add(account);
//                    int listCount = listAccounts.size();
                }
            }
//                    Log.d(TAG, "run: count " + listAccounts.size());
            reader.endArray();
            reader.close();
            Log.d(TAG, "run: upload complete " + jsonFilename);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: " + e.getMessage());
        } finally {
            return listAccounts;
        }

    }



    final public Profile readMessage(JsonReader reader) {
        Profile item = new Profile();
        boolean retSuccess = true;
        try {
            reader.beginObject();
            Calendar c1 = Calendar.getInstance();
            while (reader.hasNext()) {
                String name = reader.nextName();
                String value = "";
                int iValue = 0;
                if (name.equals("corpName")) {
                    // System.out.println(reader.nextString());
                    value = reader.nextString();
//					Log.v(TAG, "json corpName " + value);
                    item.setCorpName(value);
                } else if (name.equals("accountId")) {
                    // System.out.println(reader.nextInt());
                    iValue = reader.nextInt();
                    Log.v(TAG, "json id " + iValue);
                    item.setPassportId(iValue);
                } else if (name.equals("seq")) {
                    // System.out.println(reader.nextInt());
                    iValue = reader.nextInt();
//					Log.v(TAG, "json seq " + iValue);
                    item.setSequence(iValue);
                } else if (name.equals("userName")) {
                    value = reader.nextString();
//					Log.v(TAG, "json userName " + value);
                    item.setUserName(value);
                } else if (name.equals("userEmail")) {
                    value = reader.nextString();
//					Log.v(TAG, "json userEmail " + value);
                    item.setUserEmail(value);
                } else if (name.equals("refFrom")) {
                    iValue = reader.nextInt();
//					Log.v(TAG, "json refFrom " + iValue);
                    item.setRefFrom(iValue);
                } else if (name.equals("refTo")) {
                    iValue = reader.nextInt();
//					Log.v(TAG, "json refTo " + iValue);
                    item.setRefTo(iValue);
                } else if (name.equals("website") && reader.peek() != JsonToken.NULL) {
                    value = reader.nextString();
//					Log.v(TAG, "json website " + value);
//                    URL urlValue = new URL(value);
//                    item.setCorpWebsite(urlValue);
                    item.setCorpWebsite(value);
                } else if (name.equals("website")) {
                    reader.nextNull();
                    item.setCorpWebsite("");
                } else if (name.equals("openDt") && reader.peek() != JsonToken.NULL) {
                    value = reader.nextString();
//					Log.v(TAG, "json openDt " + value);
                    Date dte = format_ymdtime.parse(value);
                    c1.setTime(dte);
                    item.setOpenLong(c1.getTimeInMillis());
                } else if (name.equals("openDt")) {
                    reader.nextNull();
                    item.setOpenLong(new Date().getTime());
                } else if (name.equals("actvyDt") && reader.peek() != JsonToken.NULL) {
//					Log.v(TAG, "actvyDt reader " + reader);
                    Date dte;
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                        dte = new Date();
                    } else {
                        value = reader.nextString();
//						Log.v(TAG, "json actvyDt " + value);
                        dte = format_ymdtime.parse(value);
                    }
                    c1.setTime(dte);
                    item.setActvyLong(c1.getTimeInMillis());
                } else if (name.equals("actvyDt")) {
                    reader.nextNull();
                    item.setActvyLong(new Date().getTime());
                } else if (name.equals("note")) {
                    value = reader.nextString();
                    item.setNote(value);
                } else {
                    reader.skipValue(); // avoid some unhandle events
                }
            }

            reader.endObject();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            item = null;
        }
        return item;
    }

}
