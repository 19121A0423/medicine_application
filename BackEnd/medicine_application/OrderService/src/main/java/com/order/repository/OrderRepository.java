package com.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.order.entity.Orders;

@Transactional
public interface OrderRepository extends JpaRepository<Orders, Integer>{

	@Modifying  //annotation is used to indicate that the method is modifying the database.
	@Query(value="UPDATE orders SET status='delivered' where order_id=:id" , nativeQuery = true)
	void updateStatusById(@Param("id") int id);

	@Query(value="SELECT * FROM orders where status='Ordered'" , nativeQuery = true)
	List<Orders> getOrdersWhichAreNotDelivered();
	
}
