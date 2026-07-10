package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
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
 * Endpoints de ventas. Generar solo CAJERO o ADMINISTRADOR.
 * Respuestas con enlaces HATEOAS; valida stock de todos los productos.
 */
@RestController
@RequestMapping("/api/ventas")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ventas", description = "Generacion y consulta de ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    private EntityModel<Venta> toModel(Venta v) {
        return EntityModel.of(v,
                linkTo(methodOn(VentaController.class).obtenerVentaPorId(v.getId())).withSelfRel(),
                linkTo(methodOn(VentaController.class).listarVentas()).withRel("ventas"));
    }

    @Operation(summary = "Listar ventas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @GetMapping
    public CollectionModel<EntityModel<Venta>> listarVentas() {
        List<EntityModel<Venta>> ventas = ventaService.findAll().stream().map(this::toModel).toList();
        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel());
    }

    @Operation(summary = "Obtener venta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return (venta != null) ? ResponseEntity.ok(toModel(venta)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar venta",
            description = "Confirma una venta solo si todos los productos tienen stock suficiente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta registrada"),
            @ApiResponse(responseCode = "400", description = "Venta sin detalles"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    })
    @PostMapping
    public EntityModel<Venta> guardarVenta(@RequestBody Venta venta) {
        return toModel(ventaService.registrarVenta(venta));
    }
}
