package adapters.input.ui.cadastrocliente;

import services.application.cep.CEPPort;
import services.application.cliente.CadastroClientePort;
import services.application.relatorio.RelatorioClientePort;
import services.application.exportacao.ExportacaoClientePort;
import services.application.uf.UFPort;

public class UIManager {

    public static void run(CadastroClientePort cadastroService, CEPPort cepService, UFPort ufService, RelatorioClientePort relatorioService, ExportacaoClientePort exportacaoService) {
        new MainWindow(cadastroService, cepService, ufService, relatorioService, exportacaoService);
    }
}
