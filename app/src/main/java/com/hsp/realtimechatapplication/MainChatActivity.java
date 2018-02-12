package com.hsp.realtimechatapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    private static final String EMPTY_STRING="";

    private String displayName;
    private ListView chatListView;
    private EditText inputText;
    private ImageButton sendButton;

    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // set up the display name and get the Firebase reference
        setupDisplayName();
        dbReference= FirebaseDatabase.getInstance().getReference();

        inputText = (EditText) findViewById(R.id.messageInput);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        // send the message when the enter button is pressed
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });

        // send message if button is pressed
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    // retrieve the data stored localy on the device, namely the display name from the Shared Preferences
    private void setupDisplayName() {
        SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        displayName = sharedPreferences.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        if(displayName==null) {
            displayName="Anonymous";
        }
    }


    private void sendMessage() {
        Log.d("Chat", "Sending something...");
        String input= inputText.getText().toString();
        if(input.isEmpty() == false) {
            InstantMessage chat= new InstantMessage(input, displayName);
            //child() specifies that all our messages are stored in a place called "messages"
            //push() gives us a reference to this child location and then we set the value
            dbReference.child("messages").push().setValue(chat);
            inputText.setText(EMPTY_STRING);
        }
    }
}
