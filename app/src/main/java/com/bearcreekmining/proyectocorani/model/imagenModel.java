package com.bearcreekmining.proyectocorani.model;

import com.bearcreekmining.proyectocorani.R;

import java.util.ArrayList;
import java.util.List;

public class imagenModel {
    private int Thumbnail ;
    private List<imagenModel> listImagenes ;

    public imagenModel(List<imagenModel> listImagenes) {
        this.listImagenes = listImagenes;
        llenarLista();
    }

    public imagenModel() {

    }

    public imagenModel(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public void llenarLista(){
        listImagenes = new ArrayList<>();
        listImagenes.add(new imagenModel(R.mipmap.ic_house));
        listImagenes.add(new imagenModel(R.mipmap.ic_edifico));
        listImagenes.add(new imagenModel(R.mipmap.ic_box_security));
        listImagenes.add(new imagenModel(R.mipmap.ic_carro));
        listImagenes.add(new imagenModel(R.mipmap.ic_wallet));
        listImagenes.add(new imagenModel(R.mipmap.ic_mochila));
        listImagenes.add(new imagenModel(R.mipmap.ic_bolso));
        listImagenes.add(new imagenModel(R.mipmap.ic_phone));
        listImagenes.add(new imagenModel(R.mipmap.ic_laptop));
        listImagenes.add(new imagenModel(R.mipmap.ic_child_female));
        listImagenes.add(new imagenModel(R.mipmap.ic_child_man));
        listImagenes.add(new imagenModel(R.mipmap.ic_peluche));
    }

}
