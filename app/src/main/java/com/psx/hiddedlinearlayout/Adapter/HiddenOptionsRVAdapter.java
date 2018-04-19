package com.psx.hiddedlinearlayout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.psx.hiddedlinearlayout.Models.Message;
import com.psx.hiddedlinearlayout.R;
import com.psx.hiddenlinearlayoutview.HiddenLayoutView;
import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;

import java.util.ArrayList;

public class HiddenOptionsRVAdapter extends RecyclerView.Adapter<HiddenOptionsRVAdapter.MyViewHolder> {

    private ArrayList<Message> messages = new ArrayList<>();
    private AnimationUpdateListeners.OverLayoutEventListener onOverLayout;
    private AnimationUpdateListeners.UnderLayoutEventListener onUnderLayout;
    private Context context;

    public HiddenOptionsRVAdapter (ArrayList<Message> messages){
        this.messages = messages;
        setupClickListeners();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_row,parent,false);
        this.context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.hiddenLayoutView.setUnderLayoutEventListener(onUnderLayout);
        holder.hiddenLayoutView.setOverLayoutEventListener(onOverLayout);
        holder.hiddenLayoutView.setAnimationUpdateListeners(() -> Toast.makeText(context,"Max Pull event",Toast.LENGTH_SHORT).show());
    }

    private void setupClickListeners() {
        onOverLayout = v -> Toast.makeText(context,"Clicked On Revealed View",Toast.LENGTH_SHORT).show();
        onUnderLayout = new AnimationUpdateListeners.UnderLayoutEventListener() {
            @Override
            public void onUnderLayoutClickRecieved(View view) {
                Toast.makeText(context,"Clicked On Hidden View",Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        HiddenLayoutView hiddenLayoutView;

        public MyViewHolder(View itemView) {
            super(itemView);
            hiddenLayoutView = itemView.findViewById(R.id.hidden_view);
        }
    }
}
