package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.ContentProvider.RecipeProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkap7 on 7/31/2017.
 */

public class StepsActivity extends AppCompatActivity {
    ListView mIngredientList;
    ListView mStepList;
    List<String> ingredients;
    List<Step> steps;
    int recipeId = -1;
    private int numSteps;
    int REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_activity);
        mIngredientList = (ListView) findViewById(R.id.ingredient_list);
        recipeId = getIntent().getExtras().getInt("recipeId");
        populateLists(recipeId);
        mIngredientList.setAdapter(new IngredientAdapter(this, R.layout.ingredient_row, ingredients));
        mStepList = (ListView) findViewById(R.id.step_list);
        mStepList.setAdapter(new StepAdapter(this, R.layout.step_row, steps));
        mStepList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                Step clicked = steps.get(position);
                intent.putExtra("id", clicked.id);
                intent.putExtra("recipeId", recipeId);
                intent.putExtra("numSteps", numSteps);
                startActivity(intent);
            }
        });
    }

    public class Step {
        int id;
        String shortDescription;
        String thumbnailUrl;
        public Step(int id, String shortDescription, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.thumbnailUrl = thumbnailUrl;
        }
    }

    public void populateLists(int id) {
        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.RECIPES,
                null, null, null, null, null);
        cursor.moveToPosition(id);
        ingredients = new ArrayList<String>();
        steps = new ArrayList<Step>();
        try {
            JSONArray ingredientArray =
                new JSONArray(cursor.getString(cursor.getColumnIndex("ingredients")));

            JSONArray stepArray =
                new JSONArray(cursor.getString(cursor.getColumnIndex("steps")));
            numSteps = stepArray.length();
            for (int i = 0; i < ingredientArray.length(); i++) {
                JSONObject individualIngredient = ingredientArray.getJSONObject(i);
                String name = individualIngredient.getString("ingredient");
                String measure = individualIngredient.getString("measure");
                double quantity = individualIngredient.getDouble("quantity");
                ingredients.add(i, String.format("%s: %.2f %s", name, quantity, measure));
            }
            for (int i = 0; i < stepArray.length(); i++) {
                JSONObject eachStep = stepArray.getJSONObject(i);
                String shortDescription = eachStep.getString("shortDescription");
                String thumbnailUrl = eachStep.getString("thumbnailURL");
                steps.add(i, new Step(i, shortDescription, thumbnailUrl));
            }
        } catch (JSONException e) {
            Log.e("EXCEPTION", e.getMessage());
        } finally {
            cursor.close();
        }
    }
    public class IngredientAdapter extends ArrayAdapter<String> {
        List<String> mItems;

        public IngredientAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            mItems = items;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.ingredient_row, null);
            }
            TextView textView = (TextView) v.findViewById(R.id.ingredient_amt);
            textView.setText(mItems.get(position));
            return v;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }
    }

    public class StepAdapter extends ArrayAdapter<Step> {
        List<Step> mItems;

        public StepAdapter(Context context, int resource, List<Step> items) {
            super(context, resource, items);
            mItems = items;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.step_row, null);
            }
            TextView textView = (TextView) v.findViewById(R.id.short_description);
            textView.setText(mItems.get(position).shortDescription);
            return v;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }
    }

}
