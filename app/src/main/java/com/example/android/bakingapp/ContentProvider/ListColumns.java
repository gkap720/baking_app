package com.example.android.bakingapp.ContentProvider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by gkap7 on 8/1/2017.
 */

public interface ListColumns {

        @DataType(INTEGER) @PrimaryKey
        @Unique
        String _ID = "_id";

        @DataType(TEXT) @NotNull
        String NAME = "name";

        @DataType(TEXT) @NotNull
        String STEPS = "steps";

        @DataType(TEXT) @NotNull
        String INGREDIENTS = "ingredients";
}
