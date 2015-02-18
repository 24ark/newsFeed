package com.example.arkitvora.newsfeed;

/**
 * Created by kavyajain on 16/02/15.
 */
public class ProfileBasics {

    String firstName;
    String lastName;
    String email;
    int image;
    boolean friendFlag;

    public ProfileBasics(String firstName,String lastName, String email, int image, boolean friendFlag) {
        this.firstName = firstName;
        this.lastName=lastName;
        this.email = email;
        this.image = image;
        this.friendFlag = friendFlag;
    }

    //getter methods

    public String getFirstName() {
        return firstName;
    }
    public String getLastName(){return lastName;}
    public String getEmail() {
        return email;
    }
    public boolean getFriendFlag() { return friendFlag;}
    public int getImage() {
        return image;
    }
    //setter methods
    public void changeFriendFlag(){
        if(this.friendFlag){this.friendFlag=Boolean.FALSE;}else if(this.friendFlag=Boolean.FALSE)this.friendFlag=Boolean.TRUE;
    }
}
