package com.example.storegame.ui.editProFile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.R;
import com.example.storegame.databinding.FragmentEditProfileBinding;
import com.example.storegame.modle.Messages;
import com.example.storegame.modle.ResultUser;
import com.example.storegame.modle.User;
import com.example.storegame.ui.gallery.LoginFragment;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    FragmentEditProfileBinding binding;
    SharedPreferencesData sharedPreferencesData;
    RetrofitInstance retrofitInstance;
    LoginFragment.OnLoginComplete complete;
    int sex = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("ARG_PARAM1");
            mParam2 = getArguments().getString("ARG_PARAM2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesData = new SharedPreferencesData(requireContext());
        retrofitInstance = new RetrofitInstance();
        StoreGameAPI retrofit = retrofitInstance.getRetrofit(sharedPreferencesData.getToken()).create(StoreGameAPI.class);
        Call<ResultUser> callProfile = retrofit.getUserProfile();
        callProfile.enqueue(new Callback<ResultUser>() {
            @Override
            public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                if (response.isSuccessful()) {
                    User user = response.body().getUser();
                    binding.edtUsername.setText(user.getUsername());
                    binding.edtNameUser.setText(user.getName());
                    binding.edtBirthDay.setText(user.getBirthday());
                    binding.edtAddress.setText(user.getAddress());
                    binding.edtEmail.setText(user.getEmail());
                    if (user.getSex() == 1) {
                        binding.rMale.setChecked(true);
                    } else if (user.getSex() == 0) {
                        binding.rFemale.setChecked(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultUser> call, Throwable t) {

            }
        });
        binding.edtBirthDay.setOnClickListener(view12 -> {
            openPickDialog();
        });
        binding.btnSave.setOnClickListener(view1 -> {
            int radioID = binding.rGroup.getCheckedRadioButtonId();
            if (radioID == R.id.r_male) {
                sex = 1;
            } else if (radioID == R.id.r_female) {
                sex = 0;
            }
            User user = new User(binding.edtNameUser.getText().toString(), sex, binding.edtBirthDay.getText().toString(), binding.edtAddress.getText().toString(), binding.edtEmail.getText().toString());
            Call<Messages> call = retrofit.editProfile(user);
            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {
                    if (response.isSuccessful()) {
                        Toast toast = Toast.makeText(requireContext(), "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT);
                        toast.show();
                        complete.onLoginSuccess();
                    } else {
                        Toast toast = Toast.makeText(requireContext(), "Thất bại!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
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
        complete = (LoginFragment.OnLoginComplete) context;
    }

    public void openPickDialog() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                binding.edtBirthDay.setText(i + "-" + i1 + "-" + ++i2);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), onDateSetListener, year, month, day);
        datePickerDialog.show();

    }
}