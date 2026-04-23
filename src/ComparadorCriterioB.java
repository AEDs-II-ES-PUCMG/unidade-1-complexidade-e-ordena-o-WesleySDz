import java.util.Comparator;

/**
 * Critério B - Volume Total de Itens (crescente).
 * Desempate 1: Data do Pedido.
 * Desempate 2: Código Identificador do pedido.
 */
public class ComparadorCriterioB implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        int formaPagamento1 = o1.getFormaDePagamento();
		int formaPagamento2 = o2.getFormaDePagamento();
		if (formaPagamento1 != formaPagamento2){
			return Integer.compare(formaPagamento1, formaPagamento2);
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
