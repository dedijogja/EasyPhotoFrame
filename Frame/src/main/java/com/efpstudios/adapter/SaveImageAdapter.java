package com.efpstudios.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.efpstudios.R;
import com.efpstudios.activity.SaveActivity;
import com.efpstudios.recyclehelper.ViewWrapper;

public class SaveImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String[] semuaAksi;

    public SaveImageAdapter(Context context, String[] semuaAksi) {
        this.context = context;
        this.semuaAksi = semuaAksi;
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
                SaveActivity saveActivity = (SaveActivity) context;
                switch (position){
                    case 0:
                        saveActivity.saveImage();
                        break;
                    case 1:
                        saveActivity.shareImage();
                        break;
                    case 2:
                        saveActivity.setImageAs();
                        break;
                    case 3:
                        saveActivity.restoreToOriginalImage();
                        break;
                    case 4:
                        saveActivity.rotateToRight();
                        break;
                    case 5:
                        saveActivity.rotateToLeft();
                        break;
                    case 6:
                        saveActivity.imageEffectDialog();
                        break;
                    case 7:
                        saveActivity.cropImage();
                        break;
                    case 8:
                        saveActivity.forceToSquare();
                        break;
                    case 9:
                         saveActivity.fitSquareWithColor();
                        break;
                    case 10:
                        saveActivity.changeBackgroundColor();
                        break;
                    default:
                        break;
                }
            }
        });

        holder.textAksi.setText(semuaAksi[position]);
    }

    @Override
    public int getItemCount() {
        return semuaAksi.length;
    }

    class RecyPerItem extends FrameLayout {
        private LinearLayout tampunganUtama;
        private TextView textAksi;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.saveoption_item, this);
            tampunganUtama = (LinearLayout) findViewById(R.id.penampungSaveOptionItem);
            textAksi = (TextView) findViewById(R.id.textSaveOptionItem);

        }
    }
}
