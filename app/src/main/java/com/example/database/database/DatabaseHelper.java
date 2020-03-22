package com.example.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.database.Tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_TANK = "CREATE TABLE " + Config.TANK_TABLE_NAME + " ( " + Config.COLUMN_TANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Config.COLUMN_TANK_TITLE + " TEXT NOT NULL, " + Config.COLUMN_TANK_CODE + " TEXT NOT NULL )";
        Log.d(TAG, CREATE_TABLE_TANK);

        sqLiteDatabase.execSQL(CREATE_TABLE_TANK);
        Log.d(TAG, "db created. ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public  long insertTank(Tank tank)
    {long id=-1;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Config.COLUMN_TANK_TITLE, tank.getTitle());
        contentValues.put(Config.COLUMN_TANK_CODE, tank.getCode());



    try{
        id= db.insertOrThrow(Config.TANK_TABLE_NAME,null,contentValues);
    }
        catch(SQLException e){Log.d(TAG,"Exception: "+e);
            Toast.makeText(context,"Operation failed "+e,Toast.LENGTH_LONG).show();
    }
    finally

    {db.close();
    }
    return id;
    }


    public List<Tank> getAllTanks()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
       try {



           cursor = db.query(Config.TANK_TABLE_NAME, null, null, null, null, null, null);
           if(cursor!=null)
           {if(cursor.moveToFirst())
           {
               List<Tank> watertanks = new ArrayList<>();
               do {
                int id=cursor.getInt(cursor.getColumnIndex(Config.COLUMN_TANK_ID));
                    String title=cursor.getString(cursor.getColumnIndex(Config.COLUMN_TANK_TITLE));
                   String code=cursor.getString(cursor.getColumnIndex(Config.COLUMN_TANK_CODE));

                   watertanks.add(new Tank(id,title,code));




               } while (cursor.moveToNext());
               return watertanks;
           }
           }

       }

       catch(SQLException e){Log.d(TAG,"Exception: "+e);
           Toast.makeText(context,"Operation failed "+e,Toast.LENGTH_LONG).show();
       }
       finally

       {if(cursor!=null)
       cursor.close();
           db.close();
       }
        return Collections.emptyList();
       }


public void deleteEntry(long row) {
    SQLiteDatabase db=this.getWritableDatabase();
    // Deletes a row given its rowId, but I want to be able to pass
    // in the name of the KEY_NAME and have it delete that row.
    db.delete(Config.TANK_TABLE_NAME, Config.COLUMN_TANK_ID + "=" + row, null);
    db.close();
}

}

