package com.order.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.bean.AddressBean;
import com.order.bean.OrderBean;
import com.order.bean.PaymentBean;
import com.order.constants.CommonConstants;
import com.order.entity.Address;
import com.order.entity.Orders;
import com.order.exceptions.AddressNotFoundException;
import com.order.exceptions.OrderNotFoundException;
import com.order.repository.OrderRepository;
import com.order.service.AddressService;
import com.order.service.OrderService;
import com.order.service.PaymentService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private ObjectMapper mapper;
    
    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class.getSimpleName());

    /**
     * Places a new order.
     * 
     * @param orderBean The OrderBean object containing order details.
     * @return The OrderBean object representing the placed order.
     * @throws AddressNotFoundException if the address is not found.
     */
    @Override
    public OrderBean placeOrder(OrderBean orderBean) throws AddressNotFoundException {
        log.info("OrderServiceImpl::placeOrder::Started");
        if (orderBean.getCartId() <=0 || orderBean.getPayment() instanceof PaymentBean == false) {
            throw new IllegalArgumentException("Order properties cannot be empty");
        }
        
        else {
            Address address = new Address();
            if(orderBean.getAddress().getAddressId()<=0) {
                throw new AddressNotFoundException("Address in not found with id "+orderBean.getAddress().getAddressId());
            }
            else {
                AddressBean addressBean = addressService.getAddressById(orderBean.getAddress().getAddressId());
                address = mapper.convertValue(addressBean, Address.class);
            }
            
            Orders order = new Orders();
            order = mapper.convertValue(orderBean, Orders.class);
            order.setAddress(address);
            order.setOrderedDate(LocalDateTime.now());
            order.setStatus(CommonConstants.ORDERSTATUS);
            orderRepository.save(order);
            orderBean.setOrderId(order.getOrderId());
            PaymentBean payment = orderBean.getPayment();
            payment = paymentService.savePayment(payment,order);
            orderBean = mapper.convertValue(order, OrderBean.class);
            orderBean.setPayment(payment);
            log.info("OrderServiceImpl::placeOrder::Ended");
            return orderBean;
        }
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id The ID of the order.
     * @return The OrderBean object representing the retrieved order.
     * @throws OrderNotFoundException if the order is not found.
     */
    @Override
    public OrderBean getOrderById(int id) throws OrderNotFoundException {
        log.info("OrderServiceImpl::getOrderById::Started");
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Address not found with ID: " + id));

        OrderBean orderBean = new OrderBean();
        orderBean = mapper.convertValue(order, OrderBean.class);
        log.info("OrderServiceImpl::getOrderById::Ended");
        return orderBean;
    }

    /**
     * Retrieves all orders.
     * 
     * @return A list of OrderBean objects representing all orders.
     * @throws OrderNotFoundException if no orders are found.
     */
    @Override
    public List<OrderBean> getAllOrders() throws OrderNotFoundException {
        log.info("OrderServiceImpl::getAllOrders::Started");
        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found");
        }
        else {
            List<OrderBean> orderBeans = new ArrayList<>();
            orders.stream().forEach(order -> {
                OrderBean orderBean = new OrderBean();
                orderBean = mapper.convertValue(order, OrderBean.class);
                orderBeans.add(orderBean);
            });
            log.info("OrderServiceImpl::getAllOrders::Ended");
            return orderBeans;
        }
    }
  

    /**
     * Scheduled method to check orders and update their status if needed.
     * 
     * @throws OrderNotFoundException if no orders are found.
     * @throws AddressNotFoundException if the address is not found.
     */
    
	@Scheduled(fixedRate = 600000) // Check every 10 minutes, with an initial delay of 10 minutes
    public void checkProducts() throws OrderNotFoundException, AddressNotFoundException {
		log.info("OrderServiceImpl::checkProducts::Started");
		List<Orders> orders = orderRepository.getOrdersWhichAreNotDelivered();
        LocalDateTime currentTime = LocalDateTime.now();

        for (Orders order : orders) {
            if ("Ordered".equals(order.getStatus()) && currentTime.isAfter(order.getOrderedDate().plusMinutes(15))) {
            	orderRepository.updateStatusById(order.getOrderId());
            }
        }
        log.info("OrderServiceImpl::checkProducts::Ended");
    }

}
