package com.singlepage.cms.repositories;

import java.util.List;

import com.singlepage.cms.models.Users;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UsersRepository extends PagingAndSortingRepository<Users, Long> {

    List<Users> findByLastName(@Param("name") String name);

}