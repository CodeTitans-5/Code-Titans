package com.example.untpreownedstore;

import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import androidx.test.rule.ActivityTestRule;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class SignUpTest {
    @Rule
    public ActivityTestRule<SignUpActivity> rule  = new  ActivityTestRule<SignUpActivity>(SignUpActivity.class){
        @Override
        protected Intent getActivityIntent() {
            InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("firstName", "John");
            intent.putExtra("lastName", "Doe");
            return intent;
        }
    };
    @Test
    public void ensureIntentDataIsDisplayed() throws Exception {
        SignUpActivity activity = rule.getActivity();
        activity.setFirstName();
        View firstviewById = activity.findViewById(R.id.firstName);
        View lastviewById = activity.findViewById(R.id.lastName);
        assertThat(firstviewById, Matchers.notNullValue());
        assertThat(lastviewById, Matchers.notNullValue());
        assertThat(activity.firstName, Matchers.is("John"));
        assertThat(activity.lastName, Matchers.is("Doe"));
    }
    @Test
    public void ensureMaleGenderSelected() throws Exception {
        SignUpActivity activity = rule.getActivity();
        activity.maleClicked(null);
        assertThat(activity.gender, Matchers.is("male"));
    }
    @Test
    public void ensureFemaleGenderSelected() throws Exception {
        SignUpActivity activity = rule.getActivity();
        activity.femaleClicked(null);
        assertThat(activity.gender, Matchers.is("female"));
    }
    @Test
    public void ensureOtherGenderSelected() throws Exception {
        SignUpActivity activity = rule.getActivity();
        activity.otherClicked(null);
        assertThat(activity.gender, Matchers.is("other"));
    }
}
