package com.example.mytravelsver2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class GalleryRecViewAdapter extends RecyclerView.Adapter<GalleryRecViewAdapter.GalleryViewHolder>{
    private Context context;
    private List<AlbumModel> albumList;
    private GalleryFragment fragment;
    private AlbumModel album;

    public GalleryRecViewAdapter(Context context, int id, GalleryFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(List<AlbumModel> list) {
        this.albumList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_list, parent, false);
        return new GalleryRecViewAdapter.GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        album = albumList.get(position);
        if (album == null) {
            return;
        }
        holder.txtAlbum.setText(album.getNameAlbum());

        ImagesRecViewAdapter imagesAdapter = new ImagesRecViewAdapter(context, R.layout.images_list, GalleryRecViewAdapter.this);
        imagesAdapter.setData(album.getPhotoList());
        holder.rcvImagesList.setAdapter(imagesAdapter);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete this album")
                        .setMessage("Album: " + album.getNameAlbum() + "\n")
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            albumList.remove(album);
                            // delete whole album from firebase
                            for (PhotoModel photoModel : album.getPhotoList()) {
                                fragment.deleteImageFromDB(album.getNameAlbum() + "/" + photoModel.getName());
                            }

                            notifyItemRemoved(holder.getBindingAdapterPosition());

                            Toast.makeText(context, "Removed " + album.getNameAlbum(), Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
        });

        holder.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), holder.getBindingAdapterPosition());
            }
        });
    }

    public void buildImageRefTail(String imageName) {
        String imageRefTail = album.getNameAlbum() + imageName;
        fragment.deleteImageFromDB(imageRefTail);
    }

    @Override
    public int getItemCount() {
        if (albumList != null) {
            return albumList.size();
        }
        return 0;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAlbum;
        private RecyclerView rcvImagesList;
        private ImageButton btnAddImage;
        private ImageButton btnDelete;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlbum = itemView.findViewById(R.id.txtAlbum);
            rcvImagesList = itemView.findViewById(R.id.rcvImagesList);
            btnAddImage = itemView.findViewById(R.id.btnAddImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
