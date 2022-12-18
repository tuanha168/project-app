package com.example.storegame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storegame.Click.IClickItemGame;
import com.example.storegame.databinding.ItemGameNgangBinding;
import com.example.storegame.modle.Game;

import java.util.ArrayList;
import java.util.List;

public class GamesVerticalAdapter extends RecyclerView.Adapter<GamesVerticalAdapter.ViewHolder> {
    List<Game> gameList = new ArrayList<>();
    Context context;
    IClickItemGame iClickItemGame;

    public GamesVerticalAdapter(Context context, IClickItemGame iClickItemGame) {
        this.context = context;
        this.iClickItemGame = iClickItemGame;
    }

    public void setData(List<Game> gameList) {
        this.gameList = gameList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGameNgangBinding binding = ItemGameNgangBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Game game = gameList.get(position);
        if (game != null) {
            holder.binding.textNameGame.setText(game.getName());
            holder.binding.textPrice.setText(game.getPrice() + "$");
            Glide.with(context).load(game.getFeatureImage()).into(holder.binding.imgGame);
            holder.binding.textCategory.setText("Thể Loại: " + game.getCategory().getName());
            holder.binding.textDownloadedNumber.setText("Download Number: " + game.getDownloadedNumber());
            holder.binding.itemGame.setOnClickListener(view -> {
                iClickItemGame.ClickItemGame(game.getId());
            });

        }
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGameNgangBinding binding;

        public ViewHolder(@NonNull ItemGameNgangBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
