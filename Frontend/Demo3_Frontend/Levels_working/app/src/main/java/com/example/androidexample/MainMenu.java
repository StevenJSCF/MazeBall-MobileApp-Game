package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);


        Button btnStartLevel = findViewById(R.id.btnStartLevel);
        Button btnEditLevel = findViewById(R.id.btnEditLevel);
        Button btnLoadLevel = findViewById(R.id.btnLoadLevel);

        btnStartLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, LevelList.class);
                startActivity(intent);
            }
        });

        btnEditLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, LevelEditor.class);
                startActivity(intent);
            }
        });

        btnLoadLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define an intent to start the "Load Level" activity
                Intent intent = new Intent(MainMenu.this, LevelScreen.class);

                String worldData = "your_world_data_here";
                intent.putExtra("world-data", worldData);

                startActivity(intent);
            }
        });
    }
}
