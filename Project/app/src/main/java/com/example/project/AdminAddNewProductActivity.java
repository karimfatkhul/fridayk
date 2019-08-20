package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description , Price, Pname, saveCurrentDate, saveCurrentTime;
    private ImageView inputProductImage;
    private Button addNewProductButton;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private static final int GalleryPic = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageURL;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product_activity);

        inputProductImage = findViewById(R.id.select_product_image);
        addNewProductButton = findViewById(R.id.add_new_product);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);
        loadingbar = new ProgressDialog(this);

        CategoryName = getIntent().getExtras().get("category").toString();

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGalery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {
        Description = inputProductDescription.getText().toString();
        Pname = inputProductName.getText().toString();
        Price = inputProductPrice.getText().toString();

        if (imageUri==null){
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please write the product description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Please enter the product price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)){
            Toast.makeText(this, "Please write the product name", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingbar.setTitle("Adding new Product");
        loadingbar.setMessage("Please wait while adding the new product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+" "+saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask=filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message= e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Succesfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageURL=task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product image is save to Database Succesful", Toast.LENGTH_SHORT).show();
                            SaveProductInformation();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInformation() {
        HashMap<String,Object> productMap =new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageURL);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("name", Pname);

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    loadingbar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product is added succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    loadingbar.dismiss();
                    String message= task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error : " +message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void OpenGalery() {
        Intent galeryIntent=new Intent();
        galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, GalleryPic);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPic && resultCode == RESULT_OK && data!=null){
            imageUri=data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }
}
