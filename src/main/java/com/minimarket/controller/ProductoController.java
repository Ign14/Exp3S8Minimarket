package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Endpoints de Producto. Consulta publica; crear/editar/eliminar solo ADMINISTRADOR.
 * Las respuestas incluyen enlaces HATEOAS para navegar entre recursos.
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestion del catalogo de productos del minimarket")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Construye el modelo HATEOAS de un producto con enlace propio y a la coleccion.
    private EntityModel<Producto> toModel(Producto p) {
        return EntityModel.of(p,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(p.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));
    }

    @Operation(summary = "Listar productos",
            description = "Devuelve el catalogo completo de productos con enlaces HATEOAS. Endpoint publico.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente"))
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(this::toModel).toList();
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }

    @Operation(summary = "Obtener producto por ID",
            description = "Devuelve un producto especifico con enlaces HATEOAS. Endpoint publico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return (producto != null)
                ? ResponseEntity.ok(toModel(producto))
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto. Requiere rol ADMINISTRADOR.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (no es ADMINISTRADOR)")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public EntityModel<Producto> guardarProducto(@RequestBody Producto producto) {
        return toModel(productoService.save(producto));
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente. Requiere rol ADMINISTRADOR.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto existente = productoService.findById(id);
        if (existente != null) {
            producto.setId(id);
            return ResponseEntity.ok(toModel(productoService.save(producto)));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto. Requiere rol ADMINISTRADOR.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
