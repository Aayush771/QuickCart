package quickcart.quickcart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import quickcart.quickcart.Entity.Payment;
import quickcart.quickcart.Service.IPaymentService;



@RestController
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/pay/{orderId}")
    public ResponseEntity<Payment> payForOrder(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        Payment payment = paymentService.processPayment(orderId, paymentMethod);
        return ResponseEntity.ok(payment);
    }
}
