package com.example.tugasrecyclerview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterList myAdapter;
    private List<ItemList> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        itemList = new ArrayList<>();
        itemList.add(new ItemList("Idul Adha 2024", "1 Dzulhijjah jatuh pada 8 Juni", "https://akcdn.detik.net.id/community/media/visual/2023/04/14/ilustrasi-pemantauan-hilal.jpeg?w=700&q=90"));
        itemList.add(new ItemList("Puasa Sunnah Dzulhijjah", "Niat Puasa 1-9 Zulhijah 2024, Tarwiyah dan Arafah", "https://media.kompas.tv/library/image/content_article/article_img/20210125114949.jpg"));
        itemList.add(new ItemList("Listrik Sumatra Padam", "Setelah Listrik Sumatera Padam, PLN akan Tambah Jaringan Transmisi", "https://statik.tempo.co/data/2024/06/06/id_1308215/1308215_720.jpg"));

        myAdapter = new AdapterList(itemList);
        recyclerView.setAdapter((RecyclerView.Adapter) myAdapter);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}