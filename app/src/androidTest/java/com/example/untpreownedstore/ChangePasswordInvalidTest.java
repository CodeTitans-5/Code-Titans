package com.example.untpreownedstore;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import androidx.test.rule.ActivityTestRule;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
public class ChangePasswordInvalidTest {
    @Rule
    public ActivityTestRule<ChangePasswordActivity> rule2 = new ActivityTestRule<ChangePasswordActivity>(ChangePasswordActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("oldPassword", "UNT@123");
            intent.putExtra("newPassword", "UNT@123");
            intent.putExtra("verifyPassword", "UNT@123");
            return intent;
        }
    };

    @Test
    public void ensurePwdDataMatches() throws Exception {
        ChangePasswordActivity activity = rule2.getActivity();
        activity.setPwdDetails();
        View old = activity.findViewById(R.id.enter_password);
        View new1 = activity.findViewById(R.id.enter_new_password);
        View verify = activity.findViewById(R.id.confirm_new_password);
        assertThat(old, Matchers.notNullValue());
        assertThat(new1, Matchers.notNullValue());
        assertThat(verify, Matchers.notNullValue());
        assertTrue(activity.oldPassword.length() > 5 && activity.newPassword.length() > 5 && activity.verifyPassword.length() > 5);
        assertThat(activity.oldPassword, Matchers.not(activity.newPassword));
        assertThat(activity.oldPassword, Matchers.is("123456"));
        assertThat(activity.newPassword, Matchers.is("UNT@123"));
        assertThat(activity.verifyPassword, Matchers.is("UNT@123"));
    }
}
