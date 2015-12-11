package com.example.prakhargupta.myapplication5;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Prakhar Gupta on 04/10/2015.
 */
public class Fragment_One extends Fragment {
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_one,container,false);
        return rootView;
    }
}
