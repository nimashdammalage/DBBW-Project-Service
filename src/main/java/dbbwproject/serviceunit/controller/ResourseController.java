package dbbwproject.serviceunit.controller;

import com.google.api.core.SettableApiFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class ResourseController {
    final ModelMapper modelMapper;
    final DatabaseReference dbRef;

    public ResourseController(ModelMapper modelMapper, FirebaseApp firebaseApp,String resourceContainer) {
        this.modelMapper = modelMapper;
        DatabaseReference ref = FirebaseDatabase.getInstance(firebaseApp).getReference("resources");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
        dbRef = ref.child(resourceContainer);
    }

    static <T, V> ResponseWrapper<V> retrieveDataAvailability(Class<V> dtoClass, Class<T> deoClass, DatabaseReference dbReferenceWithQuery, String errMsg) {
        SettableApiFuture<T> future = SettableApiFuture.create();
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
            if (future.get() != null) {
                //entity already exists
                return new ResponseWrapper<>(ErrStatus.DATA_AVAILABLE, null, errMsg);
            } else {
                return new ResponseWrapper<>(ErrStatus.DATA_UNAVAILABLE, null, errMsg);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseWrapper<>(ErrStatus.ERROR, null, e.getMessage());
        }
    }

    static <T> ResponseWrapper<T> retrieveData(Class<T> deoClass, DatabaseReference dbReferenceWithQuery) {
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
            if (entity != null) {
                return new ResponseWrapper<>(ErrStatus.SUCCESS, entity);
            } else {
                return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseWrapper<>(ErrStatus.ERROR, null, e.getMessage());
        }
    }

    static <T> ResponseWrapperList<T> retrieveDataList(Class<T> deoClass, DatabaseReference dbReferenceWithQuery) {
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
            return new ResponseWrapperList<>(ErrStatus.SUCCESS, tList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, e.getMessage());
        }
    }

    static <T> ResponseWrapperList<T> retrieveDataList(Class<T> deoClass, Query dbReferenceWithQuery) {
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
            return new ResponseWrapperList<>(ErrStatus.SUCCESS, tList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, e.getMessage());
        }
    }

    static <T, V> ResponseWrapper<T> insertDataToDB(Class<T> dtoClass, V deoObject, DatabaseReference dbr) {
        SettableApiFuture<DatabaseError> future = SettableApiFuture.create();
        dbr.setValue(deoObject, (databaseError, databaseReference) -> future.set(databaseError));
        DatabaseError dbError = null;
        try {
            dbError = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (dbError != null) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "DB Error: " + dbError.getMessage() + " Details: " + dbError.getDetails());
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

    static <T> ResponseWrapper<T> deleteDataFromDB(Class<T> dtoClass, DatabaseReference dbr) {
        SettableApiFuture<DatabaseError> future = SettableApiFuture.create();
        dbr.removeValue((databaseError, databaseReference) -> future.set(databaseError));
        DatabaseError dbError = null;
        try {
            dbError = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (dbError != null) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "DB Error: " + dbError.getMessage() + " Details: " + dbError.getDetails());
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }
}
