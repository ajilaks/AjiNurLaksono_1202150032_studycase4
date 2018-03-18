package com.oji.ajinurlaksono_1202150032_studycase4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.InputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class CariGambar extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private Button bCari;
    private EditText eCari;
    private ImageView iURL;
    String url;
    private static final String TEXT_STATE = "currentText";
    Drawable drawable;
    Bitmap btm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_gambar); // Image link from internet
        mProgressBar = (ProgressBar) findViewById(R.id.progressbarCari); //refrencing variable
        eCari = (EditText) findViewById(R.id.eCari); //refrencing variable
        bCari = (Button) findViewById(R.id.bCari); //refrencing variable
        iURL = (ImageView) findViewById(R.id.image_view); //refrencing variable

        if(savedInstanceState!=null){
            btm = (Bitmap) savedInstanceState.getParcelable("as");  //melakukan restore saveinstance ketika tidak sama dengan null
iURL.setImageBitmap(btm);
        }

        bCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = eCari.getText().toString();
                new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view)) // ketika buttin di click, mengeksekusi inner class DownloadImageFromInternet
                        .execute(url);
                //eCari.setText();

            }
        });

    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        //ImageView gambarUrl;
        private int counter=1;
        ProgressDialog mProgressDialog = new ProgressDialog(CariGambar.this);
        @Override
        protected void onPreExecute() { //method pre execute, melakukan persiapan atau inisiasi
         //   mAdapter = (ArrayAdapter<String>) mListView.getAdapter(); //casting suggestion

            //for progress dialog
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //setting progress dialog
            mProgressDialog.setTitle("Memuat Gambar"); //setting progress dialog
            mProgressDialog.setMessage("Tunggu Dulu ya...."); //setting progress dialog
            mProgressDialog.setCancelable(false); //setting progress dialog
            mProgressDialog.setProgress(0); //setting progress dialog

            //handling cancle asynctask
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mAddItemToListView.cancel(true);
                    mProgressBar.setVisibility(View.VISIBLE); //menghilangkan progress dialog
                    dialog.dismiss();
                }
            });
            mProgressDialog.show(); //menampilkan progress dialog
        }

        public DownloadImageFromInternet(ImageView imageView) {
            iURL = imageView;
            Toast.makeText(getApplicationContext(), "Tunggu sebentar...", Toast.LENGTH_SHORT).show();
        }
//method yang dijalankan, disini akan diimplementasi link yang dari url menjadi file yang dapat dibaca yaitu bitmap
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            //try catch untuk melakuka stream
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
    //ketika sudah dilakukan proses stream, akan menjalankan proses postExecute
        protected void onPostExecute(Bitmap result) {
            if(result!=null){ //ketika result bitmap nya tidak sama dengan null atau ada hasilnya gambar dari internet
                drawable = new BitmapDrawable(getResources(), result);
                iURL.setImageDrawable(drawable);
                //gambarUrl.setImageBitmap(result);
                btm = result;


            }else{ //ketika hasil gambar dari internet tidak ditemukan
                iURL.setImageBitmap(null);
                btm = result;
                // akan menampilkan alert dialog
                //Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CariGambar.this);
                alertDialogBuilder.setMessage("Koneksi Internet Bermasalah atau Gambar Tidak Tersedia!");         //menggunakan pemanis untuk pesanan berhasil
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            mProgressBar.setVisibility(View.GONE);

            //remove progress dialog
            mProgressDialog.dismiss();
            //mListView.setVisibility(View.VISIBLE);
        }

    }
    //method save instance
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//melakukan return value dari bitmap
    outState.putParcelable("as", btm);

    }
}