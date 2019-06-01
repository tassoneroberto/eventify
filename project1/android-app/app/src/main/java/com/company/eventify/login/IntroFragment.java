package com.company.eventify.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.eventify.R;

public class IntroFragment extends Fragment {

    ImageView imageView;
    TextView introTitle;
    TextView introDescription;
    int imageResource;
    String title, description;

    public void init(int imageResource, String title, String description) {
        this.description = description;
        this.title = title;
        this.imageResource = imageResource;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro2, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageViewIntro);
        introTitle = (TextView) view.findViewById(R.id.intro_title);
        introDescription = (TextView) view.findViewById(R.id.intro_description);
        introTitle.setText(title);
        introDescription.setText(description);

        imageView.setImageResource(imageResource);
        return view;
    }

}
