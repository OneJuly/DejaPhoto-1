package team4.cse110.dejaphoto.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.settings.PrefUtils;

/**
 * Created by Sean on 6/9/2017.
 */

public class User {

    private List<String> friends;
    private List<Photo> photos;

    public User() {
        this.friends = new ArrayList<>();
        this.photos =  new ArrayList<>();
    }

    /**
     * Does this User have photo sharing enabled?
     *
     * @return
     */
    public boolean isSharingPhotos(Context context) {
        return PrefUtils.shareOwnPhotos(context);
    }

    /**
     *
     * @return
     */
    public List<String> getFriends() {
        return friends;
    }

    /**
     *
     * @return
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     *
     * @param friendId
     */
    public void addFriend(String friendId) {
        this.friends.add(friendId);
    }

    /**
     *
     * @param photo
     */
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }
}
