package cashflow.register.receivable;

import cashflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReceivableController {

    private final ReceivableService receivableService;
    private final ReceivableMapper receivableMapper;
    private final ObjectMapper objectMapper;

    @GetMapping("api/receivables")
    public List<ReceivableAPI> showAll() {
        var list = receivableService.showAll();
        return list.stream().map(receivableMapper::modelToApi).toList();
    }

    @PatchMapping(value = "api/receivables/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> partialUpdateDocument(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        var doc = receivableService.findById(id);

        try {
            var document = applyPatchToDocument(jsonPatch, doc);
            receivableService.debtEnforcement(document);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    private Receivable applyPatchToDocument(JsonPatch patch, Receivable receivable) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(receivable, JsonNode.class));
        return objectMapper.treeToValue(patched, Receivable.class);
    }
}
