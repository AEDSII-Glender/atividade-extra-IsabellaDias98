public class Playlist {

    private Lista<Musica> musicas;

    public Playlist() {
        musicas = new Lista<>();
    }

    public boolean vazia() {
        return musicas.vazia();
    }

    public void adicionar(Musica m) {
        musicas.inserirFinal(m);
    }

    public void remover(Musica m) {
        musicas.remover(m);
    }

    public Musica getPrimeira() {
        if (vazia()) return null;
        return getPorPosicao(0);
    }

    public Musica getUltima() {
        if (vazia()) return null;
        return getPorPosicao(musicas.tamanho() - 1);
    }

    public Musica proxima(Musica atual) {
        int idx = indiceDe(atual);
        if (idx == -1) return null;
        if (idx == musicas.tamanho() - 1) return null;
        return getPorPosicao(idx + 1);
    }

    public Musica anterior(Musica atual) {
        int idx = indiceDe(atual);
        if (idx <= 0) return null; // primeira ou não encontrada
        return getPorPosicao(idx - 1);
    }

    public void ordenar(int criterio) {

        int n = musicas.tamanho();
        if (n <= 1) return;

        Musica[] vetor = new Musica[n];
        for (int i = 0; i < n; i++) {
            vetor[i] = getPorPosicao(i);
        }

        java.util.Arrays.sort(vetor, (a, b) -> {
            return switch (criterio) {
                case 1 -> Integer.compare(a.getId(), b.getId());
                case 2 -> a.getTitulo().compareToIgnoreCase(b.getTitulo());
                case 3 -> Double.compare(a.getDuracao(), b.getDuracao());
                default -> 0;
            };
        });

        musicas = new Lista<>();
        for (Musica m : vetor) {
            musicas.inserirFinal(m);
        }
    }

    private Musica getPorPosicao(int pos) {
        return musicas.get(pos);
    }

    private int indiceDe(Musica m) {
        int n = musicas.tamanho();
        for (int i = 0; i < n; i++) {
            Musica atual = getPorPosicao(i);
            if (atual.equals(m)) return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return musicas.toString();
    }
}
