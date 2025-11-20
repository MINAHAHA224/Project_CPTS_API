package vn.javaweb.ComputerShop.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.javaweb.ComputerShop.domain.entity.ProductEntity;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {

   void deleteProductEntityById ( Long id);

   @Query("SELECT DISTINCT p.factory FROM ProductEntity p")
   List<String> findAllFactories();


   @Query("SELECT DISTINCT p.target FROM ProductEntity p")
   List<String> findAllTargets();



   @Query("SELECT p FROM ProductEntity p  ORDER BY  p.sold DESC LIMIT :limit")
   List<ProductEntity> getFeaturedProducts( int limit );

   @Query("SELECT p FROM ProductEntity p ORDER BY p.id DESC  LIMIT :limit")
   List<ProductEntity> getNewestProduct ( int limit);

   boolean existsProductEntityByName ( String name);

   ProductEntity findProductEntityById(long id);


   Page<ProductEntity> findAll(Pageable page);


}