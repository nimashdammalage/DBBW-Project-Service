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



    static <T> ResponseEntity<T> retrieveData(Class<T> deoClass, DatabaseReference dbReferenceWithQuery) {
        final SettableApiFuture<T> future = SettableApiFuture.create();
        dbReferenceWithQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(deoClass);
                future.set(value);
            }

            @Override
            public void onCancelled(DatabaseError dbe) {
                future.setException(new ResourceAccessException(dbe.getMessage()));
            }
        });

        try {
            T entity = future.get();
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static <T> ResponseEntity<List<T>> retrieveDataList(Class<T> deoClass, DatabaseReference dbReferenceWithQuery) {
        final SettableApiFuture<List<T>> future = SettableApiFuture.create();
        ArrayList<T> resultList = new ArrayList<>();
        dbReferenceWithQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    T value = child.getValue(deoClass);
                    resultList.add(value);
                }
                future.set(resultList);
            }

            @Override
            public void onCancelled(DatabaseError dbe) {
                future.setException(new ResourceAccessException(dbe.getMessage()));
            }
        });

        try {
            List<T> tList = future.get();
            return new ResponseEntity<>(tList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static <T> ResponseEntity<List<T>> retrieveDataList(Class<T> deoClass, Query dbReferenceWithQuery) {
        final SettableApiFuture<List<T>> future = SettableApiFuture.create();
        ArrayList<T> resultList = new ArrayList<>();
        dbReferenceWithQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    T value = child.getValue(deoClass);
                    resultList.add(value);
                }
                future.set(resultList);
            }

            @Override
            public void onCancelled(DatabaseError dbe) {
                future.setException(new ResourceAccessException(dbe.getMessage()));
            }
        });

        try {
            List<T> tList = future.get();
            return new ResponseEntity<>(tList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static <V> ResponseEntity insertDataToDB(Object deoObject, DatabaseReference dbr) {
        SettableApiFuture<DatabaseError> future = SettableApiFuture.create();
        dbr.setValue(deoObject, (databaseError, databaseReference) -> future.set(databaseError));
        DatabaseError dbError = null;
        try {
            dbError = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dbError != null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    static ResponseEntity deleteDataFromDB(DatabaseReference dbr) {
        SettableApiFuture<DatabaseError> future = SettableApiFuture.create();
        dbr.removeValue((databaseError, databaseReference) -> future.set(databaseError));
        DatabaseError dbError = null;
        try {
            dbError = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dbError != null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
