package adapters.output.exporter.json;

/**
 * Factory para criação de exportadores JSON
 */
public class JSONExporterFactory {

    public static IJSONExporter criar() {
        return new JSONClienteExporter();
    }
}
