package com.bearcreekmining.proyectocorani.actividades;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bearcreekmining.proyectocorani.R;

public class SplashActivity extends BaseActivity {


    final Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        super.initView();
        delayxD();

    }
    public void delayxD(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                irHacia(MainActivity.class);
                finish();
                // Do something after 5s = 5000ms
                //buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }
        }, 2500);//TIEMPO PARA DEJAR QUE EL MAPA CARGUE EN LA APLICACION
    }
}
