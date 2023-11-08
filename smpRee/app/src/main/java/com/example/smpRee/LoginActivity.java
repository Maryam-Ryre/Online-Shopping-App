package com.example.smpRee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNo, InputPassword;
    private Button LoginBtn;
    private ProgressDialog loadingBar;

    private TextView AdminLink, NotAdminLink;

    private String parentDbName = "Users";

    //checkbox on login activity define & initialise it
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginBtn = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNo = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox)findViewById(R.id.remember_me_chkb);
        // initialise paper

        Paper.init(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();

            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginBtn.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins"; //parent name for all admins inside database
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginBtn.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users"; //parent name for all admins inside database
            }
        });
    }

    private void loginUser() {

        String phone = InputPhoneNo.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Write your Phone # ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Write your Password ", Toast.LENGTH_SHORT).show();
        }
        else //allow user to login
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait, we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false); //if the user clicks on screen dialog box will not disappear until it completes the process
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {

        // store values to Prevalent class
        if(chkBoxRememberMe.isChecked()) {

            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        //to check if user available or not
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //snapshot.child("Users") we'll be using same activity for admin so wont write directly like this
                //under users node we have child which is phone no. which we r using for each user as unique
                //key which contains all info of a user

                if (snapshot.child(parentDbName).child(phone).exists()) {

                    //retrieve the password and phone number of user
                    //parenDbName users node
                    //pass to users class
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    //retrieve users data using setter..
                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getPassword().equals(password)){ //password entered in edit text equals that in database

                            if(parentDbName.equals("Admins")){

                                Toast.makeText(LoginActivity.this, "Admin Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                // intent = new Intent(LoginActivity.this, AdminAddNewProductActivity.class);
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users")){

                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this,"Account with this number does " +
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