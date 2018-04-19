package com.psx.hiddedlinearlayout.Adapter;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.psx.hiddedlinearlayout.Models.Message;
import com.psx.hiddedlinearlayout.R;
import com.psx.hiddenlinearlayoutview.HiddenLayoutView;
import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HiddenOptionsRVAdapter extends RecyclerView.Adapter<HiddenOptionsRVAdapter.MyViewHolder> {

    private static final String TAG = HiddenOptionsRVAdapter.class.getSimpleName();
    private ArrayList<Message> messages = new ArrayList<>();
    private AnimationUpdateListeners.OverLayoutEventListener onOverLayout;
    private AnimationUpdateListeners.UnderLayoutEventListener onUnderLayout;
    private Context context;
    private String animationType;

    public HiddenOptionsRVAdapter(ArrayList<Message> messages, String animationType){
        this.messages = messages;
        this.animationType = animationType;
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
        if (animationType.equals("fling"))
            holder.hiddenLayoutView.changeAnimation(HiddenLayoutView.ANIMATION_FLING);
        DynamicAnimation dynamicAnimation = holder.hiddenLayoutView.getAnimationInEffect();
        if (dynamicAnimation instanceof SpringAnimation)
            Log.i(TAG,"Spring");
        else if (dynamicAnimation instanceof FlingAnimation)
            Log.i(TAG,"Fling");
        else
            Log.i(TAG,"NONE");
        View messageRow = holder.hiddenLayoutView.getInflatedOverLayout();
        TextView senderName = messageRow.findViewById(R.id.sender_name);
        TextView sentTime = messageRow.findViewById(R.id.sent_time);
        TextView msgPreview = messageRow.findViewById(R.id.msg_preview);
        senderName.setText(messages.get(position).getSenderName());
        sentTime.setText(changeTimeFormat(messages.get(position).getTime()));
        msgPreview.setText(messages.get(position).getMessagePreview());
        holder.hiddenLayoutView.setAnimationUpdateListeners(() -> Toast.makeText(context,"Max Pull event",Toast.LENGTH_SHORT).show());
    }

    private void setupClickListeners() {
        onOverLayout = v -> Toast.makeText(context,"Clicked On Revealed View",Toast.LENGTH_SHORT).show();
        onUnderLayout = view -> Toast.makeText(context,"Clicked On Hidden View",Toast.LENGTH_SHORT).show();
    }

    private String changeTimeFormat (Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
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
