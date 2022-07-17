package com.efpstudios.constant;


import android.content.Context;
import android.graphics.Bitmap;

import com.efpstudios.model.EffectModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {

    public static final String NAMA_DEV_ID = "EFP+Studios";
    public static final String FOLDER_KONTEN = "frame";
    public static final String FOLDER_EMOTICON = "emoticon";

    public static String gagalLoadIklan = "gagalload";
    public static String berhasilLoadIklan = "berhasilload";

    public static final String KODE_PERINTAH = "perintah";
    public static final String PERINTAH_BUKA_DIALOG_EMOTICON = "bukaDialogEmoticon";
    public static final String PERINTAH_BUKA_DIALOG_TEXT = "bukaDialogText";
    public static final String PERINTAH_BUKA_DIALOG_CROP = "bukaDialogCrop";


    public static String[] LIST_SEMUA_KONTEN(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(FOLDER_KONTEN);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] LIST_SEMUA_EMOTICON(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(FOLDER_EMOTICON);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] PENYIMPAN_ALAMAT_KONTEN = null;
    public static Bitmap IMAGE_USER = null;
    public static List<EffectModel> IMAGE_USER_FILTER = null;
    public static List<Bitmap> SEMUA_IMAGE_BACKGROUND = null;
    public static Bitmap IMAGE_READY_SAVE = null;
}
