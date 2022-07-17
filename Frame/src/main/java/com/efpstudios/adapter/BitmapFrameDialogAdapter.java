package com.efpstudios.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.efpstudios.R;
import com.efpstudios.activity.MainActivity;
import com.efpstudios.constant.Constant;
import com.efpstudios.decryption.file.SingleDecryption;
import com.efpstudios.recyclehelper.ViewWrapper;

import java.util.List;

public class BitmapFrameDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Bitmap> kumpulanFrame;

    public BitmapFrameDialogAdapter(Context context, List<Bitmap> kumpulanFrame) {
        this.context = context;
        this.kumpulanFrame = kumpulanFrame;
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
                SingleDecryption singleDecryption = new SingleDecryption(context);
                singleDecryption.setListenerDecrypt(new SingleDecryption.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(Bitmap hasilDeskripsi) {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.updateFrameBackground(hasilDeskripsi);
                    }
                });
                singleDecryption.execute(Constant.PENYIMPAN_ALAMAT_KONTEN[position]);
            }
        });

       holder.imgFrame.setImageBitmap(kumpulanFrame.get(position));
    }

    @Override
    public int getItemCount() {
        return kumpulanFrame.size();
    }

    class RecyPerItem extends FrameLayout {
        private RelativeLayout tampunganUtama;
        private ImageView imgFrame;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.frame_item, this);
            tampunganUtama = (RelativeLayout) findViewById(R.id.tampunganUtama);
            imgFrame = (ImageView) findViewById(R.id.imgItem);

        }
    }
}
