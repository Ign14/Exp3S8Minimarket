package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
 * Endpoints del carrito. Disponible para CLIENTE, CAJERO y ADMINISTRADOR.
 * Respuestas con enlaces HATEOAS; valida stock al agregar productos.
 */
@RestController
@RequestMapping("/api/carrito")
@PreAuthorize("hasAnyRole('CLIENTE','CAJERO','ADMINISTRADOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    private EntityModel<Carrito> toModel(Carrito c) {
        return EntityModel.of(c,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(c.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"));
    }

    @Operation(summary = "Listar items del carrito", description = "Devuelve los items del carrito con enlaces HATEOAS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @GetMapping
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {
        List<EntityModel<Carrito>> items = carritoService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }

    @Operation(summary = "Obtener item del carrito por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return (carrito != null) ? ResponseEntity.ok(toModel(carrito)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Agregar producto al carrito",
            description = "Agrega un producto validando que haya stock suficiente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    })
    @PostMapping
    public EntityModel<Carrito> agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        return toModel(carritoService.agregarProducto(carrito));
    }

    @Operation(summary = "Eliminar item del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item eliminado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
