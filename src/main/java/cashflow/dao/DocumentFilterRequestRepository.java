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


    public List<Document> findByCriteria(DocumentFilterRequest DocumentFilterRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<Document> root = criteriaQuery.from(Document.class);

        if (DocumentFilterRequest.getDocumentGroup() != null) {
            Predicate documentGroupPredicate = criteriaBuilder.like(root.get("documentGroup"), "%" + DocumentFilterRequest.getDocumentGroup() + "%");
            predicates.add(documentGroupPredicate);
        }
        if (DocumentFilterRequest.getDocumentType() != null) {
            Predicate documentTypePredicate = criteriaBuilder.like(root.get("documentType"), "%" + DocumentFilterRequest.getDocumentType() + "%");
            predicates.add(documentTypePredicate);
        }
        if (DocumentFilterRequest.getDocumentNumber() != null) {
            Predicate documentNumberPredicate = criteriaBuilder.like(root.get("documentNumber"), "%" + DocumentFilterRequest.getDocumentNumber() + "%");
            predicates.add(documentNumberPredicate);
        }
        if (DocumentFilterRequest.getIssueDate() != null) {
            Predicate issueDatePredicate = criteriaBuilder.like(root.get("issueDate"), "%" + DocumentFilterRequest.getIssueDate() + "%");
            predicates.add(issueDatePredicate);
        }
        if (DocumentFilterRequest.getDueDate() != null) {
            Predicate dueDatePredicate = criteriaBuilder.like(root.get("dueDate"), "%" + DocumentFilterRequest.getDueDate() + "%");
            predicates.add(dueDatePredicate);
        }
        if (DocumentFilterRequest.getContractorName() != null) {
            Predicate contractorNamePredicate = criteriaBuilder.like(root.get("contractorName"), "%" + DocumentFilterRequest.getContractorName() + "%");
            predicates.add(contractorNamePredicate);
        }
        if (DocumentFilterRequest.getContractorVatNumber() != null) {
            Predicate contractorVATNumberPredicate = criteriaBuilder.equal(root.get("contractorVATNumber"), DocumentFilterRequest.getContractorVatNumber());
            predicates.add(contractorVATNumberPredicate);
        }
        if (DocumentFilterRequest.getAmount() != null) {
            Predicate amountPredicate = criteriaBuilder.equal(root.get("amount"), DocumentFilterRequest.getAmount());
            predicates.add(amountPredicate);
        }
        if (DocumentFilterRequest.getGreaterThanAmount() != null) {
            Predicate greaterThanOrEqualToThanAmountPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), DocumentFilterRequest.getGreaterThanAmount());
            predicates.add(greaterThanOrEqualToThanAmountPredicate);
        }
        if (DocumentFilterRequest.getLessThanAmount() != null) {
            Predicate lessThanOrEqualtoThanAmountPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("amount"), DocumentFilterRequest.getLessThanAmount());
            predicates.add(lessThanOrEqualtoThanAmountPredicate);
        }
        if (DocumentFilterRequest.getTotalAmount() != null) {
            Predicate totalAmountPredicate = criteriaBuilder.equal(root.get("totalAmount"), DocumentFilterRequest.getTotalAmount());
            predicates.add(totalAmountPredicate);
        }
        if (DocumentFilterRequest.getLessThanTotalAmount() != null) {
            Predicate lessThanTotalAmountPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), DocumentFilterRequest.getLessThanTotalAmount());
            predicates.add(lessThanTotalAmountPredicate);
        }
        if (DocumentFilterRequest.getGreaterThanTotalAmount() != null) {
            Predicate greaterThanOrEqualtoThanAmountPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), DocumentFilterRequest.getGreaterThanTotalAmount());
            predicates.add(greaterThanOrEqualtoThanAmountPredicate);
        }
        if (DocumentFilterRequest.getCurrencyCode() != null) {
            Predicate currencyCodePredicate = criteriaBuilder.equal(root.get("currencyCode"), DocumentFilterRequest.getCurrencyCode());
            predicates.add(currencyCodePredicate);
        }
        if (DocumentFilterRequest.getOrderNumber() != null) {
            Predicate orderNumberPredicate = criteriaBuilder.like(root.get("orderNumber"), "%" + DocumentFilterRequest.getOrderNumber() + "%");
            predicates.add(orderNumberPredicate);
        }


        criteriaQuery.where(
                criteriaBuilder.or((predicates.toArray(predicates.toArray(new Predicate[0]))))
        );

        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


}

