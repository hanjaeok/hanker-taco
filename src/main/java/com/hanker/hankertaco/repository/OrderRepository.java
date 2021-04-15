package com.hanker.hankertaco.repository;

import com.hanker.hankertaco.domain.Order;

public interface OrderRepository {

    Order save(Order order);

}
