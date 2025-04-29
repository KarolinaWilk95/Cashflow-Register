package cashflow.document;

import cashflow.dao.DocumentFilterRequest;
import cashflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final static String WAY_OF_SORTING = "asc";

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;
    private final ObjectMapper objectMapper;


    @GetMapping("api/register/documents")
    public List<DocumentAPI> getAllDocuments(@RequestParam(name = "search", required = false) String search,
                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(name = "size", defaultValue = "30") Integer size,
                                             @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                             @RequestParam(name = "wayOfSorting", defaultValue = "asc") String WAY_OF_SORTING) {

        Sort sort = WAY_OF_SORTING.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Document> result;

        if (search != null) {
            result = documentService.getAllDocumentsByValue(pageable, search);
        } else {
            result = documentService.getAllDocuments(pageable);
        }


        return result.stream().map(documentMapper::modelToApi).toList();
    }

    @GetMapping("api/register/documents/{id}")
    public DocumentAPI getDocumentById(@PathVariable Long id) {
        var result = documentService.findById(id);
        return documentMapper.modelToApi(result);
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @PostMapping("api/register/documents")
    public DocumentAPI addDocument(@Valid @RequestBody DocumentAPI documentAPI) {
        Document newDocument = documentMapper.apiToModel(documentAPI);
        var result = documentService.addDocument(newDocument);
        return documentMapper.modelToApi(result);
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @DeleteMapping("api/register/documents/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @PutMapping("api/register/documents/{id}")
    public ResponseEntity<Void> updateDocumentById(@PathVariable Long id, @RequestBody Document document) {
        documentService.updateDocument(id, document);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @PatchMapping(value = "api/register/documents/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> partialUpdateDocument(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        Document documentFromRepository = documentService.findById(id);
        applyPatchToDocument(jsonPatch, documentFromRepository, id);

        return ResponseEntity.noContent().build();
    }

    private void applyPatchToDocument(JsonPatch patch, Document document, Long id) {
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(document, JsonNode.class));
            Document documentUpdated = objectMapper.treeToValue(patched, Document.class);
            documentService.partialUpdateDocument(documentUpdated, id);
        } catch (JsonPatchException | JsonProcessingException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ResourceNotFoundException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("api/register/documents/filter")
    public List<DocumentAPI> filter(@RequestParam(name = "documentGroup", required = false) String documentGroup,
                                    @RequestParam(name = "documentType", required = false) String documentType,
                                    @RequestParam(name = "documentNumber", required = false) String documentNumber,
                                    @RequestParam(name = "issueDate", required = false) LocalDate issueDate,
                                    @RequestParam(name = "dueDate", required = false) LocalDate dueDate,
                                    @RequestParam(name = "contractorName", required = false) String contractorName,
                                    @RequestParam(name = "contractorVatNumber", required = false) Long contractorVatNumber,
                                    @RequestParam(name = "amount", required = false) BigDecimal amount,
                                    @RequestParam(name = "lessThanAmount", required = false) BigDecimal lessThanAmount,
                                    @RequestParam(name = "greaterThanAmount", required = false) BigDecimal greaterThanAmount,
                                    @RequestParam(name = "totalAmount", required = false) BigDecimal totalAmount,
                                    @RequestParam(name = "lessThanTotalAmount", required = false) BigDecimal lessThanTotalAmount,
                                    @RequestParam(name = "greaterThanTotalAmount", required = false) BigDecimal greaterThanTotalAmount,
                                    @RequestParam(name = "currencyCode", required = false) String currencyCode,
                                    @RequestParam(name = "orderNumber", required = false) String orderNumber) {
        DocumentFilterRequest documentSearchRequest = new DocumentFilterRequest();
        documentSearchRequest.setDocumentGroup(documentGroup);
        documentSearchRequest.setDocumentType(documentType);
        documentSearchRequest.setDocumentNumber(documentNumber);
        documentSearchRequest.setIssueDate(issueDate);
        documentSearchRequest.setDueDate(dueDate);
        documentSearchRequest.setContractorName(contractorName);
        documentSearchRequest.setContractorVatNumber(contractorVatNumber);
        documentSearchRequest.setAmount(amount);
        documentSearchRequest.setGreaterThanAmount(greaterThanAmount);
        documentSearchRequest.setLessThanAmount(lessThanAmount);
        documentSearchRequest.setTotalAmount(totalAmount);
        documentSearchRequest.setLessThanTotalAmount(lessThanTotalAmount);
        documentSearchRequest.setGreaterThanTotalAmount(greaterThanTotalAmount);
        documentSearchRequest.setCurrencyCode(currencyCode);
        documentSearchRequest.setOrderNumber(orderNumber);

        List<Document> list = documentService.filter(documentSearchRequest);

        return list.stream().map(documentMapper::modelToApi).toList();


    }


}
