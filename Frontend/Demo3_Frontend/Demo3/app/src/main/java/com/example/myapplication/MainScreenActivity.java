package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainScreenActivity extends AppCompatActivity {


    private TextView messageText;

    Button buttonToStore;
    Button buttonToInventory;

    Button buttonToAccount;

    Button buttonToFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mainscreen);
        messageText = findViewById(R.id.mainscreen_msg_txt);
        messageText.setText("Main");

        String userUrl = getIntent().getStringExtra("userUrl");



        buttonToStore = findViewById(R.id.button_to_store);
        buttonToInventory = findViewById(R.id.button_to_inventory);
        buttonToAccount = findViewById(R.id.button_to_account);
        buttonToFriends = findViewById(R.id.button_to_friends);

        buttonToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Store activity
                Intent intent = new Intent(MainScreenActivity.this, StoreActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Inventory activity
                Intent intent = new Intent(MainScreenActivity.this, InventoryActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Account activity
                Intent intent = new Intent(MainScreenActivity.this, AccountActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FriendList activity
                Intent intent = new Intent(MainScreenActivity.this, FriendListActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });
    }
}
