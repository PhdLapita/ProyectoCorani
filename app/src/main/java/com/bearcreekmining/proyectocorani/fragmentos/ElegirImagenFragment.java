package com.bearcreekmining.proyectocorani.fragmentos;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.adapter.ImagenAdapter;
import com.bearcreekmining.proyectocorani.model.imagenModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ElegirImagenFragment extends BaseFragment {
    @BindView(R.id.rvImagenes)RecyclerView rvImagenes;
    List<imagenModel> listImagenes ;
    ElegirImagenFragmentInterface elegirImagenFragmentInterface;
    @OnClick(R.id.btn_fondo)public void clicFondo(){
        elegirImagenFragmentInterface.clicFondoxD();
    }

    public void eleminarFragmento(){
        elegirImagenFragmentInterface.clicFondoxD();
        elegirImagenFragmentInterface.verificarHayImagen();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_imagen;
    }

    @Override
    public void initViewFragment() {
        super.initViewFragment();
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

        ImagenAdapter myAdapter = new ImagenAdapter(getActivity(),listImagenes,this);
        rvImagenes.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rvImagenes.setAdapter(myAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        elegirImagenFragmentInterface=(ElegirImagenFragmentInterface) context;
    }


}
