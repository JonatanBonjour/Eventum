package ch.epfl.sdp.ui.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

    private static final String DUMMY_UID = "testuid";
    private static final String DUMMY_NAME = "testname";
    private static final String DUMMY_EMAIL = "testmail";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

    @Mock
    private Authenticator<String> mAuthenticator;

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LoginAuthViewModel_Constructor_FailsIfFirstParameterIsNull() {
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<>(null, mDatabase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LoginAuthViewModel_Constructor_FailsIfSecondParameterIsNull() {
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<>(mAuthenticator, null);
    }
}
