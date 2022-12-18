package com.example.storegame.ui.detailGame;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.storegame.Adapter.ImageAdapter;
import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.R;
import com.example.storegame.databinding.FragmentDetailGameBinding;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.Messages;
import com.example.storegame.modle.ResultGames;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGameFragment extends Fragment {

    private static final String ID_GAME = "id";
    private FragmentDetailGameBinding binding;
    private Game game;
    StoreGameAPI retrofit;
    boolean isCheckCart = true;
    SharedPreferencesData sharedPreferencesData;
    RetrofitInstance retrofitInstance;
    private OnTitleToolBar titleToolBar;

    public DetailGameFragment() {
    }

    public static DetailGameFragment newInstance(Game game) {
        DetailGameFragment fragment = new DetailGameFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_GAME, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = (Game) getArguments().getSerializable(ID_GAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleToolBar.setTitleToolBar("Detail Game");
        retrofitInstance = new RetrofitInstance();
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        retrofit = retrofitInstance.getRetrofit(sharedPreferencesData.getToken()).create(StoreGameAPI.class);
        binding.txtReleased.setText(game.getReleaseDate());
        binding.txtNameGame.setText(game.getName());
        Glide.with(requireContext()).load(game.getFeatureImage()).into(binding.featureImage);
        binding.txtDeveloper.setText(game.getDeveloper());
        binding.txtDescription.setText(game.getDescription());
        binding.txtPublisher.setText(game.getPublisher());
        binding.progressBar.setVisibility(View.GONE);
        binding.txtDownloadedNumber.setText(game.getDownloadedNumber() + "");
        ImageAdapter imageAdapter = new ImageAdapter(requireContext());
        imageAdapter.setData(game.getImages());
        binding.listImg.setAdapter(imageAdapter);
        initView();
        binding.btnAddStore.setOnClickListener(view1 -> {

            Call<Messages> call = retrofit.addOrRemoveGameInStore(game.getId());
            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {
                    Toast toast;
                    if (response.isSuccessful()) {
                        toast = Toast.makeText(requireContext(), "Add " + game.getName() + " to cart successfully", Toast.LENGTH_SHORT);
                        if (isCheckCart) {
                            isCheckCart = false;
                        } else {
                            isCheckCart = true;
                        }
                    initView();
                    } else {
                        toast = Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        titleToolBar = (OnTitleToolBar) context;
    }

    public interface OnTitleToolBar {
        void setTitleToolBar(String titleToolBar);
    }

    void initView() {
        if (!sharedPreferencesData.isLogin()) {
            binding.btnAddStore.setVisibility(View.GONE);
        } else {
            binding.btnAddStore.setVisibility(View.VISIBLE);
            Call<ResultGames> call = retrofit.getGamesInCart();
            call.enqueue(new Callback<ResultGames>() {
                @Override
                public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                    if (response.isSuccessful()) {
                        List<Game> gameList = response.body().getGames();
                        for (Game game1 : gameList) {
                            if (game1.getId().equals(game.getId())) {
                                isCheckCart = true;
                                break;
                            } else {
                                isCheckCart = false;
                            }
                        }
                    }
                    if (isCheckCart) {
                        binding.btnAddStore.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                    } else {
                        binding.btnAddStore.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
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
}