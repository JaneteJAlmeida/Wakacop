package academy.wakanda.wakacop.sessaovotacao.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VotoRequest {
    private String cpfAssociado;
    private String opcao;
    private String dataNascimento;
}

