package academy.wakanda.wakacop.associado.application.service;

import academy.wakanda.wakacop.associado.infra.client.ConsultaCPFResponse;
import academy.wakanda.wakacop.associado.infra.client.SerproClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AssociadoApplicationService implements AssociadoService {
    private static final String TOKEN = "Bearer 06aef429-a981-3ec5-a1f8-71d38d86481e";
    private final SerproClientFeign serproClientFeign;


    @Override
    public void validaAssociadoAptoVoto(String cpfAssociado, String dataNascimento) {
        log.debug("[start]  validaAssociadoAptoVoto");

        log.info("[MOCK SIMULADO] Ignorando chamada externa para o CPF: {}", cpfAssociado);

        // 1. COMENTE a chamada que vai para a internet:
        // ConsultaCPFResponse consultaCPFResponse = serproClientFeign.consultaCPF(TOKEN, cpfAssociado, dataFormatadaSerpro);
        // valida(consultaCPFResponse);

        log.debug("[finish] AssociadoApplicationService - validaAssociadoAptoVoto");
    }


    private void valida(ConsultaCPFResponse consultaCPFResponse) {

    }

}