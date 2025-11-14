package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.Medicine;
import MEDISCAN.MEDI.model.PharmacyOrder;
import MEDISCAN.MEDI.repository.MedicineRepository;
import MEDISCAN.MEDI.service.MedicineService;
import MEDISCAN.MEDI.service.PharmacyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PharmacyOrderService orderService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --------------------------------------------------
    // 🏥 1️⃣ List Shops
    // --------------------------------------------------
    @GetMapping("/shops")
    public String listShops(Model model) {
        try {
            String sql = "SELECT name, address, contact, CAST(delivery_available AS UNSIGNED) AS delivery_flag FROM pharmacy";
            model.addAttribute("shops", jdbcTemplate.queryForList(sql));
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching shops: " + e.getMessage());
        }
        return "pharmacy-shops";
    }

    // --------------------------------------------------
    // 🚚 2️⃣ Delivery Page
    // --------------------------------------------------
    @GetMapping("/delivery/{name}")
    public String showPharmacyDelivery(@PathVariable("name") String name, Model model) {
        // Simulate fetching pharmacy details
        Map<String, String> pharmacy = new HashMap<>();
        pharmacy.put("name", name);
        pharmacy.put("address", "Main Street, Bengaluru");
        pharmacy.put("contact", "+91 9876543210");
        pharmacy.put("delivery", "Available");

        model.addAttribute("pharmacy", pharmacy);
        return "delivery";
    }

    @PostMapping("/delivery/confirm")
    public String confirmDelivery(@RequestParam String pharmacyName,
                                  @RequestParam String customerName,
                                  @RequestParam String deliveryAddress,
                                  @RequestParam String contactNumber,
                                  RedirectAttributes ra) {

        ra.addFlashAttribute("message",
                "✅ Delivery request for pharmacy '" + pharmacyName + "' confirmed successfully for " + customerName + "!");
        return "redirect:/pharmacy/shops";
    }

    // --------------------------------------------------
    // 💊 3️⃣ List Medicines
    // --------------------------------------------------
    @GetMapping("/medicines")
    public String listMedicines(Model model,
                                @RequestParam(value = "q", required = false) String q,
                                @RequestParam(value = "category", required = false) String category,
                                @ModelAttribute("error") String error) {

        List<Medicine> meds;
        if (q != null && !q.isBlank()) {
            meds = medicineService.searchByName(q);
        } else if (category != null && !category.isBlank()) {
            meds = medicineService.byCategory(category);
        } else {
            meds = medicineService.all();
        }

        model.addAttribute("medicines", meds);
        model.addAttribute("q", q);
        model.addAttribute("category", category);

        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }

        return "pharmacy-medicines";
    }

    // --------------------------------------------------
    // ⚙️ 4️⃣ Add Medicine
    // --------------------------------------------------
    @GetMapping("/medicines/add")
    public String addMedicineForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "add-medicine";
    }

    @PostMapping("/medicines/save")
    public String saveMedicine(@ModelAttribute Medicine medicine, RedirectAttributes ra) {
        try {
            medicineService.save(medicine);
            ra.addFlashAttribute("message", "✅ Medicine saved successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Error saving medicine: " + e.getMessage());
        }
        return "redirect:/pharmacy/medicines";
    }

    // --------------------------------------------------
    // ✏️ 5️⃣ Edit Medicine
    // --------------------------------------------------
    @GetMapping("/medicines/edit/{id}")
    public String editMedicine(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Optional<Medicine> medOpt = medicineService.findById(id);
        if (medOpt.isPresent()) {
            model.addAttribute("medicine", medOpt.get());
            return "add-medicine"; // reuse same form
        } else {
            ra.addFlashAttribute("error", "Medicine not found!");
            return "redirect:/pharmacy/medicines";
        }
    }

    // --------------------------------------------------
    // 🗑️ 6️⃣ Delete Medicine
    // --------------------------------------------------
    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            medicineService.delete(id);
            ra.addFlashAttribute("message", "🗑️ Medicine deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error deleting medicine: " + e.getMessage());
        }
        return "redirect:/pharmacy/medicines";
    }

    // --------------------------------------------------
    // 🛒 7️⃣ Cart Operations
    // --------------------------------------------------
    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Integer id,
                            @RequestParam(value = "qty", defaultValue = "1") Integer qty,
                            @SessionAttribute(name = "cart", required = false) Map<Integer, Integer> cart,
                            WebRequest request,
                            RedirectAttributes ra) {

        if (cart == null) cart = new HashMap<>();
        cart.put(id, cart.getOrDefault(id, 0) + qty);
        request.setAttribute("cart", cart, WebRequest.SCOPE_SESSION);
        ra.addFlashAttribute("message", "🛒 Medicine added to cart!");
        return "redirect:/pharmacy/medicines";
    }

    @GetMapping("/cart")
    public String viewCart(Model model,
                           @SessionAttribute(name = "cart", required = false) Map<Integer, Integer> cart) {

        Map<Medicine, Integer> items = new LinkedHashMap<>();
        double total = 0.0;

        if (cart != null) {
            for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
                medicineService.findById(e.getKey()).ifPresent(m -> items.put(m, e.getValue()));
            }
            for (Map.Entry<Medicine, Integer> e : items.entrySet()) {
                total += e.getKey().getPrice() * e.getValue();
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "pharmacy-cart";
    }

    // --------------------------------------------------
    // 📦 8️⃣ Confirm Order
    // --------------------------------------------------
    @PostMapping("/order/confirm")
    public String confirmOrder(@RequestParam String customerName,
                               @RequestParam String customerPhone,
                               @RequestParam String deliveryAddress,
                               @SessionAttribute(name = "cart", required = false) Map<Integer, Integer> cart,
                               WebRequest request,
                               RedirectAttributes ra) {

        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/pharmacy/cart";
        }

        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
            Optional<Medicine> op = medicineService.findById(e.getKey());
            if (op.isEmpty()) continue;

            Medicine m = op.get();
            int qty = e.getValue();

            m.setStock(Math.max(0, m.getStock() - qty));
            medicineService.save(m);

            PharmacyOrder order = new PharmacyOrder();
            order.setMedicineId(m.getMedicineId());
            order.setMedicineName(m.getName());
            order.setQuantity(qty);
            order.setTotalPrice(m.getPrice() * qty);
            order.setCustomerName(customerName);
            order.setCustomerPhone(customerPhone);
            order.setDeliveryAddress(deliveryAddress);
            order.setStatus("ORDERED");
            order.setOrderDate(LocalDateTime.now());
            orderService.placeOrder(order);
        }

        request.removeAttribute("cart", WebRequest.SCOPE_SESSION);
        ra.addFlashAttribute("message", "✅ Order placed successfully!");
        return "redirect:/pharmacy/medicines";
    }

    // --------------------------------------------------
    // 🧾 9️⃣ View Orders
    // --------------------------------------------------
    @GetMapping("/orders")
    public String viewOrders(Model model) {
        List<PharmacyOrder> orders = orderService.allOrders();

        // ✅ add pharmacy info to fix Thymeleaf null error
        Map<String, String> pharmacy = new HashMap<>();
        pharmacy.put("name", "MediScan Pharmacy");
        pharmacy.put("address", "MG Road, Bengaluru");
        pharmacy.put("contact", "+91 9876543210");

        model.addAttribute("pharmacy", pharmacy);
        model.addAttribute("orders", orders);
        return "pharmacy-orders";
    }

    // --------------------------------------------------
    // 🧠 🔟 Admin Dashboard
    // --------------------------------------------------
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        try {
            List<Medicine> meds = medicineService.all();
            model.addAttribute("medicines", meds);
            return "pharmacy-medicines";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading admin dashboard: " + e.getMessage());
            return "redirect:/pharmacy/admin/dashboard";
        }
    }
    @PostMapping("/delivery/submit")
    public String submitDelivery(@RequestParam String pharmacyName,
                                 @RequestParam String customerName,
                                 @RequestParam String customerPhone,
                                 @RequestParam String deliveryAddress,
                                 RedirectAttributes ra) {

        ra.addFlashAttribute("message",
                "✅ Delivery request for pharmacy '" + pharmacyName + "' submitted successfully!");
        return "redirect:/pharmacy/shops";
    }
    
}
