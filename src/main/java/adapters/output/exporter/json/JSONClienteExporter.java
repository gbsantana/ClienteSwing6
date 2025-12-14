package adapters.output.exporter.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import model.Cliente;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/**
 * Adaptador concreto para exportação em JSON
 */
public class JSONClienteExporter implements IJSONExporter {

    @Override
    public void exportar(List<Cliente> clientes) throws Exception {
        JsonArray arr = new JsonArray();

        for (Cliente c : clientes) {
            JsonObject jo = new JsonObject();

            // cpf
            JsonObject cpfObj = new JsonObject();
            if (c.getCpf() != null && c.getCpf().numero() != null)
                cpfObj.addProperty("numero", c.getCpf().numero());
            else
                cpfObj.add("numero", JsonNull.INSTANCE);
            jo.add("cpf", cpfObj);

            // nome
            jo.addProperty("nome", c.getNome());

            // dataNasc in ISO (yyyy-MM-dd)
            if (c.getDataNasc() != null)
                jo.addProperty("dataNasc", c.getDataNasc().toString());
            else
                jo.add("dataNasc", JsonNull.INSTANCE);

            // endereco
            JsonObject endObj = new JsonObject();
            model.Endereco e = c.getEndereco();
            if (e != null) {
                endObj.addProperty("logradouro", e.getLogradouro());
                if (e.getNumero() != null) endObj.addProperty("numero", e.getNumero());
                else endObj.add("numero", JsonNull.INSTANCE);
                endObj.addProperty("complemento", e.getComplemento());
                endObj.addProperty("bairro", e.getBairro());
                endObj.addProperty("cidade", e.getCidade());
                if (e.getCEP() != null) endObj.addProperty("cep", e.getCEP());
                else endObj.add("cep", JsonNull.INSTANCE);
                endObj.addProperty("uf", e.getUf() == null ? null : e.getUf().getSigla());
            }
            jo.add("endereco", endObj);

            // telefone
            JsonObject telObj = new JsonObject();
            model.Telefone t = c.getTelefone();
            if (t != null) {
                if (t.getDdd() != null) telObj.addProperty("ddd", t.getDdd());
                else telObj.add("ddd", JsonNull.INSTANCE);
                if (t.getNumero() != null) telObj.addProperty("numero", t.getNumero());
                else telObj.add("numero", JsonNull.INSTANCE);
            }
            jo.add("telefone", telObj);

            // id
            if (c.getId() != null)
                jo.addProperty("id", c.getId().toString());
            else
                jo.add("id", JsonNull.INSTANCE);

            arr.add(jo);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(arr);

        String tmp = System.getProperty("java.io.tmpdir");
        File out = new File(tmp, "clientes.json");

        try (Writer writer = new BufferedWriter(new FileWriter(out))) {
            writer.write(json);
        }

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(out);
        }
    }
}
