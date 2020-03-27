package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.event.EventViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelFactoryTest {

    @Mock
    private Authenticator<String> mAuthenticator;

    @Mock
    private AuthViewModel<String> mAuthViewModel;

    @Mock
    private EventViewModel mString;

    @Mock
    private FirebaseAuthenticator mFirebaseAuthenticator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AuthViewModelFactory_ConstructorFailsIfParameterIsNull() {
        AuthViewModelFactory<String> factory = new AuthViewModelFactory<String>(null);
    }

    @Test
    public void AuthViewModelFactory_ConstructorSucceed() {
        AuthViewModelFactory<String> factory = new AuthViewModelFactory<String>(mAuthenticator);
        factory.create(mAuthViewModel.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void AuthViewModelFactory_CreateFailedNotSuperclassAuthViewModel() {
        AuthViewModelFactory<String> factory = new AuthViewModelFactory<String>(mAuthenticator);
        factory.create(mString.getClass());
    }

    class  bugClass extends  AuthViewModel<String>{

        public bugClass(@NonNull Authenticator<String> authenticator) {
            super(authenticator);

        }
    }
    @Test (expected = IllegalArgumentException.class)
    public void AuthViewModelFactory_getClassFailed_NoSuchMethodException() throws NoSuchMethodException {
        AuthViewModelFactory<String> factory = new AuthViewModelFactory<String>(mAuthenticator);
        bugClass mbugClass= new bugClass(mAuthenticator);
        factory.create(mbugClass.getClass());
    }


}
