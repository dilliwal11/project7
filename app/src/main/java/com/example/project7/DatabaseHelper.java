package com.example.project7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String tableName = "user_table";
    private static final String col1 = "distance";
    private static final String col2 = "date";
    private static final String col3 = "time";




    public DatabaseHelper(Context context) {
        super(context, tableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createtable = "CREATE TABLE "+ tableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +col1 + " INTEGER, "+
                col2 + " TEXT, time TEXT)";


        sqLiteDatabase.execSQL(createtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tableName);
    }

        public boolean addData (String dist, String date, String time){
        SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(col1,dist);
            contentValues.put(col2,date);
            contentValues.put(col3,time);

            Log.d(TAG, "addData: "+ dist+" "+ date+" "+time+ " to "+ tableName);

            long result = db.insert(tableName,null,contentValues);

            if(result==-1)
                return false;

            else return true;
        }

        int distanceToUpdate = 0;

        public boolean check(String date){

        SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT date,time,distance FROM "+tableName+" WHERE date = ? ",new String[]{date} );
            if(c.getCount()>0){
                c.moveToNext();
                distanceToUpdate = Integer.parseInt(c.getString(2));
                Log.d("hello", "check: "+c.getString(1));
                return true;
            }
            else return false;

        }

        public void updateTodaysValue(String date, String dist, String time){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(col3,time);
            int d = Integer.parseInt(dist);
            distanceToUpdate = distanceToUpdate+d;
            cv.put(col1,distanceToUpdate);
            long result = db.update(tableName,cv,"date = ?",new String[]{date});

            if(result==-1)
                Log.d(TAG, "updateTodaysValue: not updated");

            else Log.d(TAG, "updateTodaysValue: updated !!"+distanceToUpdate);;



        }


        public String allStats(){
            String stats = "";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT ID,date,distance FROM "+tableName,null );
            if(c.getCount()>0){
                while(c.moveToNext()){

                    stats = c.getString(0)+":"+"distance: "+c.getString(2)+"ft, date: "+c.getString(1)+"\n";


                }


            }
            return stats;

        }








}
