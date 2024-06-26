package com.admin.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.admin.bean.CartBean;
import com.admin.bean.ProductBean;
import com.admin.bean.UserBean;


import com.admin.constants.Constants;
import com.admin.entity.Cart;
import com.admin.entity.Product;
import com.admin.exception.CartIdNotFoundException;
import com.admin.exception.CartListNotFoundException;
import com.admin.exception.UserIdNotFoundException;
import com.admin.repository.CartRepository;
import com.admin.repository.ProductRepository;
import com.admin.service.CartService;

@Service
public class CartServiceImplementation implements CartService {
	
	private static Logger log = LoggerFactory.getLogger(CartServiceImplementation.class.getSimpleName());
	
	@Autowired
	public CartRepository repository;
	
	@Autowired
	public ProductRepository productRepo;
		
	@Autowired
	public UserServiceImpl userService; 
	
	@Autowired
	public ProductServiceImplementation productService;

	/**
	 * Saves the cart details to the database.
	 * 
	 * @param cart The cart details to be saved.
	 * @return The saved cart details.
	 * @throws UserIdNotFoundException if the user ID is not found.
	 */
	@Override
	public CartBean saveCart(CartBean cart) throws UserIdNotFoundException {
		log.info("Cart Service Implementation Save Method Start-> {}", cart);
		
		if (cart.getUser() == null || cart.getProducts() == null) {
			throw new IllegalArgumentException("User and Product properties cannot be null");
		}
		
		UserBean userBean = userService.getUserBean(cart.getUser().getUserId());
		
		if (userBean == null) {
			throw new UserIdNotFoundException("User Does Not Found By This Id " + cart.getUser().getUserId());
		}
		
		Cart cartEntity = repository.getCartByUserId(userBean.getUserId());
		if (cartEntity != null) {
			boolean contains = cartEntity.getProducts().contains(cart.getProducts().stream().findFirst().get());
			if (!contains) {
				cart.setUser(userBean);
				cartEntity.setUserId(userBean.getUserId());
				cartEntity = beanToEntity(cartEntity, cart);
				productRepo.save(cartEntity.getProducts().stream().findFirst().get());
				repository.save(cartEntity);
				cart = entityToBean(cartEntity, cart);
			}
		} else { 
			cartEntity = new Cart();
			cart.setUser(userBean);
			cartEntity.setUserId(userBean.getUserId());
			cartEntity = beanToEntity(cartEntity, cart);
			productRepo.save(cartEntity.getProducts().stream().findFirst().get());
			repository.save(cartEntity);
			cart = entityToBean(cartEntity, cart);
		}
		
		log.info("Cart Service Implementation Save Method  End  -> {}", cart);
		return cart;
	}

	/**
	 * Converts a CartBean object to a Cart entity.
	 * 
	 * @param cartEntity The Cart entity to be updated.
	 * @param cart The CartBean object containing the new data.
	 * @return The updated Cart entity.
	 */
	public Cart beanToEntity(Cart cartEntity, CartBean cart) {
		log.info("Cart Service Implementation beanToEntity Method Start -> {}", cart.getProducts());
		
		cartEntity.setStatus(cart.getStatus());			
		List<Product> products = new ArrayList<>();
		
		if (cartEntity.getProducts() != null) {
			products = cartEntity.getProducts();
		}
		
		double total = 0;		
		for (ProductBean ele : cart.getProducts()) {
			Product obj = new Product();		
			obj.setDescription(ele.getDescription());
			obj.setName(ele.getName());
			obj.setPrice(ele.getPrice());
			obj.setQuantity(ele.getQuantity());
			obj.setQuantityProduct(ele.getQuantityProduct());
			obj.setProductId(ele.getProductId());
			obj.setCategory(ele.getCategory());
			obj.setImage(ele.getImage());
			obj.setStatus(ele.getStatus());
			products.add(obj);	
		}
		
		System.out.println("Before Set" + products);
	    HashSet<Product> set = new HashSet<>(products);
	    products = null;
	    products = new ArrayList<>(set);	
	    for (Product product : products) {
			total = (total + (product.getPrice() * product.getQuantityProduct()));
		}
		System.out.println("After Set " + products);
		
		cartEntity.setQuantity(set.size());		
		cartEntity.setAmount(total);		
		cartEntity.setProducts(products);
		
		log.info("Cart Service Implementation beanToEntity Method End -> {}", cartEntity);
		return cartEntity;
	}

	/**
	 * Converts a Cart entity to a CartBean object.
	 * 
	 * @param cartEntity The Cart entity to be converted.
	 * @param cart The CartBean object to be updated with entity data.
	 * @return The updated CartBean object.
	 */
	public CartBean entityToBean(Cart cartEntity, CartBean cart) {
		log.info("Cart Service Implementation entityToBean Method Start -> {}", cart);
		
		cart.setCartId(cartEntity.getCartId());
		cart.setStatus(cartEntity.getStatus());
		
		double total = 0;
		List<ProductBean> products = new ArrayList<>();
		if (cartEntity.getProducts() != null) {
			for (Product ele : cartEntity.getProducts()) {
				ProductBean obj = new ProductBean();
				obj.setDescription(ele.getDescription());
				obj.setName(ele.getName());
				obj.setPrice(ele.getPrice());
				obj.setProductId(ele.getProductId());
				obj.setCategory(ele.getCategory());
				obj.setQuantity(ele.getQuantity());
				obj.setQuantityProduct(ele.getQuantityProduct());
				obj.setImage(ele.getImage());
				obj.setStatus(ele.getStatus());
				total = (total + (ele.getPrice() * ele.getQuantityProduct()));
				System.out.println(total);
				products.add(obj);			
			}
		}
		cart.setQuantity(cartEntity.getQuantity());
		cart.setAmount(total);
		cart.setProducts(products);			
		log.info("Cart Service Implementation entityToBean Method End -> {}", cart);
		
		return cart;
	}

	/**
	 * Updates the quantity of a product in the cart.
	 * 
	 * @param cart The CartBean object containing the updated product quantity.
	 * @param productId The ID of the product to be updated.
	 * @return The updated CartBean object.
	 * @throws CartIdNotFoundException if the cart ID is not found.
	 */
	@Override
	public CartBean update(CartBean cart, Integer productId) throws CartIdNotFoundException {
		log.info("Cart service implementation update method {}", cart);
		
		Optional<Cart> optional = repository.findById(cart.getCartId());
		if (optional.isEmpty()) {
			throw new CartIdNotFoundException("Cart Not Found By This Id " + cart.getCartId());
		}
		
		Cart cartEntity = optional.get();
		List<Product> products = cartEntity.getProducts();

		for (Product product : products) {
			if (product.getProductId() == productId) {
				product.setQuantityProduct(cart.getProducts().get(0).getQuantityProduct());		
				System.out.println(product.getQuantityProduct());
				productRepo.save(product);
				break;
			}	
		}
		
		UserBean userBean = userService.getUserBean(cartEntity.getUserId());
		cartEntity = beanToEntity(cartEntity, cart);
		repository.save(cartEntity);
		cart = entityToBean(cartEntity, cart);
		cart.setUser(userBean);
		
		log.info("Cart service implementation update method End{}", cart);
		return cart;
	}

	/**
	 * Deletes a product from the cart.
	 * 
	 * @param cartId The ID of the cart.
	 * @param productId The ID of the product to be deleted.
	 * @return The updated CartBean object after deletion.
	 * @throws CartIdNotFoundException if the cart ID is not found.
	 */
	@Override
	public CartBean delete(Integer cartId, Integer productId) throws CartIdNotFoundException {
		log.info("Cart Service Implementation delete Method Start->");
		if (cartId == 0) {
			throw new IllegalArgumentException("Cart Id Cannot Be Empty");
		}		
		
		Optional<Cart> optional = repository.findById(cartId);
		double total = 0;
		if (optional.isPresent()) {
			Cart cartEntity = optional.get();
			List<Product> products = cartEntity.getProducts();
			int temp = -1;
			for (Product product : products) {
				temp++;
				if (product.getProductId() == productId) {
					cartEntity.setQuantity(cartEntity.getQuantity() - 1);
					total = (product.getPrice() * product.getQuantityProduct());
					product.setQuantityProduct(1);
					product.setStatus("Add To Cart");
					productRepo.save(product);
					break;
				}
			}			
			products.remove(temp);
			cartEntity.setAmount(cartEntity.getAmount() - total);
			CartBean cart = new CartBean();
			repository.save(cartEntity);
			cart = entityToBean(cartEntity, cart);			
			return cart;
		} else {
			log.info("Cart Service Implementation delete If else Method End->");
			throw new CartIdNotFoundException("Cart Not Found By This Id" + cartId);
		}	
	}

	/**
	 * Retrieves details of all carts.
	 * 
	 * @return A list of CartBean objects representing cart details.
	 * @throws CartListNotFoundException if no carts are found.
	 */
	@Override
	public List<CartBean> getCartDetails() throws CartListNotFoundException {
		log.info("Cart sevice implementation getCartDetails method start -> ");
		List<Cart> cartList = repository.findAll();
		List<CartBean> beanCartList = new ArrayList<>();
		
		if (!cartList.isEmpty()) {		
			for (Cart cart : cartList) {
				CartBean bean = new CartBean();
				UserBean userBean = userService.getUserBean(cart.getUserId());
				bean.setUser(userBean);
				bean = entityToBean(cart, bean);
				beanCartList.add(bean);
			}
		} else {
			throw new CartListNotFoundException("Cart Is Empty");
		}

		log.info("Cart sevice implementation getCartDetails method End -> ");
		return beanCartList;
	}

	/**
	 * Retrieves details of a cart by user ID.
	 * 
	 * @param userId The ID of the user.
	 * @return The CartBean object representing the cart details.
	 * @throws CartIdNotFoundException if the cart ID is not found.
	 * @throws UserIdNotFoundException if the user ID is not found.
	 * @throws CartListNotFoundException if no carts are found.
	 */
	@Override
	public CartBean getCartById(Integer userId) throws CartIdNotFoundException, UserIdNotFoundException, CartListNotFoundException {
		log.info("Cart sevice implementation getCartById method start -> {}", userId);
		if (userId == null) {
			throw new IllegalArgumentException("UserId Cannot Be Empty");
		}		
		UserBean bean = userService.getUserBean(userId);
		if (bean == null) {
			throw new UserIdNotFoundException("User Does Not Having Cart By This Id" + userId);
		}
		Cart cartEntity = repository.getCartByUserId(bean.getUserId());
		if (cartEntity == null) {
			throw new CartListNotFoundException("Cart is Empty");
		}
			
		CartBean cart = new CartBean();
		cart.setUser(bean);
		cart = entityToBean(cartEntity, cart);
			
		log.info("Cart sevice implementation getCartById method  End -> {}", cart, userId);			
		return cart;	
	}

	/**
	 * Updates the status of a cart.
	 * 
	 * @param cart The CartBean object containing the cart ID and new status.
	 * @return The updated CartBean object.
	 * @throws CartIdNotFoundException if the cart ID is not found.
	 */
	@Override
	public CartBean updateCartStatus(CartBean cart) throws CartIdNotFoundException {
		log.info("Cart Service Implementation Update Cart Status Start" + cart.getCartId());
		if (cart == null) {
			throw new IllegalArgumentException("Cart Id Cannot be Empty");
		}
		
		Optional<Cart> optional = repository.findById(cart.getCartId());
		if (optional.isPresent()) {
			Cart cartEntity = optional.get();
			List<Product> products = cartEntity.getProducts();

			for (Product product : products) {
				product.setQuantityProduct(Constants.DEFAULTPRODUCTQUANTITY);
				product.setStatus(Constants.STATUS);
				product.setQuantity(product.getQuantity() - product.getQuantityProduct());
				productRepo.save(product);
			}
			products = null;
			cartEntity.setProducts(products);
			cartEntity.setAmount(Constants.DEFALULTAMOUNT);
			cartEntity.setQuantity(Constants.DEFAULTQUANTITY);
			cartEntity.setStatus(Constants.CARTSTATUS);
			cartEntity = repository.save(cartEntity);
			System.out.println(cartEntity);
			cart = entityToBean(cartEntity, cart);
			log.info("Cart Service Implementation Update Cart Status End" + cart);
			return cart;
		} else {
			throw new CartIdNotFoundException("Cart Not Found By This Id" + cart.getCartId());
		}
	}


}
