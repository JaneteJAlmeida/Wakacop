package academy.wakanda.wakacop.sessaovotacao.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessaoVotacaoTest {

    @Test
    void deveFecharSessaoQuandoChamarMetodoObtemResultado(){
        SessaoVotacao sessao = buildSessaoNoPassado();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.obetmResultado(publicador);

        assertEquals(StatusSessaoVotacao.FECHADA, sessao.getStatus());
    }

    @Test
    void deveFecharSessaoQuandoStatusAbertaEMomentoEncerramentoEstiverNoPassado(){
        SessaoVotacao sessao = buildSessaoNoPassado();
        PublicadorResultadoSessao publicador = new PublicadorResultadoSessaoMockTest();

        sessao.atualizaStatus(publicador);

        assertEquals(StatusSessaoVotacao.FECHADA, sessao.getStatus());
    }

    @Test
    void naoDeveFecharSessaoQuandoStatusAbertaMasMomentoEncerramentoEstiverNoFuturo(){
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

    @Test
    void naoDeveAlterarStatusQuandoSessaoJaEstiverFechada(){
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

    private SessaoVotacao buildSessaoNoPassado() {
        return SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .idPauta(UUID.randomUUID())
                .status(StatusSessaoVotacao.ABERTA)
                .momentoAbertura(LocalDateTime.of(2023, 1, 1, 1, 1))
                .momentoEncerramento(LocalDateTime.of(2023, 1, 1, 1, 2))
                .votos(getVotos())
                .build();
    }

    private Map<String, VotoPauta> getVotos() {
        return Map.of(
                "04389272156", VotoPauta.builder().cpfAssociado("04389272156").opcaoVoto(OpcaoVoto.SIM).build(),
                "04389272155", VotoPauta.builder().cpfAssociado("04389272155").opcaoVoto(OpcaoVoto.NAO).build()
        );
    }
}
