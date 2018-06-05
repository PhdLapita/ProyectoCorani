package com.bearcreekmining.proyectocorani.utils;

import com.bearcreekmining.proyectocorani.R;

import java.util.ArrayList;
import java.util.List;

public class Constantes {
    public Constantes() {
        llenarLista();
    }
    public static List<Integer> listImagenes;
    public void llenarLista(){
        listImagenes = new ArrayList<>();
        listImagenes.add(R.mipmap.ic_house);
        listImagenes.add(R.mipmap.ic_edifico);
        listImagenes.add(R.mipmap.ic_box_security);
        listImagenes.add(R.mipmap.ic_carro);
        listImagenes.add(R.mipmap.ic_wallet);
        listImagenes.add(R.mipmap.ic_mochila);
        listImagenes.add(R.mipmap.ic_bolso);
        listImagenes.add(R.mipmap.ic_phone);
        listImagenes.add(R.mipmap.ic_laptop);
        listImagenes.add(R.mipmap.ic_child_female);
        listImagenes.add(R.mipmap.ic_child_man);
        listImagenes.add(R.mipmap.ic_peluche);
    }
}
