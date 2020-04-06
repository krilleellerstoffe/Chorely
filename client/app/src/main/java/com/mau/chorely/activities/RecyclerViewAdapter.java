package com.mau.chorely.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mau.chorely.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<ListItem> mItemList;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView1;
        private TextView mTextView2;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewGroupList);
            mTextView1 = itemView.findViewById(R.id.textViewGroupCard1);
            mTextView2 = itemView.findViewById(R.id.textViewGroupCard2);
        }
    }


    public RecyclerViewAdapter (ArrayList<ListItem> itemList){
        this.mItemList = itemList;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_groups, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ListItem currentItem = mItemList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
