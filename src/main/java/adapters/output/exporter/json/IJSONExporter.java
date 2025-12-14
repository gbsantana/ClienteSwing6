package adapters.output.exporter.json;

import model.Cliente;
import java.util.List;

/**
 * Interface para adaptadores de exportação em JSON
 */
public interface IJSONExporter {

    /**
     * Exporta uma lista de clientes em formato JSON
     *
     * @param clientes Lista de clientes
     * @throws Exception Se houver erro na exportação
     */
    void exportar(List<Cliente> clientes) throws Exception;
}
