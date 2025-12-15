import java.nio.charset.Charset;
import java.util.Scanner;
import java.io.File;

public class App {

    static Scanner teclado;
    static Playlist playlist;
    static Historico historico;
    static Buscador buscador;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("\nDigite ENTER para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("Playlist de Músicas");
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Adicionar música");
        System.out.println("2 - Remover música");
        System.out.println("3 - Ordenar playlist");
        System.out.println("4 - Reproduzir na ordem");
        System.out.println("5 - Reproduzir aleatoriamente");
        System.out.println("6 - Voltar reprodução (histórico)");
        System.out.println("7 - Exibir playlist atual");
        System.out.println("8 - Buscar música por ID");
        System.out.println("9 - Buscar música por duração");
        System.out.println("10 - Carregar músicas do arquivo");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    static void carregarArquivo(String nomeArquivo) {
        try {
            Scanner arquivo = new Scanner(new File(nomeArquivo), Charset.forName("UTF-8"));

            int qtd = Integer.parseInt(arquivo.nextLine());

            for (int i = 0; i < qtd; i++) {
                String linha = arquivo.nextLine();
                Musica m = criarMusicaDoTexto(linha);

                playlist.adicionar(m);
                buscador.adicionarMusica(m);
            }

            arquivo.close();
            System.out.println("\nArquivo carregado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao carregar arquivo: " + e.getMessage());
        }
    }

    static Musica criarMusicaDoTexto(String linha) {
        String[] dados = linha.split(";");

        int id = Integer.parseInt(dados[0]);
        String titulo = dados[1];
        String artista = dados[2];
        double duracao = Double.parseDouble(dados[3].replace(",", "."));

        Musica m = new MusicaSimples(titulo, artista, duracao);

        m.idMusica = id;

        return m;
    }

    static void adicionarMusica() {
        limparTela();
        cabecalho();

        System.out.println("Músicas Disponíveis:");
        System.out.println(buscador.listarPorID());

        System.out.print("\nDigite o ID da música que deseja adicionar: ");
        int id = Integer.parseInt(teclado.nextLine());

        try {
            Musica m = buscador.buscarPorID(id);
            playlist.adicionar(m);
            System.out.println("\nMúsica adicionada à playlist!");
        } catch (Exception e) {
            System.out.println("\nID não encontrado. Nenhuma música adicionada.");
        }
    }

    static void removerMusica() {
        limparTela();
        cabecalho();

        System.out.print("ID da música: ");
        int id = Integer.parseInt(teclado.nextLine());

        try {
            Musica m = buscador.buscarPorID(id);
            playlist.remover(m);
            System.out.println("Removida!");
        } catch (Exception e) {
            System.out.println("Não encontrada.");
        }
    }

    static void ordenarPlaylist() {
        limparTela();
        cabecalho();

        System.out.println("1 - ID");
        System.out.println("2 - Título");
        System.out.println("3 - Duração");
        System.out.print("Opção: ");
        int op = Integer.parseInt(teclado.nextLine());

        playlist.ordenar(op);
        System.out.println("Playlist ordenada!");
    }

    static void reproduzirNaOrdem() {
        limparTela();
        cabecalho();

        if (playlist.vazia()) {
            System.out.println("Playlist vazia!");
            return;
        }

        System.out.println("1 - Iniciar do início");
        System.out.println("2 - Iniciar do fim");
        System.out.print("Opção: ");
        int op = Integer.parseInt(teclado.nextLine());

        Musica atual = (op == 1) ? playlist.getPrimeira() : playlist.getUltima();

        while (true) {

            if (atual == null) {
                System.out.println("\nPlaylist finalizada. Deseja reiniciar?");
                System.out.println("1 - Sim");
                System.out.println("0 - Não");
                System.out.print("Opção: ");
                int reiniciar = Integer.parseInt(teclado.nextLine());

                if (reiniciar == 1) {
                    atual = (op == 1) ? playlist.getPrimeira() : playlist.getUltima();
                    continue;
                } else {
                    break;
                }
            }

            System.out.println("\nReproduzindo (" + atual.getTitulo() + " - " + atual.getArtista() + ")");
            historico.registrar(atual);

            System.out.println("\n1 - Próxima");
            System.out.println("2 - Anterior");
            System.out.println("0 - Parar");
            System.out.print("Opção: ");
            int acao = Integer.parseInt(teclado.nextLine());

            if (acao == 1) {
                atual = playlist.proxima(atual);
            } else if (acao == 2) {
                atual = playlist.anterior(atual);
            } else {
                break;
            }
        }
    }

    static void reproduzirAleatorio() {
        limparTela();
        cabecalho();

        while (true) {
            System.out.print("ID da música (0 para sair): ");
            int id = Integer.parseInt(teclado.nextLine());

            if (id == 0) break;

            try {
                Musica m = buscador.buscarPorID(id);
                System.out.println("Reproduzindo: " + m);
                historico.registrar(m);
            } catch (Exception e) {
                System.out.println("Não encontrada.");
            }
        }
    }

    static void voltarReproducao() {
        limparTela();
        cabecalho();

        if (historico.vazio()) {
            System.out.println("Nenhuma reprodução anterior.");
            return;
        }

        while (true) {
            try {
                Musica m = historico.voltar();
                System.out.println("Voltando para: " + m);

                System.out.println("\n1 - Voltar mais uma");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");
                int op = Integer.parseInt(teclado.nextLine());

                if (op == 0) break;

            } catch (Exception e) {
                System.out.println("\nNão há mais músicas no histórico.");
                break;
            }
        }
    }

    static void exibirPlaylist() {
        limparTela();
        cabecalho();
        System.out.println(playlist);
    }

    static void buscarPorID() {
        limparTela();
        cabecalho();

        System.out.print("ID: ");
        int id = Integer.parseInt(teclado.nextLine());

        try {
            System.out.println(buscador.buscarPorID(id));
        } catch (Exception e) {
            System.out.println("Não encontrada.");
        }
    }

    static void buscarPorDuracao() {
        limparTela();
        cabecalho();

        System.out.print("Duração: ");
        double d = Double.parseDouble(teclado.nextLine());

        try {
            System.out.println(buscador.buscarPorDuracao(d));
        } catch (Exception e) {
            System.out.println("Não encontrada.");
        }
    }


    public static void main(String[] args) {

        teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        playlist = new Playlist();
        historico = new Historico();
        buscador = new Buscador();

        carregarArquivo("musicas.txt");

        int op;
        do {
            limparTela();
            op = menu();

            switch (op) {
                case 1 -> adicionarMusica();
                case 2 -> removerMusica();
                case 3 -> ordenarPlaylist();
                case 4 -> reproduzirNaOrdem();
                case 5 -> reproduzirAleatorio();
                case 6 -> voltarReproducao();
                case 7 -> exibirPlaylist();
                case 8 -> buscarPorID();
                case 9 -> buscarPorDuracao();
                case 10 -> carregarArquivo("musicas.txt");
            }
            pausa();

        } while (op != 0);

        teclado.close();
    }
}