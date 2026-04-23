import java.util.Comparator;

/**
 * Critério C - Índice de Economia (decrescente).
 * O índice de economia é a diferença entre o valor de catálogo atual e o valor efetivamente pago.
 * Desempate 1: Valor Final do Pedido (crescente).
 * Desempate 2: Código Identificador do pedido (crescente).
 */
public class ComparadorCriterioC implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        double valorMedio1 = o1.valorFinal() / o1.quantidadeDeItensNoCarrinho();
        double valorMedio2 = o1.valorFinal() / o1.quantidadeDeItensNoCarrinho();
        if (valorMedio1 != valorMedio2){
            return Double.compare(valorMedio1, valorMedio2);
        }

        double valorFinal1 = o1.valorFinal();
        double valorFinal2 = o2.valorFinal();
        if (valorFinal1 != valorFinal2) {
            return Double.compare(valorFinal1, valorFinal2);
        } 

        int i1 = o1.getIdPedido();
        int i2 = o2.getIdPedido();
        return Integer.compare(i1, i2);
    }
}
