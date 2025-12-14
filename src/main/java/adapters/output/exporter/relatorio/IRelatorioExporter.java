package adapters.output.exporter.relatorio;

import model.Cliente;
import java.util.List;

/**
 * Interface para adaptadores de geração de relatórios
 */
public interface IRelatorioExporter {

    /**
     * Gera um relatório com a lista de clientes
     *
     * @param clientes Lista de clientes
     * @throws Exception Se houver erro na geração
     */
    void exportar(List<Cliente> clientes) throws Exception;
}
