package com.efpstudios.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.efpstudios.R;
import com.efpstudios.adapter.BitmapEffectDialogAdapter;
import com.efpstudios.adapter.SaveImageAdapter;
import com.efpstudios.asyncronus.ImageFilterMultipleLoad;
import com.efpstudios.asyncronus.ImageFilterSingleLoad;
import com.efpstudios.constant.Applications;
import com.efpstudios.constant.Constant;
import com.efpstudios.constant.NativeComunicate;
import com.efpstudios.decryption.file.SingleDecryption;
import com.efpstudios.model.EffectModel;
import com.efpstudios.recyclehelper.RecycleSpacing;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class SaveActivity extends AppCompatActivity implements ColorPickerDialogListener {

    private Context context = this;
    private Bitmap temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);


        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.berhasilLoadIklan)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanSaveActivity);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new NativeComunicate(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            StartAppSDK.init(this, new NativeComunicate(this).getStartAppId(),false);
            StartAppAd.disableSplash();
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanSaveActivity);
            Banner startAppBanner = new Banner(this);
            adContainer.addView(startAppBanner);
        }

        if(Constant.IMAGE_READY_SAVE!=null){
            ((ImageView) findViewById(R.id.imageSaveUtama)).setImageBitmap(Constant.IMAGE_READY_SAVE);
            temp = Constant.IMAGE_READY_SAVE;

        }else{
            if(Constant.PENYIMPAN_ALAMAT_KONTEN == null){
                try {
                    Constant.PENYIMPAN_ALAMAT_KONTEN = Constant.LIST_SEMUA_KONTEN(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Constant.IMAGE_READY_SAVE = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            ((ImageView) findViewById(R.id.imageSaveUtama)).setImageBitmap(Constant.IMAGE_READY_SAVE);
            temp = Constant.IMAGE_READY_SAVE;
        }

        SaveImageAdapter saveImageAdapter = new SaveImageAdapter(this, new String[]{
                "Save Image",
                "Share Image",
                "Set Image As",
                "Restore to Original Image",
                "Rotate to Right",
                "Rotate to Left",
                "Image Effect",
                "Crop Image",
                "Force To Square",
                "Fit Square With Color",
                "Change Background Color"
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleSaveImage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleSpacing(5, this));
        recyclerView.setAdapter(saveImageAdapter);
        saveImageAdapter.notifyDataSetChanged();


        if(getIntent().getStringExtra(Constant.KODE_PERINTAH)!=null){
            String isiPerintah = getIntent().getStringExtra(Constant.KODE_PERINTAH);
            if(isiPerintah.equals(Constant.PERINTAH_BUKA_DIALOG_CROP)) {
                cropImage();
            }
        }
    }

    public void setImageAs(){
        Toast.makeText(context, "Processing, please wait...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                OutputStream outstream;
                try {
                    outstream = context.getContentResolver().openOutputStream(uri);
                    saveBitmap().compress(Bitmap.CompressFormat.PNG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");
                context.startActivity(Intent.createChooser(intent, "Set Image As ..."));
            }

        }, 1000);
    }

    public void shareImage(){
        Toast.makeText(context, "Processing, please wait...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + getString(R.string.app_name));
                myDir.mkdirs();
                Random r = new Random();
                String fname = "Image-share" +String.valueOf(r.nextInt(10000))+ ".jpg";
                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    saveBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri imageUri = Uri.parse("file://" + file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                context.startActivity(Intent.createChooser(intent, "Share using..."));
            }
        }, 1000);
    }

    public void changeBackgroundColor(){
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(0)
                .setColor(Color.parseColor("#e74c3c"))
                .setShowAlphaSlider(true)
                .show(SaveActivity.this);
    }

    public void fitSquareWithColor(){
        LinearLayout viewUtama = (LinearLayout) findViewById(R.id.pembungkusSaveUtama);
        int width = viewUtama.getWidth();
        int height = viewUtama.getHeight();
        int square = Math.min(width, height);
        viewUtama.setLayoutParams(new LinearLayout.LayoutParams(square,square));
        findViewById(R.id.imageSaveUtama).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((ImageView) findViewById(R.id.imageSaveUtama)).setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void forceToSquare(){
        LinearLayout viewUtama = (LinearLayout) findViewById(R.id.pembungkusSaveUtama);
        int width = viewUtama.getWidth();
        int height = viewUtama.getHeight();
        int square = Math.min(width, height);
        viewUtama.setLayoutParams(new LinearLayout.LayoutParams(square,square));
        findViewById(R.id.imageSaveUtama).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ((ImageView) findViewById(R.id.imageSaveUtama)).setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void cropImage(){
        final View child = getLayoutInflater().inflate(R.layout.cropper_dialog, null);

        final CropImageView cropImageView = (CropImageView) child.findViewById(R.id.cropImageView);
        cropImageView.setImageBitmap(temp);

        child.findViewById(R.id.btnCancelCrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FrameLayout) findViewById(R.id.frameSaveDialog)).removeAllViews();
                findViewById(R.id.frameSaveDialog).setVisibility(View.GONE);
            }
        });

        child.findViewById(R.id.btnCropImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FrameLayout) findViewById(R.id.frameSaveDialog)).removeAllViews();
                findViewById(R.id.frameSaveDialog).setVisibility(View.GONE);

                ((ImageView) findViewById(R.id.imageSaveUtama)).setImageBitmap(cropImageView.getCroppedImage());
                temp = cropImageView.getCroppedImage();

                Toast.makeText(context, "Crop Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        ((FrameLayout) findViewById(R.id.frameSaveDialog)).addView(child);
        findViewById(R.id.frameSaveDialog).setVisibility(View.VISIBLE);
    }

    private Bitmap saveBitmap(){
        LinearLayout view = (LinearLayout) findViewById(R.id.pembungkusSaveUtama);
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public void imageEffectApply(String effectName){
        ImageFilterSingleLoad imageFilterSingleLoad = new ImageFilterSingleLoad(context, effectName);
        imageFilterSingleLoad.setListenerEffect(new ImageFilterSingleLoad.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(Bitmap effectResult) {
                ((FrameLayout) findViewById(R.id.frameSaveDialog)).removeAllViews();
                findViewById(R.id.frameSaveDialog).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.imageSaveUtama)).setImageBitmap(effectResult);
                temp = effectResult;
            }
        });
        imageFilterSingleLoad.execute(temp);
    }

    public void imageEffectDialog(){
        ImageFilterMultipleLoad imageFilterLoad = new ImageFilterMultipleLoad(context);
        imageFilterLoad.setListenerEffect(new ImageFilterMultipleLoad.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(List<EffectModel> hasilDeskripsi) {

                View child = getLayoutInflater().inflate(R.layout.bitmapeffect_dialog, null);

                RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);

                BitmapEffectDialogAdapter bitmapEffectDialogAdapter = new BitmapEffectDialogAdapter(context, hasilDeskripsi, "SaveActivity");
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(bitmapEffectDialogAdapter);
                bitmapEffectDialogAdapter.notifyDataSetChanged();

                child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FrameLayout) findViewById(R.id.frameSaveDialog)).removeAllViews();
                        findViewById(R.id.frameSaveDialog).setVisibility(View.GONE);
                    }
                });
                ((FrameLayout) findViewById(R.id.frameSaveDialog)).addView(child);
                findViewById(R.id.frameSaveDialog).setVisibility(View.VISIBLE);
            }
        });
        imageFilterLoad.execute(ThumbnailUtils.extractThumbnail(temp, 200, 200));
    }

    public void rotateToRight(){
        findViewById(R.id.imageSaveUtama).setRotation(findViewById(R.id.imageSaveUtama).getRotation() + 45f);
    }

    public void rotateToLeft(){
        findViewById(R.id.imageSaveUtama).setRotation(findViewById(R.id.imageSaveUtama).getRotation() - 45f);
    }

    public void restoreToOriginalImage(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Restore Confirmation");
        alertDialog.setMessage("Are you sure you want to restore the image preview with the original as this page was recently opened? This action cannot be undone");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        findViewById(R.id.pembungkusSaveUtama).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        findViewById(R.id.imageSaveUtama).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ((ImageView) findViewById(R.id.imageSaveUtama)).setImageBitmap(Constant.IMAGE_READY_SAVE);
                        ((ImageView) findViewById(R.id.imageSaveUtama)).setScaleType(ImageView.ScaleType.FIT_XY);
                        temp = Constant.IMAGE_READY_SAVE;
                        Toast.makeText(context, "Restored successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void saveImage(){
        Toast.makeText(context, "Loading, Please wait...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + getString(R.string.app_name));
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = getString(R.string.app_name) + "-" + n + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    saveBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                    MediaScannerConnection.scanFile(context, new String[]{
                                    file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                    Toast.makeText(context, "Image saved to \"" + getString(R.string.app_name) +"\"", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Close Confirmation");
        alertDialog.setMessage("Do you want to close this page and go back to previous page? Please note that any unsaved changed you have made will be lose, except you had save it before close this page");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        findViewById(R.id.pembungkusSaveUtama).setBackgroundColor(color);
        Toast.makeText(context, "Background color successfully changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
