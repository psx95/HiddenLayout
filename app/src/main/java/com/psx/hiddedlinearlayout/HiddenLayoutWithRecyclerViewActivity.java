package com.psx.hiddedlinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.psx.hiddedlinearlayout.Adapter.HiddenOptionsRVAdapter;
import com.psx.hiddedlinearlayout.Models.Message;

import java.util.ArrayList;
import java.util.Calendar;

public class HiddenLayoutWithRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Message> messageArrayList;
    private String messagePreview = "Here, your message preview appears, slide to see hidden options";
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_layout_with_recycler_view);
        recyclerView = findViewById(R.id.list_rv);
        String tag = getIntent().getStringExtra("TAG");
        prepareDummyDataForMessages(10);
        setupRecyclerView(tag);
    }

    private void setupRecyclerView(String tag) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HiddenOptionsRVAdapter(messageArrayList,tag));
    }

    private void prepareDummyDataForMessages(int numberOfDummyMessages) {
        messageArrayList = new ArrayList<>();
        Message message;
        for (int i = 1; i<= numberOfDummyMessages; i++) {
            calendar.add(Calendar.DATE,-i);
            message = new Message("Sender "+i, messagePreview,calendar.getTime());
            messageArrayList.add(message);
        }
    }
}
