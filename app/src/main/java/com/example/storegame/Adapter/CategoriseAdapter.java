package com.example.storegame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storegame.databinding.ItemCategoriesBinding;
import com.example.storegame.modle.Category;
import com.example.storegame.modle.Game;
import com.example.storegame.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoriseAdapter extends RecyclerView.Adapter<CategoriseAdapter.ViewHolder> {

    List<Category> categories = new ArrayList<>();
    List<Game> gameList = new ArrayList<>();
    Context context;
    HomeFragment.OnGameClick iSendIDGame;

    public CategoriseAdapter(Context context, HomeFragment.OnGameClick iSendIDGame) {
        this.context = context;
        this.iSendIDGame = iSendIDGame;
    }

    public void setData(List<Category> categories, List<Game> gameList) {
        this.categories = categories;
        this.gameList = gameList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoriesBinding binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        GameAdapter gameAdapter = new GameAdapter(context, id -> {
                iSendIDGame.sendIDGame(id);
        });
        if (category != null) {
            holder.binding.textNameCategory.setText(category.getName());
            List<Game> games = new ArrayList<>();
            if (gameList != null) {
                for (Game game : gameList) {
                    if (game.getCategory().getId().equals(category.getId())) {
                        games.add(game);
                    }
                }
                gameAdapter.setData(games);
                holder.binding.listGame.setAdapter(gameAdapter);
            }
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoriesBinding binding;

        public ViewHolder(@NonNull ItemCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
