package com.wildpeppers.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentId;

public class WallpapersModel {

    @Exclude
    public String id;
    public String title, desc, url;
    public String image;

    /*@Exclude
    public boolean isFavourite = false;*/

    public WallpapersModel(String id, String title, String desc, String url) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
