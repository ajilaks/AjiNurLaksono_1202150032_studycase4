package com.oji.ajinurlaksono_1202150032_studycase4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void list(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public void cariGambar(View view) {
        Intent i = new Intent(this, CariGambar.class);
        startActivity(i);
    }
}
