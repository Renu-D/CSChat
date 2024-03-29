package com.example.cschat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.HashMap;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1,reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://cschat-5a959.firebaseio.com/messages/"+UserDetails.username+"_"+UserDetails.chatWith);
        reference2 = new Firebase("https://cschat-5a959.firebaseio.com/messages/"+UserDetails.chatWith+"_"+UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals((""))) {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("message",messageText);
                    map.put("user",UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.getText().clear();
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)) {
                    addMessageBox("You:-\n"+message,1);
                }
                else {
                    addMessageBox(UserDetails.chatWith+":-\n"+message,2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message,int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(lp);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        if(type==1) {
            lp.setMargins(0,0,200,10);
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else {
            lp.setMargins(200,0,0,10);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
