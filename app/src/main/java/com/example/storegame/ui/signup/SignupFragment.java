package com.example.storegame.ui.signup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.R;
import com.example.storegame.databinding.FragmentSignupBinding;
import com.example.storegame.modle.PostUser;
import com.example.storegame.modle.Token;
import com.example.storegame.ui.gallery.LoginFragment;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    FragmentSignupBinding binding;
    SharedPreferencesData sharedPreferencesData;
    OnSignUp complete;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SignupFragment() {

    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSigun.setOnClickListener(v -> {
            signUp();
        });

    }

    private void signUp() {
        PostUser login = new PostUser();
        String nameUser = binding.nameUser.getText().toString().trim();
        String user = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        if (password.equals(confirmPassword)) {
            if (validatePassWord(password)) {
                login.setUsername(user);
                login.setName(nameUser);
                login.setPassword(password);
                RetrofitInstance retrofitInstance = new RetrofitInstance();
                StoreGameAPI retrofit = retrofitInstance.getRetrofit("").create(StoreGameAPI.class);
                Call<Token> call = retrofit.signUp(login);

                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {

                        if (response.isSuccessful()) {
                            Toast toast = Toast.makeText(requireContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT);
                            toast.show();
                            complete.onSignUpSuccess();
                        } else {
                            Toast toast = Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast toast = Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Bạn vui lòng nhập mật khẩu tối đa 8 ký tự, " +
                                "sử dụng ít nhất một số, chữ hoa, chữ thường!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Xác nhận mật khẩu không đúng !",
                    Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        complete = (OnSignUp) context;
    }

    private boolean validatePassWord(String pass) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        if (!pattern.matcher(pass).matches()) {
            return false;
        } else {
            return true;
        }
    }

    public interface OnSignUp {
        void onSignUpSuccess();
    }
}