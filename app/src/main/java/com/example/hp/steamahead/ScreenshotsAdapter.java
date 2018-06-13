package com.example.hp.steamahead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hp.steamahead.model.Screenshots;

import java.util.List;

/*
 ** Created by Gautam Krishnan {@link https://github.com/GautiKrish}
 */public class ScreenshotsAdapter  extends RecyclerView.Adapter<ScreenshotsAdapter.ScreenshotsAdapterViewHolder> {
    private static final String TAG ="number" ;
    private Context context;
    private List<Screenshots> screenshots;

     public  ScreenshotsAdapter(Context context, List<Screenshots> screenshots){
         this.context = context;
         this.screenshots = screenshots;
     }
    @NonNull
    @Override
    public ScreenshotsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_screenshot_item, parent, false);
        return new ScreenshotsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenshotsAdapterViewHolder holder, int position) {
        Screenshots screenshot = screenshots.get(position);
        Glide.with(context)
                .load(screenshot.getPath_thumbnail())
                .fitCenter()
                .into(holder.screenshotImageView);

    }

    @Override
    public int getItemCount() {
        Log.i(TAG,String.valueOf(screenshots.size()));
        return screenshots.size();

    }

    public class ScreenshotsAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView screenshotImageView;
        public ScreenshotsAdapterViewHolder(View itemView) {
            super(itemView);
            screenshotImageView=itemView.findViewById(R.id.screenshot);
        }
    }
}
