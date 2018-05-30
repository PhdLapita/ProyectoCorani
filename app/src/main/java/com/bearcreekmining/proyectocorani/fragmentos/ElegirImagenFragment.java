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


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_imagen;
    }

    @Override
    public void initViewFragment() {
        super.initViewFragment();
        listImagenes = new ArrayList<>();
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));
        listImagenes.add(new imagenModel(R.mipmap.ic_suma));

        ImagenAdapter myAdapter = new ImagenAdapter(getActivity(),listImagenes);
        rvImagenes.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rvImagenes.setAdapter(myAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        elegirImagenFragmentInterface=(ElegirImagenFragmentInterface) context;
    }
}
