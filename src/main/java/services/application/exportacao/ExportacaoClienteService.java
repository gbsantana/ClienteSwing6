package services.application.exportacao;

import adapters.output.exporter.json.IJSONExporter;
import adapters.output.exporter.json.JSONExporterFactory;
import model.Cliente;
import services.application.RequestResult;
import services.application.cliente.CadastroClientePort;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço de aplicação para exportação de clientes em JSON
 */
public class ExportacaoClienteService implements ExportacaoClientePort {

    private final CadastroClientePort cadastroService;
    private final IJSONExporter exporter;

    public ExportacaoClienteService(CadastroClientePort cadastroService) {
        this.cadastroService = cadastroService;
        this.exporter = JSONExporterFactory.criar();
    }

    public ExportacaoClienteService(CadastroClientePort cadastroService, IJSONExporter exporter) {
        this.cadastroService = cadastroService;
        this.exporter = exporter;
    }

    @Override
    public RequestResult<Void> exportarJSON() {
        try {
            var result = cadastroService.listarClientes();

            if (result == null || result.isFailure() || result.payload() == null) {
                return RequestResult.failure();
            }

            List<Cliente> clientes = result.payload();

            // Ordena por CPF
            clientes.sort(Comparator.comparingLong(c -> c.getCpf() == null ? 0L : c.getCpf().numero()));

            // Exporta usando o adaptador
            exporter.exportar(clientes);

            return RequestResult.success();

        } catch (Exception ex) {
            throw new RuntimeException("Erro na exportação do JSON: " + ex.getMessage(), ex);
        }
    }
}
