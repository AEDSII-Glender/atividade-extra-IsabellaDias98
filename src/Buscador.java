public class Buscador {

    private ABB<Integer, Musica> arvorePorID;
    private ABB<Double, Musica> arvorePorDuracao;

    public Buscador() {
        arvorePorID = new ABB<>();
        arvorePorDuracao = new ABB<>((d1, d2) -> {
            int cmp = Double.compare(d1, d2);
            return cmp;
        });
    }

    public void adicionarMusica(Musica m) {
        arvorePorID.inserir(m.getId(), m);
        arvorePorDuracao.inserir(m.getDuracao(), m);
    }

    public Musica buscarPorID(int id) {
        return arvorePorID.pesquisar(id);
    }

    public Musica buscarPorDuracao(double duracao) {
        return arvorePorDuracao.pesquisar(duracao);
    }

    public String listarPorID() {
        return arvorePorID.caminhamentoEmOrdem();
    }

    public String listarPorDuracao() {
        return arvorePorDuracao.caminhamentoEmOrdem();
    }
}
