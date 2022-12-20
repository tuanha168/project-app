package com.example.storegame;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.storegame.Api.RetrofitInstance;
import com.example.storegame.Api.StoreGameAPI;
import com.example.storegame.Data.SharedPreferencesData;
import com.example.storegame.databinding.ActivityMainBinding;
import com.example.storegame.modle.Game;
import com.example.storegame.modle.ResultGame;
import com.example.storegame.modle.ResultUser;
import com.example.storegame.modle.User;
import com.example.storegame.ui.detailGame.DetailGameFragment;
import com.example.storegame.ui.editProFile.EditProfileFragment;
import com.example.storegame.ui.gallery.LoginFragment;
import com.example.storegame.ui.home.HomeFragment;
import com.example.storegame.ui.signup.SignupFragment;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginComplete, HomeFragment.OnGameClick, DetailGameFragment.OnTitleToolBar, LoginFragment.OnSignUp , SignupFragment.OnSignUp {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    View headerView;
    SharedPreferencesData sharedPreferencesData;
    RetrofitInstance retrofitInstance;
    TextView userName, email, money;
    NavigationView navigationView;
    Intent refresh;
    ImageButton buttonEditProfile;
    MenuItem menuCarts, menuBought;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesData = new SharedPreferencesData(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        retrofitInstance = new RetrofitInstance();
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        headerView = binding.navView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        money = headerView.findViewById(R.id.txt_money);
        email = headerView.findViewById(R.id.email);

        navigationView.getMenu().getItem(2).setVisible(false);
        initView();
        refresh = new Intent(this, MainActivity.class);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(item -> {
            sharedPreferencesData.setLogin(false);
            sharedPreferencesData.setToken("");
            startActivity(refresh);
            finish();
            return false;
        });
        buttonEditProfile = headerView.findViewById(R.id.btn_edit_profile);
        buttonEditProfile.setOnClickListener(view -> {
            replaceView(R.id.nav_host_fragment_content_main, new EditProfileFragment());
            drawer.closeDrawer(GravityCompat.START);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        menuCarts = menu.findItem(R.id.action_carts);
        menuBought = menu.findItem(R.id.action_game_purchased);
        if(sharedPreferencesData.isLogin()) {
            menuCarts.setVisible(true);
            menuBought.setVisible(true);
        } else {
            menuCarts.setVisible(false);
            menuBought.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_carts:
                replaceView(R.id.nav_host_fragment_content_main, CartsFragment.newInstance("", ""));
                break;
            case R.id.action_game_purchased:
                replaceView(R.id.nav_host_fragment_content_main, new GamePurchasedFragment());
                break;
        }
        if (id == R.id.action_carts) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onLoginSuccess() {
        initView();
        binding.appBarMain.toolbar.setTitle("Home");
        menuCarts.setVisible(true);
        menuBought.setVisible(true);
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack();
    }

    public void initView() {
        if (sharedPreferencesData.isLogin()) {
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(true);
            headerView.setVisibility(View.VISIBLE);
            StoreGameAPI retrofit = retrofitInstance.getRetrofit(sharedPreferencesData.getToken()).create(StoreGameAPI.class);
            Call<ResultUser> call = retrofit.getUserProfile();
            call.enqueue(new Callback<ResultUser>() {
                @Override
                public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                    if (response.isSuccessful()) {
                        User user = response.body().getUser();
                        userName.setText(user.getName());
                        email.setText(user.getEmail());
                        money.setText("Money: " + user.getBudget() + "$");
                    } else {
                        sharedPreferencesData.setLogin(false);
                        startActivity(refresh);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResultUser> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    @Override
    public void sendIDGame(String id) {
        String token =  sharedPreferencesData.isLogin() ? sharedPreferencesData.getToken() : "";
        StoreGameAPI retrofit = retrofitInstance.getRetrofit(token).create(StoreGameAPI.class);
        Call<ResultGame> call = retrofit.getGame(id);
        call.enqueue(new Callback<ResultGame>() {
            @Override
            public void onResponse(Call<ResultGame> call, Response<ResultGame> response) {
                if (response.isSuccessful()) {
                    Game game = response.body().getGame();
                    replaceView(R.id.nav_host_fragment_content_main, DetailGameFragment.newInstance(game));
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResultGame> call, Throwable t) {

            }
        });

    }

    @Override
    public void sendKeySearch(String keySearch) {
        replaceView(R.id.nav_host_fragment_content_main, SearchFragment.newInstance(keySearch));
    }

    public void replaceView(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.add(id, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.appBarMain.toolbar.setTitle("Home");

    }

    @Override
    public void setTitleToolBar(String titleToolBar) {
        binding.appBarMain.toolbar.setTitle(titleToolBar);

    }

    @Override
    public void onSignUp() {
        replaceView(R.id.nav_host_fragment_content_main, new SignupFragment());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            Rect rect = new Rect();
            getCurrentFocus().getGlobalVisibleRect(rect);
            if (ev != null) {
                if (rect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    return super.dispatchTouchEvent(ev);
                }
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getCurrentFocus().clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSignUpSuccess() {
        FragmentManager fm = this.getSupportFragmentManager();
        int i = fm.getBackStackEntryCount();
        fm.popBackStack(null , FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}