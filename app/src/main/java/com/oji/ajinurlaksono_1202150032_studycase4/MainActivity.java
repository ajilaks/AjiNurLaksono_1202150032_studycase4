package com.oji.ajinurlaksono_1202150032_studycase4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.time.Instant;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TEXT_STATE = "currentText";

    // The TextView where we will show results
    private TextView mTextView = null;
    ArrayList<String> list = new ArrayList<String>();
    private ListView mListView;
    private ProgressBar mProgressBar;
    String[] dMasuk;
    String[] lili;
    private AddItemToListView mAddItemToListView;
    private Button mStartAsyncTask, bHps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mTextView = (TextView) findViewById(R.id.textView1);
        dMasuk= getResources().getStringArray(R.array.menu); //refrencing variable
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar); //refrencing variable
        mListView = (ListView) findViewById(R.id.listView); //refrencing variable
        mStartAsyncTask = (Button) findViewById(R.id.button_startAsyncTask); //refrencing variable
        bHps = (Button) findViewById(R.id.hapus); //refrencing variable

      //  mListView.setVisibility(View.GONE);
        if(savedInstanceState!=null){
            list = savedInstanceState.getStringArrayList(TEXT_STATE);   //melakukan restore saveinstance ketika tidak sama dengan null
        }
        /**
         * setup adapter
         */
        //ArrayAdapter<String> adapter = new ArrayList<String>(this,R.layou,mUsers);
       // mListView.setAdapter(adapter);
        //inisiasi array
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
    mListView.setAdapter(adapter); //set adapter

//new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, new ArrayList<String>())
        //memulai asynctask ketika button di click
        mStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListView.setAdapter(adapter);

                //process adapter with asyncTask
                mAddItemToListView = new AddItemToListView();
                mAddItemToListView.execute();

            }
        });
        //button reset list
        bHps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // adapter.clear();

               //adapter.remove(dMasuk[0]);
               adapter.clear();
               // adapter.notifyDataSetChanged();
            }
        });
    }

    public void hapus(View view) {

    }
    /**
     * inner class for asynctask process
     */
    public class AddItemToListView  extends AsyncTask<Void, String, Void> {
//inisiasi variable
       private ArrayAdapter<String> mAdapter;
        private int counter=1;
        ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {//method pre execute, melakukan persiapan atau inisiasi
            mAdapter = (ArrayAdapter<String>) mListView.getAdapter(); //casting array

            //for progress dialog
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //setting progress dialog
            mProgressDialog.setTitle("Memuat Data");    //setting progress dialog
            mProgressDialog.setMessage("Tunggu Dulu ya....");   //setting progress dialog
            mProgressDialog.setCancelable(false); //setting progress dialog
            mProgressDialog.setProgress(0); //setting progress dialog

            //handling cancle asynctask
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAddItemToListView.cancel(true);
                    mProgressBar.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show(); //menampilkan progress dialog
        }
//method untuk memasukkan data ke dalam list
        @Override
        protected Void doInBackground(Void... params) {
            for (String item : dMasuk){
                publishProgress(item);
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(isCancelled()){
                    mAddItemToListView.cancel(true);
                }
            }
            return null;
        }
//singkronisasi dengan item yang dimasukkan dengan progress bar
        @Override
        protected void onProgressUpdate(String... values) {
            mAdapter.add(values[0]);

            Integer current_status = (int) ((counter/(float)dMasuk.length)*100);
            mProgressBar.setProgress(current_status);

            //set progress only working for horizontal loading
            mProgressDialog.setProgress(current_status);

            //set message will not working when using horizontal loading
            mProgressDialog.setMessage(String.valueOf(current_status+"%"));
            counter++;
        }
        //ketika sudah dilakukan proses stream, akan menjalankan proses postExecute
        @Override
        protected void onPostExecute(Void aVoid) {
            //menyembunyikan progreebar
            mProgressBar.setVisibility(View.GONE);

            //remove progress dialog
            mProgressDialog.dismiss();
            mListView.setVisibility(View.VISIBLE);
        }
    }
    //method save instance
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //melakukan return value dari list array
        outState.putStringArrayList(TEXT_STATE,list);
    }

}