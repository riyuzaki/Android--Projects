package com.example.pruthvi.stillstrugglingpartb;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Scanner;


//import com.opencsv.CSVWriter;




public class StillStrugglingPartB extends Activity implements SensorEventListener {
    static {

        System.loadLibrary("jnilibsvm") ;
    }

    public native void jniSvmTrain(String cmd);
    public native String jniSvmPredict(String cmd);
    public static final String LOG_TAG = "AndroidLibSvm";

    OnClickListener listener1 = null;
    OnClickListener listener2 = null;
    OnClickListener listener3 = null;
    OnClickListener listener4 = null;
    OnClickListener listener5 = null;
    OnClickListener listener6 = null;

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    public String appFolderPath;

    DatabaseHelper mOpenHelper;

    private static final String DATABASE_NAME = "dbForTest.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "diary";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    public int t = 1;
    public String s;
    public String option = "";
    public String FinalConCat = "";
    public String Columns = "";
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String CreateColumns = "";

            for(int i = 1; i < 51; i++){
                CreateColumns = CreateColumns + "Acc_X_" + i + " text not null, " + "Acc_Y_"+ i + " text not null, " + "Acc_Z_" + i + " text not null, ";
            }
            CreateColumns = CreateColumns + "Act_Lbl text not null " ;


            String sql = "CREATE TABLE " + TABLE_NAME + " (" + CreateColumns + ");";

//            String sql = "CREATE TABLE " + TABLE_NAME + " (" + TITLE
//                    + " text not null, " + BODY + " text not null " + ");";
            Log.i("haiyang:createDB=", sql);
            db.execSQL(sql);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    private SensorManager sensorManager;
    private Sensor accelerometer;
    public double[] x_data;
    public double[] y_data;
    public double[] z_data;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    ArrayList<Float> delta_x = new ArrayList<Float>();
    ArrayList<Float> delta_y = new ArrayList<Float>();
    ArrayList<Float> delta_z = new ArrayList<Float>();

    private long lastUpdate = 0;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_still_struggling_part_b);
        //prepareListener();
        //initLayout();
        //mOpenHelper = new DatabaseHelper(this);



        for(int i = 1; i < 51; i++){
            Columns = Columns + "Acc_X_" + i + ", " + "Acc_Y_"+ i + ", " + "Acc_Z_" + i + ", ";
        }
        Columns = Columns + "Act_Lbl" ;
        //System.out.println("the columns are " + Columns);

        mOpenHelper = new DatabaseHelper(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            Toast.makeText(StillStrugglingPartB.this, "No Accelerometer in Phone", Toast.LENGTH_LONG).show();
        }

        listener3 = new OnClickListener() {
            public void onClick(View v) {
                //Log.i("S=", s);
                option = "walking";
                //System.out.println("Delta X is " + deltaX);

//                while(FinalConCat == ""){
//
//                }

                //System.out.println("FinalCon " + FinalConCat);
//                insertItem(walking);
                //insertItem(t);

            }
        };

        listener4 = new OnClickListener() {
            public void onClick(View v) {
                //Log.i("S=", s);
                option = "running";
            }
        };

        listener5 = new OnClickListener() {
            public void onClick(View v) {
                //Log.i("S=", s);
                option = "eating";
            }
        };

        listener1 = new OnClickListener() {
            public void onClick(View v) {
                CreateTable();
            }
        };
        listener2 = new OnClickListener() {
            public void onClick(View v) {
                dropTable();
            }
        };

        listener6 = new OnClickListener() {
            public void onClick(View v) {
                selectrowone();
            }
        };

        initLayout();

    }
    //public void jniSvmTrain();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        //Log.i("Option=", option);
        if(option == "walking" || option == "running" || option == "eating" ) {
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                deltaX = Math.abs(event.values[0]);
                deltaY = Math.abs(event.values[1]);
                deltaZ = Math.abs(event.values[2]);

                s = Float.toString(25.0f);


                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {


                    if (delta_x.size() < 51) {
                        delta_x.add(deltaX);
                    }
                    if (delta_y.size() < 51) {
                        delta_y.add(deltaY);
                    }
                    if (delta_z.size() < 51) {
                        delta_z.add(deltaZ);
                    }
                    else {
                        //option = "";

                        String ConCat = "";
                        FinalConCat = "";

                        for(int i = 0; i < 50; i++){
                            ConCat = ConCat + "'" + delta_x.get(i) + "', " + "'" + delta_y.get(i) + "', " + "'" + delta_z.get(i) + "', ";
                        }
                        ConCat = ConCat + "'" + option + "'";
                        FinalConCat = ConCat;
                        delta_x.clear();
                        delta_y.clear();
                        delta_z.clear();
                        insertItem(t,FinalConCat);
                        //System.out.println("FinalCon "+ FinalConCat);
                        option = "";
                        //System.out.println("FinalConCat "+ FinalConCat);
                    }
                    //Log.i("deltaX=", s);
                    //insertItem();
                    //insertItem(t);
                    lastUpdate = curTime;
                }

            }
//            insertItem(t);
//            System.out.println("FinalCon "+ FinalConCat);
//            option = "";
        }
        //insertItem(t);
    }

    private void initLayout() {
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(listener1);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(listener2);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(listener3);

        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(listener4);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(listener5);

        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(listener6);

    }

    private void prepareListener() {
        listener1 = new OnClickListener() {
            public void onClick(View v) {
                CreateTable();
            }
        };
        listener2 = new OnClickListener() {
            public void onClick(View v) {
                dropTable();
            }
        };
/*        listener3 = new OnClickListener() {
            public void onClick(View v) {
                insertItem(t);
            }
        };*/
        listener4 = new OnClickListener() {
            public void onClick(View v) {
                deleteItem();
            }
        };
        listener5 = new OnClickListener() {
            public void onClick(View v) {
                showItems();
            }
        };
    }
    private void CreateTable() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String CreateColumns = "";

        for(int i = 1; i < 51; i++){
            CreateColumns = CreateColumns + "Acc_X_" + i + " text not null, " + "Acc_Y_"+ i + " text not null, " + "Acc_Z_" + i + " text not null, ";
        }
        CreateColumns = CreateColumns + "Act_Lbl text not null" ;


        String sql = "CREATE TABLE " + TABLE_NAME + " (" + CreateColumns + ");";
        Log.i("createDB=", sql);

        try {
            db.execSQL("DROP TABLE IF EXISTS diary");
            db.execSQL(sql);
            setTitle("drop");
        } catch (SQLException e) {
            setTitle("exception");
        }
    }
    private void dropTable() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String sql = "drop table " + TABLE_NAME;
        try {
            db.execSQL(sql);
            setTitle(sql);
        } catch (SQLException e) {
            setTitle("exception");
        }
    }
    private void insertItem(int p, String values) {
        //Log.i("p=",Integer.toString(p+1));
        System.out.println("My string is " + values);
        System.out.println("The columns are " + Columns);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //String sql1 = "insert into " + TABLE_NAME + " (" + TITLE + ", " + BODY
        //        + ") values(1, 2);";
        //String sql2 = "insert into " + TABLE_NAME + " (" + TITLE + ", " + BODY
        //        + ") values(3, 4);";
//        String sql1 = "insert into " + TABLE_NAME + " (" + TITLE + ", " + BODY
//                + ") values("+ Integer.toString(p) + "," + Integer.toString(p+1) + ");";
//        String sql2 = "insert into " + TABLE_NAME + " (" + TITLE + ", " + BODY
//                + ") values("+ Integer.toString(p+2) + "," + Integer.toString(p+4) + ");";

        String sql1 = "insert into " + TABLE_NAME + " (" + Columns
                + ") values("+ values + ");";


        try {
            Log.i("sql1=", sql1);
            //Log.i("sql2=", sql2);
            db.execSQL(sql1);
            //db.execSQL(sql2);
            setTitle("done");
        } catch (SQLException e) {
            setTitle("exception");
        }
    }

    private void deleteItem() {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.delete(TABLE_NAME, " title = 'haiyang'", null);
            setTitle("title");
        } catch (SQLException e) {

        }

    }

    private void selectrowone() {



        //SQLiteDatabase db = mOpenHelper.getReadableDatabase();


        File dbFile=getDatabasePath(DATABASE_NAME);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        //System.out.println("This is it" + exportDir); // /storage/emulated/0
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }


        File file = new File(exportDir, "csvname.txt");

        //Log.i("great till now ",TABLE_NAME);

        try
        {
            file.createNewFile();

            //CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            //pw.println(1312);
            //pw.println(1231);
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            //Log.i("great till now ",TABLE_NAME);
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ TABLE_NAME,null);

            //csvWrite.writeNext(curCSV.getColumnNames());
            //pw.println(curCSV.getColumnNames());
            Log.i("great till now ",TABLE_NAME);


            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
//                //csvWrite.writeNext(arrStr);
//                    Log.i("Are you der? ", curCSV.getString(1));
//                pw.println(curCSV.getString(1));
                //String X = curCSV.getString(150);
                String X;
                if (curCSV.getString(150).equals("walking")){
                    X = "1";
                }
                else if (curCSV.getString(150).equals("running")){
                    X = "2";
                }
                else if (curCSV.getString(150).equals("eating")){
                    X = "3";
                }
                else{
                    X = curCSV.getString(150);
                }



                for(int i = 0; i < 150; i++){
                    int j = i+1;
                    X = X + " " + j + ":" + curCSV.getString(i);
                 }

                pw.println(X);

            }
            pw.close();
            //jniSvmTrain("-t 2 your_train_data_path your_model_file_path");
            //jniSvmTrain("-t 2  /app/src/main/java/training_set.txt " + exportDir+"/model.txt");

            //String systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            //appFolderPath = systemPath+"libsvm/";
            //System.out.println("PATH "+exportDir);


            //CreateAppFolderIfNeed();
            //copyAssetsDataIfNeed();

//            String dataTrainPath = appFolderPath+"heart_scale ";
//            String dataPredictPath = appFolderPath+"heart_scale ";
//            String modelPath = appFolderPath+"model ";
//            String outputPath = appFolderPath+"predict ";

            String dataTrainPath = exportDir+"/train_data ";
            String dataPredictPath = exportDir+"/test_data ";
            String modelPath = exportDir+"/model ";
            String outputPath = exportDir+"/predict ";

            //String to = appFolderPath+"heart_scale";

            // 1. check if file exist
            //File file1   = new File(to);


            //String svmTrainOptions = "-t 2 ";
            //jniSvmTrain(svmTrainOptions+dataTrainPath+modelPath);
            //jniSvmPredict(dataPredictPath+modelPath+outputPath);
            //System.out.println("SYSTEM_PATH "+ systemPath);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            float lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();


            //System.out.println("lines "+ lines);
            File train_file = new File(exportDir, "train_data");
            File test_file = new File(exportDir, "test_data");

            PrintWriter pw1 = new PrintWriter(new FileWriter(train_file));
            PrintWriter pw2 = new PrintWriter(new FileWriter(test_file));
            Scanner sc = new Scanner(new FileReader(file));
            // int numb;
            float count = 0;
            String X;
            while(sc.hasNextLine()){
                //numb = Integer.parseInt(sc.nextLine());
                //System.out.println(sc.nextLine());
                //System.out.println((2 * lines)/3);
                //pw1.println(sc.nextLine());
                System.out.println("count "+ count);
                X = sc.nextLine();
                //if(count == ((2 * lines)/3) || count < ((2 * lines)/3)){
                if(count == ((2 * lines)/3) || count < ((2 * lines)/3)){
                    pw1.println(X);
                    //System.out.println("Hey");
                }
                else {//if(count > ((2 * lines)/3)){
                    pw2.println(X);
                    //System.out.println("Ney");
                }

                count = count + 1;

            }
            pw1.close();
            pw2.close();

            //csvWrite.close();
            curCSV.close();

            String svmTrainOptions = "-t 2 ";
            jniSvmTrain(svmTrainOptions+dataTrainPath+modelPath);

            String zz = jniSvmPredict(dataPredictPath+modelPath+outputPath);

            Log.e("length", zz);



        }


        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }



    }


/*    private void CreateAppFolderIfNeed(){
        // 1. create app folder if necessary
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            folder.mkdir();
            Log.d(LOG_TAG,"Appfolder is not existed, create one");
        } else {
            Log.w(LOG_TAG,"WARN: Appfolder has not been deleted");
        }


    }

    private void copyAssetsDataIfNeed(){
        String assetsToCopy[] = {"heart_scale_predict","heart_scale_train","heart_scale"};
        //String targetPath[] = {C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigTrain+".wav", C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigPredict+".wav",C.systemPath+C.INPUT_FOLDER+"SomeoneLikeYouShort.mp3"};

        for(int i=0; i<assetsToCopy.length; i++){
            String from = assetsToCopy[i];
            String to = appFolderPath+from;

            // 1. check if file exist
            File file = new File(to);
            if(file.exists()){
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: file exist, no need to copy:"+from);
            } else {
                // do copy
                boolean copyResult = copyAsset(getAssets(), from, to);
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: copy result = "+copyResult+" of file = "+from);
            }
        }
    }*/

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "[ERROR]: copyAsset: unable to copy file = "+fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


    private void generateparams() {

    }



    private void showItems() {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        //int col[] = { TITLE, BODY };
        //Cursor cur = db.query(TABLE_NAME, col, null, null, null, null, null);
        //Integer num = cur.getCount();
        //setTitle(Integer.toString(num));
    }




}

