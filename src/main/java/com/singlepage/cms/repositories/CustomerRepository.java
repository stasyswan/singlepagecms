package com.singlepage.cms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.singlepage.cms.models.Customer;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}