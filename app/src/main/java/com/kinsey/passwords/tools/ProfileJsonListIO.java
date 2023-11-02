package com.kinsey.passwords.tools;

import android.accounts.Account;
import android.database.Cursor;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import com.kinsey.passwords.items.Profile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kinsey.passwords.MainActivity.format_ymdtime;

public class ProfileJsonListIO {
    private static final String TAG = "ProfileJsonListIO";

    public List<Profile> readProfileUri(String uri) {

//        InputStream inputStream = getContentResolver().openInputStream(uri);

//        InputStream inputStream = getContentResolver().openInputStream(uri);
        List<Profile> listAccounts = new ArrayList<Profile>();
        return listAccounts;
    }

    public List<Profile> readProfileJson(String jsonFilename) {
        String TABFunc = "readProfileJson: " + jsonFilename;
        List<Profile> listAccounts = new ArrayList<Profile>();
        try {

            FileReader readerFile = new FileReader(jsonFilename);
            final JsonReader reader = new JsonReader(readerFile);

            reader.beginArray();
            while (reader.hasNext()) {
                Profile account = readMessage(reader);

                if (account == null) {
                    break;
                } else {
                    listAccounts.add(account);
//                    Log.d(TAG, TABFunc + account.getCorpName());
//                    int listCount = listAccounts.size();
                }
            }
//                    Log.d(TAG, "run: count " + listAccounts.size());
            reader.endArray();
            reader.close();
            Log.d(TAG, "run: upload complete " + jsonFilename);
        } catch (android.util.MalformedJsonException e) {
//            e.printStackTrace();
            Log.e(TAG, "*** Malformed Object ERROR: " + e.getMessage());
//            listAccounts = null;
//            throw new android.util.MalformedJsonException(e.getMessage());
            Profile errProfile = new Profile();
            errProfile.setCorpName("Malformed Restore File");
            errProfile.setPassportId(-99);
            listAccounts.add(errProfile);


        } catch (InvalidObjectException e) {
            e.printStackTrace();
            Log.e(TAG, "*** Invalid Object ERROR: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "*** ERROR: " + e.getMessage());
        } finally {

            return listAccounts;
        }



    }


    final public Profile readMessage(JsonReader reader) {
        Profile item = new Profile(0, "", "", "", "");
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
//                    Log.v(TAG, "json id " + iValue);
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

    public int writeProfileJson(File file, List<Profile> profiles) {

        int count = 0;
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            count = writeMessagesArray(writer, profiles);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            count = 0;
        }

        return count;
    }


    private int writeMessagesArray(JsonWriter writer, List<Profile> profiles) throws IOException {
        int count = 0;
        try {

            writer.beginArray();
            for (Profile item : profiles) {
//            for (Account item : listAccounts) {
                writeMessage(writer, item);
                count++;
            }
            writer.endArray();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            Log.e(TAG, "writeMessageArrayError: " + e2.getMessage());
        }
        return count;
    }


    public void writeMessage(JsonWriter writer, Profile item)
            throws IOException {
        try {
            writer.beginObject();
            writer.name("corpName").value(item.getCorpName());
            writer.name("accountId").value(item.getPassportId());
            writer.name("seq").value(item.getSequence());
            writer.name("userName").value(item.getUserName());
            writer.name("userEmail").value(item.getUserEmail());
            writer.name("refFrom").value(item.getRefFrom());
            writer.name("refTo").value(item.getRefTo());
            if (item.getCorpWebsite() == null) {
                writer.name("website").nullValue();
            } else {
                writer.name("website").value(item.getCorpWebsite());
            }
            if (item.getOpenLong() == 0l) {
                writer.name("openDt").nullValue();
            } else {
                writer.name("openDt").value(
                        format_ymdtime.format(item.getOpenLong()));
            }
            if (item.getActvyLong() == 0l) {
                writer.name("actvyDt").nullValue();
            } else {
                writer.name("actvyDt").value(
                        format_ymdtime.format(item.getActvyLong()));
            }
            Log.d(TAG, "writeMessage: note " + item.getNote());
            writer.name("note").value(item.getNote());

            writer.endObject();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(e2.getMessage());
            Log.v(TAG, "writeMessageError: " + e2.getMessage());
        }
    }


//    public List<Account> loadAccounts() {
////        Log.d(TAG, "loadAccounts: starts ");
//        Cursor cursor = getContentResolver().query(
//                AccountsContract.CONTENT_URI, null, null, null,
//                String.format("%s COLLATE NOCASE ASC, %s COLLATE NOCASE ASC", AccountsContract.Columns.CORP_NAME_COL, AccountsContract.Columns.SEQUENCE_COL));
////                        AccountsContract.Columns.CORP_NAME_COL);
//
//        List<Account> listAccounts = new ArrayList<Account>();
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Account item = AccountsContract.getAccountFromCursor(cursor);
////                        Account item = new Account(
////                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns._ID_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_NAME_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_NAME_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.USER_EMAIL_COL)),
////                                cursor.getString(cursor.getColumnIndex(AccountsContract.Columns.CORP_WEBSITE_COL)),
////                                cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.SEQUENCE_COL)));
////
////                        if (cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL))) {
////                                item.setPassportId(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.PASSPORT_ID_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL))) {
////                                item.setOpenLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.OPEN_DATE_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL))) {
////                                item.setActvyLong(cursor.getLong(cursor.getColumnIndex(AccountsContract.Columns.ACTVY_DATE_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL))) {
////                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_FROM_COL)));
////                            }
////                        }
////                        if (cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL) != -1) {
////                            if (!cursor.isNull(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL))) {
////                                item.setRefFrom(cursor.getInt(cursor.getColumnIndex(AccountsContract.Columns.REF_TO_COL)));
////                            }
////                        }
//                listAccounts.add(item);
//            }
//            cursor.close();
//        }
//
//        return listAccounts;
//    }

}
