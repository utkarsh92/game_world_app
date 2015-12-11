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
public class Fragment_Two extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_two,container,false);
        return rootview;
    }
}
