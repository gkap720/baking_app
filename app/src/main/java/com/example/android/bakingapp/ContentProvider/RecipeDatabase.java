package com.example.android.bakingapp.ContentProvider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by gkap7 on 8/1/2017.
 */
@Database(version = RecipeDatabase.VERSION)
public final class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(ListColumns.class) public static final String RECIPES = "recipes";
}
