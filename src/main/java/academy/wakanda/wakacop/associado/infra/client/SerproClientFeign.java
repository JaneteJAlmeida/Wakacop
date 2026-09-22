package academy.wakanda.wakacop.associado.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "serproClientFeign", url = "https://serpro.gov.br/consulta-cpf-df-trial")
public interface SerproClientFeign {

    @GetMapping(value = "/v3/cpf/{cpfAssociado}/{dataNascimento}")
    ConsultaCPFResponse consultaCPF(
            @RequestHeader(value = "Authorization") String authorization,
            @PathVariable("cpfAssociado") String cpfAssociado,
            @PathVariable("dataNascimento") String dataNascimento
    );
}
