package com.seven.morningstar.voyage;

import com.seven.morningstar.responses.Response;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.seven.morningstar.util.Responder.created;
import static com.seven.morningstar.util.Responder.ok;

@RestController
@RequestMapping(value = "/administrator/voyage", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "jwtAuth")

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

    @PostMapping(path="/create", produces = {"application/json" , "application/xml"}, consumes = {"application/json" , "application/xml"})
    public ResponseEntity<Response> createResource(@Valid @RequestBody VoyageCreateRequest request) {
//       Set<VoyageRecord> records = Set.of(voyageService.create(request));
//       EntityModel<ResponseEntity<Response>> entityModel = okHal(records);
//       Map <String, Object> refToInvocationObjectMap = Map.of("view_all",methodOn(this.getClass()).getAllResources());
//       createAndIncludeLinks(refToInvocationObjectMap, entityModel);
//       return entityModel;
        return created(voyageService.create(request), "/administrator/voyage");

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