package team4.cse110.dejaphoto.friends;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team4.cse110.dejaphoto.BaseActivity;
import team4.cse110.dejaphoto.R;

public class FriendsActivity extends BaseActivity {
    /* private member variables */
    private DatabaseReference usersdb;
    private DatabaseReference friendsID;
    private ListView friendsNameView;
    private List<String> friendsNames = new ArrayList<>();
    private List<String> usersNames = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friends;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Banner for the available users interface
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //SETUP
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        usersdb = FirebaseDatabase.getInstance().getReference();
        friendsNameView = (ListView)findViewById(R.id.friendList);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersNames);
        friendsNameView.setAdapter(arrayAdapter);

        //gets the current user's Uid in user
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("user: " + user);


        //defines list view's button funcionality
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String name = (String) parent.getAdapter().getItem(position);
                if (name.indexOf("(friend)") >= 0) {
                    name = name.replace(" (friend)","");
                    usersNames.set(position, name);
                    friendsID.child(name).removeValue();
                    Log.v("Deleting friend: ", name);
                }
                else{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(user).child("friendsList");
                    mDatabase.child(name).setValue(name);
                    Log.v("Adding friend: ", name);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        };
        friendsNameView.setOnItemClickListener(mMessageClickedHandler);


        //maintains an arrayList of Strings representing friend Uid's
        friendsID = FirebaseDatabase.getInstance().getReference().child(user).child("friendsList");
        friendsID.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                friendsNames.add(value);
                arrayAdapter.notifyDataSetChanged();
                Log.v("Added to friendsNames: ", value);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                friendsNames.remove(value);
                arrayAdapter.notifyDataSetChanged();
                Log.v("Del from friendsNames: ", value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //creates the arrayList of users (represented by ID strings) to be printed
        usersdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersNames.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String appUser = postSnapshot.getKey();

                    //does not allow the adding of the current him/herself as a friend
                    //and skips over photos
                    if (appUser.equals(user) || appUser.equals("photos")){
                        continue;
                    }

                    //concatenates a flag onto users that are already friends
                    for(int i = 0; i < friendsNames.size(); ++i){
                        if(friendsNames.get(i).equals(appUser)){
                            appUser = appUser + " (friend)";
                            break;
                        }
                    }
                    //adds non-friends to the arrayList of all users
                    Log.v("Adding to usersNames: ", appUser);
                    usersNames.add(appUser);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
