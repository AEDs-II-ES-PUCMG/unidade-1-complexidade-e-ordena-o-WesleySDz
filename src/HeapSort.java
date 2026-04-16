import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class HeapSort<T extends Comparable<T>> implements IOrdenador<T> {

    private long comparacoes;
	private long movimentacoes;
	private LocalDateTime inicio;
	private LocalDateTime termino;
    private Comparator<T> comparador;

    @Override
    public T[] ordenar(T[] dados) {
        return ordenar(dados, T::compareTo);
    }

    @Override
	public T[] ordenar(T[] dados, Comparator<T> comparador) {
        this.comparador = comparador;
        T[] dadosOrdenados = java.util.Arrays.copyOf(dados, dados.length + 1);
        inicio = LocalDateTime.now();
        heapsort(dadosOrdenados);
        termino = LocalDateTime.now();
        return dadosOrdenados;
    }

    void heapsort(T[] dados) {

        // Criando outro vetor, com todos os elementos do vetor anterior reposicionados (uma posição a frente)
        // de forma a ignorar a posição zero	    
        T[] tmp = java.util.Arrays.copyOf(dados, dados.length);
        for (int i = 0; i < dados.length - 1; i++) {
            tmp[i + 1] = dados[i];
        }

        // Construção do heap
        for (int tamHeap = (tmp.length - 1) / 2; tamHeap >= 1; tamHeap--) {
            restaura(tmp, tamHeap, tmp.length - 1);
        }

        //Ordenação propriamente dita
        int tamHeap = tmp.length - 1;
        troca(tmp, 1, tamHeap--);
        while (tamHeap > 1) {
            restaura(tmp, 1, tamHeap);
            troca(tmp, 1, tamHeap--);
        }

        //Alterar o vetor para voltar à posição zero
        for (int i = 0; i < dados.length -1; i++) {
            dados[i] = tmp[i + 1];
        }
    }

    void restaura(T[] array, int i, int tamHeap) {

        int maior = i;
        int filho = getMaiorFilho(array, i, tamHeap);

        if (comparador.compare(array[i], array[filho]) < 0) {
            maior = filho;
        }
        if (maior != i) {
            troca(array, i, maior);
            if (maior <= tamHeap / 2) {
                restaura(array, maior, tamHeap);
            }
        }
    }

    int getMaiorFilho(T[] array, int i, int tamHeap) {

        int filho;

        if (2 * i == tamHeap || comparador.compare(array[2 * i], array[2 * i + 1]) > 0) {
            filho = 2 * i;
        }
        else {
            filho = 2 * i + 1;
        }
        return filho;
    }

    void troca(T[] array, int i, int j) {

        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public long getComparacoes() {
        return comparacoes;
    }

    @Override
    public long getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        return Duration.between(inicio, termino).toMillis();
    }

}
