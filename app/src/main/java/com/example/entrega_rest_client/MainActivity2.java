package com.example.entrega_rest_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private TextView id_txt;
    private TextView title_txt;
    private TextView singer_txt;
    private EditText editTitle;
    private EditText editSinger;
    private String id;
    private String title;
    private String singer;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        id = intent.getStringExtra("track_id");
        title = intent.getStringExtra("track_title");
        singer = intent.getStringExtra("track_singer");
        position = intent.getIntExtra("position", 0);

        id_txt = findViewById(R.id.txt_id);
        title_txt = findViewById(R.id.txt_title2);
        singer_txt = findViewById(R.id.txt_singer2);
        editTitle = findViewById(R.id.editTxt_title2);
        editSinger = findViewById(R.id.editTxt_singer2);


        id_txt.setText(id);
        title_txt.setText(title);
        singer_txt.setText(singer);
        editTitle.setText(title);
        editSinger.setText(singer);
    }

    public void click_accept(View v){
        title = editTitle.getText().toString();
        singer = editSinger.getText().toString();
        returnToMainActivity();
    }

    public void click_cancel(View v){
        returnToMainActivity();
    }

    public void returnToMainActivity(){
        Intent returnIntent = new Intent(MainActivity2.this, MainActivity.class);
        returnIntent.putExtra("position", position);
        returnIntent.putExtra("track_id", id);
        returnIntent.putExtra("track_title", title);
        returnIntent.putExtra("track_singer", singer);
        setResult(MainActivity.RESULT_OK, returnIntent);

        finish();
    }
}