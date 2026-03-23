import java.time.LocalDateTime;
import java.util.ArrayList;

public class Evento {
    String nome;
    String endereco;
    String cidade;
    String categoria;
    LocalDateTime data;
    String descricao;

    ArrayList<String> participantes = new ArrayList<>();

    public Evento(String nome, String endereco, String cidade,
                  String categoria, LocalDateTime data, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.categoria = categoria;
        this.data = data;
        this.descricao = descricao;
    }
}
