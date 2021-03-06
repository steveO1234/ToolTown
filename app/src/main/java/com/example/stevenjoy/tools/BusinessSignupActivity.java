package com.example.stevenjoy.tools;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.DriverManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
//import butterknife.InjectView;


/**
 * Created by stevenjoy on 6/29/17.
 */

public class BusinessSignupActivity  extends AppCompatActivity {


    @BindView(R.id.business_Name) EditText _fname;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @BindView(R.id.input_Address) EditText _address;
    @BindView(R.id.input_city) EditText _city;
    @BindView(R.id.input_state) EditText _state;
    @BindView(R.id.input_Phone) EditText _phone;

    private Firebase mref;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // private DatabaseReference mDatabase;
    private String mUserId;

    private Map<String, String> users = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            startActivity(new Intent(BusinessSignupActivity.this, MainActivity.class));

        } else {
            mUserId = mFirebaseUser.getUid();
        }

        // add Users to create users object in json file
        mref = new Firebase("https://tools-aee24.firebaseio.com/Business");

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == true) {

                    users.put("Business" ,_fname.getText().toString());
                    users.put("Email" ,_emailText.getText().toString());
                    users.put("Address" ,_address.getText().toString());
                    users.put("City" ,_city.getText().toString());
                    users.put("State" ,_state.getText().toString());
                    users.put("Phone" ,_phone.getText().toString());

                    mref.child(mUserId).push().
                            child("Companies").setValue(users);


                    // Log.d("String", "worked");
                } else {
                    Log.d("String", "didnt work");
                }
            }
        });

    }


    public boolean validate() {
        boolean valid = true;

        /** login validations necessary characters needed*/
        if (_fname.getText().toString().isEmpty() || _fname.getText().toString().length() < 3) {
            _fname.setError("enter your first name");
            valid = false;
        } else {
            _fname.setError(null);
        }

//        if (_lname.getText().toString().isEmpty()) {
//            _lname.setError("Enter your last name");
//            valid = false;
//        } else {
//            _lname.setError(null);
//        }

        if (_emailText.getText().toString().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(_emailText.getText().toString()).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (_phone.getText().toString().length() < 10) {
            _phone.setError("Enter a valid phone number including area code");
            valid = false;
        } else {
            _phone.setError(null);
        }

//        if (_passwordText.getText().toString().isEmpty() ||
//                _passwordText.getText().toString().length() < 4 || _passwordText.getText().toString().length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");  // pop up screen for error
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        if (_age.getText().toString().isEmpty() || _age.getText().toString().length() > 3) {
//            _age.setError("Enter a valid age");
//            valid = false;
//        } else {
//            _age.setError(null);
//        }

        if (_address.getText().toString().isEmpty()) {
            _address.setError("Please enter Address");
            valid = false;
        } else {
            _address.setError(null);
        }

        if (_city.getText().toString().isEmpty()) {
            _city.setError("Please enter City");
            valid = false;
        } else {
            _city.setError(null);
        }

        if (_state.getText().toString().isEmpty()) {
            _state.setError("Please enter State");
            valid = false;
        } else {
            _state.setError(null);
        }
        return valid;
    }


    public void insertDatabase() {

        final String Fname = _fname.getText().toString();
       // final String L_name = _lname.getText().toString();
        final String UserEmail = _emailText.getText().toString();
       // final String UserAge = _age.getText().toString();
        final String UserAddress = _address.getText().toString();
        final String UserCity = _city.getText().toString();
        final String UserState = _state.getText().toString();
      //  final String Userpassword = _passwordText.getText().toString();
        final String PhoneNum = _phone.getText().toString();


        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    insert(Fname, UserAddress, UserCity, UserState, UserEmail, PhoneNum);
                    //insertLogin(UserEmail,Userpassword, Fname);
                    Log.d("String", "made it to new thread");


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void insert(String... params) {
        Log.d("String", params[0]);
        Log.d("String", params[1]);
        Log.d("String", params[2]);
        Log.d("String", params[3]);
        Log.d("String", params[4]);
        Log.d("String", params[5]);
        Log.d("String", params[6]);
        Log.d("String", params[7]);
        Log.d("String", params[8]);


        String sql = "INSERT INTO Table.users (Fname, Lname, Age, Address, City, State, Email, Password, Phone)" +
                " VALUES ('" + params[0] + "' , '" + params[1] + "', '" + params[2] + "', '" + params[3] + "', '" + params[4] +
                "', '" + params[5] + "', '" + params[6] + "', '" + params[7] + "','" + params[8] + "');";
        // String dbuserName = "root";
        // String dbpassword = "MPwlzJpOJOEDHBaa";
        try {
            Connection conn = DBConnection.doInBackground();
            if (conn != null) {
                Log.d("String","Connected to the database");
            }
            else {
                Log.d("String","not workign ");
            }
            // Class.forName("com.mysql.jdbc.Driver");
            // String url = "jdbc:mysql://35.188.56.186/tool-time?zeroDateTimeBehavior=convertToNull";
            // Connection c = DriverManager.getConnection(url, dbuserName, dbpassword);
            PreparedStatement st = conn.prepareStatement(sql);
            st.execute();
            st.close();
            //     c.close();
        }
        //catch (ClassNotFoundException e)  {
        //   e.printStackTrace();
        //}
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

