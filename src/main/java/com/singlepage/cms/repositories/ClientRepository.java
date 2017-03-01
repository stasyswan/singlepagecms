//package com.singlepage.cms.repository;
//
///**
// * Created by alebedieva on 21.02.17.
// */
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import com.devmedia.server.model.Client;
//
//@RepositoryRestResource(collectionResourceRel = "client", path = "clients")
//public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
//    /**
//     * Method that returns a lista of clients doing a search by the passed name parameter.
//     *
//     * @param name
//     * @return list of clients
//     */
//    List<Client> findByName(@Param("name") String name);
//
//    /**
//     * Method that returns a client with only its name doing a search by the passed id parameter.
//     *
//     * @param id
//     * @return client of the id passed as parameter.
//     */
//    @Query("SELECT c.Name FROM Client c where c.id = :id")
//    Client findNameById(@Param("id") Long id);
//
//    /**
//     * Method that returns a list of clients doing a search by the passed name parameter and sorting them by the name.
//     *
//     * @param name
//     * @return list of clients
//     */
//    List<Client> findByNameOrderByName(@Param("name") String name);
//
//}