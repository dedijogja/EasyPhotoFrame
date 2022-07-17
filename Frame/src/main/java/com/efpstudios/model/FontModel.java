package com.efpstudios.model;


import android.graphics.Typeface;

public class FontModel {

    private String fontName;
    private Typeface typeface;

    public FontModel(String fontName, Typeface typeface) {
        this.fontName = fontName;
        this.typeface = typeface;
    }

    public String getFontName() {
        return fontName;
    }

    public Typeface getTypeface() {
        return typeface;
    }
}
