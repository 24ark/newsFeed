package com.example.arkitvora.newsfeed;

/**
 * Created by arkitvora on 04/02/15.
 */
public class PersonData {


    String name;
    String email;
    int image;
    int id_;
    int likeCount;

    public PersonData(String name, String email, int image, int id_, int likeCount) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.id_ = id_;
        this.likeCount = likeCount;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public int getLikeCount() { return likeCount;}


    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}
