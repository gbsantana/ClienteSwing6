package main;

import adapters.infrastructure.opencep.OpenCEPFactory;
import adapters.infrastructure.persistence.DBConnection;
import adapters.infrastructure.persistence.SQLDAOFactory;
import adapters.input.ui.cadastrocliente.UIManager;
import services.application.cep.CEPService;
import services.application.cep.ServicoCEPFactory;
import services.application.cliente.CadastroClienteCtrl;
import services.application.relatorio.RelatorioClienteService;
import services.application.exportacao.ExportacaoClienteService;
import services.application.uf.UFService;
import services.domain.persistence.DAOFactory;
import services.domain.persistence.RepositoryPool;

import java.io.File;

public class Main {

    // Caminho para o arquivo do SQLite
    private static final String DB_PATH = "C:/Projetos/TPS/locadora.db";

	public static void main(String[] args) {

        // ---------------------------------------------------------------------
        // Usando o SQLite

        // Verifica se o arquivo SQLite existe
        if (! new File(DB_PATH).exists()) {
            System.out.printf("Erro: arquivo '%s' não encontrado!", DB_PATH);
            return;
        }

        // Define a URL de conexão com o BD
        DBConnection.setUrl("jdbc:sqlite:" + DB_PATH);

        // Testa se o BD está no ar
        try {
            DBConnection.get();
            DBConnection.close();
        } catch(Exception ignored) {
            System.out.println("Erro: problemas na conexão com o SQLite");
            System.exit(1);
        }

        // Cria a fábrica de DAOs
        var factory = new SQLDAOFactory();

        // Fim SQLite
        // ---------------------------------------------------------------------

        // Injeta a fábrica de DAO
        DAOFactory.register(factory);

        // Define os tempos do pool de repositórios
        RepositoryPool.setup(60, 180);

		// Cria a fábrica do serviço de busca do endereço pelo CEP
		ServicoCEPFactory cepServiceFactory = new OpenCEPFactory();
		
		// Cria o serviço da UF
		var ufService = new UFService();
	
		// Cria o serviço de CEP injetando o serviço de busca do endereço pelo CEP
		var cepService = new CEPService(cepServiceFactory.cria());
		
		// Cria o controller do serviço de cadastro de clientes
		var controller = new CadastroClienteCtrl();
		
		// Cria os serviços de aplicação de exportação (relatório e JSON)
		var relatorioService = new RelatorioClienteService(controller);
		var exportacaoService = new ExportacaoClienteService(controller);
		
		// Cria a UI injetando os serviços que ela precisa e dispara a execução
		UIManager.run(controller, cepService, ufService, relatorioService, exportacaoService);
	}
}
