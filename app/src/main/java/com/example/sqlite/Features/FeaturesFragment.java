package com.example.sqlite.Features;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.sqlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FeaturesFragment extends Fragment implements FeatureAdapter.ClickListener {

    private RecyclerView recyclerView;
    private ArrayList<FeatureModelClass> arrayList;
    private FeatureAdapter featureAdapter;
    private FeatureDatabase featureDatabase;
    private FloatingActionButton fab;
    private String Name, Description;

    public FeaturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_features, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featureDatabase = new FeatureDatabase(getActivity());
        recyclerView = view.findViewById(R.id.rv_feature);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = view.findViewById(R.id.fab);

        featureDatabase = new FeatureDatabase(getActivity());
        featureAdapter = new FeatureAdapter(getActivity(),this);

        fab.setOnClickListener(v -> {
            AddNewFeature();
        });
        loaddata();
    }

    private void loaddata() {
        arrayList = featureDatabase.getData();
        featureAdapter.addItem(arrayList);
        recyclerView.setAdapter(featureAdapter);
    }

    private void AddNewFeature() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.create_feature);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        TextInputEditText etName = dialog.findViewById(R.id.et_name);
        TextInputEditText etDescription = dialog.findViewById(R.id.et_description);
        Button btnCreate = dialog.findViewById(R.id.btn_create);

        btnCreate.setOnClickListener(v -> {

            Name = etName.getText().toString();
            if (Name.isEmpty()) {
                etName.setError("Must required!");
                return;
            }
            Description = etDescription.getText().toString();
            if (Description.isEmpty()) {
                etDescription.setError("Must required");
                return;
            }
            long rowId = featureDatabase.InsertData(Name, Description);
            if (rowId > 0) {
                Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();
                loaddata();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "This feature name is available in list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClick(FeatureModelClass list, int position) {
        int id = Integer.parseInt(list.getId());
        String name = list.getName();
        String description = list.getDiscription();

        Dialog mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.update_delete_feature);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.show();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(mDialog.getWindow().getAttributes());
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.getWindow().setAttributes(params);

        TextInputEditText etName = mDialog.findViewById(R.id.et_name);
        TextInputEditText etDescription = mDialog.findViewById(R.id.et_description);
        Button btnUpdate = mDialog.findViewById(R.id.btn_update);
        Button btnDelete = mDialog.findViewById(R.id.btn_delete);

        etName.setText(name);
        etDescription.setText(description);

        btnUpdate.setOnClickListener(v -> {

            Name = etName.getText().toString();
            if (Name.isEmpty()) {
                etName.setError("Must required!");
                return;
            }
            Description = etDescription.getText().toString();
            if (Description.isEmpty()) {
                etDescription.setError("Must required");
                return;
            }

            featureDatabase.Update(id, etName.getText().toString(), etDescription.getText().toString());
            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
            loaddata();
            mDialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            featureDatabase.Delete(id);
            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            loaddata();
            mDialog.dismiss();
        });
    }
}
