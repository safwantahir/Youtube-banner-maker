package com.example.backgroundremoverlibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_fragment, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView start=view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsFragment optionsFragment = new OptionsFragment();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, optionsFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        return view;
    }
}