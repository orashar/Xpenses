package com.example.android.xpenses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentStats extends Fragment {

    View statsView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        statsView = inflater.inflate(R.layout.fragment_stats, container, false);


        Log.v("FragmentStats", "onCreate is done");
        return statsView;
    }
}
