package com.efpstudios.asyncronus;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.efpstudios.constant.EffectConstant;
import com.efpstudios.model.EffectModel;

import java.util.ArrayList;
import java.util.List;

public class ImageFilterMultipleLoad extends AsyncTask<Bitmap, Void, List<EffectModel>> {

    private ProgressDialog dialog;
    private ListenerDecrypt listenerDecrypt;
    private Context context;

    public ImageFilterMultipleLoad(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Loading Image Effect",
                "Please wait ...", true);
        dialog.show();
    }


    @Override
    protected List<EffectModel> doInBackground(Bitmap... params) {
        List<EffectModel> listModel = new ArrayList<>();

        EffectModel effectModel = new EffectModel();
        effectModel.setBitmap(EffectConstant.BITMAP_WITH_EFFECT(params[0], "normal"));
        effectModel.setEffectName("normal");
        listModel.add(effectModel);

        for(int i = 0; i<EffectConstant.ALL_EFFECT().length; i++){
            EffectModel effectModel2 = new EffectModel();
            effectModel2.setBitmap(EffectConstant.BITMAP_WITH_EFFECT(params[0], EffectConstant.ALL_EFFECT()[i]));
            effectModel2.setEffectName(EffectConstant.ALL_EFFECT()[i]);
            listModel.add(effectModel2);
        }

        return listModel;
    }

    @Override
    protected void onPostExecute(List<EffectModel> effectResult) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(effectResult);
    }


    public void setListenerEffect(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(List<EffectModel> effectResult);
    }
}
