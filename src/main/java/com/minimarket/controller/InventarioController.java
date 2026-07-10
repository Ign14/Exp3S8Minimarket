package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
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
 * Endpoints de inventario. Disponible para CAJERO y ADMINISTRADOR.
 * Respuestas con enlaces HATEOAS; registra entradas/salidas y ajusta el stock.
 */
@RestController
@RequestMapping("/api/inventario")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Inventario", description = "Registro y consulta de movimientos de stock")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    private EntityModel<Inventario> toModel(Inventario inv) {
        return EntityModel.of(inv,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(inv.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarMovimientos()).withRel("inventario"));
    }

    @Operation(summary = "Listar movimientos de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarMovimientos() {
        List<EntityModel<Inventario>> movimientos = inventarioService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(movimientos,
                linkTo(methodOn(InventarioController.class).listarMovimientos()).withSelfRel());
    }

    @Operation(summary = "Obtener movimiento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null) ? ResponseEntity.ok(toModel(inventario)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar movimiento de inventario",
            description = "Registra una entrada o salida asociada a un producto y ajusta su stock.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento registrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (sin producto o tipo invalido)"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente para la salida")
    })
    @PostMapping
    public EntityModel<Inventario> registrarMovimiento(@RequestBody Inventario inventario) {
        return toModel(inventarioService.registrarMovimiento(inventario));
    }

    @Operation(summary = "Eliminar movimiento de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
