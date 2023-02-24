package com.seven.ije.voyage;

import com.seven.ije.responses.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import static com.seven.ije.util.Responder.ok;
@RestController
@RequestMapping("/administrator/voyage")
public class VoyageController {
    VoyageService voyageService;
    public VoyageController(VoyageService voyageService) {
        this.voyageService = voyageService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllResources() {
        return ok(voyageService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@Valid @RequestParam(name = "id") UUID voyageNo) {
        return ok(Set.of(voyageService.get(voyageNo)));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createResource(@Valid @RequestBody VoyageCreateRequest request) {
       return ok(Set.of(voyageService.create(request)));
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody VoyageUpdateRequest request) {
       return ok(Set.of(voyageService.update(request)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@Valid @RequestParam(name = "id") UUID voyageNo){
        voyageService.delete(voyageNo);
        return ok(Collections.emptySet());
    }
}