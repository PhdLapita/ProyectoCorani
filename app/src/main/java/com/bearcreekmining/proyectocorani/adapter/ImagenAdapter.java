package com.bearcreekmining.proyectocorani.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.actividades.RegistrarLLaveActivity;
import com.bearcreekmining.proyectocorani.fragmentos.ElegirImagenFragment;
import com.bearcreekmining.proyectocorani.model.imagenModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagenAdapter extends RecyclerView.Adapter<ImagenAdapter.ImagenHolder> {
    private Context mContext ;
    private List<imagenModel> mData ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    ElegirImagenFragment elegirImagenFragment;

    public ImagenAdapter(Context mContext, List<imagenModel> mData, ElegirImagenFragment elegirImagenFragment) {
        this.mContext = mContext;
        this.mData = mData;
        this.elegirImagenFragment = elegirImagenFragment;
    }

    @NonNull
    @Override
    public ImagenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.adaper_imagen,parent,false);
        return new ImagenAdapter.ImagenHolder(view);      }

    @Override
    public void onBindViewHolder(@NonNull ImagenHolder holder, final int position) {

        holder.imgLista.setImageResource(mData.get(position).getThumbnail());
        holder.cvImagenLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImagen(position);
                Log.d("adapterImagen", String.valueOf(position));
                elegirImagenFragment.eleminarFragmento();
                /*Intent intent = new Intent(mContext,RegistrarLLaveActivity.class);
                // passing data to the book activity
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                // start the activity
                mContext.startActivity(intent);*/
            }
        });
    }

    public void saveImagen(int numero){
        SharedPreferences preferencias = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt("iamgenNumber", numero);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImagenHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgLista)ImageView imgLista;
        @BindView(R.id.cvImagenLista)CardView cvImagenLista;

        public ImagenHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

