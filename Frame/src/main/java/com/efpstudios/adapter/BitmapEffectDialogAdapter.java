package com.efpstudios.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.efpstudios.R;
import com.efpstudios.activity.MainActivity;
import com.efpstudios.activity.SaveActivity;
import com.efpstudios.asyncronus.ImageFilterSingleLoad;
import com.efpstudios.constant.Constant;
import com.efpstudios.model.EffectModel;
import com.efpstudios.recyclehelper.ViewWrapper;

import java.util.List;

public class BitmapEffectDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<EffectModel> kumpulanBitmapDialogEffect;
    private String idActivity;

    public BitmapEffectDialogAdapter(Context context, List<EffectModel> kumpulanBitmapDialogEffect, String idActivity) {
        this.context = context;
        this.kumpulanBitmapDialogEffect = kumpulanBitmapDialogEffect;
        this.idActivity = idActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(new RecyPerItem(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {
        final RecyPerItem holder = (RecyPerItem) holders.itemView;
        holder.tampunganUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idActivity.equals("MainActivity")) {
                    ImageFilterSingleLoad imageFilterSingleLoad = new ImageFilterSingleLoad(context, kumpulanBitmapDialogEffect.get(position).getEffectName());
                    imageFilterSingleLoad.setListenerEffect(new ImageFilterSingleLoad.ListenerDecrypt() {
                        @Override
                        public void onSelesaiDecrypt(Bitmap effectResult) {
                            MainActivity mainActivity = (MainActivity) context;
                            ((FrameLayout) mainActivity.findViewById(R.id.frameDialog)).removeAllViews();
                            mainActivity.findViewById(R.id.frameDialog).setVisibility(View.GONE);
                            mainActivity.updateBitmap(effectResult);
                        }
                    });
                    imageFilterSingleLoad.execute(Constant.IMAGE_USER);
                }else if(idActivity.equals("SaveActivity")){
                    SaveActivity saveActivity = (SaveActivity) context;
                    saveActivity.imageEffectApply(kumpulanBitmapDialogEffect.get(position).getEffectName());
                }
            }
        });
        holder.imgFrame.setImageBitmap(kumpulanBitmapDialogEffect.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return kumpulanBitmapDialogEffect.size();
    }

    class RecyPerItem extends FrameLayout {
        private LinearLayout tampunganUtama;
        private ImageView imgFrame;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.bitmapeffect_item, this);
            tampunganUtama = (LinearLayout) findViewById(R.id.penampungBitmapEffectItem);
            imgFrame = (ImageView) findViewById(R.id.imgBitmapEffectItem);

        }
    }
}
