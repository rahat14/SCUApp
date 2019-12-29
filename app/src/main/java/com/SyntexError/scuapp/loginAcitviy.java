package com.SyntexError.scuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.SyntexError.scuapp.adminPackage.adminDashBoard;
import com.SyntexError.scuapp.studentsPackage.studentDashBoard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class loginAcitviy extends AppCompatActivity {
    String status = null , email , pass , newEmail;
    EditText user  , pas ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //init buton


        getSupportActionBar().hide();

        final Button preceedBtn = findViewById(R.id.proceedBtn);
        final Button regBtn = findViewById(R.id.regBtn) ;
        user = findViewById(R.id.usernameEt);
        pas = findViewById(R.id.passEt);





        // button clicks for the button

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to the register btn

                Intent i = new Intent( getApplicationContext() , RegisterAcitivity.class);
                startActivity( i);


            }
        });


        preceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the string
                email = user.getText().toString() ;
                pass = pas.getText().toString();



                // go with the  login method


                    loginTheUser(email , pass) ;

            }
        }) ;





    }

    private void loginTheUser(String email, String pass) {

        // create the firebase  instance => and point the  database to the "user" directory
     final    DatabaseReference loginRef = FirebaseDatabase.getInstance().getReference("users");

        // now check user available via parent node  which unique

         newEmail=email.replace('.','+') ;
        final  String parentNode = newEmail+pass ;
        // now  connect to db
        Toast.makeText(getApplicationContext(),  "ff : " + parentNode , Toast.LENGTH_SHORT).show();

        loginRef.child(parentNode)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // reading the db
                // check the parentNode is there or not

                if(dataSnapshot.exists())
                {
                    // user exists
                    // now check if it is a  student  or  admin

                    loginRef.child(parentNode).child("state").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String state = dataSnapshot.getValue().toString() ;
                            if(state.equals("student"))
                            {
                                // user is a student and send user to the student dash board

                                saveLogin(state) ;
                                Intent oi = new Intent(getApplicationContext() , studentDashBoard.class );
                                startActivity(oi);


                            }
                            else
                            {
                                 // this user is a admin
                                saveLogin(state) ;
                                Intent oi = new Intent(getApplicationContext() , adminDashBoard.class );
                                startActivity(oi);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    Toasty.error(getApplicationContext()  , " Error: User Not Found With Details !! " ,  Toasty.LENGTH_LONG  , false).show();



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveLogin(String stat) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username" , newEmail) ;
        editor.putString("password", pass);
        editor.putString("state",stat) ;
        editor.apply();
        editor.commit();



    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if the user is all ready here
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        String   USERNAME  = prefs.getString("username", "No name defined");//"No name defined" is the default value.
        String PASS = prefs.getString("password", "No name defined");
        String stat = prefs.getString("state", "No name defined");

        if(stat.equals("student"))
        {

            Intent oi = new Intent(getApplicationContext() , studentDashBoard.class );
            startActivity(oi);

        }
        else if (stat.equals("admin"))
        {

            Intent oi = new Intent(getApplicationContext() , adminDashBoard.class );
            startActivity(oi);

        }
        else {

            // do nothing


        }



    }
}
