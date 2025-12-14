package adapters.output.exporter.relatorio;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Cliente;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Adaptador concreto para geração de relatório em PDF
 */
public class PDFRelatorioExporter implements IRelatorioExporter {

    @Override
    public void exportar(List<Cliente> clientes) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        String tmp = System.getProperty("java.io.tmpdir");
        File out = new File(tmp, "clientes.pdf");

        try (FileOutputStream fos = new FileOutputStream(out)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("Relatório de Clientes", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{15f, 25f, 15f, 30f, 15f});
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            String[] headers = {"CPF", "Nome", "Data de nascimento", "Endereço", "Telefone"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorder(Rectangle.BOX);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(header);
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Cliente c : clientes) {
                String cpf = formatCPF(c.getCpf());
                String nome = c.getNome() == null ? "" : c.getNome();
                String data = c.getDataNasc() == null ? "" : c.getDataNasc().format(df);
                String endereco = formatEndereco(c.getEndereco());
                String telefone = formatTelefone(c.getTelefone());

                PdfPCell cpfCell = new PdfPCell(new Phrase(cpf));
                cpfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cpfCell.setBorder(Rectangle.BOX);

                PdfPCell nomeCell = new PdfPCell(new Phrase(nome));
                nomeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nomeCell.setBorder(Rectangle.BOX);

                PdfPCell dataCell = new PdfPCell(new Phrase(data));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataCell.setBorder(Rectangle.BOX);

                PdfPCell endCell = new PdfPCell(new Phrase(endereco));
                endCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                endCell.setBorder(Rectangle.BOX);

                PdfPCell telCell = new PdfPCell(new Phrase(telefone));
                telCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                telCell.setBorder(Rectangle.BOX);

                table.addCell(cpfCell);
                table.addCell(nomeCell);
                table.addCell(dataCell);
                table.addCell(endCell);
                table.addCell(telCell);
            }

            document.add(table);
            document.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(out);
            }

        } catch (Exception e) {
            if (document.isOpen())
                document.close();
            throw e;
        }
    }

    private String formatCPF(model.CPF cpf) {
        if (cpf == null || cpf.numero() == null) return "";
        String s = String.format("%011d", cpf.numero());
        return s.substring(0, 3) + "." + s.substring(3, 6) + "." + s.substring(6, 9) + "-" + s.substring(9);
    }

    private String formatEndereco(model.Endereco e) {
        if (e == null) return "";
        StringBuilder sb = new StringBuilder();
        if (e.getLogradouro() != null) sb.append(e.getLogradouro());
        if (e.getNumero() != null) sb.append(", " + e.getNumero());
        if (e.getComplemento() != null && !e.getComplemento().isBlank()) sb.append(", " + e.getComplemento());
        sb.append("\n");
        if (e.getBairro() != null) sb.append(e.getBairro());
        sb.append("\n");
        if (e.getCidade() != null) sb.append(e.getCidade());
        sb.append(" - ");
        if (e.getUf() != null) sb.append(e.getUf().getSigla());
        sb.append("\n");
        if (e.getCEP() != null) {
            String scep = String.format("%08d", e.getCEP());
            sb.append(scep.substring(0, 5) + "-" + scep.substring(5));
        }
        return sb.toString();
    }

    private String formatTelefone(model.Telefone t) {
        if (t == null || t.getNumero() == null || t.getDdd() == null) return "";
        String num = String.valueOf(t.getNumero());
        int n = num.length();
        int leftLen = n > 4 ? n - 4 : 0;
        String left = leftLen > 0 ? num.substring(0, leftLen) : "";
        String right = leftLen > 0 ? num.substring(leftLen) : num;
        return "(" + t.getDdd() + ") " + left + (left.isEmpty() ? "" : "-") + right;
    }
}
