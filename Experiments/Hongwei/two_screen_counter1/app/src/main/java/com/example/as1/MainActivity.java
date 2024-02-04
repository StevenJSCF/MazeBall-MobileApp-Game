package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;

    Button openYouTubeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.toCounterBtn);

        openYouTubeBtn = findViewById(R.id.openYouTubeBtn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });


        openYouTubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley";


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(youtubeUrl));

                intent.setPackage("com.android.chrome");


                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);

                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No app can open YouTube.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}