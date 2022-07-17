package com.efpstudios.decryption.file;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.efpstudios.constant.Constant;
import com.efpstudios.constant.NativeComunicate;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SingleDecryption extends AsyncTask<String, Void, Bitmap> {

    private ProgressDialog dialog;
    private ListenerDecrypt listenerDecrypt;
    private Context context;

    public SingleDecryption(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Applying frame",
                "Please wait ...", true);
        dialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        SecretKey secretKey =  new SecretKeySpec(Base64.decode(new NativeComunicate(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new NativeComunicate(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(Constant.FOLDER_KONTEN + "/" + params[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] hasilDekripsi = AesCipher.doFinal(bytes);
            bitmap = BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length);
        } catch (IOException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap hasilDeskripsi) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(hasilDeskripsi);
    }


    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(Bitmap hasilDeskripsi);
    }
}
