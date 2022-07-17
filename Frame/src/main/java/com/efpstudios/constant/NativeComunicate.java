package com.efpstudios.constant;

import android.content.Context;
import android.util.Log;

import com.efpstudios.decryption.text.deskriptor.DeskripsiText;

public class NativeComunicate {
    static {
            System.loadLibrary("native-lib");
    }

    private Context context;

    public NativeComunicate(Context context){
        if(!context.getPackageName().equals(packageName(context))){
            throw new RuntimeException(new DeskripsiText(keyDesText(context), smesek(context)).dapatkanTextAsli());
        }else{
            this.context = context;
        }
    }

    public String getKeyAssets() {
        return keyDesAssets(context);
    }


    public String getAdInterstitial() {
        Log.d("enskripsi getAdInter", new DeskripsiText(keyDesText(context), adInterstitial(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), adInterstitial(context)).dapatkanTextAsli();
    }

    public String getAdBanner() {
        Log.d("enskripsi Banner", new DeskripsiText(keyDesText(context), adBanner(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), adBanner(context)).dapatkanTextAsli();
    }


    public String getStartAppId() {
        Log.d("enskripsi startAppId", new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli();
    }


    public native String packageName(Context context);
    public native String keyDesText(Context context);
    public native String keyDesAssets(Context context);
    public native String adInterstitial(Context context);
    public native String adBanner(Context context);
    public native String startAppId(Context context);
    public native String smesek(Context context);
}
