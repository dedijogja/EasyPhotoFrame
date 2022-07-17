package com.efpstudios.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RelativeSquarebasedHeight extends RelativeLayout {

    public RelativeSquarebasedHeight(Context context) {
        super(context);
    }

    public RelativeSquarebasedHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeSquarebasedHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RelativeSquarebasedHeight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
