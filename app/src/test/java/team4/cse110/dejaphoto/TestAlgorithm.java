package team4.cse110.dejaphoto;

import android.content.Context;
import android.test.mock.MockContext;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.utilities.DejaAlgorithm;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the DejaAlgorithm class' methods.
 */
public class TestAlgorithm {
    private DejaAlgorithm dejaAlgorithm;

    private FirebaseAuth auth;

    Photo photo1;

    Context context;

    /**
     * Standard test fixture - TODO
     */
    @Before
    public void setup() {
        context = new MockContext();

        //dejaAlgorithm = new DejaAlgorithm(context);
        //auth = FirebaseAuth.getInstance();
        //auth.createUserWithEmailAndPassword("boc024@ucsd.edu", "$DL@2020");


        //photo1 = new Photo(context, "2_Mt_Everest.jpg");
        photo1 = new Photo(context, "2_Mt_Everest.jpg", true);
    }

    /**
     * This method tests the incKarma() and hasKarma() methods.
     */
    @Test
    public void testKarma() {
        photo1.setKarma(1);
        assertEquals(photo1.getKarma(), 1);
        photo1.setKarma(0);
        assertEquals(photo1.getKarma(), 0);
    }

    /**
     * This method tests the release() method.
     */
    @Test
    public void testRelease() {

    }

    /**
     * This method tests the addToCache() method.
     */
    @Test
    public void testCache() {

    }
}
