package com.example.tugasrecyclerview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;


public class NewsAdd extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText title, desc;
    private ImageView imageView;
    private  Button saveNews, chooseImage;
    private Uri imageUri;
    private FirebaseFirestore dbNews;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_add);

        dbNews = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        imageView = findViewById(R.id.imageView);
        saveNews = findViewById(R.id.btnAdd);
        chooseImage = findViewById(R.id.btnChooseImage);

        progressDialog = new ProgressDialog(NewsAdd.this);
        progressDialog.setTitle("Loading...");

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsTitle = title.getText().toString().trim();
                String newsDesc = desc.getText().toString().trim();

                if (newsTitle.isEmpty() || newsDesc.isEmpty()) {
                    Toast.makeText(NewsAdd.this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                uploadImageToStorage(newsTitle, newsDesc);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImageToStorage(String newsTitle, String newsDesc) {
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference().child("news_images/" + System.currentTimeMillis() + ".jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveDate(newsTitle, newsDesc, imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveDate(String newsTitle, String newsDesc, String imageUrl) {
        Map<String, Object> news = new HashMap<>();
        news.put("title", newsTitle);
        news.put("desc", newsDesc);
        news.put("imageUrl", imageUrl);

        dbNews.collection("news")
                .add(news)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(NewsAdd.this, "News added successfully", Toast.LENGTH_SHORT).show();
                    title.setText("");
                    desc.setText("");
                    imageView.setImageResource(0);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(NewsAdd.this, "Error adding news: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.w("NewsAdd", "Error adding document", e);
                });
    }
}