package com.kabhinav.rocknrules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {

    private EditText username, userpassword, userEmail;
    private TextView userlogin;
    private Button regbutton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference();


                regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {

                    String user_Email = userEmail.getText().toString().trim();
                    String user_password = userpassword.getText().toString().trim();
                    String name = username.getText().toString().trim();
                    HashMap<String,String> dataMap = new HashMap<String,String >();
                    dataMap.put("Name",name);
                    dataMap.put("Email",user_Email);

                    mdatabase.push().setValue(dataMap);
                    firebaseAuth.createUserWithEmailAndPassword(user_Email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                setEmailVerfication();
                                Toast.makeText(RegistrationActivity.this, "Registration Successful..", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                            }

                            startActivity(new Intent(RegistrationActivity.this, MapsActivity.class));
                        }

                    });
                }

            }

        });



        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });
    }

    private void setEmailVerfication() {
          FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
          if(user!=null){
              user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(RegistrationActivity.this,"Check YOur Email For Verification",Toast.LENGTH_SHORT).show();

                      }
                  }
              });
          }

    }


    private void setupUIViews() {
        username = (EditText) findViewById(R.id.namer);
        userEmail = (EditText) findViewById(R.id.emailr);
        userpassword = (EditText) findViewById(R.id.passwordr);
        userlogin = (TextView) findViewById(R.id.tvlogin);
        regbutton = (Button) findViewById(R.id.btgr);
    }

    private Boolean validate() {
        Boolean result = false;
        String name = username.getText().toString();
        String password = userpassword.getText().toString();
        String email = userEmail.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }
}