package com.ek.hk_pos.order;

import com.ek.hk_pos.customer.Customer;
import com.ek.hk_pos.customer.CustomerRepository;
import com.ek.hk_pos.exception.BadRequestException;
import com.ek.hk_pos.exception.ResourceNotFoundException;
import com.ek.hk_pos.product.Product;
import com.ek.hk_pos.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order findById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found with id: "+ id));
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order create(OrderRequest request){
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Customer not found with id: "+ request.getCustomerId()));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        return orderRepository.save(order);
    }

    public Order addItem(Long orderId, OrderItemRequest request){
        Order order = findById(orderId);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new BadRequestException("Cannot modify an order that is not PENDING");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+ request.getProductId()));

        if(product.getStockQuantity() < request.getQuantity()){
            throw new BadRequestException("Insufficient stock for product:" + product.getName());
        }

        OrderItem existingItem = order.getItems().stream()
                .filter(item-> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if(existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity()+ request.getQuantity());
        }else{
            OrderItem newItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            order.getItems().add(newItem);
        }

        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);

        recalculateTotal(order);
        return  orderRepository.save(order);
    }

    public  Order removeItem(Long orderId, Long itemId){
        Order order = findById(orderId);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new BadRequestException("Cannot modify and order that is not PENDING");
        }

        OrderItem item = order.getItems().stream()
                .filter(i->i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id: "+ itemId));

        Product product = item.getProduct();
        product.setStockQuantity(product.getStockQuantity()+ item.getQuantity());
        productRepository.save(product);

        order.getItems().remove(item);
        recalculateTotal(order);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long orderId, OrderStatus status){
        Order order = findById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id){
        findById(id);
        orderRepository.deleteById(id);
    }

    private void recalculateTotal(Order order){
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }

}
