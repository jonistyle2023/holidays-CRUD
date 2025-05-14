package com.example.myproject;

import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.*;
import java.util.ArrayList;
import java.util.List;

import static io.helidon.http.Status.*;

/* Para esta parte pensaba darle un enfoque similar al de spring boot, separando la lógica con una segunda clase
 * llamada FeriadoController, pero viendo que en helidon, no es obligatorio dividir, mejor opto por realizarlo
 * en una sola clase que combine:
 * - lógica de negocio simple (con almacenamiento en memoria)
 * - Además del enrutamiento HTTP */

public class FeriadoService implements HttpService {
    // Lista en memoria para almacenar feriados
    private static final List<Feriado> feriados = new ArrayList<>();

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::handleGetAll)
                .get("/{index}", this::handleGetByIndex)
                .post("/", this::handleCreate)
                .put("/{index}", this::handleUpdate)
                .delete("/{index}", this::handleDelete);
    }

    private void handleGetAll(ServerRequest req, ServerResponse res) {
        res.send(feriados);
    }

    private void handleGetByIndex(ServerRequest req, ServerResponse res) {
        int index = Integer.parseInt(req.path().pathParameters().get("index"));
        if (index >= 0 && index < feriados.size()) {
            res.send(feriados.get(index));
        } else {
            res.status(NOT_FOUND_404).send("Feriado no encontrado");
        }
    }

    private void handleCreate(ServerRequest req, ServerResponse res) {
        try {
            Feriado feriado = req.content().as(Feriado.class);
            feriados.add(feriado);
            res.status(CREATED_201).send("Feriado agregado correctamente");
        } catch (Exception ex) {
            res.status(BAD_REQUEST_400).send("Error al crear el feriado: " + ex.getMessage());
        }
    }

    private void handleUpdate(ServerRequest req, ServerResponse res) {
        int index = Integer.parseInt(req.path().pathParameters().get("index"));
        if (index < 0 || index >= feriados.size()) {
            res.status(NOT_FOUND_404).send("Feriado no encontrado");
            return;
        }

        try {
            Feriado updated = req.content().as(Feriado.class);
            feriados.set(index, updated);
            res.send("Feriado actualizado");
        } catch (Exception ex) {
            res.status(BAD_REQUEST_400).send("Error al actualizar: " + ex.getMessage());
        }
    }

    private void handleDelete(ServerRequest req, ServerResponse res) {
        int index = Integer.parseInt(req.path().pathParameters().get("index"));
        if (index >= 0 && index < feriados.size()) {
            feriados.remove(index);
            res.send("Feriado eliminado");
        } else {
            res.status(NOT_FOUND_404).send("Feriado no encontrado");
        }
    }
}
