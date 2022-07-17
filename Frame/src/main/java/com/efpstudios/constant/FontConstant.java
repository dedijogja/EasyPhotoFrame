package com.efpstudios.constant;


import android.content.Context;
import android.graphics.Typeface;

import com.efpstudios.model.FontModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FontConstant {

    private static String FOLDER_FONT = "font";

    private static String[] SEMUA_NAMA_FONT_DENGAN_FORMAT (Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(FOLDER_FONT);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    private static String[] SEMUA_NAMA_FONT_TANPA_FORMAT(String[] semuaNamaFontDenganFormat){
        String[] temp = new String[semuaNamaFontDenganFormat.length];
        for (int i = 0; i<semuaNamaFontDenganFormat.length; i++){
            temp[i] = semuaNamaFontDenganFormat[i].substring(0, semuaNamaFontDenganFormat[i].length()-4);
        }
        return temp;
    }

    public static FontModel[] SEMUA_FONT_MODEL(Context context) throws IOException {
        String[] semuaNamaFont = SEMUA_NAMA_FONT_TANPA_FORMAT(SEMUA_NAMA_FONT_DENGAN_FORMAT(context));
        FontModel[] fontModel = new FontModel[semuaNamaFont.length];
        for(int i = 0; i<semuaNamaFont.length; i++){
            fontModel[i] = new FontModel(semuaNamaFont[i], Typeface.createFromAsset(context.getAssets(), FOLDER_FONT+"/"+semuaNamaFont[i]+".ttf"));
        }
        return fontModel;
    }

//    public static String[] SEMUA_NAMA_FONT(Context context) throws IOException {
//        FontModel[] fontModels = SEMUA_FONT_MODEL(context);
//        String[] temp = new String[fontModels.length];
//        for(int i = 0; i<fontModels.length; i++){
//            temp[i] = fontModels[i].getFontName();
//        }
//        return temp;
//    }
}
