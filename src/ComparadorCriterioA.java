import java.util.Comparator;

/**
 * Critério A - Valor Final do Pedido (crescente).
 * Desempate 1: Volume Total de Itens (quantProdutos).
 * Desempate 2: Código Identificador do primeiro item do pedido.
 */
public class ComparadorCriterioA implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        double valorFinal1 = o1.valorFinal();
        double valorFinal2 = o2.valorFinal();
        if (valorFinal1 != valorFinal2) {
            return Double.compare(valorFinal1, valorFinal2);
        }

        double volume1 = o1.getQuantosProdutos();
        double volume2 = o2.getQuantosProdutos();
        if (volume1 != volume2) {
            return Double.compare(volume1, volume2);
        }

        int i1 = o1.getIdPrimeiroProduto();
        int i2 = o2.getIdPrimeiroProduto();
        return Integer.compare(i1, i2);
    }
}
