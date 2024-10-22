package com.uva.dbcs.recharge.Repository;

import com.uva.dbcs.recharge.Model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RechargeRepository extends JpaRepository<Recharge, Integer> {
    List<Recharge> findByUserId(Integer userId);
}
