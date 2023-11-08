package com.example.smpRee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowBtn, loginBtn;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowBtn = (Button)findViewById(R.id.main_join_now_btn);
        loginBtn = (Button)findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //read the data to retrieve user key
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey != "") { //null

            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please Wait");
                loadingBar.setCanceledOnTouchOutside(false); //if the user clicks on screen dialog box will not disappear until it completes the process
                loadingBar.show();
            }

        }

//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Testing");
    }

    private void AllowAccess(final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        //to check if user available or not
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //under users node we have child which is phone no. which we r using for each user as unique
                //key which contains all info of a user

                if (snapshot.child("Users").child(phone).exists()) {

                    //retrieve the password and phone number of user
                    //parenDbName users node
                    //pass to users class
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);

                    //retrieve users data using setter.
                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getPassword().equals(password)){ //password enterd in edit text equals that in database

                            Toast.makeText(MainActivity.this, "Already Logged in", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Incorrect Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Account with this number does " +
                            "not exist", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();

                    //Toast.makeText(LoginActivity.this,"Create an Account First", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}