package cashflow.document;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @GetMapping("api/documents")
    public List<DocumentAPI> showAll() {
        List<Document> result = documentService.showAll();
        return result.stream().map(documentMapper::modelToApi).toList();
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

}
