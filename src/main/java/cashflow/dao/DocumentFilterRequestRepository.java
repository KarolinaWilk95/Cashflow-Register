package cashflow.dao;

import cashflow.document.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class DocumentFilterRequestRepository {

    private final EntityManager entityManager;


    public List<Document> findByCriteria(DocumentFilterRequest documentFilterRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<Document> root = criteriaQuery.from(Document.class);

        if (documentFilterRequest.getDocumentGroup() != null) {
            var doc_group = String.valueOf(root.get("documentGroup"));
            var enum_doc_group = documentFilterRequest.getDocumentGroup();
            Predicate documentGroupPredicate = criteriaBuilder.like(root.get("documentGroup").as(String.class), enum_doc_group);
            predicates.add(documentGroupPredicate);
        }
        if (documentFilterRequest.getDocumentType() != null) {
            Predicate documentTypePredicate = criteriaBuilder.equal(root.get("documentType"), documentFilterRequest.getDocumentType());
            predicates.add(documentTypePredicate);
        }
        if (documentFilterRequest.getDocumentNumber() != null) {
            Predicate documentNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("documentNumber")), "%" + documentFilterRequest.getDocumentNumber().toLowerCase() + "%");
            predicates.add(documentNumberPredicate);
        }
        if (documentFilterRequest.getIssueDate() != null) {
            Predicate issueDatePredicate = criteriaBuilder.like(root.get("issueDate"), "%" + documentFilterRequest.getIssueDate() + "%");
            predicates.add(issueDatePredicate);
        }
        if (documentFilterRequest.getDueDate() != null) {
            Predicate dueDatePredicate = criteriaBuilder.like(root.get("dueDate"), "%" + documentFilterRequest.getDueDate() + "%");
            predicates.add(dueDatePredicate);
        }
        if (documentFilterRequest.getContractorName() != null) {
            Predicate contractorNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("contractorName")), "%" + documentFilterRequest.getContractorName().toLowerCase() + "%");
            predicates.add(contractorNamePredicate);
        }
        if (documentFilterRequest.getContractorVatNumber() != null) {
            Predicate contractorVATNumberPredicate = criteriaBuilder.equal(root.get("contractorVatNumber"), documentFilterRequest.getContractorVatNumber());
            predicates.add(contractorVATNumberPredicate);
        }
        if (documentFilterRequest.getAmount() != null) {
            Predicate amountPredicate = criteriaBuilder.equal(root.get("amount"), documentFilterRequest.getAmount());
            predicates.add(amountPredicate);
        }
        if (documentFilterRequest.getGreaterThanAmount() != null) {
            Predicate greaterThanOrEqualToThanAmountPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), documentFilterRequest.getGreaterThanAmount());
            predicates.add(greaterThanOrEqualToThanAmountPredicate);
        }
        if (documentFilterRequest.getLessThanAmount() != null) {
            Predicate lessThanOrEqualtoThanAmountPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("amount"), documentFilterRequest.getLessThanAmount());
            predicates.add(lessThanOrEqualtoThanAmountPredicate);
        }
        if (documentFilterRequest.getTotalAmount() != null) {
            Predicate totalAmountPredicate = criteriaBuilder.equal(root.get("totalAmount"), documentFilterRequest.getTotalAmount());
            predicates.add(totalAmountPredicate);
        }
        if (documentFilterRequest.getLessThanTotalAmount() != null) {
            Predicate lessThanTotalAmountPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), documentFilterRequest.getLessThanTotalAmount());
            predicates.add(lessThanTotalAmountPredicate);
        }
        if (documentFilterRequest.getGreaterThanTotalAmount() != null) {
            Predicate greaterThanOrEqualtoThanAmountPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), documentFilterRequest.getGreaterThanTotalAmount());
            predicates.add(greaterThanOrEqualtoThanAmountPredicate);
        }
        if (documentFilterRequest.getCurrencyCode() != null) {
            Predicate currencyCodePredicate = criteriaBuilder.equal(root.get("currencyCode"), documentFilterRequest.getCurrencyCode());
            predicates.add(currencyCodePredicate);
        }
        if (documentFilterRequest.getOrderNumber() != null) {
            Predicate orderNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("orderNumber")), "%" + documentFilterRequest.getOrderNumber().toLowerCase() + "%");
            predicates.add(orderNumberPredicate);
        }


        criteriaQuery.where(
                criteriaBuilder.or((predicates.toArray(predicates.toArray(new Predicate[0]))))
        );

        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


}

