public class Historico {

    private Lista<Musica> historico;

    public Historico() {
        historico = new Lista<>();
    }

    public void registrar(Musica musica) {
        historico.inserir(musica, 0);
    }

    public Musica voltar() {
        if (historico.vazia())
            throw new IllegalStateException("Nenhuma reprodução anterior!");
        return historico.remover(0);
    }

    public boolean vazio() {
        return historico.vazia();
    }

    @Override
    public String toString() {
        return historico.toString();
    }
}
