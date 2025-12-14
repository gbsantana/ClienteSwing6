package services.application.relatorio;

import services.application.RequestResult;

/**
 * Interface (output port) que define o contrato para geração de relatórios de clientes
 */
public interface RelatorioClientePort {

    /**
     * Gera um relatório com todos os clientes do BD ordenados por CPF
     *
     * @return Resultado da operação
     */
    RequestResult<Void> gerarRelatorio();
}
