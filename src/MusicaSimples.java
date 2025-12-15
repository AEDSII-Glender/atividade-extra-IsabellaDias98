/**
 Professor, eu criei essa classe só pra gerar os dados das músicas no formato correto
 */
public class MusicaSimples extends Musica {

    public MusicaSimples(String titulo, String artista, double duracao) {
        super(titulo, artista, duracao);
    }

    @Override
    public String gerarDadosTexto() {
        return idMusica + ";" +
                tituloDaMusica + ";" +
                nomeDoArtista + ";" +
                duracao;
    }
}
