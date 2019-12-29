package com.SyntexError.scuapp.studentsPackage;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.models.modelsForReq;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class bookingPageActivity extends AppCompatActivity {


String nameOfEquipment  , quantityAvailable , pickTime  , pickDate  , retunDate , returnTime  , details ,
    quantityNeed ;
Button reqBtn ;
String amPmChecker , iteMID , userID , userName;

EditText quantityNeedEt  ,detailsEt  ;
TextView pickTimeTv , pickDateTv , returnDateTv , returntimeTv ;
DatePickerDialog datePickerDialog  ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_borrow_page);

        Intent i = getIntent();
        nameOfEquipment = i.getStringExtra("NAME") ;
        quantityAvailable = i.getStringExtra("QUANTITY") ;
        iteMID = i.getStringExtra("ITEMID") ;

        // intitview
        pickDateTv = findViewById(R.id.dateEdit);
        TextView title  = findViewById(R.id.title);
        pickTimeTv = findViewById(R.id.timeEdit);
        returnDateTv = findViewById(R.id.dropdateEdit);
        returntimeTv = findViewById(R.id.droptimeEdit);
        quantityNeedEt = findViewById(R.id.quatityEt);
        detailsEt = findViewById(R.id.detailsEt);
        reqBtn = findViewById(R.id.reqBtn);


        // now  get time and  date form the views by a  click
        title.setText("You Want To Book : " +nameOfEquipment );

        returntimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog  = new TimePickerDialog(bookingPageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay >= 12){
                            amPmChecker = "AM";

                        }
                        else  {
                            amPmChecker= "PM";
                        }

                        returntimeTv.setText(hourOfDay+":"+minute+" "+amPmChecker);
                        returnTime= returntimeTv.getText().toString();


                    }
                }, 0,0, false);


                timePickerDialog.show();

            }
        });

        pickTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog  = new TimePickerDialog(bookingPageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay >= 12){
                            amPmChecker = "AM";

                        }
                        else  {
                            amPmChecker= "PM";
                        }

                        pickTimeTv.setText(hourOfDay+":"+minute+" "+amPmChecker);
                        pickTime= pickTimeTv.getText().toString();


                    }
                }, 0,0, false);


                timePickerDialog.show();
            }
        });


        returnDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(bookingPageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        returnDateTv.setText(dayOfMonth + "/"+(month+1)+"/"+year);
                        retunDate = returnDateTv.getText().toString() ;

                    }
                } ,year , month , day);

                datePickerDialog.show();
            }
        });

        pickDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(bookingPageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        pickDateTv.setText(dayOfMonth + "/"+(month+1)+"/"+year);
                        pickDate = pickDateTv.getText().toString() ;

                    }
                } ,year , month , day);

                datePickerDialog.show();


            }
        });





        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the data from the views
                quantityNeed = quantityNeedEt.getText().toString()  ;
                details = detailsEt.getText().toString();

                if(TextUtils.isEmpty(quantityNeed) ||TextUtils.isEmpty(details) ||TextUtils.isEmpty(retunDate)
                        ||TextUtils.isEmpty(returnTime) ||TextUtils.isEmpty(pickDate) ||
                        TextUtils.isEmpty(pickTime) )
                {
                    Toasty.error(getApplicationContext() , "Please Give The Valid Value !!" , Toasty.LENGTH_LONG , false ).show();

                }
                else {

                    // check the quantity available

                    if(Integer.parseInt(quantityAvailable) >= Integer.parseInt(quantityNeed))
                    {
                        sendTheRequestToServer() ;
                    }
                    else {
                        Toasty.error(getApplicationContext() , "The Requested Quantity is not Available  !!" , Toasty.LENGTH_LONG ).show();

                    }



                }


            }
        });





    }

    private void sendTheRequestToServer() {




        // create request db link
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("reqDb");
    //    String itemID , quantity ,  fromDate,  toDate , comment , userID , status , isDamage , userName , reqID
        // create  req model for upload ;
        String reqID = mref.push().getKey() ;

        String  mail =userName.replace('+','.') ;
        modelsForReq model = new modelsForReq(iteMID  , quantityNeed ,pickTime + " " +pickDate , returnTime + " "+ retunDate
         , details , userID , "pending" , "no" , mail ,reqID ,nameOfEquipment  ) ;


        mref.child(reqID).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                AlertDialog dialog  = new AlertDialog.Builder(bookingPageActivity.this).create();
                dialog.setTitle("Your Request Has Been Submitted");
                dialog.setCancelable(false);
                dialog.setMessage("Please Wait While we Approve Your Request !! ");
                dialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if the user is all ready here
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
           userName  = prefs.getString("username", "No name defined");//"No name defined" is the default value.
        String PASS = prefs.getString("password", "No name defined");
        String stat = prefs.getString("state", "No name defined");

        userID = userName+ PASS ;


    }
}
