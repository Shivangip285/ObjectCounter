package com.example.ObjectCounter.controller;

import com.example.ObjectCounter.Dto.ObjectCountDto;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.service.ObjectCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "http://localhost:3000")
public class ObjectCountController {

    public final ObjectCountService objectCountService;

    public ObjectCountController(ObjectCountService objectCountService) {
        this.objectCountService = objectCountService;
    }

    @GetMapping("/getCount/{id}")
    public ResponseEntity<ObjectCountDto> getCount(@PathVariable (value = "id") Integer id) {
        return ResponseEntity.ok().body(objectCountService.getObjectById(id).get());
    }

    @Operation(summary = "Get all object count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the object",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectCount.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid object",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No object found",
                    content = @Content) })

    @GetMapping("/getAllCounter")
    public List<ObjectCountDto> getAllCounterInAscCountOrder() {
        return objectCountService.getAllObjectCounter();
    }


    @PostMapping("/createCount")
    public ResponseEntity<ObjectCountDto> createCount(@Valid @RequestBody ObjectCount objectCount1) throws InstantiationException {
        return ResponseEntity.ok().body(objectCountService.createObjectCount(objectCount1));
    }

    @PutMapping("/incrementCount/{id}")
    public ResponseEntity<ObjectCountDto> incrementCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        return ResponseEntity.ok().body(objectCountService.incrementObjectCount(id));
    }
    @PutMapping("/decrementCount/{id}")
    public ResponseEntity<ObjectCountDto> decrementCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        return ResponseEntity.ok().body(objectCountService.decrementObjectCount(id));
    }
    @DeleteMapping("/deleteCounter/{id}")
    public ResponseEntity<ObjectCountDto> deleteCounter(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        return ResponseEntity.ok().body(objectCountService.deleteObjectCounter(id));
    }
}
