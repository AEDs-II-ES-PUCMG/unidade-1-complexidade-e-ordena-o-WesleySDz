
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    static ProdutoPerecivel[] produtosPereciveis;
    static Produto[] produtosPorId;
    static Produto[] produtosPorDescricao;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;

    // #region utilidades
    static Scanner teclado;

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.err.println("5 - Heapsort");
        System.err.println("6 - Quicksort");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Padrão");
        System.out.println("2 - Por código");
        System.err.println("3 - Por desconto");

        return lerNumero("Digite sua opção", Integer.class);
    }

    // #endregion
    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        try {
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine());

            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado. Produtos não carregados");
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    static void indicesDeBusca() {
        if (produtos != null) {
            produtosPorId = Arrays.copyOf(produtos, quantProdutos);
            produtosPorDescricao = Arrays.copyOf(produtos, quantProdutos);

            Arrays.sort(produtosPorId, (p1, p2) -> Integer.compare(p1.hashCode(), p2.hashCode()));
            Arrays.sort(produtosPorDescricao, (p1, p2) -> p1.descricao.compareToIgnoreCase(p2.descricao));
        } else {
            System.out.println("Nenhum produto carregado. Índices de busca não criados.");
        }
    }

    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        System.out.println("1 - Buscar por Identificador (ID)");
        System.out.println("2 - Buscar por Descrição");

        int opcao = lerNumero("Digite sua opção", Integer.class);
        Produto produto = null;
        switch (opcao) {
            case 1 -> {
                int id = lerNumero("Digite o ID do produto", Integer.class);
                produto = pesquisaBinarioId(id);
            }
            case 2 -> {
                System.out.print("Digite a descrição do produto: ");
                String descricao = teclado.nextLine();
                produto = pesquisaBinarioDescricao(descricao);
            }
            default ->
                System.out.println("Opção inválida. Por favor, escolha 1 ou 2.");
        }
        return produto;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados inválidos";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        System.out.println("Filtrando por valor máximo:");
        double valor = lerNumero("valor", Double.class);
        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() < valor) {
                relatorio.append(produtos[i]).append("\n");
            }
        }
        System.out.println(relatorio.toString());
    }

    static Produto pesquisaBinarioId(int idProcurado) {
        int inicio = 0;
        int fim = quantProdutos - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (produtosPorId[meio].hashCode() == idProcurado) {
                return produtosPorId[meio];
            } else if (produtosPorId[meio].hashCode() < idProcurado) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return null; // Produto não encontrado
    }

    static Produto pesquisaBinarioDescricao(String descricaoProcurada) {
        int inicio = 0;
        int fim = quantProdutos - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comparacao = produtosPorDescricao[meio].descricao.compareToIgnoreCase(descricaoProcurada);
            if (comparacao == 0) {
                return produtosPorDescricao[meio];
            } else if (comparacao < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return null; // Produto não encontrado
    }

    static void ordenarProdutos() {
        cabecalho();

        int opcao = exibirMenuOrdenadores();
        switch (opcao) {
            case 1 ->
                ordenador = new Bubblesort<>();
            case 2 ->
                ordenador = new InsertSort<>();
            case 3 ->
                ordenador = new SelectionSort<>();
            case 4 ->
                ordenador = new Mergesort<>();
            case 5 ->
                ordenador = new HeapSort<>();
            case 6 ->
                ordenador = new QuickSort<>();
            default ->
                System.out.println("Opção inválida. Por favor, escolha um número entre 1 e 6.");
        }

        Produto[] ordenados = null;
        int opcao2 = exibirMenuComparadores();
        switch (opcao2) {
            case 1 ->
                ordenados = ordenador.ordenar(produtos);
            case 2 -> // Ordena por codigo (ID)
                ordenados = ordenador.ordenar(produtos, (p1, p2) -> Integer.compare(p1.hashCode(), p2.hashCode()));
            case 3 -> // Ordena por desconto, mas só os produtos pereciveis tem
                ordenados = ordenador.ordenar(produtos, (p1, p2) -> {
                    // O desconto se aplica se a validade estiver vencendo em até 7 dias dentro do metodo valorDeVenda (desconto fixo definido na classe Perecivel), então o valor de venda já considera isso. Para os não perecíveis, o desconto é 0.
                    double valorBase1 = p1.precoCusto * (1 + p1.margemLucro);
                    double valorBase2 = p2.precoCusto * (1 + p2.margemLucro);

                    double desconto1 = valorBase1 - p1.valorDeVenda();
                    double desconto2 = valorBase2 - p2.valorDeVenda();

                    return Double.compare(desconto1, desconto2);
                });
            default ->
                System.out.println("Opção inválida. Por favor, escolha um número entre 1 e 3.");
        }

        if (ordenados != null) {
            verificarSubstituicao(ordenados);
        }

    }

    static void embaralharProdutos() {
        Collections.shuffle(Arrays.asList(produtos));
    }

    static void verificarSubstituicao(Produto[] copiaDados) {
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)? ");
        String resposta = teclado.nextLine().toUpperCase();
        if (resposta.equals("S")) {
            produtos = Arrays.copyOf(copiaDados, copiaDados.length);
        }
    }

    static void listarProdutos() {
        cabecalho();
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);

        indicesDeBusca();

        embaralharProdutos();

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 ->
                    mostrarProduto(localizarProduto());
                case 2 ->
                    filtrarPorPrecoMaximo();
                case 3 ->
                    ordenarProdutos();
                case 4 ->
                    embaralharProdutos();
                case 5 ->
                    listarProdutos();
                case 0 ->
                    System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);
        teclado.close();
    }
}
