package com.example.storegame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storegame.Click.IClickItemGame;
import com.example.storegame.databinding.ItemGameBinding;
import com.example.storegame.databinding.ItemImgGameBinding;
import com.example.storegame.modle.Game;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<String> imgURLs = new ArrayList<>();
    Context context;
    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> imgURL) {
            this.imgURLs = imgURL;
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImgGameBinding binding = ItemImgGameBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imgURL = imgURLs.get(position);
        if(imgURL != null) {
            Glide.with(context).load(imgURL).into(holder.binding.imgGameF);
        }
    }

    @Override
    public int getItemCount() {
        return imgURLs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemImgGameBinding binding;

        public ViewHolder(@NonNull ItemImgGameBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
