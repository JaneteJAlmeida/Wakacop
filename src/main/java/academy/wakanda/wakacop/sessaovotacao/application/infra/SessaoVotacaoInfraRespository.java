package academy.wakanda.wakacop.sessaovotacao.application.infra;

import academy.wakanda.wakacop.sessaovotacao.application.service.SessaoVotacaoRespository;
import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class SessaoVotacaoInfraRespository implements SessaoVotacaoRespository {

    private final SessaoVotacaoSpringDataJPARepository sessaoVotacaoSpringDataJPARepository;

    @Override
    public SessaoVotacao salva(SessaoVotacao sessaoVotacao) {
        log.info("[start] SessaoVotacaoInfraRespository - salva");
        sessaoVotacaoSpringDataJPARepository.save(sessaoVotacao);
        log.info("[start] SessaoVotacaoInfraRespository - salva");

        return sessaoVotacao;
    }

    @Override
    public SessaoVotacao buscaPorId(UUID idSessao) {
        log.info("[start] SessaoVotacaoInfraRespository - buscaPorId");
        SessaoVotacao sessao = sessaoVotacaoSpringDataJPARepository.findById(idSessao)
                        .orElseThrow(() -> new RuntimeException("Sessão não encontrada!"));
        log.info("[start] SessaoVotacaoInfraRespository - buscaPorId");
        return sessao;
    }
}
