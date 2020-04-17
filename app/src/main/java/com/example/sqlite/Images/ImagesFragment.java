package com.example.sqlite.Images;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sqlite.Features.FeatureModelClass;
import com.example.sqlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ImageDatabase imageDatabase;
    private ImageAdapter imageAdapter;
    private ArrayList<ImageModelClass> arrayList;

    public ImagesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        fab = view.findViewById(R.id.fab);

        imageAdapter = new ImageAdapter();
        imageDatabase = new ImageDatabase(getActivity());

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),ImageLoadActivity.class);
            startActivity(intent);
        });
        loaddata();
    }

    private void loaddata() {
        arrayList = imageDatabase.getData();
        imageAdapter.addItem(arrayList);
        recyclerView.setAdapter(imageAdapter);
    }
}
