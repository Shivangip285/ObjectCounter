package com.example.ObjectCounter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping()
@CrossOrigin(origins = "http://localhost:3000")
public class ObjectCountController {

    public final ObjectCountService objectCountService;

    @GetMapping("/getCount/{id}")
    public ObjectCount getCount(@PathVariable (value = "id") Integer id) {
//        RestTemplate restTemplate=new RestTemplate();
//        restTemplate.getForEntity("/id",ObjectCounter);

        //return objectRepository.findByObjectName(objectName);
//        try {
//            Optional<ObjectCount> byId = objectRepository.findById(id);
//
//        }
//        catch(NoSuchElementException e){
//            throw new NoSuchElementException();
//        }
//        return objectRepository.findById(id).get();


//		Employee employee = employeeRepository.findById(employeeId)
//				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
//		return ResponseEntity.ok().body(employee);


//        if(byId.isEmpty()) {
//            System.out.println("abc");
//            throw new NoSuchElementException();
//        }

        return objectCountService.getObjectById(id).get();

    }
    //@GetMapping("/{id}")
    @Operation(summary = "Get all object count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the object",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Counter.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid object",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No object found",
                    content = @Content) })

    @GetMapping("/getAllCounter")
    public List<ObjectCount> getAllCounter() {

//        RestTemplate restTemplate=new RestTemplate();
//        restTemplate.getForObject("http://localhost:8080/getAllCounter",ObjectCount.class);

        return objectCountService.getAllObjectCounter();

    }


    public ObjectCountController(ObjectCountService objectCountService) {
        this.objectCountService = objectCountService;
    }

    @PostMapping("/createCount")
    public ObjectCount createCount(@Valid @RequestBody ObjectCount objectCount1) throws InstantiationException {
        return objectCountService.createObjectCount(objectCount1);
    }

    @PutMapping("/incrementCount/{id}")
    public ObjectCount incrementCount(@PathVariable(value="id") Integer id ){
        return objectCountService.incrementObjectCount(id);
    }
    @PutMapping("/decrementCount/{id}")
    public ObjectCount decrementCount(@PathVariable(value="id") Integer id ){
        return objectCountService.decrementObjectCount(id);
    }
    @DeleteMapping("/deleteCounter/{id}")
    public ObjectCount deleteCounter(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        return objectCountService.deleteObjectCounter(id);
    }
}
