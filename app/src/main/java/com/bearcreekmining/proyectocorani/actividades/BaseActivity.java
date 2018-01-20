package com.bearcreekmining.proyectocorani.actividades;

import com.bearcreekmining.proyectocorani.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import butterknife.ButterKnife;

/**
 * Created by bear on 11/01/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupToolbar();
        bindViews();
        initView();
    }

    public void initView() {}

    public void setupToolbar() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }

    @Nullable
    public Toolbar getToolbar(){
        return mToolbar;
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();

    public void irHacia(Class<?> to){
        Intent i= new Intent(getApplicationContext(),to);
        startActivity(i);
    }
    public void showFragment(Fragment fragment, int containerViewId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    public void deleteFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, int containerViewId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }
}
