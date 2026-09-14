package academy.wakanda.wakacop.pauta.application.service;

import academy.wakanda.wakacop.pauta.application.api.NovaPautaRequest;
import academy.wakanda.wakacop.pauta.application.api.PautaCadastradaResponse;
import academy.wakanda.wakacop.pauta.domain.Pauta;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface PautaService {
    PautaCadastradaResponse cadastraPauta(NovaPautaRequest novaPauta);

    Pauta getPautaPorId(@NotNull UUID idPauta);
}
