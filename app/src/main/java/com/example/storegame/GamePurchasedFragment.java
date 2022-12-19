package com.example.storegame;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.storegame.Adapter.GamesVerticalAdapter;
import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.databinding.FragmentGamePurchasedBinding;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.GameBought;
import com.example.storegame.modle.ResultGames;
import com.example.storegame.modle.ResultGamesBought;
import com.example.storegame.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GamePurchasedFragment extends Fragment {

    FragmentGamePurchasedBinding binding;
    RetrofitInstance retrofitInstance;
    StoreGameAPI retrofit;
    SharedPreferencesData sharedPreferencesData;
    List<Game> gameList = new ArrayList<>();
    GamesVerticalAdapter gamesVerticalAdapter;
    HomeFragment.OnGameClick iClickItemGame;

    public GamePurchasedFragment() {
    }

    public static GamePurchasedFragment newInstance(String param1, String param2) {
        GamePurchasedFragment fragment = new GamePurchasedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamePurchasedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofitInstance = new RetrofitInstance();
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        retrofit = retrofitInstance.getRetrofit(sharedPreferencesData.getToken()).create(StoreGameAPI.class);
        gamesVerticalAdapter = new GamesVerticalAdapter(requireContext(), id -> {
            iClickItemGame.sendIDGame(id);
        });
        Call<ResultGamesBought> call = retrofit.getGameBought();
        call.enqueue(new Callback<ResultGamesBought>() {
            @Override
            public void onResponse(Call<ResultGamesBought> call, Response<ResultGamesBought> response) {
                if (response.isSuccessful()) {
                    List<GameBought> gameBougths = response.body().getGames();
                    for (GameBought gameBought:gameBougths) {
                        gameList.add(gameBought.getGame());
                    }
                    gamesVerticalAdapter.setData(gameList);
                    binding.gamePurchased.setAdapter(gamesVerticalAdapter);
                    if (gameList.size() <= 0) {
                        binding.textNoSearch.setVisibility(View.VISIBLE);
                    } else {
                        binding.textNoSearch.setVisibility(View.GONE);
                    }
                } else {
                    Toast toast = Toast.makeText(requireContext(), "Call games faille", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResultGamesBought> call, Throwable t) {

            }
        });
//        call.enqueue(new Callback<ResultGames>() {
//            @Override
//            public void onResponse(Call<ResultGamesBought> call, Response<ResultGamesBought> response) {
//                if (response.isSuccessful()) {
//                    gameList = response.body().getGames();
//                    gamesVerticalAdapter.setData(gameList);
//                    binding.gamePurchased.setAdapter(gamesVerticalAdapter);
//                    if (gameList.size() <= 0) {
//                        binding.textNoSearch.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.textNoSearch.setVisibility(View.GONE);
//                    }
//                } else {
//                    Toast toast = Toast.makeText(requireContext(), "Call games faille", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultGamesBought> call, Throwable t) {
//                Toast toast = Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iClickItemGame = (HomeFragment.OnGameClick) context;
    }
}