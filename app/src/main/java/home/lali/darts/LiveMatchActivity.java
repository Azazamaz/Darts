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

import home.lali.darts.adapter.LiveMatchAdapter;
import home.lali.darts.adapter.RecyclerItemDecorator;
import home.lali.darts.model.OnlineMatches;

public class LiveMatchActivity extends AppCompatActivity {

    private RecyclerView scoreMainList;

    private DatabaseReference databaseRef;
    private ChildEventListener childEventListener;

    private List<OnlineMatches> matchList;
    private List<String> keyList;
    private LiveMatchAdapter liveMatchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_match);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("online_matches");

        matchList = new ArrayList<>();
        keyList = new ArrayList<>();
        liveMatchAdapter = new LiveMatchAdapter(matchList);

        attachDatabaseReadListener();

        scoreMainList = findViewById(R.id.liveMainList);
        scoreMainList.setHasFixedSize(true);
		scoreMainList.addItemDecoration(new RecyclerItemDecorator(this));
        scoreMainList.setLayoutManager(new LinearLayoutManager(this));
        scoreMainList.setAdapter(liveMatchAdapter);
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

                    liveMatchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    OnlineMatches onlineMatch = dataSnapshot.getValue(OnlineMatches.class);

                    int index = keyList.indexOf(key);
                    matchList.set(index, onlineMatch);

                    liveMatchAdapter.notifyDataSetChanged();
                }

                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();

                    int index = keyList.indexOf(key);
                    matchList.remove(index);
                    keyList.remove(index);

                    liveMatchAdapter.notifyDataSetChanged();
                }
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };

            databaseRef.orderByChild("live").equalTo(true).addChildEventListener(childEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
            databaseRef.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
}
