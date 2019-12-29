package com.SyntexError.scuapp.adminPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.SyntexError.scuapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class runningReqDetails extends AppCompatActivity {
    String nameOfEquipment   , pickTime    , retunDate , getReqID  , details ,
            quantityNeed , userMail ;
    EditText quantityNeedEt  ,detailsEt  ;
    TextView pickTimeTv , pickDateTv , returnDateTv , returntimeTv ;
    Button recieveBtn  , sendMsgBtn  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_req_details);
        Intent o = getIntent() ;

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference("reqDb");
        nameOfEquipment =    o.getStringExtra("name") ;
        pickTime =  o.getStringExtra("pickDate") ;
        retunDate =  o.getStringExtra("toDate") ;
        details =  o.getStringExtra("details") ;
        getReqID =  o.getStringExtra("reqid") ;
        userMail = o.getStringExtra("mail") ;

        quantityNeed =  o.getStringExtra("quatity") ;

        // intitview
        pickDateTv = findViewById(R.id.dateEdit);
        TextView title  = findViewById(R.id.title);

        returnDateTv = findViewById(R.id.dropdateEdit);

        quantityNeedEt = findViewById(R.id.quatityEt);
        detailsEt = findViewById(R.id.detailsEt);
        recieveBtn = findViewById(R.id.receiveBtn);
        sendMsgBtn = findViewById(R.id.sendMSgBtn);
        detailsEt.setFocusable(false);
        quantityNeedEt.setFocusable(false);

        title.setText("Equipment Name : " + nameOfEquipment);
        detailsEt.setText(details);
        quantityNeedEt.setText(quantityNeed);
        returnDateTv.setText(retunDate);
        pickDateTv.setText(pickTime);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send  the msg to return

         userMail = userMail.replace("+" , ".") ;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + userMail));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "On Return Of An equipment");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "On Returning Of The Equipment Name :" + nameOfEquipment+
                        " You Borrowed . Please " +
                        "Return It Quickly ");


                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));



            }
        });

        recieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mref.child(getReqID).child("status").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        AlertDialog dialog = new AlertDialog.Builder(runningReqDetails.this).create();
                        dialog.setTitle("Request Has Been Completed  !!");
                        dialog.setMessage("Booking Has Been Returned !!");
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
}
