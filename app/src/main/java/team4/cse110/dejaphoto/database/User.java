package team4.cse110.dejaphoto.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.settings.PrefUtils;

/**
 * This class manages the info (friends and photos) for individual users.
 */
public class User {

    private List<String> friends;
    private List<Photo> photos;

    /**
     * This constructor creates new empty lists of friends and photos.
     */
    public User() {
        this.friends = new ArrayList<>();
        this.photos =  new ArrayList<>();
    }

    /**
     * Does this User have photo sharing enabled?
     * @return true if the user if sharing their photos; false otherwise.
     */
    public boolean isSharingPhotos(Context context) {
        return PrefUtils.shareOwnPhotos(context);
    }

    /**
     * This method retrieves the list of the user's friends.
     * @return a list of the user's friends.
     */
    public List<String> getFriends() {
        return friends;
    }

    /**
     * This method retrieves the list of the user's photos.
     * @return a list of the user's photos.
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * This method adds a friend to the user's friends list.
     * @param friendId - the ID of the friend to be added
     */
    public void addFriend(String friendId) {
        this.friends.add(friendId);
    }

    /**
     * This method adds a photo to the user's photo album.
     * @param photo - the photo to be added to the album
     */
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }
}
