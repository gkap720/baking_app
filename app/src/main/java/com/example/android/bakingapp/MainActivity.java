package com.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.ContentProvider.ListColumns;
import com.example.android.bakingapp.ContentProvider.RecipeProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecyclerViewItemClickListener{
    private RecyclerView mRecyclerView;
    private List<String> mRecipes;
    private String RECIPE_ADDRESS = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Override
    public void onRecyclerViewItemClicked(int position) {
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra("recipeId", position);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipes = new ArrayList<String>();
        try {
            new DownloadRecipesTask().execute(new URL(RECIPE_ADDRESS));
        } catch (IOException e) {
            Toast.makeText(this, "URL no longer valid", Toast.LENGTH_LONG).show();
        }
    }
    private class DownloadRecipesTask extends AsyncTask<URL, String, String> {
        @Override
        protected String doInBackground(URL... params)  {
            try {
                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONArray recipeJSON = new JSONArray(responseStrBuilder.toString());
                int id;
                String name;
                String steps;
                String ingredients;
                getContentResolver().delete(RecipeProvider.Recipes.RECIPES, null, null);
                for(int i = 0; i < recipeJSON.length(); i++) {
                    JSONObject curr = recipeJSON.getJSONObject(i);
                    id = curr.getInt("id");
                    steps = curr.getJSONArray("steps").toString();
                    ingredients = curr.getJSONArray("ingredients").toString();
                    name = curr.getString("name");
                    ContentValues cv = new ContentValues();
                    cv.put("_id", id);
                    cv.put("steps", steps);
                    cv.put("ingredients", ingredients);
                    cv.put("name", name);
                    getContentResolver().insert(RecipeProvider.Recipes.RECIPES, cv);
                }
                connection.disconnect();
            } catch(JSONException e) {
                //error reading JSON
                Log.e("AHHH", "error reading JSON");
            } catch(IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            populateRecyclerView();
        }
    }

    public void populateRecyclerView() {
        ArrayList<String> recipes = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.RECIPES,
                null, null, null, null, null);
        int i = 0;
        while(cursor.moveToNext()) {
            recipes.add(i, cursor.getString(cursor.getColumnIndex(ListColumns.NAME)));
            i++;
        }
        mRecipes = recipes;
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipes);
        recipeAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(recipeAdapter);
    }
}
