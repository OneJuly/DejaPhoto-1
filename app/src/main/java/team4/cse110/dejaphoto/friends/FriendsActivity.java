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


        //defines list view's button funcionality
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String name = (String) parent.getAdapter().getItem(position);
                System.out.println("position:" + Integer.toString(position));
                System.out.println("name:" + name);

                if (name.indexOf("(friend)") >= 0) {
                    //friendsNames.remove(position-1);
                    //name = name.replace(" (friend)","");
                    //usersNames.set(position, name);
                    name = name.replace(" (friend)","");
                    usersNames.set(position, name);
                    System.out.println("new entry: " + name);
                    friendsID.child(name).removeValue();

                }
                else{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Sam").child("friendsList");
                    mDatabase.child(name).setValue(name);
                }

                arrayAdapter.notifyDataSetChanged();
            }
        };
        friendsNameView.setOnItemClickListener(mMessageClickedHandler);


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
                            break;
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
//
//        friendsNameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                String entry = (String) parent.getAdapter().getItem(position);
//                System.out.println("entry is: " + entry);
//                //concatenates a flag onto users that are already friends
//                if (entry.indexOf("(friend)") >= 0){
//                    entry = entry.replace(" (friend)","");
//                    usersNames.set(position, entry);
//                    System.out.println("new entry: " + entry);
//                    friendsID.child(entry).removeValue();
//                }
//                else{
//                    // friendsID.child(entry).push();
//                    friendsID.push().setValue(entry);
//                }
//
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });


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
