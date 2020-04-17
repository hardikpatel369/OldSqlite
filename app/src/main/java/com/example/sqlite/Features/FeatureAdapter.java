package com.example.sqlite.Features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.Images.ImageAdapter;
import com.example.sqlite.Images.ImageDatabase;
import com.example.sqlite.Images.ImageModelClass;
import com.example.sqlite.R;

import java.util.ArrayList;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FeatureModelClass> arrayList;

    public void addItem(ArrayList<FeatureModelClass> arrayList){
        this.arrayList = arrayList;
    }

    public interface ClickListener {
        void OnClick(FeatureModelClass list, int position);
    }
    private ClickListener clickListener;

    public FeatureAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FeatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.feature_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FeatureAdapter.ViewHolder holder, int position) {
        FeatureModelClass featureModelClass = arrayList.get(position);

        holder.id.setText("Id : " + featureModelClass.getId());
        holder.name.setText("Name : " + featureModelClass.getName());
        holder.description.setText("Description : " + featureModelClass.getDiscription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickListener != null){
                    clickListener.OnClick(featureModelClass,position);
                }else {
                    Toast.makeText(context, "clickListener null!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView description;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tv_feature_id);
            name = itemView.findViewById(R.id.tv_feature_name);
            description = itemView.findViewById(R.id.tv_feature_description);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}