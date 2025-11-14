package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.PharmacyOrder;
import MEDISCAN.MEDI.repository.PharmacyOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyOrderService {

    @Autowired
    private PharmacyOrderRepository orderRepository;

    public PharmacyOrder placeOrder(PharmacyOrder order) {
        return orderRepository.save(order);
    }

    public List<PharmacyOrder> allOrders() {
        return orderRepository.findAll();
    }

    public PharmacyOrder update(PharmacyOrder order) {
        return orderRepository.save(order);
    }
}
