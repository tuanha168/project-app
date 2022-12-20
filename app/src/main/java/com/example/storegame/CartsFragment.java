package com.example.storegame;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Debug;
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
import com.example.storegame.databinding.DialogPayBinding;
import com.example.storegame.databinding.FragmentStoreBinding;
import com.example.storegame.modle.Carts;
import com.example.storegame.modle.Code;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.Messages;
import com.example.storegame.modle.ResultGames;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartsFragment extends Fragment {

    private FragmentStoreBinding binding;

    SharedPreferencesData sharedPreferencesData;
    RetrofitInstance retrofitInstance;
    GamesVerticalAdapter gamesAdapter;
    List<Game> gameList;
    List<String> carts;
    Carts cart;
    StoreGameAPI retrofit;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartsFragment() {
    }

    public static CartsFragment newInstance(String param1, String param2) {
        CartsFragment fragment = new CartsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofitInstance = new RetrofitInstance();
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        gameList = new ArrayList<>();
        carts = new ArrayList<>();
        retrofit = retrofitInstance.getRetrofit(sharedPreferencesData.getToken()).create(StoreGameAPI.class);
        init();
        gamesAdapter = new GamesVerticalAdapter(requireContext(), id -> {

        });

        binding.btnPay.setOnClickListener(view1 -> {
            Call<Messages> call1 = retrofit.payGames(cart);
            call1.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {
                    Toast toast;
                    if (response.isSuccessful()) {
                        toast = Toast.makeText(requireContext(), "Mua thành công", Toast.LENGTH_SHORT);
                        init();
                    } else {
                        toast = Toast.makeText(requireContext(), "Không thành công", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    Toast toast = Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        });

    }

    public void init() {
        Call<ResultGames> call = retrofit.getGamesInCart();
        call.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    double total = 0;
                    gameList = response.body().getGames();
                    gamesAdapter.setData(gameList);
                    binding.gameStore.setAdapter(gamesAdapter);
                    for (Game game : gameList) {
                        carts.add(game.getId());
                        total = total + game.getPrice();
                    }
                    cart = new Carts(carts);
                    DecimalFormat myFormatter = new DecimalFormat("#.##");
                    String output = myFormatter.format(total);
                    binding.textTotalMoney.setText("Total Money: " + output + " $");
                } else {
                    Toast toast = Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT);
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
}