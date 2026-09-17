package academy.wakanda.wakacop.sessaovotacao.application.service;

import academy.wakanda.wakacop.associado.application.service.AssociadoService;
import academy.wakanda.wakacop.pauta.application.service.PautaService;
import academy.wakanda.wakacop.pauta.domain.Pauta;
import academy.wakanda.wakacop.sessaovotacao.api.VotoRequest;
import academy.wakanda.wakacop.sessaovotacao.application.api.ResultadoSessaoResponse;
import academy.wakanda.wakacop.sessaovotacao.application.api.SessaoAberturaRequest;
import academy.wakanda.wakacop.sessaovotacao.application.api.SessaoAberturaResponse;
import academy.wakanda.wakacop.sessaovotacao.application.api.VotoResponse;
import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;
import academy.wakanda.wakacop.sessaovotacao.domain.VotoPauta;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SessaoVotacaoApplicationService implements SessaoVotacaoService {
    private final SessaoVotacaoRespository sessaoVotacaoRespository;
    private final PautaService pautaService;
    private final AssociadoService associadoService;

    @Override
    public SessaoAberturaResponse abreSessao(SessaoAberturaRequest sessaoAberturaRequest) {
        log.info("[start] SessaoVotacaoapplicationService - abreSessao");
        Pauta pauta = pautaService.getPautaPorId(sessaoAberturaRequest.getIdPauta());
        SessaoVotacao sessaoVotacao = sessaoVotacaoRespository.salva(new SessaoVotacao(sessaoAberturaRequest, pauta));
        log.info("[finish] SessaoVotacaoapplicationService - abreSessao");
        return new SessaoAberturaResponse(sessaoVotacao);

    }

    @Override
    public VotoResponse recebeVoto(UUID idSessao, VotoRequest novoVoto) {
        log.info("[start] SessaoVotacaoapplicationService - recebeVoto");
        SessaoVotacao sessao = sessaoVotacaoRespository.buscaPorId(idSessao);
        VotoPauta voto = sessao.recebeVoto(novoVoto, associadoService);
        sessaoVotacaoRespository.salva(sessao);
        log.info("[finish] SessaoVotacaoapplicationService - recebeVoto");
        return new VotoResponse(voto);
    }

    @Override
    public ResultadoSessaoResponse obtemResultado(UUID idSessao) {
        log.info("[start] SessaoVotacaoapplicationService - obtemResultado");
        SessaoVotacao sessao = sessaoVotacaoRespository.buscaPorId(idSessao);
        ResultadoSessaoResponse resultado = sessao.obetmResultado();
        sessaoVotacaoRespository.salva(sessao);
        log.info("[finish] SessaoVotacaoapplicationService - obtemResultado");
        return resultado;
    }
}
