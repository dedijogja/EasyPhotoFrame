package com.efpstudios.recyclehelper;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

public class RecycleSpacing extends RecyclerView.ItemDecoration {
    private int jarak;
    private Context context;

    public RecycleSpacing(int jarak, Context context) {
        this.jarak = jarak;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = pxToDp(jarak);


        //if(parent.getChildAdapterPosition(view) == 0)
            //outRect.top = pxToDp(10);
    }

    private int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}