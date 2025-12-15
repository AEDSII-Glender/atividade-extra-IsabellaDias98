import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V>{

	private No<K, V> raiz; // referência à raiz da árvore.
	private Comparator<K> comparador; //comparador empregado para definir "menores" e "maiores".
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;
    private V removido;

    /**
	 * Método auxiliar para inicialização da árvore binária de busca.
	 * 
	 * Este método define a raiz da árvore como {@code null} e seu tamanho como 0.
	 * Utiliza o comparador fornecido para definir a organização dos elementos na árvore.
	 * @param comparador o comparador para organizar os elementos da árvore.
	 */
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	/**
	 * Construtor da classe.
	 * O comparador padrão de ordem natural será utilizado.
	 */ 
	@SuppressWarnings("unchecked")
	public ABB() {
	    init((Comparator<K>) Comparator.naturalOrder());
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária de busca vazia.
	 *  
	 * @param comparador o comparador a ser utilizado para organizar os elementos da árvore.  
	 */
	public ABB(Comparator<K> comparador) {
	    init(comparador);
	}

    /**
     * Construtor da classe.
     * Esse construtor cria uma nova árvore binária a partir de uma outra árvore binária de busca,
     * com os mesmos itens, mas usando uma nova chave.
     * @param original a árvore binária de busca original.
     * @param funcaoChave a função que irá extrair a nova chave de cada item para a nova árvore.
     */
    public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
        ABB<K, V> nova = new ABB<>();
        nova = copiarArvore(original.raiz, funcaoChave, nova);
        this.raiz = nova.raiz;
    }
    
    /**
     * Recursivamente, copia os elementos da árvore original para esta, num processo análogo ao caminhamento em ordem.
     * @param <T> Tipo da nova chave.
     * @param raizArvore raiz da árvore original que será copiada.
     * @param funcaoChave função extratora da nova chave para cada item da árvore.
     * @param novaArvore Nova árvore. Parâmetro usado para permitir o retorno da recursividade.
     * @return A nova árvore com os itens copiados e usando a chave indicada pela função extratora.
     */
    private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {
    	
        if (raizArvore != null) {
    		novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
            V item = raizArvore.getItem();
            T chave = funcaoChave.apply(item);
    		novaArvore.inserir(chave, item);
    		novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
    	}
        return novaArvore;
    }
    
    /**
	 * Método booleano que indica se a árvore está vazia ou não.
	 * @return
	 * verdadeiro: se a raiz da árvore for null, o que significa que a árvore está vazia.
	 * falso: se a raiz da árvore não for null, o que significa que a árvore não está vazia.
	 */
	public Boolean vazia() {
	    return (this.raiz == null);
	}
    
    @Override
    /**
     * Método que encapsula a pesquisa recursiva de itens na árvore.
     * @param chave a chave do item que será pesquisado na árvore.
     * @return o valor associado à chave.
     */
	public V pesquisar(K chave) {
    	comparacoes = 0;
    	inicio = System.nanoTime();
    	V procurado = pesquisar(raiz, chave);
    	termino = System.nanoTime();
    	return procurado;
	}
    
    private V pesquisar(No<K, V> raizArvore, K procurado) {
    	
    	int comparacao;
    	
    	comparacoes++;
    	if (raizArvore == null)
    		throw new NoSuchElementException("O item não foi localizado na árvore!");
    	
    	comparacao = comparador.compare(procurado, raizArvore.getChave());
    	
    	if (comparacao == 0)
    		return raizArvore.getItem();
    	else if (comparacao < 0)
    		return pesquisar(raizArvore.getEsquerda(), procurado);
    	else
    		return pesquisar(raizArvore.getDireita(), procurado);
    }
    
    @Override
    /**
     * Método que encapsula a adição recursiva de itens à árvore, associando-o à chave fornecida.
     * @param chave a chave associada ao item que será inserido na árvore.
     * @param item o item que será inserido na árvore.
     * 
     * @return o tamanho atualizado da árvore após a execução da operação de inserção.
     */
    public int inserir(K chave, V item) {
        inicio = System.nanoTime();
        comparacoes = 0;
        raiz = inserir(raiz, chave, item);
        termino = System.nanoTime();
        return tamanho;
    }

    private No<K, V> inserir(No<K, V> raizArvore, K chave, V item) {
        if (raizArvore == null) {
            tamanho++;
            return new No<>(chave, item);
        }
        int comp = comparador.compare(chave, raizArvore.getChave());
        comparacoes++;
        if (comp < 0) {
            raizArvore.setEsquerda(inserir(raizArvore.getEsquerda(), chave, item));
        } else if (comp > 0) {
            raizArvore.setDireita(inserir(raizArvore.getDireita(), chave, item));
        } else {
            raizArvore.setItem(item);
        }
        return raizArvore;
    }

    @Override 
    public String toString(){
    	return percorrer();
    }

    @Override
    public String percorrer() {
    	return caminhamentoEmOrdem();
    }

    public String caminhamentoEmOrdem() {
        StringBuilder string = new StringBuilder();
        caminhamentoEmOrdem(raiz, string);
        return string.toString();
    }

    private void caminhamentoEmOrdem(No<K, V> no, StringBuilder string) {
        if (no != null) {
            caminhamentoEmOrdem(no.getEsquerda(), string);
            string.append(no.getItem()).append("\n");
            caminhamentoEmOrdem(no.getDireita(), string);
        }
    }

    @Override
    /**
     * Método que encapsula a remoção recursiva de um item da árvore.
     * @param chave a chave do item que deverá ser localizado e removido da árvore.
     * @return o valor associado ao item removido.
     */
    public V remover(K chave) {
        inicio = System.nanoTime();
        comparacoes = 0;

        removido = null;
        raiz = remover(raiz, chave);

        termino = System.nanoTime();

        if (removido == null)
            throw new NoSuchElementException("Item não encontrado!");

        return removido;
    }

    private No<K, V> remover(No<K, V> raizArvore, K chave) {

        if (raizArvore == null)
            return null;

        int comp = comparador.compare(chave, raizArvore.getChave());
        comparacoes++;

        if (comp < 0) {
            raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), chave));
        }
        else if (comp > 0) {
            raizArvore.setDireita(remover(raizArvore.getDireita(), chave));
        }
        else {
            removido = raizArvore.getItem();
            tamanho--;

            if (raizArvore.getEsquerda() == null && raizArvore.getDireita() == null)
                return null;

            if (raizArvore.getEsquerda() == null)
                return raizArvore.getDireita();

            if (raizArvore.getDireita() == null)
                return raizArvore.getEsquerda();

            No<K, V> sucessor = menor(raizArvore.getDireita());
            raizArvore.setChave(sucessor.getChave());
            raizArvore.setItem(sucessor.getItem());

            raizArvore.setDireita(remover(raizArvore.getDireita(), sucessor.getChave()));
        }

        return raizArvore;
    }

    private No<K, V> menor(No<K, V> no) {
        while (no.getEsquerda() != null)
            no = no.getEsquerda();
        return no;
    }

    @Override
	public int tamanho() {
		return tamanho;
	}
	
	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}
}