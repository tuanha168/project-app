package com.example.storegame.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.databinding.FragmentLoginBinding;
import com.example.storegame.modle.Login;
import com.example.storegame.modle.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    SharedPreferencesData sharedPreferencesData;
    OnLoginComplete complete;
    OnSignUp onSignUp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        binding.btnLogin.setOnClickListener(view1 -> {
            login();
        });
        binding.signUp.setOnClickListener(view12 -> {
            onSignUp.onSignUp();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void login() {
        String user = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        Login login = new Login(user, password);
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        StoreGameAPI retrofit = retrofitInstance.getRetrofit("").create(StoreGameAPI.class);
        Call<Token> call = retrofit.login(login);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    sharedPreferencesData.setLogin(true);
                    sharedPreferencesData.setToken(response.body().getToken());
                    complete.onLoginSuccess();
                } else {
                    Toast toast = Toast.makeText(requireContext(),"Tài khoản hoặc mật khẩu của bạn không đúng, vui lòng nhập lại!",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast toast = Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        complete = (OnLoginComplete) context;
        onSignUp = (OnSignUp) context;
    }

    public interface OnLoginComplete {
        void onLoginSuccess();
    }
    public  interface OnSignUp {
        void onSignUp();
    }
}