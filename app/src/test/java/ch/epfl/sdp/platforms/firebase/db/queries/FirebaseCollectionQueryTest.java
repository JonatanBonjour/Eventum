package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.platforms.firebase.db.queries.FirebaseCollectionQuery;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseCollectionQueryTest {

    private final static String DUMMY_STRING = "test";
    private final static String[] DUMMY_STRINGS = {"test1", "test2", "test3"};
    private final static Object DUMMY_OBJECT = new Object();
    private final static Integer DUMMY_INT = 4;
    private final static Exception DUMMY_EXCEPTION = new Exception();
    private final static String DUMMY_ID = "slkdfjghsdflkjg354sadf45";
    private final static double DUMMY_DOUBLE = 42;

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private Task<QuerySnapshot> mQuerySnapshotTask;

    @Mock
    private Task<DocumentReference> mDocumentReferenceTask;

    @Mock
    private QuerySnapshot mQuerySnapshot;

    @Mock
    private DocumentSnapshot mDocumentSnapshot1;
    @Mock
    private DocumentSnapshot mDocumentSnapshot2;
    @Mock
    private DocumentSnapshot mDocumentSnapshot3;

    @Mock
    private Query mQuery;

    @Mock
    private GeoPoint mGeoPoint;

    @Captor
    private ArgumentCaptor<OnCompleteListener<QuerySnapshot>> mQuerySnapshotCompleteListenerCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener<DocumentReference>> mDocumentReferenceCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Constructor_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(null, mCollectionReference);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Constructor_FailsWithNullSecondArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Document_FailsWithNullParameter() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.document(null);
    }

    @Test
    public void FirebaseCollectionQuery_Document_ReturnsNewDocumentQueryWithCorrectRef() {
        when(mDocumentReference.collection(any(String.class))).thenReturn(mCollectionReference);
        when(mCollectionReference.document(any(String.class))).thenReturn(mDocumentReference);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        DocumentQuery documentQuery = firebaseCollectionQuery.document(DUMMY_STRING);
        documentQuery.collection(DUMMY_STRING);
        verify(mDocumentReference).collection(DUMMY_STRING);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_WhereFieldEqualTo_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.whereFieldEqualTo(null, DUMMY_OBJECT);
    }

    @Test
    public void FirebaseCollectionQuery_WhereFieldEqualTo_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mQuery);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FilterQuery filterQuery = firebaseCollectionQuery.whereFieldEqualTo(DUMMY_STRING, DUMMY_OBJECT);
        verify(mCollectionReference).whereEqualTo(DUMMY_STRING, DUMMY_OBJECT);
        filterQuery.limitCount(1);
        verify(mQuery).limit(1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_OrderBy_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.orderBy(null);
    }

    @Test
    public void FirebaseCollectionQuery_OrderBy_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.orderBy(any(String.class))).thenReturn(mQuery);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FilterQuery filterQuery = firebaseCollectionQuery.orderBy(DUMMY_STRING);
        verify(mCollectionReference).orderBy(DUMMY_STRING);
        filterQuery.limitCount(1);
        verify(mQuery).limit(1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_LimitCount_FailsWithValueEqualToZero() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.limitCount(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_LimitCount_FailsWithValueSmallerThenZero() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.limitCount(-1);
    }

    /* TODO need to be changed
    @Test
    public void FirebaseCollectionQuery_LimitCount_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.limit(anyLong())).thenReturn(mQuery);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FilterQuery filterQuery = firebaseCollectionQuery.limitCount(DUMMY_INT);
        verify(mCollectionReference).limit(DUMMY_INT);
        filterQuery.limitCount(1);
        verify(mQuery).limit(1);
    }*/

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Get_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(null, result -> {

        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Get_FailsWithNullSecondArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(String.class, null);
    }

    @Test
    public void FirebaseCollectionQuery_Get_CallsCallbackWithDeserializedListOfObjects() {
        when(mDocumentSnapshot1.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[0]));
        when(mDocumentSnapshot2.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[1]));
        when(mDocumentSnapshot3.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[2]));
        when(mQuerySnapshot.getDocuments()).thenReturn(new ArrayList<>(Arrays.asList(mDocumentSnapshot1, mDocumentSnapshot2, mDocumentSnapshot3)));
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(true);
        when(mQuerySnapshotTask.getResult()).thenReturn(mQuerySnapshot);
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mCollectionReference.get()).thenReturn(mQuerySnapshotTask);

        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(String.class, result -> {
            assertTrue(result.isSuccessful());
            for(int i = 0; i < result.getData().size(); i++) {
                assertEquals(DUMMY_STRINGS[i], result.getData().get(i));
            }
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test
    public void FirebaseCollectionQuery_Get_CallsCallbackWithEmptyListIfNoResults() {
        when(mQuerySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(true);
        when(mQuerySnapshotTask.getResult()).thenReturn(mQuerySnapshot);
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mCollectionReference.get()).thenReturn(mQuerySnapshotTask);

        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(String.class, result -> {
            assertTrue(result.isSuccessful());
            assertTrue(result.getData().isEmpty());
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test
    public void FirebaseCollectionQuery_Get_CallsCallbackWithExceptionIfAnErrorOccurs() {
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(false);
        when(mQuerySnapshotTask.getException()).thenReturn(DUMMY_EXCEPTION);
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mCollectionReference.get()).thenReturn(mQuerySnapshotTask);

        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(String.class, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Livedata_FailsWithNullArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.liveData(null);
    }

    @Test
    public void FirebaseCollectionQuery_Livedata_CreationDoesNotFail() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        LiveData<List<String>> stringsLiveData = firebaseCollectionQuery.liveData(String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Create_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(null, result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Create_FailsWithNullSecondArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(DUMMY_OBJECT, null);
    }

    @Test
    public void FirebaseCollectionQuery_Create_CallsCallbackWithNewDocumentId() {
        when(mDocumentReference.getId()).thenReturn(DUMMY_ID);
        when(mDocumentReferenceTask.isSuccessful()).thenReturn(true);
        when(mDocumentReferenceTask.getResult()).thenReturn(mDocumentReference);
        when(mDocumentReferenceTask.addOnCompleteListener(mDocumentReferenceCompleteListenerCaptor.capture())).thenReturn(null);
        when(mCollectionReference.add(any())).thenReturn(mDocumentReferenceTask);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(DUMMY_STRING, result -> {
            assertTrue(result.isSuccessful());
            assertEquals(DUMMY_ID, result.getData());
        });

        mDocumentReferenceCompleteListenerCaptor.getValue().onComplete(mDocumentReferenceTask);
    }

    @Test
    public void FirebaseCollectionQuery_Create_CallsCallbackWithExceptionIfAnErrorOccurred() {
        when(mDocumentReferenceTask.isSuccessful()).thenReturn(false);
        when(mDocumentReferenceTask.getException()).thenReturn(DUMMY_EXCEPTION);
        when(mDocumentReferenceTask.addOnCompleteListener(mDocumentReferenceCompleteListenerCaptor.capture())).thenReturn(null);
        when(mCollectionReference.add(any())).thenReturn(mDocumentReferenceTask);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(DUMMY_STRING, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mDocumentReferenceCompleteListenerCaptor.getValue().onComplete(mDocumentReferenceTask);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_AtLocation_failsWhenNullArgument(){
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.atLocation(null, DUMMY_DOUBLE);
    }

    @Test
    public void FirebaseCollectionQuery_AtLocation_ReturnsFirebaseGeoFirestoreQuery(){
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = (FirebaseGeoFirestoreQuery) firebaseCollectionQuery.atLocation(mGeoPoint, DUMMY_DOUBLE);
        assertNotEquals(firebaseGeoFirestoreQuery, null);
        assertEquals(firebaseGeoFirestoreQuery.getmLocation(), mGeoPoint);
        assertTrue(firebaseGeoFirestoreQuery.getmRadius() ==  DUMMY_DOUBLE);
    }

}
