package com.example.mytravelsver2;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytravelsver2.databinding.RecycleviewImagesBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class ImagesRecViewAdapter extends RecyclerView.Adapter<ImagesRecViewAdapter.ImageViewHolder> {
    private List<PhotoModel> photoList;
    private Context context;
    private GalleryRecViewAdapter galleryRcvAdapter;

    public ImagesRecViewAdapter(Context context, int id, GalleryRecViewAdapter galleryRcvAdapter) {
        this.context = context;
        this.galleryRcvAdapter = galleryRcvAdapter;
    }

    public void setData(List<PhotoModel> list) {
        this.photoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleviewImagesBinding binding = RecycleviewImagesBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        PhotoModel photoModel = photoList.get(position);
        Uri uri = photoModel.getUri();
        if (uri == null) {
            return;
        }
//        holder.binding.imageCardView.setImageURI(uri);
//        holder.binding.imageCardView.setImageBitmap();
        Glide.with(context).load(uri).timeout(6000).into(holder.binding.imageCardView);

        holder.binding.imageCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Remove image")
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            photoList.remove(holder.getBindingAdapterPosition());
                            galleryRcvAdapter.buildImageRefTail("/" + photoModel.getName());
                            notifyItemRemoved(holder.getBindingAdapterPosition());
                        })
                        .show();
                return true;
            }
        });

        holder.binding.imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show full image
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ImageFragment imageFragment = new ImageFragment(uri);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.galleryLayout, imageFragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (photoList != null) {
            return photoList.size();
        }
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        RecycleviewImagesBinding binding;

        public ImageViewHolder(@NonNull RecycleviewImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
