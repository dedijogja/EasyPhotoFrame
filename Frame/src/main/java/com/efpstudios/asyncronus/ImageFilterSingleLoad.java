package com.efpstudios.asyncronus;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.efpstudios.constant.EffectConstant;

public class ImageFilterSingleLoad extends AsyncTask<Bitmap, Void, Bitmap>{

    private ProgressDialog dialog;
    private ListenerDecrypt listenerDecrypt;
    private Context context;
    private String effectName;

    public ImageFilterSingleLoad(Context context, String effectName) {
        this.context = context;
        this.effectName = effectName;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Applying effect",
                "Please wait ...", true);
        dialog.show();
    }


    @Override
    protected Bitmap doInBackground(Bitmap... params) {
        return EffectConstant.BITMAP_WITH_EFFECT(params[0], effectName);
    }


    @Override
    protected void onPostExecute(Bitmap effectResult) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(effectResult);
    }


    public void setListenerEffect(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(Bitmap effectResult);
    }
}
