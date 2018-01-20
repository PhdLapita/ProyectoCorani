package com.bearcreekmining.proyectocorani.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bear on 11/01/18.
 */

public abstract class BaseFragment extends Fragment {
    View view;
    //Bundle savedInstanceState;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(getLayoutId(),container,false);
        //this.savedInstanceState=savedInstanceState;
        bindViews();
        initViewFragment();
        return view;
    }
    protected abstract int getLayoutId();

    private void bindViews() {
        unbinder= ButterKnife.bind(this,view);
    }
    public void initViewFragment(){}

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void irHacia(Class<?> to){
        Intent i= new Intent(getActivity(),to);
        startActivity(i);
    }
}
