package com.SyntexError.scuapp.studentsPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.adminPackage.addProductActivity;
import com.SyntexError.scuapp.adminPackage.runningReqDetails;
import com.SyntexError.scuapp.models.modelForProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class returnActivity extends AppCompatActivity {

    String nameOfEquipment   , pickTime    , retunDate , getReqID  , details ,
            quantityNeed , userMail ;
    EditText quantityNeedEt  ,detailsEt  ;
    TextView pickTimeTv , pickDateTv , returnDateTv , returntimeTv ;
    Button recieveBtn  , sendMsgBtn  ;
    Uri mFilePathUri ;
    ProgressDialog progressDialog ;


    private Bitmap compressedImageFile;
    ImageView returnImage ;
     DatabaseReference mref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        Context context;
        progressDialog =new ProgressDialog(returnActivity.this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Adding Returned Image Data !!");
        Intent o = getIntent();

         mref = FirebaseDatabase.getInstance().getReference("reqDb");
        nameOfEquipment = o.getStringExtra("name");
        pickTime = o.getStringExtra("pickDate");
        retunDate = o.getStringExtra("toDate");
        details = o.getStringExtra("details");
        getReqID = o.getStringExtra("reqid");
        userMail = o.getStringExtra("mail");


        quantityNeed = o.getStringExtra("quatity");

        // intitview
        pickDateTv = findViewById(R.id.dateEdit);
        TextView title = findViewById(R.id.title);

        returnDateTv = findViewById(R.id.dropdateEdit);

        quantityNeedEt = findViewById(R.id.quatityEt);
        detailsEt = findViewById(R.id.detailsEt);
        recieveBtn = findViewById(R.id.receiveBtn);
        detailsEt.setFocusable(false);
        quantityNeedEt.setFocusable(false);
        returnImage = findViewById(R.id.returnIMage);
        title.setText("Equipment Name : " + nameOfEquipment);
        detailsEt.setText(details);
        quantityNeedEt.setText(quantityNeed);
        returnDateTv.setText(retunDate);
        pickDateTv.setText(pickTime);


        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking  if user  has permission to upload the image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(returnActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(returnActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        BringImagePicker();


                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }


            }
        });


        recieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mFilePathUri != null)
                {


                    // PHOTO UPLOAD

                    File newImageFile = new File(mFilePathUri.getPath());

                    try {

                        compressedImageFile = new Compressor(returnActivity.this)
                                .setMaxHeight(920)
                                .setMaxWidth(920)
                                .setQuality(40)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    progressDialog.show();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] imageData = baos.toByteArray();
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference("completedDonationPic");
                    UploadTask filePath = mStorage.child(getReqID + ".jpg").putBytes(imageData);
                    filePath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloaduri = uriTask.getResult();

                            // ID  create

                            // create product model



                            mref.child(getReqID).child("returnedImage").child("image").setValue(downloaduri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                  finishTheBooking();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            })  ;



                        }
                    })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // hide progrees bar
                                    //   mprogressDialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                            //  progressDialog.setTitle(" Uploading ...........");
                        }
                    });




                }
                else {


                    Toasty.error(getApplicationContext() , "Please Upload The Returned Item Image !!!"  ,Toasty.LENGTH_LONG, false).show();

                }



        }


        });


    }

    private  void  finishTheBooking ()
    {
        progressDialog.dismiss();

        mref.child(getReqID).child("status").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                AlertDialog dialog = new AlertDialog.Builder(returnActivity.this).create();
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
        });

    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(returnActivity.this);
    }


    // check for permission
    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);



        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();
                returnImage.setImageURI(mFilePathUri);






            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

    }
}
