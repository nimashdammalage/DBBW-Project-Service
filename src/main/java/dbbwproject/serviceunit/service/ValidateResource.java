package dbbwproject.serviceunit.service;

import com.google.api.core.SettableApiFuture;
import com.google.firebase.database.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.ExecutionException;

class ValidateResource {
    static void validateArgument(boolean isTrue, String err) {
        if (isTrue) throw new IllegalArgumentException(err);
    }

    static <T> T validateDataAvaiAndReturn(boolean enableChk, Class<T> deoClass, boolean dataRequired, Query query, String err) {

        if (enableChk) {
            return validateDataAvaiAndReturn(deoClass, dataRequired, query, err);
        }
        return null;
    }

    static <T> T validateDataAvaiAndReturn(boolean enableChk, Class<T> deoClass, boolean dataRequired, DatabaseReference dbRef, String err) {

        if (enableChk) {
            return validateDataAvaiAndReturn(deoClass, dataRequired, dbRef, err);
        }
        return null;
    }

    static <T> T validateDataAvaiAndReturn(Class<T> deoClass, boolean dataRequired, DatabaseReference dbReferenceWithQuery, String err) {
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

        return validateData(dataRequired, err, future);
    }

    static <T> T validateDataAvaiAndReturn(Class<T> deoClass, boolean dataRequired, Query query, String err) {
        SettableApiFuture<T> future = SettableApiFuture.create();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(deoClass);
                future.set(value);
            }

            @Override
            public void onCancelled(DatabaseError dbe) {
                future.setException(new IllegalStateException(dbe.getMessage()));
            }
        });

        return validateData(dataRequired, err, future);
    }

    private static <T> T validateData(boolean dataRequired, String err, SettableApiFuture<T> future) {
        try {
            T data = future.get();
            if (dataRequired) {
                if (data == null) throw new ResourceAccessException(err);
                return data;
            } else {
                if (data != null) throw new ResourceAccessException(err);
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException("error in validateDataAvaiAndReturn", e);
        }
    }
}
