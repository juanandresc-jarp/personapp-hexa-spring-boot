package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

    @Autowired
    private PersonaInputAdapterRest personaInputAdapterRest;

    // GET: Obtener todas las personas
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@PathVariable String database) {
        log.info("GET /api/v1/persona/{}", database);
        return ResponseEntity.ok(personaInputAdapterRest.historial(database));
    }

    // GET: Obtener persona por ID
    @GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOne(@PathVariable String database, @PathVariable int id) {
        log.info("GET /api/v1/persona/{}/{}", database, id);
        return personaInputAdapterRest.obtenerPersona(database, id);
    }

    // POST: Crear nueva persona
    @PostMapping(path = "/{database}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@PathVariable String database, @RequestBody PersonaRequest request) {
        log.info("POST /api/v1/persona/{}", database);
        return personaInputAdapterRest.crearPersona(request, database);
    }

    // PUT: Actualizar persona existente
    @PutMapping(path = "/{database}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String database, @PathVariable int id, @RequestBody PersonaRequest request) {
        log.info("PUT /api/v1/persona/{}/{}", database, id);
        return personaInputAdapterRest.actualizarPersona(database, id, request);
    }

    // DELETE: Eliminar persona por ID
    @DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable String database, @PathVariable int id) {
        log.info("DELETE /api/v1/persona/{}/{}", database, id);
        return personaInputAdapterRest.eliminarPersona(database, id);
    }
}
