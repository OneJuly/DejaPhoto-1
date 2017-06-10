package team4.cse110.dejaphoto;

import android.content.Context;
import android.test.mock.MockContext;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import team4.cse110.dejaphoto.database.FirebasePhotoDatabase;
import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.utilities.DejaAlgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the DejaAlgorithm class' methods.
 */
public class TestAlgorithm {
    private DejaAlgorithm dejaAlg;

    private FirebaseAuth auth;
    private FirebasePhotoDatabase db;

    Photo photo1;
    private List<Photo> album;
    private List<Photo> cache;
    private int cachePos; // last returned by prev(), next() or release(). -1 if cache is empty.

    Context context;

    /**
     * Standard test fixture.
     */
    @Before
    public void setup() {
        context = new MockContext();

        photo1 = new Photo(context, "2_Mt_Everest.jpg", true);

        dejaAlg = new DejaAlgorithm(context);
        dejaAlg.addToCache(photo1);
        //auth = FirebaseAuth.getInstance();
        //auth.createUserWithEmailAndPassword("boc024@ucsd.edu", "$DL@2020");

        //photo1 = new Photo(context, "2_Mt_Everest.jpg");
        //album = db.getPhotos();
        //cachePos = 0;
    }

    /**
     * This method tests the incKarma() and hasKarma() methods.
     */
    @Test
    public void testKarma() {
        dejaAlg.incKarma();
        assertTrue(dejaAlg.hasKarma());
    }

    @Test
    public void testPath() {
        assertEquals(photo1.getPath(), "2_Mt_Everest.jpg");
    }

    /**
     * This method tests the release() method.
     */
    @Test
    public void testRelease() {
        assertTrue(dejaAlg.getCurrentPhoto() != null);
        dejaAlg.release();
        assertEquals(dejaAlg.getCurrentPhoto(), null);
    }

    /**
     * This method tests the addToCache() method.
     */
    @Test
    public void testCache() {

    }

    /**
     * This method tests the next() method
     */
    @Test
    public void testNext() {

    }
    /**
     * This method tests the prev() method.
     */
    @Test
    public void testPrev() {

    }
}
