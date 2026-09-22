package academy.wakanda.wakacop.associado.infra.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConsultaCPFResponse {
    @JsonProperty("ni")
    private String ni;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("situacao")
    private Situacao situacao;

    @JsonProperty("nascimento")
    private String nascimento;


    public boolean isvalid() {
        return this.situacao != null && "0".equals(this.situacao.getCodigo());
    }

    public boolean isValid() {
        return false;
    }

    @Getter
    @ToString
    public static class Situacao {
        @JsonProperty("codigo")
        private String codigo;

        @JsonProperty("descricao")
        private String descricao;
    }
}
