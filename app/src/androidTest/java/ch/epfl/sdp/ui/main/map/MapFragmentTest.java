package ch.epfl.sdp.ui.main.map;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sdp.utils.TestUtils.selectNavigation;

@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {

    @Rule public GrantPermissionRule mPermissionFine = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule public GrantPermissionRule mPermissionCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        selectNavigation(R.id.nav_map);
    }

    @Test
    public void checkThatMapIsDisplayed() {
        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }

}
