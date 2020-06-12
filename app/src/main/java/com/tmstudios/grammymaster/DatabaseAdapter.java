package com.tmstudios.grammymaster;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class DatabaseAdapter extends SQLiteOpenHelper
{
	private SQLiteDatabase db;
	private static final String DATABASE_NAME="database.db";
	private final String DATABASE_PATH;
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME="awards";
	private final Context ctx;
	
	public static class Result
	{
		int year;
		String award;
		boolean winner;
		String nominee;
	}

	public static class Condition{
		int startYear=0;
		int endYear=3000;
		boolean winnersOnly=false;
		String award = null;
		String search = null;
	}
	private static DatabaseAdapter instance;

	public void createDatabase() throws IOException
    {

		boolean dbExist = checkDataBase();

		if (dbExist)
		{
			if (BuildConfig.DEBUG)
				Log.v("DB Exists", "db exists");
			// By calling this method here onUpgrade will be called on a
			// writeable database, but only if the version number has been
			// bumped
			//onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
		}

		boolean dbExist1 = checkDataBase();
		if (!dbExist1)
		{
			//this.getReadableDatabase();
			try
			{
				//this.close();    
				copyDataBase();
			}
			catch (IOException e)
			{
				throw new RuntimeException("Error copying database",e);
			}
		}

    }
	
    //Check database already exist or not
    private boolean checkDataBase()
    {
		boolean checkDB = false;
		String myPath = DATABASE_PATH + DATABASE_NAME;
		File dbfile = new File(myPath);
		checkDB = dbfile.exists();
		return checkDB;
    }
	
    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {

        InputStream mInput = ctx.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
		{
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
	
	//delete database
    public void deleteDatabase()
    {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file.exists())
		{
			file.delete();
			if(BuildConfig.DEBUG)
				System.out.println("delete database file.");
		}
    }
	//Open database
    public void openDatabase() throws SQLException
    {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void closeDataBase() throws SQLException
    {
		if (db != null)
			db.close();
		super.close();
    }
	
    @Override
    public void onCreate(SQLiteDatabase db)
	{
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (newVersion > oldVersion)
		{
			if (BuildConfig.DEBUG)
				Log.v("Database Upgrade", "Database version higher than old.");
			deleteDatabase();
		}

    }

	private DatabaseAdapter(Context ctx)
	{
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = ctx;
		DATABASE_PATH = ctx.getDatabasePath("database").getAbsolutePath();
		try{
			
			createDatabase();
			openDatabase();
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	public static DatabaseAdapter getInstance(Context ctx)
	{
		if (instance == null)
		{
			instance = new DatabaseAdapter(ctx);
		}
		else
		{
			if (BuildConfig.DEBUG)
				Log.w("DatabaseAdabter:getInstance", "init called more than once");
		}
		return instance;
	}
	
	
	public ArrayList<Result> getAwardList(Condition cond){
		return getAwardList(cond,0);
	}
//	public ArrayList<Role> parseRoles(String names,String roles){
//		String[] b = names.split(";");
//		ArrayList<Role> c = new ArrayList<Role>(a.length);
//		for(int i =0;i<a.length;i++){
//			c.add(new Role(b[i],i<a.length?a[i]:null));
//		}
//		return c;
//	}
	public ArrayList<Result> getAwardList(Condition cond,int start){
		String where = "id >= ? AND year >= ? AND YEAR<= ?";
		ArrayList<String> d = new ArrayList<String>();
		d.add(start+"");
		d.add(cond.startYear+"");
		d.add(cond.endYear+"");
		//d.add(String.format("(%d,%d)" ,cond.startYear,cond.endYear));
		if(cond.award != null){
		  where +=" AND award IS ?";
		  d.add(cond.award);
}
        
        if(cond.search != null){
          where +=" AND names LIKE ?";
          d.add(cond.search);
        }
		if(cond.winnersOnly){
			where+="  AND winner IS True";
		}
		Cursor c = null;
		ArrayList<Result> results = new ArrayList<Result>(50);
		try
		{
			c = db.query(true, TABLE_NAME, new String[]{"award,year,winner,names"},where, d.toArray(new String[]{}), null, null, null, "50");
			c.moveToFirst();
			for (int i=0;i < c.getCount();i++)
			{
				Result a = new Result();
				a.award = c.getString(0);
				a.year = c.getInt(1);
				a.winner = c.getInt(2)>0;
				a.nominee = c.getString(3);
				results.add(a);
				c.moveToNext();
			}
			Log.e("result", ""+results.size());
		}
		catch (SQLException e)
		{
			Log.e("exception", "", e);
		}
		
		return results;

	}

		}
