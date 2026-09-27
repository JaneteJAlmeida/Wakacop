package academy.wakanda.wakacop.sessaovotacao.application.api;

import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;
import academy.wakanda.wakacop.sessaovotacao.domain.StatusSessaoVotacao;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class ResultadoSessaoResponse {
    private UUID idSessao;
    private UUID idPauta;
    private StatusSessaoVotacao status;
    private String momentoAbertura;
    private String momentoEncerramento;
    private Long totalVotos;
    private Long totalSim;
    private Long totalNao;

    public ResultadoSessaoResponse(SessaoVotacao sessao) {
        this.idSessao = sessao.getId();
        this.idPauta = sessao.getIdPauta();
        this.status = sessao.getStatus();


        this.momentoAbertura = sessao.getMomentoAbertura() != null ? sessao.getMomentoAbertura().toString() : null;
        this.momentoEncerramento = sessao.getMomentoEncerramento() != null ? sessao.getMomentoEncerramento().toString() : null;

        this.totalVotos = sessao.getTotalVotos();
        this.totalSim = sessao.getTotalSim();
        this.totalNao = sessao.getTotalNao();
    }
}
