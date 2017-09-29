package com.example.chris.popularmovies.utilites;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by chris on 9/28/17.
 */

public class Utility {
    public static int numOfGridColumns (Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numCols = (int) (dpWidth/180);
        return numCols;
    }

    public static int getMaxGridCellWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels/numOfGridColumns(context)* 0.75);
    }
}
