package com.example.android.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.ContentProvider.RecipeProvider;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gkap7 on 7/31/2017.
 */

public class DetailActivity extends AppCompatActivity {
    SimpleExoPlayer mExoPlayer;
    private int recipeId;
    private int stepId;
    private int numSteps;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        TextView description = (TextView) findViewById(R.id.long_description);
        Bundle extras = getIntent().getExtras();
        recipeId = extras.getInt("recipeId");
        stepId = extras.getInt("id");
        numSteps = extras.getInt("numSteps");
        String stringUri = "";
        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.RECIPES,
                null,null,null,null,null);
        cursor.moveToPosition(recipeId);
        try {
            JSONObject step = new JSONArray(cursor.getString(cursor.getColumnIndex("steps"))).getJSONObject(stepId);
            stringUri = step.getString("videoURL");
            description.setText(step.getString("description"));
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } finally {
            cursor.close();
        }
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        mExoPlayer.setPlayWhenReady(true);
        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.video_player);
        if(stepId == numSteps-1) {
            findViewById(R.id.recipe_forward).setVisibility(View.GONE);
        }
        if(stepId == 0) {
            findViewById(R.id.recipe_back).setVisibility(View.GONE);
        }
        if (!(stringUri.equals(""))) {
            Uri uri = Uri.parse(stringUri);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DefaultHttpDataSourceFactory dataSourceFactory =
                    new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "BakingApp"), bandwidthMeter);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(
                    uri,
                    dataSourceFactory, extractorsFactory, null, null);
            mExoPlayer.prepare(videoSource);
            exoPlayerView.setPlayer(mExoPlayer);
        }
        else {
            //if url invalid, make video player invisible, set text to "center" and make it larger
            exoPlayerView.setVisibility(View.GONE);
            description.setGravity(17);
            description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExoPlayer.release();
    }

    public void forwardClicked(View v) {
        Intent intent = new Intent(this.getApplicationContext(), DetailActivity.class);
        intent.putExtra("recipeId", recipeId);
        intent.putExtra("id", stepId+1);
        intent.putExtra("numSteps", numSteps);
        startActivity(intent);
    }
    public void backClicked(View v) {
        Intent intent = new Intent(this.getApplicationContext(), DetailActivity.class);
        intent.putExtra("recipeId", recipeId);
        intent.putExtra("id", stepId-1);
        intent.putExtra("numSteps", numSteps);
        startActivity(intent);
    }

    @Override
    public boolean navigateUpTo(Intent upIntent) {
        upIntent.putExtra("recipeId", recipeId);
        return super.navigateUpTo(upIntent);
    }
}
