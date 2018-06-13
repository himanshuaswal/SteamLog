package com.example.hp.steamahead.network;

import com.example.hp.steamahead.model.Game;
import com.example.hp.steamahead.model.TopGames;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 ** Created by Gautam Krishnan {@link https://github.com/GautiKrish}
 */public interface SteamRetrofit {
     String STEAM_SPY_URL = "https://steamspy.com/";
        String GAME_DETAILS_URL = "http://store.steampowered.com/api/";


    @GET("appdetails/")
    Call<Map<String, Game>> getGameDetails(@Query("appids") String APPIDS);

    @GET("api.php/")
    Call<Map<String, TopGames>> getTopGames(@Query("request") String REQUEST);

    Retrofit gamesRetrofit = new Retrofit.Builder()
            .baseUrl(GAME_DETAILS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit topGamesRetrofit = new Retrofit.Builder()
            .baseUrl(STEAM_SPY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
