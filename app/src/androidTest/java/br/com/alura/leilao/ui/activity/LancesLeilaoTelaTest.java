package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class LancesLeilaoTelaTest extends BaseTesteIntegracao {
    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activityTestRule =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBancosDeDados();
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLance() throws IOException {
//        Salvar leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

//        Inicializar a main Activity
        activityTestRule.launchActivity(new Intent());

//        Clica no leilão
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(actionOnItemAtPosition(0, click()));

//        Clica no fab da tela de lances do leilão
        onView(withId(R.id.lances_leilao_fab_adiciona))
                .perform(click());

//        Verifica se aparece dialog de aviso por não ter usuáro com título e mensagem esperada
        onView(withText("Usuários não encontrados")).check(matches(isDisplayed()));

        onView(
                withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."))
                .check(matches(isDisplayed()));

//        Clica no botão "Cadastrar usuário"
        onView(withText("Cadastrar usuário"))
                .perform(click());

//        Clica no fab tela de lista de usuários
        onView(
                allOf(withId(R.id.lista_usuario_fab_adiciona),
                        isDisplayed()))
                .perform(click());

//        Clica no EditText e preenche com o nome do usuário
        onView(
                allOf(withId(R.id.form_usuario_nome_edittext),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.form_usuario_nome_edittext),
                        isDisplayed()))
                .perform(replaceText("Alex"), closeSoftKeyboard());

//        Clica em Adicionar
        onView(
                allOf(withId(android.R.id.button1),
                        withText("Adicionar"),
                        isDisplayed()))
                .perform(scrollTo(), click());

//        onView(
//                allOf(withId(R.id.item_usuario_id_com_nome),
//                        isDisplayed()))
//                .check(matches(withText("(1) Alex")));

//        Clica no back do Android
        pressBack();

//        Clica no fab lances do leilão
        onView(withId(R.id.lances_leilao_fab_adiciona))
                .perform(click());

//        Verifica visibilidade do dialog com o título esperado
        onView(withText("Novo lance")).check(matches(isDisplayed()));

//        Clica no edittext de valor e preenche
        onView(
                allOf(withId(R.id.form_lance_valor_edittext), isDisplayed()))
                .perform(click(),
                        replaceText("200"),
                        closeSoftKeyboard());

//        Seleciona o usuário
        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed()))
                .perform(click());

//        Clica no botão "Propor"

//        Fazer assertion para as views de maior e menor lance, e também, para os maiores lances
    }

    private void limpaBancosDeDados() throws IOException {
        limpaBancoDeDadosDaApi();
        limpaBancoDeDadosInterno();
    }

    @After
    public void tearDown() throws IOException {
        limpaBancosDeDados();
    }
}
