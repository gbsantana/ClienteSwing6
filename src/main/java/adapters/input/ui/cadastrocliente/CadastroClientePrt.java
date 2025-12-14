package adapters.input.ui.cadastrocliente;

import services.application.cep.CEPPort;
import services.application.cliente.CadastroClientePort;
import services.application.cliente.CadastroClienteRequest;
import services.application.relatorio.RelatorioClientePort;
import services.application.exportacao.ExportacaoClientePort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe PRESENTER do cadastro de clientes
 */
public class CadastroClientePrt implements ActionListener {

    private final MainWindow window;
    private final CadastroClienteView cadastroView;
    private final CadastroClientePort cadastroService;
    private final CEPPort cepService;
    private final RelatorioClientePort relatorioService;
    private final ExportacaoClientePort exportacaoService;

    public CadastroClientePrt(MainWindow window, CadastroClienteView cadastroView, CadastroClientePort cadastroService, CEPPort cepService, RelatorioClientePort relatorioService, ExportacaoClientePort exportacaoService) {
    	// PRESENTER referencia a WINDOW
        this.window = window;

        // PRESENTER referencia a VIEW
        this.cadastroView = cadastroView;
        
        // VIEW referencia o PRESENTER
        cadastroView.setPresenter(this);

        // PRESENTER referencia o CONTROLLER DO UC
        this.cadastroService = cadastroService;

        // PRESENTER referencia o serviço do CEP
        this.cepService = cepService;

        // PRESENTER referencia os serviços de exportação
        this.relatorioService = relatorioService;
        this.exportacaoService = exportacaoService;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cadastroView.getBtSalvar())
            criarCliente();
        else if (e.getSource() == cadastroView.getBtBuscar())
            buscarEnderecoPorCEP();
        else if (e.getSource() == window.getItemRelatorioCliente())
            gerarRelatorioCliente();
        else if (e.getSource() == window.getItemExportaCliente())
            gerarJSONCliente();
    }

    /**
     * Exporta a lista de clientes no formato JSON
     */
    private void gerarJSONCliente() {
        try {
            var result = exportacaoService.exportarJSON();

            if (result.isFailure()) {
                JOptionPane.showMessageDialog(cadastroView, "Erro na exportação do JSON", "Exportação de Clientes", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(cadastroView, "Erro na exportação do JSON: " + ex.getMessage(), "Exportação de Clientes", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gera um relatório em PDF com todos os clientes
     */
    private void gerarRelatorioCliente() {
        try {
            var result = relatorioService.gerarRelatorio();

            if (result.isFailure()) {
                JOptionPane.showMessageDialog(cadastroView, "Erro na geração do relatório", "Relatório de Clientes", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(cadastroView, "Erro na geração do relatório: " + ex.getMessage(), "Relatório de Clientes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Busca o endereço pelo CEP
     */
    private void buscarEnderecoPorCEP() {
    	
    	try {
    		// Converte o CEP
    		Integer cep = Integer.parseInt(cadastroView.getCEP());
    		
    		var result = cepService.buscaPorCEP(cep);
        	
    		// Se resultado é nulo, então não conseguiu executar o serviço
    		if (result == null) {
    			cadastroView.notifyCEPError();
    		}
    		else if (result.isSuccess()) {
        		var dados = result.payload();
        		 
        		cadastroView.notifyCEPExist(dados);
        	}
        	else if (result.isFailure())
        		cadastroView.notifyCEPNotExist();
    	} catch(Exception ex) {
    		// CEP inválido
    		cadastroView.notifyCEPIsInvalid();
    	}
	}

    /**
     * Cadastra um novo cliente
     */
	private void criarCliente() {
    	
    	Long cpf;
    	LocalDate dtNasc;
    	Integer numero, cep, ddd, numeroTel;
    	
    	// Converte os dados da VIEW para o DTO
    	
    	// Converte o CPF
    	try {
    		cpf = Long.parseLong(cadastroView.getCpf());
    	} catch(Exception ex) {
    		cpf =  null;
    	}
    	
    	// Converte a data de nascimento
    	try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
    		dtNasc = LocalDate.parse(cadastroView.getDtNasc(), formatter);
    	} catch(Exception ex) {
    		dtNasc = null;
    	}
    	
    	// Converte o número
    	try {
    		numero = Integer.parseInt(cadastroView.getNumero());
    	} catch(Exception ex) {
    		numero =  null;
    	}
    	
    	// Converte o CEP
    	try {
    		cep = Integer.parseInt(cadastroView.getCEP());
    	} catch(Exception ex) {
    		cep =  null;
    	}
    	
    	// Converte o DDD
		if (! cadastroView.getDDD().isBlank())
			try {
				ddd = Integer.parseInt(cadastroView.getDDD());
			} catch(Exception ex) {
	    		ddd =  0;
	    	}
		else
			ddd = null;
    	
    	// Converte o número de telefone
    	try {
    		numeroTel = Integer.parseInt(cadastroView.getNumeroTel());
    	} catch(Exception ex) {
    		numeroTel =  null;
    	}
    	
    	// Cria o DTO 
    	var dto = new CadastroClienteRequest(cpf, 
    			                             cadastroView.getNome(),
    			                             dtNasc, 
    			                             cadastroView.getLogradouro(),
    			                             numero, 
    			                             cadastroView.getComplemento(),
    			                             cadastroView.getBairro(),
    			                             cadastroView.getCidade(),
    			                             cep, 
    			                             cadastroView.getSiglaUF(),
    			                             ddd, 
    			                             numeroTel);
    	
    	// Solicita ao controller do UC que cadastre o novo cliente
    	var result = cadastroService.criarCliente(dto);
    	
    	// Avisa a VIEW sobre o sucesso ou fracasso da operação
    	if (result.isFailure())
    		cadastroView.notifyErrors(result.payload());
    	else
    		cadastroView.notifySuccess();
    }
   
}
