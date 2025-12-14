package adapters.input.ui.cadastrocliente;

import services.application.cep.CEPPort;
import services.application.cliente.CadastroClientePort;
import services.application.relatorio.RelatorioClientePort;
import services.application.exportacao.ExportacaoClientePort;
import services.application.uf.UFPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {

    public final static int WIDTH = 1100;
    public final static int HEIGHT = 400;

    private final CadastroClientePrt presenter;

    private JPanel painelAtual;
    private final CadastroClienteView cadastroClientePanel;
    private final ListaClientesView listaClientesPanel;

    private JMenuItem itemCriaCliente;
    private JMenuItem itemListaClientes;
    private JMenuItem itemRelatorioCliente;
    private JMenuItem itemExportaCliente;

    public MainWindow(CadastroClientePort cadastroService, CEPPort cepService, UFPort ufService, RelatorioClientePort relatorioService, ExportacaoClientePort exportacaoService) {
        super();

        // Cria a view para cadastro de clientes injetando o serviço da UF
        cadastroClientePanel = new CadastroClienteView(ufService);

        // Cria a view para listagem de clientes (injeta o serviço de cadastro)
        listaClientesPanel = new ListaClientesView(cadastroService);

        // Cria o presenter que vai tratar os eventos do cadastro
        presenter = new CadastroClientePrt(this, cadastroClientePanel, cadastroService, cepService, relatorioService, exportacaoService);

        setJMenuBar(criaMenu());

        setTitle("Cadastro de Clientes - versão 6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);    // Define o tamanho da janela
        setLocationRelativeTo(null);             // Centraliza em relação à tela
        setResizable(false);                     // Não pode redimensionar
        setVisible(true);
    }

    public JMenuItem getItemRelatorioCliente() {
        return itemRelatorioCliente;
    }

    public JMenuItem getItemExportaCliente() {
        return itemExportaCliente;
    }

    public void fechaPainel() {
        mostraPainel(null, null);
    }

    private JMenuBar criaMenu() {
        var menuBar = new JMenuBar();
        var menuCliente = new JMenu("Cliente");

        itemCriaCliente = new JMenuItem("Criar");
        itemListaClientes = new JMenuItem("Listar");
        itemRelatorioCliente = new JMenuItem("Relatório");
        itemExportaCliente = new JMenuItem("Exportar");

        itemCriaCliente.addActionListener(this);
        itemListaClientes.addActionListener(this);
        itemRelatorioCliente.addActionListener(presenter);
        itemExportaCliente.addActionListener(presenter);

        menuCliente.add(itemCriaCliente);
        menuCliente.add(itemListaClientes);
        menuCliente.add(itemRelatorioCliente);
        menuCliente.add(itemExportaCliente);

        menuBar.add(menuCliente);

        return menuBar;
    }

    private void mostraPainel(JPanel painel, String position) {
        if (painelAtual != null) {
            painelAtual.setVisible(false);
            remove(painelAtual);
        }

        painelAtual = painel;

        if (painelAtual != null) {
            getContentPane().add(painelAtual, position);
            painelAtual.setVisible(true);

            // Importante para garantir que o painel ficará visível.
            painelAtual.revalidate();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var item = (JMenuItem) e.getSource();

        if (item == itemCriaCliente) {
            JPanel p = new JPanel(new BorderLayout());
            p.add(cadastroClientePanel, BorderLayout.NORTH);
            mostraPainel(p, BorderLayout.WEST);
        }
        else if (item == itemListaClientes) {
            listaClientesPanel.apresentarDados();
            mostraPainel(listaClientesPanel, BorderLayout.CENTER);
        }
    }
}
