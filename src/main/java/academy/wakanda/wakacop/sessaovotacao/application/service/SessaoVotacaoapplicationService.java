package academy.wakanda.wakacop.sessaovotacao.application.service;

import academy.wakanda.wakacop.sessaovotacao.application.api.SessaoAberturaRequest;
import academy.wakanda.wakacop.sessaovotacao.application.api.SessaoAberturaResponse;
import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class SessaoVotacaoapplicationService implements SessaoVotacaoService {
    private final SessaoVotacaoRespository sessaoVotacaoRespository;

    @Override
    public SessaoAberturaResponse abreSessao(SessaoAberturaRequest sessaoAberturaRequest) {
        log.info("[start] SessaoVotacaoapplicationService - abreSessao");
        SessaoVotacao sessaoVotacao = sessaoVotacaoRespository.salva(new SessaoVotacao(sessaoAberturaRequest));
        log.info("[finish] SessaoVotacaoapplicationService - abreSessao");
        return new SessaoAberturaResponse(sessaoVotacao);

    }
}
