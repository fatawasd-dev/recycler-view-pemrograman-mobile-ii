package com.example.tugasrecyclerview;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterList myAdapter;
    private List<ItemList> itemList;
    private FloatingActionButton floatingActionButton;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        floatingActionButton = findViewById(R.id.floatAddNews);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading...");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        itemList = new ArrayList<>();
        myAdapter = new AdapterList(itemList);
        recyclerView.setAdapter(myAdapter);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddPage = new Intent(MainActivity.this, NewsAdd.class);
                startActivity(toAddPage);
            }
        });

        myAdapter.setOnItemClickListener(new AdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(ItemList item) {
                Intent intent = new Intent(MainActivity.this, NewsDetail.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("title", item.getJudul());
                intent.putExtra("desc", item.getSubJudul());
                intent.putExtra("imageUrl", item.getImageUrl());
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DefaultActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        progressDialog.show();
        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            itemList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemList item = new ItemList(
                                        document.getString("title"),
                                        document.getString("desc"),
                                        document.getString("imageUrl")
                                );

                                item.setId(document.getId());
                                itemList.add(item);
                                Log.d("data", document.getId() + " => " + document.getData());
                            }
                            myAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("data", "Error getting documents", task.getException());
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, DefaultActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}