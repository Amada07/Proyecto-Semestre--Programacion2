/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;
import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import com.umg.bienestar.sesiones_bienestar.service.IClienteService;
import com.umg.bienestar.sesiones_bienestar.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author amada
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Gestión de Clientes", 
     description = "CRUD completo con validaciones usando HashMap y ArrayList - Principios SOLID aplicados")
public class ClienteController {
    
    private final IClienteService clienteService;
    
    // DIP - Inyección de dependencias
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @Operation(summary = "Crear cliente", 
               description = "Crea un nuevo cliente con validaciones de unicidad y formato")
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Cliente cliente) {
        try {
            Cliente clienteCreado = clienteService.crear(cliente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente creado exitosamente con validaciones completas");
            response.put("data", clienteCreado);
            response.put("validaciones", "Email único, formato DPI, formato teléfono");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (ValidationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("campo", e.getCampo());
            response.put("valor", e.getValor());
            response.put("errorCode", "VALIDATION_ERROR");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @Operation(summary = "Listar todos los clientes", 
               description = "Obtiene todos los clientes desde ArrayList - demuestra CRUD con colecciones")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Datos obtenidos desde ArrayList");
        response.put("data", clientes);
        response.put("total", clientes.size());
        response.put("colecciones", "HashMap (búsqueda O(1)) + ArrayList (listados)");
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Buscar cliente por ID", 
               description = "Búsqueda rápida O(1) usando HashMap")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        Map<String, Object> response = new HashMap<>();
        
        if (cliente.isPresent()) {
            response.put("success", true);
            response.put("message", "Cliente encontrado usando HashMap O(1)");
            response.put("data", cliente.get());
            response.put("rendimiento", "Búsqueda por ID en tiempo constante O(1)");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Cliente no encontrado con ID: " + id);
            response.put("errorCode", "NOT_FOUND");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @Operation(summary = "Buscar cliente por email", 
               description = "Demuestra validación de unicidad - HashMap por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> buscarPorEmail(
            @Parameter(description = "Email del cliente") @PathVariable String email) {
        
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        Map<String, Object> response = new HashMap<>();
        
        if (cliente.isPresent()) {
            response.put("success", true);
            response.put("message", "Cliente encontrado - demuestra unicidad de email");
            response.put("data", cliente.get());
            response.put("unicidad", "Email único garantizado por HashMap");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Cliente no encontrado con email: " + email);
            response.put("errorCode", "NOT_FOUND");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @Operation(summary = "Actualizar cliente", 
               description = "Actualiza con validaciones y notificaciones - SRP + OCP + DIP")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id, @RequestBody Cliente cliente) {
        
        try {
            cliente.setId(id);
            Cliente clienteActualizado = clienteService.actualizar(cliente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente actualizado con principios SOLID");
            response.put("data", clienteActualizado);
            response.put("principios", Arrays.asList(
                "SRP: Validador separado", 
                "OCP: Notificaciones extensibles", 
                "DIP: Inyección de dependencias"
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (ValidationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("errorCode", "VALIDATION_ERROR");
            
            return ResponseEntity.badRequest().body(response);
            
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("errorCode", "NOT_FOUND");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @Operation(summary = "Eliminar cliente", 
               description = "Elimina con validaciones de negocio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        try {
            clienteService.eliminar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cliente eliminado exitosamente de todas las colecciones");
            response.put("colecciones", "Eliminado de HashMap por ID + HashMap por email + ArrayList");
            
            return ResponseEntity.ok(response);
            
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("errorCode", "NOT_FOUND");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (BusinessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("errorCode", "BUSINESS_RULE_VIOLATION");
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    
    @Operation(summary = "Demo de principios implementados", 
               description = "Endpoint que demuestra todos los principios SOLID aplicados")
    @GetMapping("/demo/solid")
    public ResponseEntity<Map<String, Object>> demostracionSOLID() {
        Map<String, Object> demo = new HashMap<>();
        demo.put("CRUD_Colecciones", "HashMap + ArrayList implementados");
        demo.put("Validaciones", Arrays.asList(
            "Unicidad de email",
            "Formato de email (regex)",
            "Formato de DPI (8-13 dígitos)",
            "Formato de teléfono (8-15 dígitos)",
            "Campos obligatorios"
        ));
        demo.put("Principios_SOLID", Map.of(
            "SRP", "ValidadorCliente - solo responsabilidad de validar",
            "OCP", "Sistema notificaciones - extensible sin modificar código",
            "LSP", "Interfaces sustituibles",
            "ISP", "Interfaces específicas por responsabilidad",
            "DIP", "Depende de abstracciones, no implementaciones"
        ));
        demo.put("totalClientes", clienteService.contarClientes());
        
        return ResponseEntity.ok(demo);
    }
}
