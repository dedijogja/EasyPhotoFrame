package com.efpstudios.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.efpstudios.R;
import com.efpstudios.adapter.BitmapEffectDialogAdapter;
import com.efpstudios.adapter.BitmapFrameDialogAdapter;
import com.efpstudios.adapter.EmoticonAdapter;
import com.efpstudios.adapter.TextAdapter;
import com.efpstudios.asyncronus.EmoticonMultiLoad;
import com.efpstudios.asyncronus.ImageFilterMultipleLoad;
import com.efpstudios.constant.Applications;
import com.efpstudios.constant.Constant;
import com.efpstudios.constant.FontConstant;
import com.efpstudios.constant.NativeComunicate;
import com.efpstudios.decryption.file.MultiDecryption;
import com.efpstudios.decryption.file.SingleDecryption;
import com.efpstudios.model.EffectModel;
import com.efpstudios.model.FontModel;
import com.efpstudios.view.StickerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener{

    private StickerView stickerView;

    private StickerView currentStickerText;

    private static final int KODE_REQ_GALERI = 1;
    private static final int KODE_REQ_KAMERA = 2;
    private Context context = this;
    private Typeface currentTypeface = null;
    private int typeTypeFace = Typeface.NORMAL;

    private int textColor = Color.parseColor("#c0392b");
    private View childTextDialog;
    private boolean statusUbahImageUser = true;

    private boolean noDialogOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.berhasilLoadIklan)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanMainActivity);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(new NativeComunicate(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            StartAppSDK.init(this, new NativeComunicate(this).getStartAppId(),false);
            StartAppAd.disableSplash();
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanMainActivity);
            Banner startAppBanner = new Banner(this);
            adContainer.addView(startAppBanner);
        }

        //init
        stickerView = new StickerView(this);
        childTextDialog = getLayoutInflater().inflate(R.layout.text_dialog, null);

        if(Constant.PENYIMPAN_ALAMAT_KONTEN!=null) {
            if (Constant.IMAGE_USER != null) {
                addStickerView(Constant.IMAGE_USER, ((FrameLayout) findViewById(R.id.areaSticker)));
                mainImportant();
            } else {
                Constant.IMAGE_USER = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                addStickerView(Constant.IMAGE_USER, ((FrameLayout) findViewById(R.id.areaSticker)));
                mainImportant();
            }
        }else{
            try {
                Constant.PENYIMPAN_ALAMAT_KONTEN = Constant.LIST_SEMUA_KONTEN(this);
                if (Constant.IMAGE_USER != null) {
                    addStickerView(Constant.IMAGE_USER, ((FrameLayout) findViewById(R.id.areaSticker)));
                    mainImportant();
                } else {
                    Constant.IMAGE_USER = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    addStickerView(Constant.IMAGE_USER, ((FrameLayout) findViewById(R.id.areaSticker)));
                    mainImportant();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(getIntent().getStringExtra(Constant.KODE_PERINTAH)!=null){
            String isiPerintah = getIntent().getStringExtra(Constant.KODE_PERINTAH);
            switch (isiPerintah) {
                case Constant.PERINTAH_BUKA_DIALOG_EMOTICON:
                    try {
                        showEmoticonDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.PERINTAH_BUKA_DIALOG_TEXT:
                    showColorPickerDialog();
                    break;
            }
        }
    }

    private void mainImportant(){
        Random generator = new Random();
        int randomIndex = generator.nextInt(Constant.PENYIMPAN_ALAMAT_KONTEN.length);
        SingleDecryption singleDecryption = new SingleDecryption(this);
        singleDecryption.setListenerDecrypt(new SingleDecryption.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(Bitmap hasilDeskripsi) {
                ((ImageView) findViewById(R.id.imgViewRoot)).setImageBitmap(hasilDeskripsi);
                ((ImageView) findViewById(R.id.imgViewRootAtas)).setImageBitmap(hasilDeskripsi);
            }
        });
        singleDecryption.execute(Constant.PENYIMPAN_ALAMAT_KONTEN[randomIndex]);


        ((Switch) findViewById(R.id.switchPrevEdit)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(currentStickerText!=null){
                        currentStickerText.setInEdit(false);
                    }
                    stickerView.setInEdit(false);
                    findViewById(R.id.imgViewRoot).setVisibility(View.GONE);
                    findViewById(R.id.imgViewRootAtas).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.textPrevEdit)).setText("Preview Mode");
                }else{
                    if(currentStickerText!=null){
                        currentStickerText.setInEdit(true);
                    }
                    stickerView.setInEdit(true);
                    findViewById(R.id.imgViewRoot).setVisibility(View.VISIBLE);
                    findViewById(R.id.imgViewRootAtas).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.textPrevEdit)).setText("Edit Mode");
                }
            }
        });

        findViewById(R.id.btnUbahBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    showBitmapFrameDialog();
                }
            }
        });

        findViewById(R.id.btnTambahText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    showTextDialog();
                }
            }
        });

        findViewById(R.id.btnTambahEmotiocn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    try {
                        showEmoticonDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.pembungkusUtamaEditor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    if (((Switch) findViewById(R.id.switchPrevEdit)).isChecked()) {
                        ((Switch) findViewById(R.id.switchPrevEdit)).setChecked(false);
                    } else {
                        ((Switch) findViewById(R.id.switchPrevEdit)).setChecked(true);
                    }
                }
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    ((Switch) findViewById(R.id.switchPrevEdit)).setChecked(true);
                    ((Switch) findViewById(R.id.switchPrevEdit)).setChecked(false);
                    ((Switch) findViewById(R.id.switchPrevEdit)).setChecked(true);

                    final Applications app = (Applications) getApplication();
                    String status = app.getStatusIklan();
                    if (status.equals(Constant.berhasilLoadIklan)) {
                        if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                                || !app.getInterstitial().isLoaded()) {
                            aksiSimpan(false);
                        }
                        app.getInterstitial().setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                aksiSimpan(false);

                                app.loadIntersTitial();
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                if (app.getHitungFailed() < 2) {
                                    app.loadIntersTitial();
                                    app.setHitungFailed();
                                }
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdLoaded() {
                                app.setHitungFailed(0);
                                super.onAdLoaded();
                            }
                        });
                        app.tampilkanInterstitial();
                    } else {
                        if (app.getPenghitungStartApp() == 0) {
                            app.setPenghitungStartApp(1);
                            aksiSimpan(true);
                        } else {
                            app.setPenghitungStartApp(0);
                            aksiSimpan(false);
                        }
                    }

                }
            }
        });

        findViewById(R.id.btnCropImgUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened) {
                    cropImageUser();
                }
            }
        });

        findViewById(R.id.btnFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDialogOpened){
                    showBitmapEffectDialog();
                }
            }
        });

    }

    private void aksiSimpan(final boolean startApp){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Constant.IMAGE_READY_SAVE = getSaveReadyBitmap();
                startActivity(new Intent(MainActivity.this, SaveActivity.class));
                if (startApp){
                    StartAppAd.showAd(context);
                }
            }
        }, 300);
    }

    private Bitmap getSaveReadyBitmap(){
        RelativeLayout view = (RelativeLayout) findViewById(R.id.pembungkusUtamaEditor);
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public void cropImageUser(){
        noDialogOpened = false;
        final View child = getLayoutInflater().inflate(R.layout.cropper_dialog, null);

        final CropImageView cropImageView = (CropImageView) child.findViewById(R.id.cropImageView);
        cropImageView.setImageBitmap(Constant.IMAGE_USER);

        child.findViewById(R.id.btnCancelCrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDialogOpened = true;
                ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                findViewById(R.id.frameDialog).setVisibility(View.GONE);
            }
        });

        child.findViewById(R.id.btnCropImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDialogOpened = true;
                Constant.IMAGE_USER = cropImageView.getCroppedImage();
                stickerView.setBitmap(Constant.IMAGE_USER);
                statusUbahImageUser = true;
                ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                findViewById(R.id.frameDialog).setVisibility(View.GONE);

                Toast.makeText(context, "Crop Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
        findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
    }

    public void tambahStickerEmoticon(Bitmap bitmap){
        noDialogOpened = true;
        addStickerText(bitmap);

        ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
        findViewById(R.id.frameDialog).setVisibility(View.GONE);
    }

    private void showEmoticonDialog() throws IOException {
        noDialogOpened = false;
        EmoticonMultiLoad emoticonMultiLoad = new EmoticonMultiLoad(this);
        emoticonMultiLoad.setListenerDecrypt(new EmoticonMultiLoad.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi) {
                View child = getLayoutInflater().inflate(R.layout.bitmapeffect_dialog, null);

                RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);

                EmoticonAdapter emoticonAdapter = new EmoticonAdapter(context, hasilDeskripsi);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 5);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(emoticonAdapter);
                emoticonAdapter.notifyDataSetChanged();

                child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noDialogOpened = true;
                        ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                        findViewById(R.id.frameDialog).setVisibility(View.GONE);
                    }
                });
                ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
                findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
            }
        });
        emoticonMultiLoad.execute(Constant.LIST_SEMUA_EMOTICON(this));
    }

    private void showTextDialog(){
        noDialogOpened = false;
        childTextDialog.findViewById(R.id.btnTextChangeColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
        childTextDialog.findViewById(R.id.btnTextCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDialogOpened = true;
                ((FrameLayout)findViewById(R.id.frameDialog)).removeAllViews();
                findViewById(R.id.frameDialog).setVisibility(View.GONE);
            }
        });
        childTextDialog.findViewById(R.id.btnTextOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDialogOpened = true;
                Bitmap bitmap = stringToBitmap(((EditText) childTextDialog.findViewById(R.id.editTextDialog)).getText().toString(),
                        ((SeekBar) childTextDialog.findViewById(R.id.seekbarTextFontSize)).getProgress(),
                        textColor,
                        currentTypeface
                        );
                addStickerText(bitmap);

                ((FrameLayout)findViewById(R.id.frameDialog)).removeAllViews();
                findViewById(R.id.frameDialog).setVisibility(View.GONE);
            }
        });
        ((SeekBar) childTextDialog.findViewById(R.id.seekbarTextFontSize)).setProgress(25);
        ((SeekBar) childTextDialog.findViewById(R.id.seekbarTextFontSize)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTextSize((progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FontModel[] fontModels = null;
        try {
            fontModels = FontConstant.SEMUA_FONT_MODEL(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] semuaFont = new String[fontModels.length];
        final Typeface[] semuaTypeFace = new Typeface[fontModels.length];
        for(int i =0; i<semuaTypeFace.length; i++){
            semuaFont[i] = fontModels[i].getFontName();
            semuaTypeFace[i] = fontModels[i].getTypeface();
        }
        currentTypeface = semuaTypeFace[0];
        ArrayAdapter<String> adapterSpinnerTextFontStyle = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Normal", "Bold", "Italic", "Bold Italic"
        });
        ((Spinner) childTextDialog.findViewById(R.id.spinnerTextFontStyle)).setAdapter(adapterSpinnerTextFontStyle);
        ((Spinner) childTextDialog.findViewById(R.id.spinnerTextFontStyle)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        typeTypeFace = Typeface.NORMAL;
                        ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTypeface(currentTypeface, typeTypeFace);
                        break;
                    case 1:
                        typeTypeFace = Typeface.BOLD;
                        ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTypeface(currentTypeface, typeTypeFace);
                        break;
                    case 2:
                        typeTypeFace = Typeface.ITALIC;
                        ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTypeface(currentTypeface, typeTypeFace);
                        break;
                    case 3:
                        typeTypeFace = Typeface.BOLD_ITALIC;
                        ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTypeface(currentTypeface, typeTypeFace);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextAdapter adapterSpinnerTextChooseFont = new TextAdapter(MainActivity.this,
                R.layout.spinner_row, semuaFont, semuaTypeFace);

        ((Spinner) childTextDialog.findViewById(R.id.spinnerTextChooseFont)).setAdapter(adapterSpinnerTextChooseFont);
        ((Spinner) childTextDialog.findViewById(R.id.spinnerTextChooseFont)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTypeface = semuaTypeFace[position];
                ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTypeface(currentTypeface, typeTypeFace);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((FrameLayout)findViewById(R.id.frameDialog)).addView(childTextDialog);
        findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
    }

    private void showColorPickerDialog(){
        noDialogOpened = false;
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(0)
                .setColor(textColor)
                .setShowAlphaSlider(true)
                .show(MainActivity.this);
    }

    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        noDialogOpened = true;
        textColor = color;
        childTextDialog.findViewById(R.id.btnTextChangeColor).setBackgroundColor(textColor);
        ((EditText) childTextDialog.findViewById(R.id.editTextDialog)).setTextColor(textColor);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        noDialogOpened = true;
    }

    public Bitmap stringToBitmap(String text, float fontSize, int fontColor, Typeface typeface){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(fontSize);
        paint.setColor(fontColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(typeface);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void updateFrameBackground(Bitmap bitmap){
        noDialogOpened = true;
        ((ImageView) findViewById(R.id.imgViewRoot)).setImageBitmap(bitmap);
        ((ImageView) findViewById(R.id.imgViewRootAtas)).setImageBitmap(bitmap);

        ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
        findViewById(R.id.frameDialog).setVisibility(View.GONE);
    }

    private void showBitmapFrameDialog(){
        noDialogOpened = false;
        if(Constant.SEMUA_IMAGE_BACKGROUND == null) {
            MultiDecryption multiDecryption = new MultiDecryption(this);
            multiDecryption.setListenerDecrypt(new MultiDecryption.ListenerDecrypt() {
                @Override
                public void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi) {
                    Constant.SEMUA_IMAGE_BACKGROUND = hasilDeskripsi;

                    View child = getLayoutInflater().inflate(R.layout.frame_dialog, null);

                    RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);
                    BitmapFrameDialogAdapter bitmapFrameDialogAdapter = new BitmapFrameDialogAdapter(context, Constant.SEMUA_IMAGE_BACKGROUND);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(bitmapFrameDialogAdapter);
                    bitmapFrameDialogAdapter.notifyDataSetChanged();

                    child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noDialogOpened = true;
                            ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                            findViewById(R.id.frameDialog).setVisibility(View.GONE);
                        }
                    });
                    ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
                    findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
                }
            });
            multiDecryption.execute(Constant.PENYIMPAN_ALAMAT_KONTEN);
        }else{
            View child = getLayoutInflater().inflate(R.layout.frame_dialog, null);

            RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);
            BitmapFrameDialogAdapter bitmapFrameDialogAdapter = new BitmapFrameDialogAdapter(context, Constant.SEMUA_IMAGE_BACKGROUND);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(bitmapFrameDialogAdapter);
            bitmapFrameDialogAdapter.notifyDataSetChanged();

            child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noDialogOpened = true;
                    ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                    findViewById(R.id.frameDialog).setVisibility(View.GONE);
                }
            });
            ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
            findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == KODE_REQ_GALERI) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                yourSelectedImage = getResizedBitmap(yourSelectedImage, 1000);
                Constant.IMAGE_USER = yourSelectedImage;
                stickerView.setBitmap(Constant.IMAGE_USER);
                statusUbahImageUser = true;
            }else if(requestCode == KODE_REQ_KAMERA){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = getResizedBitmap(photo, 1000);
                Constant.IMAGE_USER = photo;
                stickerView.setBitmap(Constant.IMAGE_USER);
                statusUbahImageUser = true;
            }
        }else{
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void addStickerText(Bitmap bitmap){
        final StickerView stickerText = new StickerView(this);
        stickerText.setBitmap(bitmap);
        stickerText.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                ((FrameLayout) findViewById(R.id.areaStickerTextEmot)).removeView(stickerText);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                currentStickerText.setInEdit(false);
                currentStickerText = stickerView;
                currentStickerText.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                Toast.makeText(MainActivity.this, "Effect not available for this item!", Toast.LENGTH_SHORT).show();
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ((FrameLayout) findViewById(R.id.areaStickerTextEmot)).addView(stickerText, lp);
        setCurrentEdit(stickerText);
    }

    private void setCurrentEdit(StickerView stickerText) {
        if (currentStickerText != null) {
            currentStickerText.setInEdit(false);
        }

        currentStickerText = stickerText;
        stickerText.setInEdit(true);
    }

    private void addStickerView(Bitmap bitmap, final FrameLayout rootView) {
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                final String[] items = {"Choose From Gallery", "Pick From Camera", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Change Your Image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int posisi) {
                        switch (posisi){
                            case 0 :
                                Intent views = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                views.setType("image/*");
                                startActivityForResult(views, KODE_REQ_GALERI);
                                break;
                            case 1 :
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, KODE_REQ_KAMERA);
                                break;
                            default:
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }

            @Override
            public void onTop(final StickerView stickerView) {
                if(noDialogOpened) {
                    showBitmapEffectDialog();
                }
            }

            @Override
            public void onEdit(StickerView stickerView) {

            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootView.addView(stickerView, lp);
        stickerView.setInEdit(true);
    }

    public void showBitmapEffectDialog(){
        noDialogOpened = false;
        if(statusUbahImageUser) {
            ImageFilterMultipleLoad imageFilterLoad = new ImageFilterMultipleLoad(context);
            imageFilterLoad.setListenerEffect(new ImageFilterMultipleLoad.ListenerDecrypt() {
                @Override
                public void onSelesaiDecrypt(List<EffectModel> hasilDeskripsi) {
                    statusUbahImageUser = false;
                    Constant.IMAGE_USER_FILTER = hasilDeskripsi;

                    View child = getLayoutInflater().inflate(R.layout.bitmapeffect_dialog, null);

                    RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);

                    BitmapEffectDialogAdapter bitmapEffectDialogAdapter = new BitmapEffectDialogAdapter(context, hasilDeskripsi, "MainActivity");
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(bitmapEffectDialogAdapter);
                    bitmapEffectDialogAdapter.notifyDataSetChanged();

                    child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noDialogOpened = true;
                            ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                            findViewById(R.id.frameDialog).setVisibility(View.GONE);
                        }
                    });
                    ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
                    findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
                }
            });
            imageFilterLoad.execute(ThumbnailUtils.extractThumbnail(Constant.IMAGE_USER, 200, 200));
        }else{
            View child = getLayoutInflater().inflate(R.layout.bitmapeffect_dialog, null);

            RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycleBitmapEffectDialog);

            BitmapEffectDialogAdapter bitmapEffectDialogAdapter = new BitmapEffectDialogAdapter(context, Constant.IMAGE_USER_FILTER,"MainActivity");
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(bitmapEffectDialogAdapter);
            bitmapEffectDialogAdapter.notifyDataSetChanged();

            child.findViewById(R.id.btnCloseBitmapEffectDialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noDialogOpened = true;
                    ((FrameLayout) findViewById(R.id.frameDialog)).removeAllViews();
                    findViewById(R.id.frameDialog).setVisibility(View.GONE);
                }
            });
            ((FrameLayout) findViewById(R.id.frameDialog)).addView(child);
            findViewById(R.id.frameDialog).setVisibility(View.VISIBLE);
        }
    }

    public void updateBitmap(Bitmap bitmap){
        noDialogOpened = true;
        stickerView.setBitmap(bitmap);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again for exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}
