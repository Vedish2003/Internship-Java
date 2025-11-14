package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.Medicine;
import MEDISCAN.MEDI.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> all() { return medicineRepository.findAll(); }
    public Optional<Medicine> findById(Integer id) { return medicineRepository.findById(id); }
    public Medicine save(Medicine m) { return medicineRepository.save(m); }
    public void delete(Integer id) { medicineRepository.deleteById(id); }
    public List<Medicine> searchByName(String q){ return medicineRepository.findByNameContainingIgnoreCase(q); }
    public List<Medicine> byCategory(String cat){ return medicineRepository.findByCategoryIgnoreCase(cat); }
}
