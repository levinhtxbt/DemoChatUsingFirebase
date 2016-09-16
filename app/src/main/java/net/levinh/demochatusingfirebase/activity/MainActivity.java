package net.levinh.demochatusingfirebase.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.levinh.demochatusingfirebase.MainApplication;
import net.levinh.demochatusingfirebase.R;
import net.levinh.demochatusingfirebase.adapter.OnItemClickListener;
import net.levinh.demochatusingfirebase.adapter.RecyclerViewAdapter;
import net.levinh.demochatusingfirebase.models.Message;
import net.levinh.demochatusingfirebase.models.User;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView rcvMessage;
    EditText mEditTextMessage;
    Button mButtonSend;
    DatabaseReference mDatabase;
    RecyclerViewAdapter mAdapter;
    String mUser;
    boolean doubleBackToExitPressedOnce = false;
    ChildEventListener mMessageChildListener;
    ChildEventListener mUserChildListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getCurrentUser();
        initDatabase();

    }


    private void initView() {
        mEditTextMessage = (EditText) findViewById(R.id.tvMessage);
        mButtonSend = (Button) findViewById(R.id.btnSend);
        rcvMessage = (RecyclerView) findViewById(R.id.rcvMessage);
        mAdapter = new RecyclerViewAdapter(this, R.layout.item_recyclerview, this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true);
        rcvMessage.setLayoutManager(layout);
        rcvMessage.setHasFixedSize(true);
        rcvMessage.setAdapter(mAdapter);
        mButtonSend.setOnClickListener(this);
    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(MainApplication.URL_DATABASE + "/message");
        Query query = mDatabase.orderByChild("time");

        mMessageChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Message msg = dataSnapshot.getValue(Message.class);
                DatabaseReference dbUser = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(MainApplication.URL_DATABASE + "/users");

                dbUser.child(msg.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        msg.setDisplayName(user.getDisplayName());
                        mAdapter.pushMessage(msg);
                        rcvMessage.smoothScrollToPosition(mAdapter.getItemCount());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message msg = dataSnapshot.getValue(Message.class);
                if (msg != null) {
                    mAdapter.editMessage(msg);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message msg = dataSnapshot.getValue(Message.class);
                if (msg != null) {
                    mAdapter.removeMessage(msg);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mUserChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


    private void getCurrentUser() {
        mUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Log.d(TAG, "getCurrentUser: " + user.getDisplayName());
//        Log.d(TAG, "getCurrentUser: " + user.getPhotoUrl());
//        Log.d(TAG, "getCurrentUser: " + user.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addChildEventListener(mMessageChildListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(mMessageChildListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        if (mEditTextMessage.getText().toString().isEmpty())
            return;
        mDatabase.keepSynced(true);
        mDatabase.push().setValue(new Message(mUser, mEditTextMessage.getText().toString(),
                Calendar.getInstance().getTimeInMillis()));
        mEditTextMessage.setText("");
    }

    @Override
    public void onItemClick(Message msg) {
        Date date = new Date(msg.getTime());
        Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();
    }
}
