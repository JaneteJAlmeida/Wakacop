package academy.wakanda.wakacop.sessaovotacao.domain;

import academy.wakanda.wakacop.associado.application.service.AssociadoService;
import academy.wakanda.wakacop.pauta.domain.Pauta;
import academy.wakanda.wakacop.sessaovotacao.api.VotoRequest;
import academy.wakanda.wakacop.sessaovotacao.application.api.SessaoAberturaRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class SessaoVotacaoTest {

    @Test
    void deveFecharSessaoQuandoChamarMetodoObtemResultado() {
        SessaoVotacao sessao = buildSessaoNoPassado();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.obetmResultado(publicador);

        assertEquals(StatusSessaoVotacao.FECHADA, sessao.getStatus());
    }
    private SessaoVotacao buildSessaoNoPassado() {
        Map<String, VotoPauta> mapaVotos = new HashMap<>();
        return SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(LocalDateTime.of(2023, 1, 1, 1, 1))
                .momentoEncerramento(LocalDateTime.of(2023, 1, 1, 1, 2))
                .votos(mapaVotos)
                .build();
    }

    @Test
    void deveFecharSessaoQuandoStatusAbertaEMomentoEncerramentoEstiverNoPassado() {
        SessaoVotacao sessao = buildSessaoNoPassado();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.atualizaStatus(publicador);

        assertEquals(StatusSessaoVotacao.FECHADA, sessao.getStatus());
    }

    @Test
    void naoDeveFecharSessaoQuandoStatusAbertaMasMomentoEncerramentoEstiverNoFuturo() {
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(LocalDateTime.now().minusMinutes(5))
                .momentoEncerramento(LocalDateTime.now().plusHours(2))
                .votos(getVotos())
                .build();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.atualizaStatus(publicador);

        assertEquals(StatusSessaoVotacao.ABERTA, sessao.getStatus());
    }

    private Map<String, VotoPauta> getVotos() {
        SessaoVotacao dummy = SessaoVotacao.builder().id(UUID.randomUUID()).build();
        return Map.of(
                "04389272156", new VotoPauta(dummy, new VotoRequest("04389272156", OpcaoVoto.SIM.name(), "1990-01-01")),
                "04389272155", new VotoPauta(dummy, new VotoRequest("04389272155", OpcaoVoto.NAO.name(), "1990-01-01"))
        );
    }


    @Test
    void naoDeveAlterarStatusQuandoSessaoJaEstiverFechada() {
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.FECHADA)
                .momentoAbertura(LocalDateTime.of(2023, 1, 1, 1, 1))
                .momentoEncerramento(LocalDateTime.of(2023, 1, 1, 1, 2))
                .votos(getVotos())
                .build();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.atualizaStatus(publicador);

        assertEquals(StatusSessaoVotacao.FECHADA, sessao.getStatus());
    }

    @Test
    void deveAdicionarVotoComSucessoQuandoAssociadoNaoVotou() {

        LocalDateTime agora = LocalDateTime.now();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(agora.minusMinutes(1))
                .momentoEncerramento(agora.plusMinutes(10))
                .votos(new HashMap<>())
                .build();

        VotoRequest votoRequest = new VotoRequest("11122233344", OpcaoVoto.SIM.name(), "1995-01-01");

        AssociadoService associadoServiceMock = Mockito.mock(AssociadoService.class);
        doNothing().when(associadoServiceMock).validaAssociadoAptoVoto("11122233344", "1995-01-01");

        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();


        sessao.recebeVoto(votoRequest, associadoServiceMock, publicador);


        assertNotNull(sessao.getVotos());
        assertEquals(1, sessao.getVotos().size());
        assertEquals(1L, sessao.getTotalVotos());
        assertEquals(1L, sessao.getTotalSim());
        assertEquals(0L, sessao.getTotalNao());
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoTentarVotarDuasVezes() {

        LocalDateTime agora = LocalDateTime.now();
        String cpfDuplicado = "04389272156";

        Map<String, VotoPauta> mapaVotos = new HashMap<>();
        SessaoVotacao sessaoDummy = SessaoVotacao.builder().id(UUID.randomUUID()).build();
        VotoRequest requestExistente = new VotoRequest(cpfDuplicado, OpcaoVoto.SIM.name(), "1995-01-01");
        mapaVotos.put(cpfDuplicado, new VotoPauta(sessaoDummy, requestExistente));

        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(agora.minusMinutes(1))
                .momentoEncerramento(agora.plusMinutes(10))
                .votos(mapaVotos)
                .build();

        VotoRequest votoRepetidoRequest = new VotoRequest(cpfDuplicado, OpcaoVoto.NAO.name(), "1995-01-01");
        AssociadoService associadoServiceMock = Mockito.mock(AssociadoService.class);
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();


        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            sessao.recebeVoto(votoRepetidoRequest, associadoServiceMock, publicador);
        });

        assertEquals("Associado já votou nessa Sessão!", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoAoVotarSeSessaoEstiverFechada() {

        SessaoVotacao sessao = buildSessaoNoPassado();
        VotoRequest votoRequest = new VotoRequest("11122233344", OpcaoVoto.SIM.name(), "1995-01-01");
        AssociadoService associadoServiceMock = Mockito.mock(AssociadoService.class);
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();


        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            sessao.recebeVoto(votoRequest, associadoServiceMock, publicador);
        });

        assertEquals("Sessão está fechada!", excecao.getMessage());
    }

    @Test
    void deveCriarSessaoVotacaoAtravesDoConstrutorDeNegocio() {

        SessaoAberturaRequest requestMock = Mockito.mock(SessaoAberturaRequest.class);
        Pauta pautaMock = Mockito.mock(Pauta.class);

        UUID idPautaRandom = UUID.randomUUID();
        when(pautaMock.getId()).thenReturn(idPautaRandom);
        when(requestMock.obterTempoDuracao()).thenReturn(Optional.of(15));

        SessaoVotacao novaSessao = new SessaoVotacao(requestMock, pautaMock);

        assertEquals(idPautaRandom, novaSessao.getIdPauta());
        assertEquals(15, novaSessao.getTempoDuracao());
        assertEquals(StatusSessaoVotacao.ABERTA, novaSessao.getStatus());
        assertNotNull(novaSessao.getMomentoAbertura());
        assertNotNull(novaSessao.getMomentoEncerramento());
        assertTrue(novaSessao.getVotos().isEmpty());

        novaSessao.recebeVoto((VotoPauta) null);
    }

    @Test
    void deveExercitarMetodosEstruturaisEGettersSessaoVotacao() {
        SessaoVotacao sessao1 = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .tempoDuracao(60)
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(LocalDateTime.now())
                .momentoEncerramento(LocalDateTime.now().plusMinutes(60))
                .votos(new HashMap<>())
                .build();

        SessaoVotacao sessao2 = SessaoVotacao.builder().id(UUID.randomUUID()).build();

        assertNotNull(sessao1.toString());
        assertNotNull(sessao1.getId());
        assertNotNull(sessao1.getIdPauta());
        assertNotNull(sessao1.getTempoDuracao());
        assertNotNull(sessao1.getStatus());
        assertNotNull(sessao1.getMomentoAbertura());
        assertNotNull(sessao1.getMomentoEncerramento());
        assertNotNull(sessao1.getVotos());
        assertNotEquals(sessao1, sessao2);
        assertNotEquals(sessao1.hashCode(), sessao2.hashCode());
    }

    @Test
    void deveExercitarMetodosEGettersVotoPauta() {
        SessaoVotacao sessaoIdMock = SessaoVotacao.builder().id(UUID.randomUUID()).build();
        VotoRequest request = new VotoRequest("11122233344", OpcaoVoto.SIM.name(), "1995-01-01");

        VotoPauta votoPauta = new VotoPauta(sessaoIdMock, request);

        assertNotNull(votoPauta.toString());
        assertEquals(sessaoIdMock.getId(), votoPauta.getIdSessao());
        assertNotNull(votoPauta.getSessaoVotacao());
        assertNotNull(votoPauta.getCpfAssociado());
        assertNotNull(votoPauta.getOpcaoVoto());
        assertNotNull(votoPauta.getMomentoVoto());
        assertNull(votoPauta.getId());
    }

    @Test
    void deveExercitarConstrutoresPrivadosDoHibernatePorReflection() throws Exception {

        Constructor<SessaoVotacao> construtorSessao = SessaoVotacao.class.getDeclaredConstructor();
        construtorSessao.setAccessible(true);
        SessaoVotacao sessaoPrivada = construtorSessao.newInstance();
        assertNotNull(sessaoPrivada);

        Constructor<VotoPauta> construtorVoto = VotoPauta.class.getDeclaredConstructor();
        construtorVoto.setAccessible(true);
        VotoPauta votoPrivado = construtorVoto.newInstance();
        assertNotNull(votoPrivado);
    }
}