package com.example.androidexample;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Drawnotification {
    static String gamemode;
    static String levelID;

    @SuppressLint("SetTextI18n")
    public static void showGameInvitationDialog(Context context, String invitationMessage) {

        String userId = "";
        String userName = "";
        String SessionID = "";
         String gamemode = "";
         String levelID = "";


        String[] parts = invitationMessage.split(",");

        if (parts.length == 5) {
            userId = parts[0];
            SessionID = parts[1];
            userName = parts[2];
            gamemode = parts[3];
            levelID = parts[4];

        }
        System.out.println("UseerID"+userId);
        if(!userId.equals(MainActivity.getID())){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Create a linear layout for the dialog content
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // Create a TextView for the message
        TextView textViewMessage = new TextView(context);
        textViewMessage.setText(userName + " has invited you to "+gamemode+" a game!");
        textViewMessage.setTextSize(18);
        textViewMessage.setGravity(Gravity.CENTER);
        layout.addView(textViewMessage);

        // Create a horizontal layout for the buttons
        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setGravity(Gravity.CENTER);

        // Create an Accept ImageView
        ImageView imageViewAccept = new ImageView(context);
        imageViewAccept.setImageResource(R.drawable.ic_custom_check);
        imageViewAccept.setLayoutParams(new LinearLayout.LayoutParams(
                120, // Set the width of the button
                120  // Set the height of the button
        ));


        // Create a Decline ImageView
        ImageView imageViewDecline = new ImageView(context);
        imageViewDecline.setImageResource(R.drawable.ic_ignore);
        imageViewDecline.setLayoutParams(new LinearLayout.LayoutParams(
                120, // Set the width of the button
                120  // Set the height of the button
        ));


        // Add the buttons to the horizontal layout
        buttonsLayout.addView(imageViewAccept);
        buttonsLayout.addView(imageViewDecline);

        // Add the buttons layout to the main layout
        layout.addView(buttonsLayout);

        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        imageViewAccept.setClickable(true);

        String finalSessionID = SessionID;
        String finalGamemode = gamemode;
        String finalLevelID = levelID;
        imageViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Accept");
                dialog.dismiss();
                loadGame(context, finalSessionID, finalGamemode, finalLevelID);

            }
        });
        imageViewDecline.setClickable(true);
        imageViewDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the invitation decline
                // You can perform any necessary actions here

                System.out.println("Decline");
                dialog.dismiss();

            }
        });
    }
    private static void loadGame(Context context, String SessionID, String gamemode, String levelID){
        if(gamemode.equals("edit")) {
            Intent intent = new Intent(context, LevelEditor.class);
            intent.putExtra("userId",SessionID );
            context.startActivity(intent);
        }
        else if(gamemode.equals("play")) {
            Intent intent = new Intent(context, LevelScreen.class);
            System.out.println(levelID);
            intent.putExtra("selectedLevel", Integer.parseInt(levelID) );
            intent.putExtra("userId",SessionID);
            context.startActivity(intent);
        }
    }
}
