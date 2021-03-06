package ch.epfl.sdp;

import org.junit.Test;

import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

import static org.junit.Assert.assertEquals;

public class ChatMessageDatabaseBuilderTest {


    String text = "text";
    Date date = new Date();
    String uid = "uid";
    String name ="name";
    ChatMessage chatMessage = new ChatMessage(text, date, uid, name);

    @Test
    public void EventDatabaseObjectBuilder_CheckSymmetry() {

        Map<String, Object> data =
                DatabaseObjectBuilderRegistry.getBuilder(ChatMessage.class).serializeToMap(chatMessage);

        data.put("date", new Timestamp(date));  //firebase replace ServerTimestamp to date in the backend
        ChatMessage resultChatMessage = DatabaseObjectBuilderRegistry.getBuilder(ChatMessage.class).buildFromMap(data);

        assertEquals(chatMessage.getText(), resultChatMessage.getText());
        assertEquals(chatMessage.getDate(), resultChatMessage.getDate());
        assertEquals(chatMessage.getUid(), resultChatMessage.getUid());
        assertEquals(chatMessage.getName(), resultChatMessage.getName());
        assertEquals(new ChatMessageDatabaseBuilder().getLocation(null), null);
        assertEquals(new ChatMessageDatabaseBuilder().hasLocation(), false);
    }

}
