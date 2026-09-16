package academy.wakanda.wakacop.sessaovotacao.application.service;

import academy.wakanda.wakacop.sessaovotacao.domain.SessaoVotacao;

import java.util.UUID;

public interface SessaoVotacaoRespository {
    SessaoVotacao salva(SessaoVotacao sessaoVotacao);
    SessaoVotacao buscaPorId(UUID idSessao);
}
