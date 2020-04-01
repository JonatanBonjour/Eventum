package ch.epfl.sdp.ui.createevent;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.mocks.MockEvents;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    private static SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private static final Event mMockEvent = MockEvents.getCurrentEvent();
    private static final String DATE = mFormatter.format(mMockEvent.getDate());
    private static final String TITLE = mMockEvent.getTitle();
    private static final String DESCRIPTION = mMockEvent.getDescription();
    private static final String EMPTY = "";
    private static final int DAY = mMockEvent.getDate().getDay();
    private static final int MONTH = mMockEvent.getDate().getMonth();
    private static final int YEAR = mMockEvent.getDate().getYear();

    @Rule
    public final ActivityTestRule<CreateEventActivity> mActivityRule =
            new ActivityTestRule<>(CreateEventActivity.class);

    @Test
    public void testCreateEventFragment() {
        // Now try with correct values
        onView(ViewMatchers.withId(R.id.title)).perform(
                clearText(),
                typeText(TITLE),
                closeSoftKeyboard());

        onView(withId(R.id.description)).perform(
                clearText(),
                typeText(DESCRIPTION),
                closeSoftKeyboard());

        onView(withId(R.id.date)).perform(
                PickerActions.setDate(YEAR, MONTH, DAY),
                closeSoftKeyboard());

        onView(withId(R.id.createButton))
                .perform(click());
    }

    @Test
    public void testCreateIncorrectEventFragment() {
        // Try with incorrect values
        onView(withHint(is("Title"))).perform(
                clearText(),
                typeText(EMPTY),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                scrollTo(),
                click());
    }
}
