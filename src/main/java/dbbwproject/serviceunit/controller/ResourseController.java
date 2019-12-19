package dbbwproject.serviceunit.controller;

import com.google.api.core.SettableApiFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class ResourseController {
    final ModelMapper modelMapper;
    final DatabaseReference dbRef;

    public ResourseController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        this.modelMapper = modelMapper;
        dbRef = FirebaseDatabase.getInstance(firebaseApp).getReference("resources");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("db error" + error);
            }
        });
    }
}
