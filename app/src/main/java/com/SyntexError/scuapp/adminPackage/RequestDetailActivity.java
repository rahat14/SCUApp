package com.SyntexError.scuapp.adminPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.models.modelForStudents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetailActivity extends AppCompatActivity {
    String nameOfEquipment   , pickTime    , retunDate , getReqID  , details ,
            quantityNeed , userID  ;
    EditText quantityNeedEt  ,detailsEt  ;
    TextView pickTimeTv , pickDateTv , returnDateTv , returntimeTv , studentName , StudentNum ;
    Button decBtn  , accptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        Intent o = getIntent() ;

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference("reqDb");
     nameOfEquipment =    o.getStringExtra("name") ;
     pickTime =  o.getStringExtra("pickDate") ;
     retunDate =  o.getStringExtra("toDate") ;
     details =  o.getStringExtra("details") ;
     getReqID =  o.getStringExtra("reqid") ;
     quantityNeed =  o.getStringExtra("quatity") ;
     userID = o.getStringExtra("userID") ;

        // intitview
        pickDateTv = findViewById(R.id.dateEdit);
        TextView title  = findViewById(R.id.title);

        returnDateTv = findViewById(R.id.dropdateEdit);

        quantityNeedEt = findViewById(R.id.quatityEt);
        detailsEt = findViewById(R.id.detailsEt);
        decBtn = findViewById(R.id.decBtn);
        accptBtn = findViewById(R.id.accptBtn);
        detailsEt.setFocusable(false);
        quantityNeedEt.setFocusable(false);

        title.setText("Equipment Name : " + nameOfEquipment);
        detailsEt.setText(details);
        quantityNeedEt.setText(quantityNeed);
        returnDateTv.setText(retunDate);

        accptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // acept the req
                //  now open  db connection and edit the req

             mref.child(getReqID).child("status").setValue("Running").addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {


                     AlertDialog dialog = new AlertDialog.Builder(RequestDetailActivity.this).create();
                     dialog.setTitle("Request Has Been Updated !!");
                     dialog.setMessage("Booking Has Been Accepted !!");
                     dialog.setButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             dialog.dismiss();
                             finish();

                         }
                     });

                     dialog.show();


                 }
             }) ;





            }
        });
        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mref.child(getReqID).child("status").setValue("Canceled").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        AlertDialog dialog = new AlertDialog.Builder(RequestDetailActivity.this).create();
                        dialog.setTitle("Request Has Been Canceled !!");
                        dialog.setMessage("Booking Has Been Declined !!");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                finish();

                            }
                        });

                        dialog.show();


                    }
                }) ;

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        studentName  =findViewById(R.id.studentName) ;
        StudentNum = findViewById(R.id.studentNum) ;



        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users").child(userID);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                modelForStudents model = dataSnapshot.getValue(modelForStudents.class) ;

                studentName.setText("Booker Name : "+model.getName());
                StudentNum.setText("Booker Phone : "+model.getPh());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
