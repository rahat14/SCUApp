package com.SyntexError.scuapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.SyntexError.scuapp.models.modelForStudents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class RegisterAcitivity extends AppCompatActivity {

    EditText name , pass , conpass , id , club , clubpos , email , phone ;
    String NAME  , pASS , CONPASS , ID , CLUB , Clubpos ,Mail , Ph ;

    ProgressBar progressBar   ;
    Button submitBtn  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        // init views

        name = findViewById(R.id.fullName);
        pass = findViewById(R.id.passEt);
        conpass = findViewById(R.id.conpassEt);
        id = findViewById(R.id.id);
        club = findViewById(R.id.clubEt);
        clubpos = findViewById(R.id.clubposEt);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.pContact);
        submitBtn = findViewById(R.id.submitBtn) ;
        progressBar = findViewById(R.id.pbar);
        progressBar.setVisibility(View.GONE);

        // btn click
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              // getting all the text from the from
                NAME = name.getText().toString() ;
                pASS = pass.getText().toString() ;
                CONPASS = conpass.getText().toString() ;
                ID = id.getText().toString() ;
                CLUB = club.getText().toString() ;
                Clubpos = clubpos.getText().toString() ;
                Mail = email.getText().toString() ;
                Ph = phone.getText().toString() ;
                // now check if they are empty or not
               if (TextUtils.isEmpty(NAME)  ||TextUtils.isEmpty(pASS)  ||TextUtils.isEmpty(CONPASS)  ||TextUtils.isEmpty(ID)
                       ||TextUtils.isEmpty(CLUB)  ||
                       TextUtils.isEmpty(Clubpos)  ||TextUtils.isEmpty(Mail)  ||TextUtils.isEmpty(Ph))
               {
                   // one if the above text is empty so , show a msg to  user
                   Toasty.error(getApplicationContext()  , " Fill the Data Properly ", Toasty.LENGTH_LONG  , false).show();

               }
               else {
                    // All ok  Now check the pass and conpas is same or not

                   if(pASS.equals(CONPASS))
                   {
                       // pass and confirm pass matches => go with the registration

                       registerUserInTheApp() ;



                   }
                   else {

                       Toasty.error(getApplicationContext()  , " Passwords doest match  ", Toasty.LENGTH_LONG  , false).show();

                   }




               }





            }
        });






    }

    private void registerUserInTheApp() {
        // calling the Firebase and making a instance
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users"); // calling the location of the data store

        // as all user will have the unique pass and email we will concate it and store as a parent node
        // so , we can easily  find the user here .

        // now call the user model and create user with information
        String parentNodee = Mail+pASS ;
        // removing . from parent node
        String parentNode=parentNodee.replace('.','+') ;

        modelForStudents  userModel = new modelForStudents(NAME , Mail ,ID , pASS ,Ph, CLUB,Clubpos , parentNode, "student") ;

        // now Write to the db
        userRef.child(parentNode).setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // upload done
                        progressBar.setVisibility(View.GONE);
                        // send the user to login page to login
                        Intent i  = new Intent(getApplicationContext() , loginAcitviy.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toasty.error(getApplicationContext()  , " Error:  "+e.getMessage(), Toasty.LENGTH_LONG  , false).show();

            }
        }) ;





    }
}
