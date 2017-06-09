package team4.cse110.dejaphoto.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private DatabaseReference usersID;
    private DatabaseReference friendsID;
    private ListView friendsNameView;
    private List<String> friendsNames = new ArrayList<>();
    private List<String> usersNames = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friends;
    }

    //gives the list view tap.click functionality
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {




        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //links tap/tap functionality to each item in the list view
        friendsNameView.setOnItemClickListener(mMessageClickedHandler);




        //Banner for the available users interface
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        usersdb = FirebaseDatabase.getInstance().getReference();
        //usersdb.getKey();
        //usersdb = FirebaseDatabase.getInstance().getReference().child("A87fcgB4XOdlla7IiiN4pMC4FUy1").child("friendsList");
        friendsNameView = (ListView)findViewById(R.id.friendList);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersNames);
        friendsNameView.setAdapter(arrayAdapter);


        //creates an arrayList of friends represented as strings
        /*
        FirebaseDatabase.getInstance().getReference().child("Sam").child("friendsList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getValue()) {
                            String friend = dataSnapshot.getValue().toString();
                            friendsNames.add(friend);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
        friendsID = FirebaseDatabase.getInstance().getReference().child("Sam").child("friendsList");
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
                    String user = postSnapshot.getKey();

                    System.out.println(Integer.toString(friendsNames.size()));

                    //concatenates a flag onto users that are already friends
                    for(int i = 0; i < friendsNames.size(); ++i){

                        System.out.println("comparing " + friendsNames.get(i) + " and " + user);

                        if(friendsNames.get(i).equals(user)){
                            user = user + " (friend)";
                        }
                    }

                    usersNames.add(user);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /*
        usersdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String friend = postSnapshot.getKey();
                    friendsNames.add(friend);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //String value = dataSnapshot.getValue(String.class);
                //String key = dataSnapshot.getKey();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        */

/*
        friendsNameView = (ListView)findViewById(R.id.friendList);
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsNames);
        friendsNameView.setAdapter(arrayAdapter);

        //loop through users
        usersID = FirebaseDatabase.getInstance().getReference();
        usersID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for (DataSnapshot postSnapshot1: dataSnapshot1.getChildren()) {

                    //loop through user's friends
                    friendsID = FirebaseDatabase.getInstance().getReference();
                    friendsID.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            for (DataSnapshot postSnapshot2 : dataSnapshot2.getChildren()) {
                                String friend = postSnapshot2.getValue(String.class);
                                friendsNames.add(friend);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //...
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //...
            }
        });
*/

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
