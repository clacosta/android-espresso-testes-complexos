package br.com.alura.leilao.ui.activity;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.alura.leilao.BuildConfig;
import br.com.alura.leilao.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

//@LargeTest
//@RunWith(AndroidJUnit4.class)
public class ListaUsuariosTelaTest {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> mainActivityTestRule = new ActivityTestRule<>(ListaLeilaoActivity.class);

    @Before
    public void setup() {
        clearDatabaseTest();
    }

    @Test
    public void deve_AparecerUmUsuarioNaListaDeUsuarios_QuandoCadastrarUmUsuario() {
        onView(
                allOf(withId(R.id.lista_leilao_menu_usuarios),
                        withContentDescription("Usuários"),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.lista_usuario_fab_adiciona),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.form_usuario_nome),
                                0),
                        0),
                        isDisplayed()))
                .perform(replaceText("Alex"), closeSoftKeyboard());

        onView(
                allOf(withId(android.R.id.button1), withText("Adicionar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)))
                .perform(scrollTo(), click());

        onView(
                allOf(withId(R.id.item_usuario_id_com_nome), withText("(1) Alex"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_usuario_recyclerview),
                                        0),
                                0),
                        isDisplayed()))
                .check(matches(withText("(1) Alex")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void clearDatabaseTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.deleteDatabase(BuildConfig.DATABASE);
    }

    @After
    public void tearDown() {
        clearDatabaseTest();
    }
}
