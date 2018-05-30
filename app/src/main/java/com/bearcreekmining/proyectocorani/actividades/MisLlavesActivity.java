package com.bearcreekmining.proyectocorani.actividades;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.bearcreekmining.proyectocorani.R;
import butterknife.BindView;
import butterknife.OnClick;

public class MisLlavesActivity extends BaseActivity {

    @BindView(R.id.fab)FloatingActionButton fab;

    @OnClick(R.id.fab) public void clicBotonFlotante(View view){
                irHacia(RegistrarLLaveActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mis_llaves;
    }

    @Override
    public void initView() {
        super.initView();
    }


}
