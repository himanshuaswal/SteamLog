package com.example.hp.steamahead;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.steamahead.model.Game;
import com.example.hp.steamahead.model.GameData;
import com.example.hp.steamahead.model.TopGames;
import com.example.hp.steamahead.network.SteamRetrofit;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private RecyclerView mRecyclerView;
    private GameAdapter mGameAdapter;
    ArrayList<TopGames> mTopGames = new ArrayList<>();
    ArrayList<GameData> mGames = new ArrayList<>();
    Call<Map<String, Game>> gamesCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Welcome.You are signed in ", Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

        mRecyclerView = findViewById(R.id.game_catalogue);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mGameAdapter = new GameAdapter(this , new GameAdapter.GameAdapterOnClickHandler() {
            @Override
            public void onClick(GameData gameData, ImageView imageView) {
                Intent intentToStartDetailsActivity = new Intent(MainActivity.this, DetailsActivity.class);
                intentToStartDetailsActivity.putExtra("Game_Detail",gameData);
                startActivity(intentToStartDetailsActivity);
            }
        },mGames);
        mRecyclerView.setAdapter(mGameAdapter);
        fetchTopGames();
    }

    private void fetchTopGames() {
        final SteamRetrofit topGamesAPI = SteamRetrofit.topGamesRetrofit.create(SteamRetrofit.class);
        Call<Map<String, TopGames>> topGamesCall = topGamesAPI.getTopGames("top100in2weeks");
        topGamesCall.enqueue(new Callback<Map<String, TopGames>>() {
            @Override
            public void onResponse(Call<Map<String, TopGames>> call, Response<Map<String, TopGames>> response) {
                Map<String, TopGames> topGamesMap = response.body();
                if (topGamesMap != null) {
                    for (Map.Entry<String, TopGames> entry : topGamesMap.entrySet()) {
                        mTopGames.add(entry.getValue());
                        Log.e("GAME FETCHED: ", entry.getValue().getName());
                        int gameAddedId = mTopGames.get(mTopGames.size() - 1).getAppid();
                        fetchGames(gameAddedId, entry.getValue().getPlayers());
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, TopGames>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Couldn't load top 100 games!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchGames(final int gameAddedId, final long players) {
        SteamRetrofit gamesAPI = SteamRetrofit.gamesRetrofit.create(SteamRetrofit.class);
        gamesCall = gamesAPI.getGameDetails(String.valueOf(gameAddedId));
        gamesCall.enqueue(new Callback<Map<String, Game>>() {
            @Override
            public void onResponse(Call<Map<String, Game>> call, Response<Map<String, Game>> response) {
                Map<String, Game> gameMap = response.body();
                if (gameMap != null) {
                    for (Map.Entry<String, Game> entry : gameMap.entrySet()) {
                        if (entry.getValue().getData() != null) {
                            mGames.add(entry.getValue().getData());
                            mGames.get(mGames.size() - 1).setPlayers(players);
                            mGameAdapter.notifyDataSetChanged();
                            Log.e("GAME DATA ADDED", entry.getValue().getData().getName());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Game>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Couldn't load images for these games, bro!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}



