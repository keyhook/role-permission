package org.ovida.auth.helper;

import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaTestHelper {

  private final TestEntityManager testEntityManager;

  public JpaTestHelper(TestEntityManager testEntityManager) {
    this.testEntityManager = testEntityManager;
  }

  @Transactional
  public <E> E create(E entity) {
    return testEntityManager.persist(entity);
  }

  private <E> TypedQuery<E> buildQuery(String field, Object value, Class<E> clazz) {
    var criteriaBuilder = testEntityManager.getEntityManager().getCriteriaBuilder();

    var criteriaQuery = criteriaBuilder.createQuery(clazz);

    var rootClazz = criteriaQuery.from(clazz);

    var predicate = criteriaBuilder.equal(rootClazz.get(field), value);

    criteriaQuery.where(predicate);

    return testEntityManager
      .getEntityManager()
      .createQuery(criteriaQuery);
  }

  @Transactional(readOnly = true)
  public <E> List<E> getResults(String field, Object value, Class<E> clazz) {
    return buildQuery(field, value, clazz)
      .getResultList();
  }

  @Transactional(readOnly = true)
  public <E> E getSingleResult(String field, Object value, Class<E> clazz) {
    return buildQuery(field, value, clazz)
      .getSingleResult();
  }

  private <E> TypedQuery<E> buildNestedQuery(String field, String nestedField, Object value, Class<E> clazz) {
    var criteriaBuilder = testEntityManager.getEntityManager().getCriteriaBuilder();

    var criteriaQuery = criteriaBuilder.createQuery(clazz);

    var rootClazz = criteriaQuery.from(clazz);

    var predicate = criteriaBuilder.equal(rootClazz.get(field).get(nestedField), value);

    criteriaQuery.where(predicate);

    return testEntityManager
      .getEntityManager()
      .createQuery(criteriaQuery);
  }

  @Transactional(readOnly = true)
  public <E> List<E> getNestedResults(String field, String nestedField, Object value, Class<E> clazz) {
    return buildNestedQuery(field, nestedField, value, clazz)
      .getResultList();
  }

  @Transactional
  public <E> void delete(E entity) {
    testEntityManager.remove(entity);
  }
}
