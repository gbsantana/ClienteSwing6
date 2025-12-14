package services.application.relatorio;

import adapters.output.exporter.relatorio.IRelatorioExporter;
import adapters.output.exporter.relatorio.RelatorioExporterFactory;
import model.Cliente;
import services.application.RequestResult;
import services.application.cliente.CadastroClientePort;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço de aplicação para geração de relatórios de clientes
 */
public class RelatorioClienteService implements RelatorioClientePort {

    private final CadastroClientePort cadastroService;
    private final IRelatorioExporter exporter;

    public RelatorioClienteService(CadastroClientePort cadastroService) {
        this.cadastroService = cadastroService;
        this.exporter = RelatorioExporterFactory.criar();
    }

    public RelatorioClienteService(CadastroClientePort cadastroService, IRelatorioExporter exporter) {
        this.cadastroService = cadastroService;
        this.exporter = exporter;
    }

    @Override
    public RequestResult<Void> gerarRelatorio() {
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
            throw new RuntimeException("Erro na geração do relatório: " + ex.getMessage(), ex);
        }
    }
}
