package lk.ijse.posbackend.repository;

import lk.ijse.posbackend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    Orders findTopByOrderByOrderIdDesc();

}