package com.example.storegame.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.storegame.Adapter.CategoriseAdapter;
import com.example.storegame.Adapter.GameAdapter;
import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.R;
import com.example.storegame.databinding.DialogPayBinding;
import com.example.storegame.databinding.FragmentHomeBinding;
import com.example.storegame.modle.Category;
import com.example.storegame.modle.Code;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.Messages;
import com.example.storegame.modle.ResultCategories;
import com.example.storegame.modle.ResultGames;
import com.example.storegame.ui.signup.SignupFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Category> categories = new ArrayList<>();
    List<Game> gameList = new ArrayList<>();
    CategoriseAdapter categoriseAdapter;
    RetrofitInstance retrofitInstance;
    StoreGameAPI retrofit;
    SharedPreferencesData sharedPreferencesData;
    OnGameClick onHomeClick;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        categoriseAdapter = new CategoriseAdapter(requireContext(), onHomeClick);
        retrofitInstance = new RetrofitInstance();
        retrofit = retrofitInstance.getRetrofit("").create(StoreGameAPI.class);
        if (sharedPreferencesData.isLogin()) {
            binding.viewLogin.setVisibility(View.GONE);
            binding.viewLogined.setVisibility(View.VISIBLE);
        }
        binding.listCategories.setAdapter(categoriseAdapter);
        Call<ResultCategories> call = retrofit.getAllCategories();
        call.enqueue(new Callback<ResultCategories>() {
            @Override
            public void onResponse(Call<ResultCategories> call, Response<ResultCategories> response) {
                if (response.isSuccessful()) {
                    categories = response.body().getCategories();
                    Call<ResultGames> call1 = retrofit.getAllGames();
                    call1.enqueue(new Callback<ResultGames>() {
                        @Override
                        public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                            if (response.isSuccessful()) {
                                gameList = response.body().getGames();
                                categoriseAdapter.setData(categories, gameList);
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
            }

            @Override
            public void onFailure(Call<ResultCategories> call, Throwable t) {
                Toast toast = Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        binding.btnSigun.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.nav_host_fragment_content_main, new SignupFragment());
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();
        });
        Call<ResultGames> callMostGames = retrofit.getFiveDownloadedMostGames();
        callMostGames.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    List<Game> gameListMost = response.body().getGames();

                    GameAdapter gameAdapter = new GameAdapter(requireContext(), id -> {
                        onHomeClick.sendIDGame(id);
                    });
                    gameAdapter.setData(gameListMost);
                    binding.list5MostDownloadedGames.setAdapter(gameAdapter);

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
        Call<ResultGames> callRecommendGames = retrofit.getFiveRecommend();
        callRecommendGames.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    List<Game> gameListMost = response.body().getGames();
                    GameAdapter gameAdapter = new GameAdapter(requireContext(), id -> {
                        onHomeClick.sendIDGame(id);

                    });
                    gameAdapter.setData(gameListMost);
                    binding.listTop5RecommendGames.setAdapter(gameAdapter);

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
        Call<ResultGames> callSaleGames = retrofit.getFiveSaleGames();
        callSaleGames.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    List<Game> gameListMost = response.body().getGames();
                    GameAdapter gameAdapter = new GameAdapter(requireContext(), id -> {
                        onHomeClick.sendIDGame(id);

                    });
                    gameAdapter.setData(gameListMost);
                    binding.listTop5SaleGames.setAdapter(gameAdapter);
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

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edSearch.getText().toString().isEmpty()) {
                    binding.btnSearch.setEnabled(false);
                } else {
                    binding.btnSearch.setEnabled(true);
                }
            }
        });
        binding.btnSearch.setOnClickListener(view1 -> {
            String keySearch = binding.edSearch.getText().toString();
            onHomeClick.sendKeySearch(keySearch);
        });

    }

    public void callGames() {
        Call<ResultGames> call = retrofit.getAllGames();
        call.enqueue(new Callback<ResultGames>() {
            @Override
            public void onResponse(Call<ResultGames> call, Response<ResultGames> response) {
                if (response.isSuccessful()) {
                    gameList = response.body().getGames();
                    Toast toast = Toast.makeText(requireContext(), "Call games ok", Toast.LENGTH_SHORT);
                    toast.show();
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onHomeClick = (OnGameClick) context;
    }

    public interface OnGameClick {
        void sendIDGame(String id);

        void sendKeySearch(String keySearch);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}