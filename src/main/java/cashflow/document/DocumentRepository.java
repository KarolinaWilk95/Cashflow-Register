package cashflow.document;

import cashflow.dao.DocumentFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.JpaExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>,
        JpaSpecificationExecutor<Document> {


    default Specification<Document> hasColumnValue(String columnName, String value) {

        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(columnName), "%" + value + "%"));

    }


    default Specification<Document> hasValue(String value) {


        return (root, query, criteriaBuilder) -> {

            var columns = Document.class.getDeclaredFields();

            List<String> searchableColumns = Arrays.stream(columns)
                    .map(Field::getName)
                    .toList();

            String searchValue = "%" + value.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            for (var column : columns) {
                if (column.getType().equals(String.class)) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(column.getName())), searchValue));
                } else if (column.getType().equals(BigDecimal.class) || column.getType().equals(Long.class)) {

                    predicates.add(criteriaBuilder.like(((JpaExpression) root.get(column.getName())).cast(String.class), searchValue));
                }
            }

            return criteriaBuilder.or(predicates.toArray(predicates.toArray(new Predicate[0])));

        };
    }


    default Page<Document> findAllDocuments(String query, Pageable pageable) {
        return findAll(hasValue(query), pageable);
    }


}

