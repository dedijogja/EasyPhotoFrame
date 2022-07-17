package com.efpstudios.graphicprocessing;


import android.graphics.Bitmap;

public class BitmapEffect {
    public static int EFFECT_BLACK = 1;

    public static Bitmap getBitmapWithEffect(Bitmap bitmap, int effect){
        if(effect == EFFECT_BLACK){
            return BitmapFilter.applyHighlightEffect(bitmap);
        }
        return null;
    }
}
