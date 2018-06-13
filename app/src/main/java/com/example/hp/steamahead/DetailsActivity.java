package com.example.hp.steamahead;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
            mScoreTextView.setText("75");
        if (gameData.getRelease_date() != null)
            mDateTextView.append(gameData.getRelease_date().getDate());

    }
}
