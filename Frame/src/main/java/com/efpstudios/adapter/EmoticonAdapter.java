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
import com.efpstudios.asyncronus.EmoticonSingleLoad;
import com.efpstudios.constant.Constant;
import com.efpstudios.recyclehelper.ViewWrapper;

import java.io.IOException;
import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Bitmap> kumpulanEmotivon;

    public EmoticonAdapter(Context context, List<Bitmap> kumpulanEmotivon) {
        this.context = context;
        this.kumpulanEmotivon = kumpulanEmotivon;
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
                EmoticonSingleLoad emoticonSingleLoad = new EmoticonSingleLoad(context);
                emoticonSingleLoad.setListenerEffect(new EmoticonSingleLoad.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(Bitmap result) {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.tambahStickerEmoticon(result);
                    }
                });
                try {
                    emoticonSingleLoad.execute(Constant.LIST_SEMUA_EMOTICON(context)[position]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.imgFrame.setImageBitmap(kumpulanEmotivon.get(position));
    }

    @Override
    public int getItemCount() {
        return kumpulanEmotivon.size();
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
