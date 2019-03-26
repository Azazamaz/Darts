package home.lali.darts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import home.lali.darts.adapter.OnlineMatchAdapter;
import home.lali.darts.adapter.RecyclerItemDecorator;
import home.lali.darts.model.OnlineMatches;

public class GlobalStatActivity extends AppCompatActivity {

    private RecyclerView mainList;

    private DatabaseReference databaseRef;
    private ChildEventListener childEventListener;

    private List<OnlineMatches> matchList;
    private List<String> keyList;
    private OnlineMatchAdapter matchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_stat);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = firebaseDatabase.getReference().child("online_matches");

        matchList = new ArrayList<>();
        keyList = new ArrayList<>();
        matchAdapter = new OnlineMatchAdapter(matchList);

        attachDatabaseReadListener();

        mainList = findViewById(R.id.mainList);
        mainList.setHasFixedSize(true);
        mainList.addItemDecoration(new RecyclerItemDecorator(this));
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.setAdapter(matchAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    keyList.add(key);

                    OnlineMatches onlineMatch = dataSnapshot.getValue(OnlineMatches.class);
                    matchList.add(onlineMatch);

                    matchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    OnlineMatches onlineMatch = dataSnapshot.getValue(OnlineMatches.class);

                    int index = keyList.indexOf(key);
                    matchList.set(index, onlineMatch);

                    matchAdapter.notifyDataSetChanged();
                }

                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();

                    int index = keyList.indexOf(key);
                    matchList.remove(index);

                    matchAdapter.notifyDataSetChanged();
                }
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };

            databaseRef.orderByChild("live").equalTo(false).addChildEventListener(childEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
            databaseRef.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
}
