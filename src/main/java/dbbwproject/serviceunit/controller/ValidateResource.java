package dbbwproject.serviceunit.controller;

import com.google.api.core.SettableApiFuture;
import com.google.firebase.database.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.ExecutionException;

public class ValidateResource {
    static void validateArgument(boolean isTrue, String err) {
        if (isTrue) throw new IllegalArgumentException(err);
    }

    static <T> void validateDataAvailability(boolean enableChk, Class<T> deoClass, boolean dataRequired, Query query, String err) {

        if (enableChk) {
            validateDataAvailability(deoClass, dataRequired, query, err);
        }
    }

    static <T> void validateDataAvailability(boolean enableChk, Class<T> deoClass, boolean dataRequired, DatabaseReference dbRef, String err) {

        if (enableChk) {
            validateDataAvailability(deoClass, dataRequired, dbRef, err);
        }
    }

    static <T> void validateDataAvailability(Class<T> deoClass, boolean dataRequired, DatabaseReference dbReferenceWithQuery, String err) {
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

        validateData(dataRequired, err, future);
    }

    static <T> void validateDataAvailability(Class<T> deoClass, boolean dataRequired, Query query, String err) {
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

        validateData(dataRequired, err, future);
    }

    private static <T> void validateData(boolean dataRequired, String err, SettableApiFuture<T> future) {
        try {
            T data = future.get();
            if (dataRequired && data == null) {
                throw new ResourceAccessException(err);
            }
            if (!dataRequired && data != null) {
                throw new ResourceAccessException(err);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException("error in validateDataAvailability", e);
        }
    }

    static <T> void validateDataComparison(Class<T> deoClass, boolean shouldBeEqual, Query dbQuery, T inputValue, String errorMsg) {
        SettableApiFuture<T> future = SettableApiFuture.create();
        dbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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

        validateComparison(shouldBeEqual, inputValue, errorMsg, future);
    }

    public static <T> void validateDataComparison(boolean enableChk, Class<T> deoClass, boolean shouldBeEqual, Query dbQuery, T inputValue, String errorMsg) {
        if (enableChk) validateDataComparison(deoClass, shouldBeEqual, dbQuery, inputValue, errorMsg);
    }

    public static <T> void validateDataComparison(boolean enableChk, Class<T> deoClass, boolean shouldBeEqual, DatabaseReference dbRef, T inputValue, String errorMsg) {
        if (enableChk) validateDataComparison(deoClass, shouldBeEqual, dbRef, inputValue, errorMsg);
    }

    static <T> void validateDataComparison(Class<T> deoClass, boolean shouldBeEqual, DatabaseReference dbReference, T inputValue, String errorMsg) {
        SettableApiFuture<T> future = SettableApiFuture.create();
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        validateComparison(shouldBeEqual, inputValue, errorMsg, future);
    }

    private static <T> void validateComparison(boolean shouldBeEqual, T inputValue, String errorMsg, SettableApiFuture<T> future) {
        try {
            T data = future.get();
            if (shouldBeEqual) {
                if (data == null && inputValue == null) {
                    return;
                } else if (!(data != null && inputValue != null)) {
                    throw new ResourceAccessException(errorMsg);
                } else {
                    if (!inputValue.equals(data)) throw new ResourceAccessException(errorMsg);
                }
            } else {
                if (data == null && inputValue == null) {
                    throw new ResourceAccessException(errorMsg);
                } else if (!(data != null && inputValue != null)) {
                    return;
                } else {
                    if (inputValue.equals(data)) throw new ResourceAccessException(errorMsg);
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException("error in validateDataComparison", e);
        }
    }
}
