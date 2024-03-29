package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;
import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

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
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed()))
                .perform(click());

//        Verifica se aparece dialog de aviso por não ter usuáro com título e mensagem esperada
        onView(allOf(withText("Usuários não encontrados"),
                withId(R.id.alertTitle))).check(matches(isDisplayed()));

        onView(allOf(
                withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."),
                withId(android.R.id.message)))
                .check(matches(isDisplayed()));

//        Clica no botão "Cadastrar usuário"
        onView(allOf(withText("Cadastrar usuário"), isDisplayed()))
                .perform(click());

//        Clica no fab tela de lista de usuários
        cadastrarUsuario("Alex");

//        onView(
//                allOf(withId(R.id.item_usuario_id_com_nome),
//                        isDisplayed()))
//                .check(matches(withText("(1) Alex")));

//        Clica no back do Android
        pressBack();

//        Clica no fab lances do leilão
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed()))
                .perform(click());

//        Verifica visibilidade do dialog com o título esperado
        onView(allOf(withText("Novo lance"), withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));

//        Clica no edittext de valor e preenche
        onView(allOf(withId(R.id.form_lance_valor_edittext), isDisplayed()))
                .perform(click(),
                        typeText("200"),
                        closeSoftKeyboard());

//        Seleciona o usuário
        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed()))
                .perform(click());
        onData(is(new Usuario(1, "Alex")))
                .inRoot(isPlatformPopup())
                .perform(click());

//        Clica no botão "Propor"
        onView(allOf(withText("Propor"), isDisplayed()))
                .perform(click());

//        Fazer assertion para as views de maior e menor lance, e também, para os maiores lances
        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(allOf(withId(R.id.lances_leilao_maior_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(200.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_menor_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(200.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_maiores_lances), isDisplayed()))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(200.0) + " - (1) Alex\n"), isDisplayed())));
    }

    @Test
    public void deve_AtualizarLancesNoLeilao_QuandoReceberTresLances() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));
        tentaSalvarUsuariosNoBancoDeDados(new Usuario("Alex"), new Usuario("Fran"));

        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(actionOnItemAtPosition(0, click()));

//        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed()))
//                .perform(click());
//        onView(allOf(withText("Usuários não encontrados"),
//                withId(R.id.alertTitle))).check(matches(isDisplayed()));
//        onView(allOf(
//                withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."),
//                withId(android.R.id.message)))
//                .check(matches(isDisplayed()));
//
//        onView(allOf(withText("Cadastrar usuário"), isDisplayed()))
//                .perform(click());
//
//        cadastrarUsuario("Alex");
//        cadastrarUsuario("Fran");
//
//        pressBack();

        propoemLance("200", new Usuario(1, "Alex"));
        propoemLance("300", new Usuario(2, "Fran"));
        propoemLance("400", new Usuario(1, "Alex"));

        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(allOf(withId(R.id.lances_leilao_maior_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(400.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_menor_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(200.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_maiores_lances), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(400.0) + " - (1) Alex\n" +
                                formatadorDeMoeda.formata(300.0) + " - (2) Fran\n" +
                                formatadorDeMoeda.formata(200.0) + " - (1) Alex\n"), isDisplayed())));
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLanceMuitoAlto() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));
        tentaSalvarUsuariosNoBancoDeDados(new Usuario("Alex"));

        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(actionOnItemAtPosition(0, click()));

        propoemLance("2000000000", new Usuario(1, "Alex"));

        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(allOf(withId(R.id.lances_leilao_maior_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(2000000000.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_menor_lance), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(2000000000.0)),
                        isDisplayed())));

        onView(allOf(withId(R.id.lances_leilao_maiores_lances), isDisplayed()))
                .check(matches(allOf(
                        withText(formatadorDeMoeda.formata(2000000000.0) + " - (1) Alex\n"), isDisplayed())));
    }

    private void propoemLance(String valorLance, Usuario usuarioLance) {
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed()))
                .perform(click());

        onView(allOf(withText("Novo lance"), withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.form_lance_valor_edittext), isDisplayed()))
                .perform(click(),
                        typeText(valorLance),
                        closeSoftKeyboard());

        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed()))
                .perform(click());
        onData(is(usuarioLance))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(allOf(withText("Propor"), isDisplayed()))
                .perform(click());
    }

    private void tentaSalvarUsuariosNoBancoDeDados(Usuario... usuarios) {
        for (Usuario usuario : usuarios
        ) {
            final Usuario usuarioSalvo = new UsuarioDAO(InstrumentationRegistry.getTargetContext())
                    .salva(usuario);
            if (usuarioSalvo == null) {
                fail("Usuário " + usuario.getNome() + " não foi salvo.");
            }
        }
    }

    private void cadastrarUsuario(String nomeUsuario) {
        onView(
                allOf(withId(R.id.lista_usuario_fab_adiciona),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.form_usuario_nome_edittext),
                        isDisplayed()))
                .perform(click(), typeText(nomeUsuario), closeSoftKeyboard());

        onView(
                allOf(withId(android.R.id.button1),
                        withText("Adicionar"),
                        isDisplayed()))
                .perform(scrollTo(), click());
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
