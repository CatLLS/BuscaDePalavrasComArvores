package consultaSarms;

import estruturaCat.ListaArquivos;
import indexacaoCaue.Indice;

public class AvaliadorConsulta {
    private final Indice indice; 

    public AvaliadorConsulta(Indice indice) {
        this.indice = indice;
    }

    public ListaArquivos consultar(String consulta) throws ConsultaInvalidaException {
        try {
            return avaliar(new AnalisadorSintatico(AnalisadorLexico.tokenizar(consulta)).analisar());
        } catch (IllegalArgumentException e) {
            throw new ConsultaInvalidaException(e.getMessage(), e);
        }
    }

    // Percurso pós-ordem: resolve recursivamente os ramos filhos antes da raiz,
    // garantindo que as operações agreguem os subconjuntos corretos.
    public ListaArquivos avaliar(NoConsulta no) {
        // Caso base (Nó Folha): realiza a extração do conjunto no índice
        if (no.tipo == TokenConsulta.Tipo.PALAVRA) {
            return indice.buscar(no.palavra);
        } 
        // Operação de Interseção (A ∩ B)
        else if (no.tipo == TokenConsulta.Tipo.E) {
            ListaArquivos esq = avaliar(no.esquerdo);
            ListaArquivos dir = avaliar(no.direito);
            return esq.intersecao(dir);
        } 
        // Operação de União (A ∪ B)
        else if (no.tipo == TokenConsulta.Tipo.OU) {
            ListaArquivos esq = avaliar(no.esquerdo);
            ListaArquivos dir = avaliar(no.direito);
            return esq.uniao(dir);
        } 
        // Operação de Complemento Unário (U - A)
        else if (no.tipo == TokenConsulta.Tipo.NAO) {
            ListaArquivos dir = avaliar(no.direito);
            // Requer o espaço amostral inteiro (universo) para derivar o complemento
            return indice.getTodosOsDocumentos().diferenca(dir);
        }
        
        throw new IllegalStateException("Tipo de nó desconhecido na árvore de consulta.");
    }
}