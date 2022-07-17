package com.efpstudios.constant;


import android.graphics.Bitmap;
import android.graphics.Color;

import com.mukesh.image_processing.ImageProcessingConstants;
import com.mukesh.image_processing.ImageProcessor;

public class EffectConstant {

    public static Bitmap BITMAP_WITH_EFFECT(Bitmap bitmap, String effectCode){
        ImageProcessor mImageProcessor = new ImageProcessor();
        switch(effectCode){
            case EFFECT_INVERT:
                return mImageProcessor.doInvert(bitmap);
            case EFFECT_GREY_SCALE:
                return mImageProcessor.doGreyScale(bitmap);
            case EFFECT_GAMMA:
                return mImageProcessor.doGamma(bitmap, 0.6, 0.6, 0.6);
            case EFFECT_COLOR_FILTER:
                return mImageProcessor.doColorFilter(bitmap, 1, 0, 0);
            case EFFECT_SEPIA_TO_NIGHT:
                return mImageProcessor.createSepiaToningEffect(bitmap, 150, 0.7, 0.3, 0.12);
            case EFFECT_DECREASE_COLOR_DEPTH:
                return  mImageProcessor.decreaseColorDepth(bitmap, 32);
            case EFFECT_CONTRAST:
                return mImageProcessor.createContrast(bitmap, 50);
            case EFFECT_BRIGHTNESS:
                return mImageProcessor.doBrightness(bitmap, -60);
            case EFFECT_GAUSSIAN_BLUR:
                return  mImageProcessor.applyGaussianBlur(bitmap);
            case EFFECT_SHARPEN:
                return  mImageProcessor.sharpen(bitmap, 11);
            case EFFECT_MEAN_REMOVAL:
                return  mImageProcessor.applyMeanRemoval(bitmap);
            case EFFECT_SMOOTH:
                return mImageProcessor.smooth(bitmap, 100);
            case EFFECT_EMBOSS:
                return mImageProcessor.emboss(bitmap);
            case EFFECT_ENGRAVE:
                return mImageProcessor.engrave(bitmap);
            case EFFECT_BOOST:
                return mImageProcessor.boost(bitmap, ImageProcessingConstants.RED, 1.5);
            case EFFECT_TINT_IMAGE:
                return mImageProcessor.tintImage(bitmap, 50);
            case EFFECT_REPLACE_COLOR:
                return mImageProcessor.replaceColor(bitmap, Color.BLACK, Color.BLUE);
            case EFFECT_FLEA:
                return mImageProcessor.applyFleaEffect(bitmap);
            case EFFECT_BLACK_FILTER:
                return mImageProcessor.applyBlackFilter(bitmap);
            case EFFECT_SNOW_EFFECT:
                return mImageProcessor.applySnowEffect(bitmap);
            case EFFECT_SHADING_FILTER:
                return mImageProcessor.applyShadingFilter(bitmap, Color.MAGENTA);
            case EFFECT_SATURATION_FILTER:
                return mImageProcessor.applySaturationFilter(bitmap, 1);
            case EFFECT_HUE_FILTER:
                return mImageProcessor.applyHueFilter(bitmap, 5);
            case EFFECT_REFLECTION:
                return mImageProcessor.applyReflection(bitmap);
        }
        return bitmap;
    }

    public static final String EFFECT_INVERT = "Invert";
    public static final String EFFECT_GREY_SCALE = "Gray Scale";
    public static final String EFFECT_GAMMA = "Gamma";
    public static final String EFFECT_COLOR_FILTER = "Color Filter";
    public static final String EFFECT_SEPIA_TO_NIGHT = "Sepia To Night";
    public static final String EFFECT_DECREASE_COLOR_DEPTH = "Decrease Color Depth";
    public static final String EFFECT_CONTRAST = "Contrast";
    public static final String EFFECT_BRIGHTNESS = "Brightness";
    public static final String EFFECT_GAUSSIAN_BLUR = "Gaussian Blur";
    public static final String EFFECT_SHARPEN = "Sharpen";
    public static final String EFFECT_MEAN_REMOVAL = "Mean removal";
    public static final String EFFECT_SMOOTH = "Smooth";
    public static final String EFFECT_EMBOSS = "Emboss";
    public static final String EFFECT_ENGRAVE = "Engrave";
    public static final String EFFECT_BOOST = "Boost";
    public static final String EFFECT_TINT_IMAGE = "Tint Image";
    public static final String EFFECT_REPLACE_COLOR = "Replace Color";
    public static final String EFFECT_FLEA = "Flea";
    public static final String EFFECT_BLACK_FILTER = "Black Filter";
    public static final String EFFECT_SNOW_EFFECT = "Snow Effect";
    public static final String EFFECT_SHADING_FILTER = "Shading Filter";
    public static final String EFFECT_SATURATION_FILTER = "Saturation Filter";
    public static final String EFFECT_HUE_FILTER = "Hue Filter";
    public static final String EFFECT_REFLECTION = "Reflection";

    public static final String[] ALL_EFFECT(){
        return new String[] {
            EFFECT_INVERT,
            EFFECT_GREY_SCALE,
            EFFECT_GAMMA,
            EFFECT_COLOR_FILTER,
            EFFECT_SEPIA_TO_NIGHT,
            EFFECT_DECREASE_COLOR_DEPTH,
            EFFECT_CONTRAST,
            EFFECT_BRIGHTNESS,
            EFFECT_GAUSSIAN_BLUR,
            EFFECT_SHARPEN,
            EFFECT_MEAN_REMOVAL,
            EFFECT_SMOOTH,
            EFFECT_EMBOSS,
            EFFECT_ENGRAVE,
            EFFECT_BOOST,
            EFFECT_TINT_IMAGE,
            EFFECT_REPLACE_COLOR,
            EFFECT_FLEA,
            EFFECT_BLACK_FILTER,
            EFFECT_SNOW_EFFECT,
            EFFECT_SHADING_FILTER,
            EFFECT_SATURATION_FILTER,
            EFFECT_HUE_FILTER,
            EFFECT_REFLECTION
        };
    }

}
