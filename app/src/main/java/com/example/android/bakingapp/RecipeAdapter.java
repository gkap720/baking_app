package com.example.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gkap7 on 7/31/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    List<String> mRecipes;
    OnRecyclerViewItemClickListener listener;
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
        }
    }

    RecipeAdapter(List<String> strings) {
        mRecipes = strings;
    }
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(v);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, final int position) {
        holder.recipeName.setText(mRecipes.get(position));
        holder.recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnRecyclerViewItemClickListener
    {
        public void onRecyclerViewItemClicked(int position);
    }
}
