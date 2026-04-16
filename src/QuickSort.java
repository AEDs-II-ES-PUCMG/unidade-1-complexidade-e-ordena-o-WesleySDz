import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class QuickSort<T extends Comparable<T>> implements IOrdenador<T>{

    private long comparacoes;
	private long movimentacoes;
	private LocalDateTime inicio;
	private LocalDateTime termino;
    private Comparator<T> comparador;

    public QuickSort() {
        comparacoes = 0;
        movimentacoes = 0;
    }

    @Override
    public T[] ordenar(T[] dados) {
        return ordenar(dados, T::compareTo);
    }

    @Override
	public T[] ordenar(T[] dados, Comparator<T> comparador) {
        this.comparador = comparador;
        T[] dadosOrdenados = java.util.Arrays.copyOf(dados, dados.length);
        inicio = LocalDateTime.now();
        quicksort(dadosOrdenados, 0, dadosOrdenados.length - 1);
        termino = LocalDateTime.now();
        return dadosOrdenados;
    }

    /**
     * Algoritmo de ordenação Quicksort.
     *
     * @param int esq: início da partição a ser ordenada
     * @param int dir: fim da partição a ser ordenada
     */
    private void quicksort(T[] array, int esq, int dir) {

        int part;
        if (esq < dir) {
            part = particao(array, esq, dir);
            quicksort(array, esq, part - 1);
            quicksort(array, part + 1, dir);
        }
    }

    private int particao(T[] array, int inicio, int fim) {

        T pivot = array[fim];
        int part = inicio - 1;
        for (int i = inicio; i < fim; i++) {
            comparacoes++;
            if (comparador.compare(array[i], pivot) < 0) {
                comparacoes++;
                part++;
                swap(array, part, i);
            }
        }
        part++;
        swap(array, part, fim);
        return (part);
    }

    private void swap(T[] array, int i, int j) {
        movimentacoes++;

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
