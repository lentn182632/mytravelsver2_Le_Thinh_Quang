package com.example.mytravelsver2;

import java.util.List;

public class AlbumModel {
    private String nameAlbum;
    private List<PhotoModel> photoList;

    public AlbumModel() {
    }

    public AlbumModel(String nameAlbum, List<PhotoModel> photoList) {
        this.nameAlbum = nameAlbum;
        this.photoList = photoList;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public List<PhotoModel> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoModel> photoList) {
        this.photoList = photoList;
    }

    public void addPhotoList(List<PhotoModel> photoList) {
        this.photoList.addAll(photoList);
    }
}

