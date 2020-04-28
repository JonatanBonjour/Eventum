package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the chat
 */
public class ChatViewModel extends ViewModel {

    static class ChatViewModelFactory extends ParameterizedViewModelFactory {

        ChatViewModelFactory() {
            super(Database.class, String.class, Authenticator.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }

        void setAuthenticator(@NonNull Authenticator authenticator){
            setValue(2, verifyNotNull(authenticator));
        }
    }

    interface OnMessageAddedCallback {
        void onFailure(Exception exception);
    }

    private CollectionQuery mMessageCollection;
    private FilterQuery mOrderedMessagesQuery;
    private LiveData<List<ChatMessage>> mMessageLiveData;
    private UserInfo mUser;

    /**
     * Constructor of the chat view model
     *
     * @param database where the chat messages will be uploaded
     * @param eventRef the reference of an event
     * @param authenticator of the user
     */
    public ChatViewModel(@NonNull Database database, @NonNull String eventRef, Authenticator authenticator) {
        verifyNotNull(database, eventRef, authenticator);

        mMessageCollection = database.query("events").document(eventRef).collection("messages");
        mOrderedMessagesQuery = mMessageCollection.orderBy("date");
        mUser = authenticator.getCurrentUser();

    }

    /**
     * Method to add a new chat message for an event in the database
     *
     * @param message to be uploaded in the database
     * @param callback called when the upload is done
     */
    public void addMessage(@NonNull String message, @NonNull OnMessageAddedCallback callback) {

        ChatMessage chatMessage = new ChatMessage(message, new Date(), mUser.getUid(), mUser.getDisplayName());
        mMessageCollection.create(chatMessage, res -> {
            if (!res.isSuccessful()) {
                callback.onFailure(res.getException());
            }
        });
    }

    /**
     * Method to get the reference of the user
     *
     * @return the reference of the user
     */
    public String getUserRef() {
        return mUser.getUid();
    }

    /**
     * Method to get the chat messages
     *
     * @return the messages
     */
    public LiveData<List<ChatMessage>> getMessages() {
        if (mMessageLiveData == null) {
            mMessageLiveData = mOrderedMessagesQuery.livedata(ChatMessage.class);
        }
        return mMessageLiveData;
    }

}
