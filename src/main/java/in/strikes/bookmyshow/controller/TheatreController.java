package in.strikes.bookmyshow.controller;

import in.strikes.bookmyshow.dto.request.TheatreRequest;
import in.strikes.bookmyshow.dto.response.TheatreResponse;
import in.strikes.bookmyshow.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping
    public ResponseEntity<TheatreResponse> create(@Valid @RequestBody TheatreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreResponse> update(@PathVariable Long id, @Valid @RequestBody TheatreRequest request) {
        return ResponseEntity.ok(theatreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        theatreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TheatreResponse>> getByCity(@RequestParam String city) {
        return ResponseEntity.ok(theatreService.getByCity(city));
    }
}
