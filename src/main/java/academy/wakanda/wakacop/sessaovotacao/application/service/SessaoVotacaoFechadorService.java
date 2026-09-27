package academy.wakanda.wakacop.sessaovotacao.application.service;

import academy.wakanda.wakacop.sessaovotacao.domain.PublicadorResultadoSessao;
import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SessaoVotacaoFechadorService {
    private final SessaoVotacaoRespository sessaoVotacaoRespository;
    private final PublicadorResultadoSessao publicador;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void fechaSessoesEncerradas(){
        log.debug("[start] SessaoVotacaoFechadorService - fechaSessoesEncerradas");
        List<SessaoVotacao> sessoesAbertas = sessaoVotacaoRespository.buscaAbertas();
        log.debug("[sessoesAbertas] {}", sessoesAbertas);
        sessoesAbertas.forEach(sessaoVotacao -> {
            sessaoVotacao.obetmResultado(publicador);
            sessaoVotacaoRespository.salva(sessaoVotacao);
        });
        log.debug("[finish] SessaoVotacaoFechadorService - fechaSessoesEncerradas");
    }
}
