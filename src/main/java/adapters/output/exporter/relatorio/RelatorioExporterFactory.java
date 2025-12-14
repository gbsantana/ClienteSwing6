package adapters.output.exporter.relatorio;

/**
 * Factory para criação de exportadores de relatório
 */
public class RelatorioExporterFactory {

    public static IRelatorioExporter criar() {
        return new PDFRelatorioExporter();
    }
}
