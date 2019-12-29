package com.SyntexError.scuapp.adminPackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.SyntexError.scuapp.R;
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

public class addProductActivity extends AppCompatActivity {
    private Bitmap compressedImageFile;
EditText nameEt , detailsEt , categoryEt , quatityEt  ;
Button addBtn ;
ImageView addImage ;
String  Name , Detaisl , Category  , Quantity ;
    DatabaseReference reference ;
    Uri mFilePathUri ;
    String ID;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_page);


        // init views

        addBtn = findViewById(R.id.addBtn );
        addImage = findViewById(R.id.pimage);
        nameEt = findViewById(R.id.titleEt);
        detailsEt = findViewById(R.id.detailsEt);
        categoryEt = findViewById(R.id.categoryEt);
        quatityEt = findViewById(R.id.quatityEt);

        // creating a progress dialoge

        progressDialog = new ProgressDialog(addProductActivity.this );
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait !!" );
        progressDialog.setMessage("While We Add The Item To Server  ");

        // listen for  btn clik

        // listen for  add image
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checking  if user  has permission to upload the image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(addProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(addProductActivity.this,
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the data from the views
                Quantity = quatityEt.getText().toString();
                Name = nameEt.getText().toString();
                Category = categoryEt.getText().toString() ;
                Detaisl = detailsEt.getText().toString() ;

                if(TextUtils.isEmpty(Quantity) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Category)
                        || TextUtils.isEmpty(Detaisl) )
                {
                    Toasty.error(getApplicationContext() , "Enter The Data Properly !!" ).show();

                }
                else {


                    addItemToServer() ;


                }



            }
        });


    }

    private void addItemToServer() {
        // check  pic is selected or not
        if (mFilePathUri!= null){

            // upload the image to server 1st then retrieve the image link => write it to the server

            // PHOTO UPLOAD

            File newImageFile = new File(mFilePathUri.getPath());

            try {

                compressedImageFile = new Compressor(addProductActivity.this)
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
            UploadTask filePath = mStorage.child(ID + ".jpg").putBytes(imageData);
            filePath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloaduri = uriTask.getResult();

                    // ID  create
                    reference = FirebaseDatabase.getInstance().getReference("items"); // creating the item location of db
                    String ID = reference.push().getKey()  ;
                    // create product model
                    modelForProduct product = new modelForProduct(downloaduri.toString() , Detaisl , Name , "0" , Quantity
                            , Category , ID) ;


                    reference.child(ID).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {



                            progressDialog.dismiss();
                            finish();
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

            Toasty.error(getApplicationContext() , "Please Select An Image For Upload !!" ).show();
        }




    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(addProductActivity.this);
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
                 addImage.setImageURI(mFilePathUri);






            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

    }
}

