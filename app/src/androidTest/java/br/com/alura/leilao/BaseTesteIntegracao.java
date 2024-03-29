package br.com.alura.leilao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

import static org.junit.Assert.fail;

public abstract class BaseTesteIntegracao {
    private static final String ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API = "Banco de dados não foi limpo";
    private static final String LEILAO_NAO_FOI_SALVO = "Leilão não foi salvo: ";
    private final TesteWebClient webClient = new TesteWebClient();

    protected void limpaBancoDeDadosDaApi() throws IOException {
        final Boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            fail(ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API);
        }
    }

    protected void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes
        ) {
            final Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                fail(LEILAO_NAO_FOI_SALVO + leilao.getDescricao());
            }
        }
    }

    protected void limpaBancoDeDadosInterno() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.deleteDatabase(BuildConfig.DATABASE);
    }
}
