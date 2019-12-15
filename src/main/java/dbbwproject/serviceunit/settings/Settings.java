package dbbwproject.serviceunit.settings;

import com.google.firebase.database.*;
import dbbwproject.serviceunit.dao.FSettings;
import org.springframework.web.client.ResourceAccessException;

public class Settings {
    private final DatabaseReference dbr;
    private FSettings data;
    private static Settings instance;

    private Settings() {
        this.dbr = FirebaseDatabase.getInstance().getReference("resources/settings");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("settings changed in database. loading");
                data = dataSnapshot.getValue(FSettings.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("data changed settings");
            }
        });

    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public FSettings getData() {
        if (data == null) throw new ResourceAccessException("Setting data has not populated.Please setup settings");
        return data;
    }
}
