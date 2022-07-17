package com.efpstudios.model;

import android.graphics.Bitmap;

public class EffectModel {
    private Bitmap bitmap;
    private String effectName;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }
}
