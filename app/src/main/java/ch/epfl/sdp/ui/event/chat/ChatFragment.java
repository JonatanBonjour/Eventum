package ch.epfl.sdp.ui.event.chat;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentChatBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fragment for the chat
 */
public class ChatFragment extends Fragment {

    private final ChatViewModel.ChatViewModelFactory mFactory;
    private ChatViewModel mViewModel;
    private FragmentChatBinding mBinding;
    private MessageListAdapter mAdapter;

    /**
     * Constructor of the chat fragment
     */
    public ChatFragment() {
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
    }

    /**
     * Constructor of the chat fragment, only for testing purposes!
     *
     * @param database      The database service to use
     * @param eventRef      The database reference of the current event
     * @param authenticator The authentication service to use
     */
    @VisibleForTesting
    public ChatFragment(@NonNull Database database, @NonNull String eventRef, @NonNull Authenticator authenticator) {
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setAuthenticator(authenticator);
        mFactory.setEventRef(eventRef);
    }

    public static ChatFragment getInstance(@NonNull String eventRef) {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, verifyNotNull(eventRef));

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        mBinding.buttonChatboxSend.setOnClickListener(v -> {
            trySendMessage(exception -> Toast.makeText(getContext(), "Couldn't send message", Toast.LENGTH_SHORT).show());
        });


        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mFactory.setEventRef(verifyNotNull(args.getString(UIConstants.BUNDLE_EVENT_REF)));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(ChatViewModel.class);

        mAdapter = new MessageListAdapter(mViewModel.getUserRef(), getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(true);

        mBinding.reyclerviewMessageList.setLayoutManager(layoutManager);
        mBinding.reyclerviewMessageList.setAdapter(mAdapter);

        mViewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            List<Pair<ChatMessage, LiveData<User>>> chat = new ArrayList<>();
            for (DatabaseObject<ChatMessage> messageObject : messages) {
                chat.add(new Pair<>(messageObject.getObject(), mViewModel.getUser(messageObject.getObject().getUid())));
            }
            mAdapter.setChatList(chat);
            mBinding.reyclerviewMessageList.scrollToPosition(mAdapter.getItemCount() - 1);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void trySendMessage(@NonNull ChatViewModel.OnMessageAddedCallback callback) {
        String message = mBinding.edittextChatbox.getText().toString();
        if (!message.isEmpty()) {
            mBinding.edittextChatbox.getText().clear();

            //to database
            mViewModel.addMessage(message, callback);
        }
    }

}
