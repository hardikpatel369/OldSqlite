package com.example.sqlite.Images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageModelClass> arrayList;

    public void addItem(ArrayList<ImageModelClass> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        ImageModelClass imageModelClass = arrayList.get(position);

        holder.id.setText("Id : " + imageModelClass.getId());
        holder.name.setText("Name : " + imageModelClass.getFileName());
        holder.path.setText("Path : "+imageModelClass.getFilePath());

        String image = ("".concat("file://").concat(imageModelClass.getFilePath()));

        Log.d("image---", "onBindViewHolder: "+image);

        Picasso.get().load(image).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView path;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tv_image_id);
            name = itemView.findViewById(R.id.tv_image_name);
            path = itemView.findViewById(R.id.tv_image_path);
            image = itemView.findViewById(R.id.iv_image);
        }
    }
}
