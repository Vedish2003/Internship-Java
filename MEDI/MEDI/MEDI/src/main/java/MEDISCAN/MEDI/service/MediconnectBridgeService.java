package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.MediconnectBridge;
import MEDISCAN.MEDI.repository.MediconnectBridgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediconnectBridgeService {

    @Autowired
    private MediconnectBridgeRepository repository;

    public List<MediconnectBridge> getAllHospitals() {
        return repository.findAll();
    }

    public Optional<MediconnectBridge> getHospitalById(int id) {
        return repository.findById(id);
    }

    public List<MediconnectBridge> searchHospitalsByName(String name) {
        return repository.findByHospitalNameContainingIgnoreCase(name);
    }
}
