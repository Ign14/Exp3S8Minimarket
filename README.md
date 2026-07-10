# MiniMarket Plus - Backend con OpenAPI (Swagger) y HATEOAS (Exp3 - Semana 8)

Backend REST de MiniMarket Plus documentado con **OpenAPI/Swagger** e **HATEOAS**,
manteniendo la seguridad JWT y las pruebas unitarias de semanas anteriores.

## Tecnologías

- Java 17 (probado en Java 24), Spring Boot 3.4.1
- springdoc-openapi 2.7.0 (OpenAPI 3 + Swagger UI)
- spring-boot-starter-hateoas (EntityModel / CollectionModel)
- Spring Security + JWT (JJWT 0.12.6)
- Spring Data JPA + H2 (en memoria)
- JUnit 5, Mockito, spring-security-test — **64 pruebas, 0 fallos**

## Cómo ejecutar

```bash
./mvnw spring-boot:run
```

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

Usuarios de prueba (creados al iniciar): admin/admin123 (ADMINISTRADOR),
cajero/cajero123 (CAJERO), cliente/cliente123 (CLIENTE).

## Documentación OpenAPI

Todos los controladores están anotados con `@Tag`, `@Operation` y `@ApiResponses`
(9 tags, 17 rutas). El esquema de seguridad JWT (`bearerAuth`) está declarado, por lo
que Swagger UI muestra el botón **Authorize** para probar los endpoints protegidos.
Para autorizarte: haz login en `POST /api/auth/login`, copia el `token` y pégalo en
Authorize (formato Bearer).

## HATEOAS

Los endpoints de **Producto, Carrito, Inventario, Usuario y Venta** devuelven
`EntityModel` / `CollectionModel` con enlaces dinámicos. Ejemplo real (`GET /api/productos/1`):

```json
{
  "id": 1,
  "nombre": "Arroz 1kg",
  "precio": 1290.0,
  "stock": 50,
  "categoria": { "id": 1, "nombre": "Abarrotes" },
  "_links": {
    "self":      { "href": "http://localhost:8080/api/productos/1" },
    "productos": { "href": "http://localhost:8080/api/productos" }
  }
}
```

## Cómo ejecutar las pruebas

```bash
./mvnw test
```

Incluye pruebas de servicios (Mockito), seguridad (JWT, roles), autorización (MockMvc)
y una prueba dedicada (`DocumentacionApiTest`) que valida que `/v3/api-docs` responde,
que documenta el esquema JWT y que las respuestas incluyen enlaces HATEOAS.

## Estructura

```
src/main/java/com/minimarket
├── config       → OpenApiConfig (info + esquema JWT), DataInitializer
├── controller   → endpoints REST con @Operation y HATEOAS
├── entity       → entidades JPA
├── repository   → repositorios Spring Data JPA
├── service      → lógica de negocio (stock, inventario)
└── security     → JWT, filtro, roles, manejadores 401/403
```

## Validación con Postman

Exporta el contrato desde `http://localhost:8080/v3/api-docs` e impórtalo en Postman
(Import → Link/Raw) para generar la colección y probar los endpoints, incluyendo el
envío del token JWT en el header `Authorization: Bearer <token>`.

## Notas

- La seguridad JWT y los roles (CLIENTE, CAJERO, ADMINISTRADOR) se mantienen intactos.
- Las rutas de Swagger (`/swagger-ui/**`, `/v3/api-docs/**`) son públicas para poder
  consultar la documentación sin autenticación.
