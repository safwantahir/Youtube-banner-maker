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

public class OptionsFragment extends Fragment {
    ImageView Createbutton,TemplateButton,MyDesigns;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.options_fragment, container, false);

       Createbutton=view.findViewById(R.id.create);
       TemplateButton=view.findViewById(R.id.template);
       MyDesigns=view.findViewById(R.id.my_designs);

       MyDesigns.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MyDesigns myDesigns = new MyDesigns();
               FragmentTransaction transaction = requireFragmentManager().beginTransaction();

               transaction.replace(R.id.fragmentContainer, myDesigns);
               transaction.addToBackStack(null);

               transaction.commit();
           }
       });

     Createbutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             SizeSelection startCreating = new SizeSelection();
             FragmentTransaction transaction = requireFragmentManager().beginTransaction();

             transaction.replace(R.id.fragmentContainer, startCreating);
             transaction.addToBackStack(null);

             transaction.commit();
         }
     });



        TemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Templates templates = new Templates();
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentContainer, templates);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        return view;
    }
}