package com.example.mytravelsver2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GalleryFragment extends Fragment implements AddAlbumDialogFragment.DialogListener {
    private FloatingActionButton btnAddAlbum;
    private RecyclerView rcvGallery;
    private GalleryRecViewAdapter galleryRcvAdapter;
    private volatile List<AlbumModel> albumList;
    private FirebaseAuth auth;
    private List<PhotoModel> photoList;
    private StorageReference storageRef;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference().child(auth.getCurrentUser().getEmail()).child("Gallery");
        albumList = downloadGallery();
        while (albumList == null) {
            // wait download
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        btnAddAlbum = view.findViewById(R.id.btnAddAlbum);

        // Set the adapter
        Context context = view.getContext();
        rcvGallery = view.findViewById(R.id.rcvGallery);
        rcvGallery.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        galleryRcvAdapter = new GalleryRecViewAdapter(context, R.layout.fragment_gallery, GalleryFragment.this);
        galleryRcvAdapter.setData(albumList);
        rcvGallery.setAdapter(galleryRcvAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode < 100 && resultCode == Activity.RESULT_OK && data != null) {
            photoList = new ArrayList<>();
            PhotoModel photoModel;
            if (data.getClipData() != null) {
                int itemCount = data.getClipData().getItemCount();
                for (int i=0; i<itemCount; i++) {
                    photoModel = new PhotoModel();
                    photoModel.setUri(data.getClipData().getItemAt(i).getUri());
                    photoModel.setName(UUID.randomUUID().toString());
                    photoList.add(photoModel);
                }
            } else if(data.getData() != null) {
                photoModel = new PhotoModel();
                photoModel.setUri(data.getData());
                photoList.add(photoModel);
            }
            if (!photoList.isEmpty()) {
                albumList.get(requestCode).addPhotoList(photoList);
                galleryRcvAdapter.setData(albumList);
                uploadGallery();
            }
        }
    }

    private void uploadGallery() {
        StorageReference albumRef, imageRef;
        for (AlbumModel album : albumList) {
            albumRef = storageRef.child(album.getNameAlbum());
            for (PhotoModel photoModel : album.getPhotoList()) {
                imageRef = albumRef.child(photoModel.getName());
                imageRef.putFile(photoModel.getUri())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("Upload image", "Successful");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Upload image", "Failed");
                            }
                        });
            }
        }
    }

    public void deleteImageFromDB(String imageRefTail) {
        StorageReference imageRef = storageRef.child(imageRefTail);

        // Delete the file
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("Delete image", "Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.e("Delete image", "Failed at: " + imageRef.toString());

            }
        });
    }

    private List<AlbumModel> downloadGallery() {
        albumList = new ArrayList<>();

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        int albumIndex = 0;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under storageRef.
                            // You may call listAll() recursively on them.
                            List<PhotoModel> photoList = new ArrayList<>();
                            prefix.listAll()
                                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                        @Override
                                        public void onSuccess(ListResult listResult) {
                                            int photoIndex = 0;
                                            for (StorageReference item : listResult.getItems()) {
                                                PhotoModel photoModel = new PhotoModel();
                                                // All the items under storageRef.
                                                int finalPhotoIndex = photoIndex;
                                                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        // Got the download URL
                                                        photoModel.setUri(uri);
                                                        photoModel.setName(item.getName());
                                                        photoList.add(photoModel);
                                                        galleryRcvAdapter.setData(albumList);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Handle any errors
                                                        Log.e("Download Uri", "Failed");
                                                    }
                                                });
                                                photoIndex++;
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("List Album", "Failed");
                                        }
                                    });
                            albumList.add(new AlbumModel(prefix.getName(), photoList));
                            albumIndex++;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.e("List Gallery", "Failed");
                    }
                });

        return albumList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        // Create an instance of the dialog fragment and show it
        AddAlbumDialogFragment dialog = new AddAlbumDialogFragment();
        dialog.setTargetFragment(this, 6);
        dialog.show(getParentFragmentManager(), dialog.getTag());
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(AddAlbumDialogFragment dialog, String albumName) {
        // User touched the dialog's positive button
        if (!albumName.isEmpty()) {
            albumList.add(new AlbumModel(albumName, new ArrayList<>()));
            galleryRcvAdapter.setData(albumList);
        } else {
            Toast.makeText(requireContext(), "Album's name is empty", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(AddAlbumDialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }
}
