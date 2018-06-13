package com.example.hp.steamahead;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hp.steamahead.model.GameData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mGamePosterImage;
    private TextView mScoreTextView;
    private TextView mDateTextView;
    private TextView mDescriptionTextView;
    private RecyclerView mScreenshotsRecyclerView;
    private ScreenshotsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mGamePosterImage = findViewById(R.id.game_image);
        mScoreTextView = findViewById(R.id.score);
        mDateTextView = findViewById(R.id.date);
        mDescriptionTextView = findViewById(R.id.description);
        Intent intent = getIntent();
        GameData gameData = intent.getParcelableExtra("Game_Detail");
        mScreenshotsRecyclerView = findViewById(R.id.screenshots);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mScreenshotsRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ScreenshotsAdapter(this,gameData.getScreenshots());
        mScreenshotsRecyclerView.setAdapter(mAdapter);
        Glide.with(this)
                .load(gameData.getHeader_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(mGamePosterImage);
        String htmlDescription = gameData.getDetailed_description();
        Document document = Jsoup.parse(htmlDescription);
        String description = document.body().text();
        mDescriptionTextView.setText(description);

        if (gameData.getMetacritic() != null)
            mScoreTextView.append(String.valueOf(gameData.getMetacritic().getScore()));
        else
            mScoreTextView.append("75");
        if (gameData.getRelease_date() != null)
            mDateTextView.append(gameData.getRelease_date().getDate());

    }
}
