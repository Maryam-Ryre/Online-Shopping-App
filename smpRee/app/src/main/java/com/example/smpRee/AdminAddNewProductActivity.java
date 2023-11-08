package com.example.smpRee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductBtn;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;

    //create a folder by using StorageReference in firebase storage & inside that we will add our images
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;

    private ProgressDialog loadingBar;


    //FirebaseStorage storage;
    //StorageReference filesRef;
    // storage = FirebaseStorage.getInstance();
    //filesRef = storage.getReference().child("files");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);


        CategoryName = getIntent().getExtras().get("category").toString();
        //Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();

        //child name - folder which we'll be creating in storage - will be by this name
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductBtn = (Button) findViewById(R.id.add_new_product);

        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery(); // from where admin will select image
            }
        });

        AddNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();
            }
        });
    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {

            ImageUri = data.getData();
            //display image uri on image view
            InputProductImage.setImageURI(ImageUri);

        }
    }


    private void ValidateProductData(){

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this,"Product Image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)) {
            Toast.makeText(this,"Product Description is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)) {
            Toast.makeText(this,"Product Price is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname)) {
            Toast.makeText(this,"Product Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else{ // store these in database

            StoreProductInformation();
        }
    }



    private void StoreProductInformation() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Admin Please Wait, While New Product is Being Added");
        loadingBar.setCanceledOnTouchOutside(false); //if the user clicks on screen dialog box will not disappear until it completes the process
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("h:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        // random key combine time and date
        productRandomKey = saveCurrentDate + saveCurrentTime;

        //store image uri inside firebase storage
        //then we'll be able to store the link of that image in database & display to user

        //pass image uri that contains image
        //default name of image name
        //both strings +
        //link of product image
        //storing inside firebase storage by this name
        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment()
        + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //display to admin why image is not uploading
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // to firebase storage

                //Toast.makeText(AdminAddNewProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                // going to perform url task, get url or image link
                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        //return null;

                        if (!task.isSuccessful()){
                            // loadingBar.dismiss();
                            throw task.getException();
                        }

                        // get image url
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }

                    //remove semicolon next line & tell the admin that task is successful
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){ //if image loaded successfully

                            downloadImageUrl = task.getResult().toString(); // to get link or the url

                            /*Toast.makeText(AdminAddNewProductActivity.this, "Got Product Image" +
                                    " successfully ", Toast.LENGTH_SHORT).show();*/

                            // store all info in firebase database

                            SaveProductInfoToDatabase();

                        }
                    }
                });

            }
        });
    }




    private void SaveProductInfoToDatabase() {

        //store using hashmap
        HashMap<String, Object> productMap = new HashMap<>();
        //putting data to it
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl); //link is in downloadImageUrl
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        // now create a database reference and create another node for all the products
        //first give a unique random key for each product - products not replaced with each other - each product must contain unique info
        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this,
                                    "Product Successfully Added", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                        else{

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,
                                    "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}