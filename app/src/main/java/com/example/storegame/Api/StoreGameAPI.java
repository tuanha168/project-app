package com.example.storegame.Api;

import com.example.storegame.modle.Carts;
import com.example.storegame.modle.Code;
import com.example.storegame.modle.Login;
import com.example.storegame.modle.Messages;
import com.example.storegame.modle.PostUser;
import com.example.storegame.modle.ResultCategories;
import com.example.storegame.modle.ResultGame;
import com.example.storegame.modle.ResultGames;
import com.example.storegame.modle.ResultGamesBought;
import com.example.storegame.modle.ResultUser;
import com.example.storegame.modle.Token;
import com.example.storegame.modle.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StoreGameAPI {
    @POST("api/v1/auth/login")
    Call<Token> login(@Body Login login);

    @GET("api/v1/categories")
    Call<ResultCategories> getAllCategories();

    @GET("api/v1/auth/profile")
    Call<ResultUser> getUserProfile();

    @GET("api/v1/games")
    Call<ResultGames> getAllGames();

    @POST("api/v1/auth/signup")
    Call<Token> signUp(@Body PostUser postUser);

    @GET("/api/v1/games/{id}")
    Call<ResultGame> getGame(@Path("id") String id);

    @GET("api/v1/games/most-downloaded")
    Call<ResultGames> getFiveDownloadedMostGames();

    @GET("api/v1/games/recommend")
    Call<ResultGames> getFiveRecommend();

    @GET("api/v1/games/sale")
    Call<ResultGames> getFiveSaleGames();

    @PUT("api/v1/auth/profile")
    Call<Messages> editProfile(@Body User user);

    @POST("api/v1/games/{id}/cart")
    Call<Messages> addOrRemoveGameInStore(@Path("id") String id);

    @GET("api/v1/games/cart")
    Call<ResultGames> getGamesInCart();

    @POST("api/v1/auth/activate-code")
    Call<Messages> activeCode(@Body Code codes);

    @POST("api/v1/games/purchase")
    Call<Messages> payGames(@Body Carts carts);

    @GET("/api/v1/games/bought")
    Call<ResultGamesBought> getGameBought();

    @PUT("/api/v1/games/{id}/change-install-status-game")
    Call<Messages> downloadedGame(@Path("id") String id);
}
