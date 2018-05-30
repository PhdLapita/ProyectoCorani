package com.bearcreekmining.proyectocorani.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bearcreekmining.proyectocorani.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class itemListAdapter extends RecyclerView.Adapter<itemListAdapter.itemListHolder> {

    @NonNull
    @Override
    public itemListAdapter.itemListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull itemListAdapter.itemListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class itemListHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtItemListaDevice)TextView txtItemListaDevice;
        @BindView(R.id.imgItemListaDevice)ImageView imgItemListaDevice;
        public itemListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
