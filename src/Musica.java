public abstract class Musica implements Comparable<Musica> {

    private static int ultimoID = 10000;

    protected int idMusica;
    protected String tituloDaMusica;
    protected String nomeDoArtista;
    protected double duracao;

    protected Musica(String titulo, String artista, double duracao) {

        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("Título inválido");

        if (artista == null || artista.isBlank())
            throw new IllegalArgumentException("Artista inválido");

        if (duracao <= 0)
            throw new IllegalArgumentException("Duração inválida");

        this.tituloDaMusica = titulo;
        this.nomeDoArtista = artista;
        this.duracao = duracao;
        this.idMusica = ultimoID++;
    }

    public int getId() {
        return idMusica;
    }

    public String getTitulo() {
        return tituloDaMusica;
    }

    public String getArtista() {
        return nomeDoArtista;
    }

    public double getDuracao() {
        return duracao;
    }

    @Override
    public int compareTo(Musica outra) {
        return this.tituloDaMusica.compareToIgnoreCase(outra.tituloDaMusica);
    }

    @Override
    public int hashCode() {
        return idMusica;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Musica)) return false;
        Musica outra = (Musica) obj;
        return this.idMusica == outra.idMusica;
    }

    @Override
    public String toString() {
        return String.format("%d; %s; %s; %.2f", idMusica, tituloDaMusica, nomeDoArtista, duracao);
    }

    public abstract String gerarDadosTexto();
}
