package in.strikes.bookmyshow.controller;

import in.strikes.bookmyshow.dto.request.ScreenRequest;
import in.strikes.bookmyshow.dto.response.ScreenResponse;
import in.strikes.bookmyshow.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenResponse> create(@Valid @RequestBody ScreenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(screenService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(screenService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        screenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
