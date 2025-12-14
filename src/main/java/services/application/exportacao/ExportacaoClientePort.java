package services.application.exportacao;

import services.application.RequestResult;

/**
 * Interface (output port) que define o contrato para exportação de clientes em JSON
 */
public interface ExportacaoClientePort {

    /**
     * Exporta todos os clientes do BD em formato JSON, ordenados por CPF
     *
     * @return Resultado da operação
     */
    RequestResult<Void> exportarJSON();
}
