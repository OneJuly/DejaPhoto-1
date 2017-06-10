package team4.cse110.dejaphoto.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
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
             //   System.out.println("position:" + Integer.toString(position));
             //   System.out.println("name:" + name);


                if (name.indexOf("(friend)") >= 0) {
                    name = name.replace(" (friend)","");
                    usersNames.set(position, name);
                    System.out.println("new entry: " + name);
                    friendsID.child(name).removeValue();

                }
                else{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(user).child("friendsList");
                    mDatabase.child(name).setValue(name);
                }

                arrayAdapter.notifyDataSetChanged();
            }
        };
        friendsNameView.setOnItemClickListener(mMessageClickedHandler);


        friendsID = FirebaseDatabase.getInstance().getReference().child(user).child("friendsList");
        friendsID.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                friendsNames.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                friendsNames.remove(value);
                arrayAdapter.notifyDataSetChanged();
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

                    if (appUser.equals(user)){
                        continue;
                    }

                    //concatenates a flag onto users that are already friends
                    for(int i = 0; i < friendsNames.size(); ++i){
                        if(friendsNames.get(i).equals(appUser)){
                            appUser = appUser + " (friend)";
                            break;
                        }
                    }
                    usersNames.add(appUser);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //adds functionality to he FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
