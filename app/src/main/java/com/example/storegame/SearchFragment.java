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
import com.example.storegame.databinding.FragmentSearchBinding;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.ResultGames;
import com.example.storegame.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    RetrofitInstance retrofitInstance;
    StoreGameAPI retrofit;
    List<Game> gameList = new ArrayList<>();
    GamesVerticalAdapter gamesVerticalAdapter;
    HomeFragment.OnGameClick iClickItemGame;
    private static final String KEY_SEARCH = "key_search";

    private String keySearch;

    public SearchFragment() {
    }


    public static SearchFragment newInstance(String key_search) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SEARCH, key_search);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keySearch = getArguments().getString(KEY_SEARCH);
        }
        retrofitInstance = new RetrofitInstance();
        retrofit = retrofitInstance.getRetrofit("").create(StoreGameAPI.class);
        gamesVerticalAdapter = new GamesVerticalAdapter(requireContext(), id -> {
            iClickItemGame.sendIDGame(id);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Call<ResultGames> call = retrofit.getAllGames();
        binding.textKeySearch.setText("Tìm kiến game: " + keySearch);
        call.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    gameList = response.body().getGames();
                    List<Game> gamesSearch = new ArrayList<>();
                    for (Game game : gameList) {
                        if (game.getName().toLowerCase().contains(keySearch) || game.getName().toUpperCase().contains(keySearch) || game.getName().contains(keySearch)) {
                            gamesSearch.add(game);
                        }
                    }
                    if (gamesSearch.size() <= 0) {
                        binding.textNoSearch.setVisibility(View.VISIBLE);
                    } else {
                        binding.textNoSearch.setVisibility(View.GONE);
                    }
                    gamesVerticalAdapter.setData(gamesSearch);
                    binding.gameSearch.setAdapter(gamesVerticalAdapter);
                } else {
                    Toast toast = Toast.makeText(requireContext(), "Call games faille", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResultGames> call, Throwable t) {
                Toast toast = Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iClickItemGame = (HomeFragment.OnGameClick) context;
    }
}