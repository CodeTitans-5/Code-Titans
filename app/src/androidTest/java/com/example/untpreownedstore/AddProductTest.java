package com.example.untpreownedstore;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class AddProductTest {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mCurrentUser;
    @Rule
    public ActivityTestRule<AddProductActivity> rule  = new  ActivityTestRule<AddProductActivity>(AddProductActivity.class){
        @Override
        protected Intent getActivityIntent() {
            InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("productName", "Car1");
            intent.putExtra("productDescription", "Test Car Desc");
            intent.putExtra("productPrice", "5000");
            return intent;
        }
    };
    @Test
    public void ensureIntentDataIsDisplayed() throws Exception {
        AddProductActivity activity = rule.getActivity();
        activity.setProductDetails();
        View pNameviewById = activity.findViewById(R.id.productName);
        View pDescviewById = activity.findViewById(R.id.productDescription);
        View pPriceviewById = activity.findViewById(R.id.productPrice);
        assertThat(pNameviewById, Matchers.notNullValue());
        assertThat(pDescviewById, Matchers.notNullValue());
        assertThat(pPriceviewById, Matchers.notNullValue());
        assertThat(activity.productName, Matchers.is("Car1"));
        assertThat(activity.productDescription, Matchers.is("Test Car Desc"));
        assertThat(activity.productPrice, Matchers.is("5000"));
    }
}
