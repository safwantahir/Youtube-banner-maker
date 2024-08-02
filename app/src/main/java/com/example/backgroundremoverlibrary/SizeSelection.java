package com.example.backgroundremoverlibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SizeSelection extends Fragment {
ImageView first,second,third,fourth ;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.size_selection, container, false);
        first=view.findViewById(R.id.size1);
        second=view.findViewById(R.id.size2);
        third=view.findViewById(R.id.size3);
        fourth=view.findViewById(R.id.size4);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirstBanner firstBanner = new FirstBanner();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, firstBanner);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondBanner secondBanner = new SecondBanner();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, secondBanner);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirdBanner thirdBanner = new ThirdBanner();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, thirdBanner);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FourthBanner fourthBanner = new FourthBanner();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, fourthBanner);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });


        return view;
    }
}
