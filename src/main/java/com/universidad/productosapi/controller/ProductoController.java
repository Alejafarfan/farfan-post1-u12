package com.universidad.productosapi.controller;

import com.universidad.productosapi.model.Producto;
import com.universidad.productosapi.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository repo;

    public ProductoController(ProductoRepository repo) {
        this.repo = repo;
    }

    // GET /api/productos — listar todos
    @GetMapping
    public List<Producto> listar() {
        return repo.findAll();
    }

    // GET /api/productos/{id} — buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/productos — crear
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto guardado = repo.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT /api/productos/{id} — actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return repo.findById(id).map(p -> {
            p.setNombre(datos.getNombre());
            p.setDescripcion(datos.getDescripcion());
            p.setPrecio(datos.getPrecio());
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/productos/{id} — eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
