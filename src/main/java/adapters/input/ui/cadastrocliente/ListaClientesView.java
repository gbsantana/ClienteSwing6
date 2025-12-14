package adapters.input.ui.cadastrocliente;

import adapters.output.presenter.html.ClientesHTMLPresenter;
import services.application.cliente.CadastroClientePort;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import model.Cliente;

public class ListaClientesView extends JPanel {

    private final JEditorPane pnListaClientes;
    private final CadastroClientePort cadastroService;

    public ListaClientesView(CadastroClientePort cadastroService) {

        this.cadastroService = cadastroService;

        setLayout(new BorderLayout());

        pnListaClientes = new JEditorPane();
        pnListaClientes.setContentType("text/html");
        pnListaClientes.setEditable(false);

        add(new JScrollPane(pnListaClientes), BorderLayout.CENTER);
    }

    public void apresentarDados() {
        var result = cadastroService.listarClientes();

        if (result == null || result.isFailure() || result.payload() == null) {
            String html = "<html><body><table border='1'><tr><td colspan='5'>Erro ao recuperar lista de clientes</td></tr></table></body></html>";
            pnListaClientes.setText(html);
        } else {
            List<Cliente> clientes = result.payload();
            String html = ClientesHTMLPresenter.gerarTabelaHTML(clientes);
            pnListaClientes.setText(html);
        }
    }
}
