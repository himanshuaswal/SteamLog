package com.example.hp.steamahead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.steamahead.model.GameData;

import java.util.ArrayList;

/*
 ** Created by Gautam Krishnan {@link https://github.com/GautiKrish}
 */public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameAdapterViewHolder> {
    private Context context;
    private GameAdapterOnClickHandler clickHandler;
    private ArrayList<GameData> gameData;

    public GameAdapter(Context context, GameAdapterOnClickHandler clickHandler, ArrayList<GameData> gameData) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.gameData = gameData;
    }

    @NonNull
    @Override
    public GameAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_game_item, parent, false);
        return new GameAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapterViewHolder holder, int position) {
        Glide.with(context)
                .load(gameData.get(position).getHeader_image())
                .fitCenter()
                .into(holder.mImageView);
        holder.mGameNameTextView.setText(gameData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (gameData == null) return 0;
        Log.i("GAME SIZE: ", String.valueOf(gameData.size()));
        return gameData.size();
    }

    public class GameAdapterViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;
        final TextView mGameNameTextView;

        GameAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.game_thumbnail);
            mGameNameTextView = itemView.findViewById(R.id.game_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickHandler.onClick(gameData.get(getAdapterPosition()), mImageView);
                }
            });
        }

    }

    public interface GameAdapterOnClickHandler {
        void onClick(GameData gameData, ImageView imageView);
    }
}
