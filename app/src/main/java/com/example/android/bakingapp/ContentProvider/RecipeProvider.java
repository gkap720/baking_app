package com.example.android.bakingapp.ContentProvider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by gkap7 on 8/1/2017.
 */
@ContentProvider(authority = RecipeProvider.AUTHORITY, database = RecipeDatabase.class)
public final class RecipeProvider {
    public static final String AUTHORITY = "com.example.android.BakingApp.ContentProvider.RecipeProvider";

    @TableEndpoint(table = RecipeDatabase.RECIPES) public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/list",
                defaultSort = ListColumns._ID + " ASC")
        public static final Uri RECIPES = Uri.parse("content://" + AUTHORITY + "/recipes");
    }
}
