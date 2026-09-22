package academy.wakanda.wakacop.sessaovotacao.api;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VotoRequest {
    private String cpfAssociado;
    private String opcao;
    private String dataNascimento;
}

