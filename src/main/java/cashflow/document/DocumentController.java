package cashflow.document;

import cashflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;
    private final ObjectMapper objectMapper;

    @GetMapping("api/documents")
    public List<DocumentAPI> showAll() {
        List<Document> result = documentService.showAll();
        return result.stream().map(documentMapper::modelToApi).toList();
    }

    @GetMapping("api/documents/{id}")
    public DocumentAPI findDocumentById(@PathVariable Long id) {
        var result = documentService.findById(id);
        return documentMapper.modelToApi(result);
    }

    @PostMapping("api/documents")
    public DocumentAPI addDocument(@RequestBody DocumentAPI documentAPI) {
        Document newDocument = documentMapper.apiToModel(documentAPI);
        var result = documentService.addDocument(newDocument);
        return documentMapper.modelToApi(result);
    }

    @DeleteMapping("api/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("api/documents/{id}")
    public ResponseEntity<Void> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        documentService.updateDocument(id, document);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PatchMapping(value = "api/documents/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> partialUpdateDocument(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        Optional<Document> documentOptional = Optional.ofNullable(documentService.findById(id));

        if (documentOptional.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }
        try {
            Document doc = applyPatchToDocument(jsonPatch, documentOptional.get());
            documentService.partialUpdateDocument(doc,id);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    private Document applyPatchToDocument(JsonPatch patch, Document document) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(document, JsonNode.class));
        return objectMapper.treeToValue(patched, Document.class);
    }
}
