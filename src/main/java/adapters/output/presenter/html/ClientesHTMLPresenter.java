package adapters.output.presenter.html;

import model.Cliente;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Adaptador para geração de HTML a partir de uma lista de clientes
 */
public class ClientesHTMLPresenter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String gerarTabelaHTML(List<Cliente> clientes) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><table border='1' style='border-collapse:collapse'>");
        builder.append("<tr><th>CPF</th><th>Nome</th><th>Data de Nascimento</th><th>Endereço</th><th>Telefone</th></tr>");

        for (Cliente c : clientes) {
            String cpf = formatCPF(c.getCpf());
            String nome = c.getNome() == null ? "" : c.getNome();
            String data = c.getDataNasc() == null ? "" : c.getDataNasc().format(DATE_FORMATTER);
            String endereco = formatEndereco(c.getEndereco());
            String telefone = formatTelefone(c.getTelefone());

            builder.append("<tr>");
            builder.append("<td>" + cpf + "</td>");
            builder.append("<td>" + nome + "</td>");
            builder.append("<td>" + data + "</td>");
            builder.append("<td>" + endereco + "</td>");
            builder.append("<td>" + telefone + "</td>");
            builder.append("</tr>");
        }

        builder.append("</table></body></html>");
        return builder.toString();
    }

    private static String formatCPF(model.CPF cpf) {
        if (cpf == null || cpf.numero() == null) return "";
        String s = String.format("%011d", cpf.numero());
        return s.substring(0, 3) + "." + s.substring(3, 6) + "." + s.substring(6, 9) + "-" + s.substring(9);
    }

    private static String formatCEP(Integer cep) {
        if (cep == null) return "";
        String s = String.format("%08d", cep);
        return s.substring(0, 5) + "-" + s.substring(5);
    }

    private static String formatEndereco(model.Endereco e) {
        if (e == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(e.getLogradouro() == null ? "" : e.getLogradouro());
        if (e.getNumero() != null) sb.append(", " + e.getNumero());
        if (e.getComplemento() != null && !e.getComplemento().isBlank()) sb.append(", " + e.getComplemento());
        sb.append("<br>");
        sb.append(e.getBairro() == null ? "" : e.getBairro());
        sb.append(" - ");
        sb.append(e.getCidade() == null ? "" : e.getCidade());
        sb.append("<br>");
        sb.append(formatCEP(e.getCEP()));
        sb.append(" - ");
        sb.append(e.getUf() == null ? "" : e.getUf().getSigla());
        return sb.toString();
    }

    private static String formatTelefone(model.Telefone t) {
        if (t == null || t.getNumero() == null || t.getDdd() == null) return "";
        String num = String.valueOf(t.getNumero());
        int n = num.length();
        int leftLen = n > 4 ? n - 4 : 0;
        String left = leftLen > 0 ? num.substring(0, leftLen) : "";
        String right = leftLen > 0 ? num.substring(leftLen) : num;
        return "(" + t.getDdd() + ") " + left + (left.isEmpty() ? "" : "-") + right;
    }
}
