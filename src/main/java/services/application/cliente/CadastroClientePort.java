package services.application.cliente;

import model.ErroCliente;
import model.Cliente;
import services.application.RequestResult;
import services.domain.persistence.ClienteDTO;

import java.util.List;

/**
 * Interface que define o serviço de cadastro de clientes
 */
public interface CadastroClientePort {

	RequestResult<List<ErroCliente>> criarCliente(CadastroClienteRequest dto);

	RequestResult<List<Cliente>> listarClientes();
}