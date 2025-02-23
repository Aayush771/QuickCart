package quickcart.quickcart.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quickcart.quickcart.Entity.Cart;
import quickcart.quickcart.Entity.CartItem;
import quickcart.quickcart.Entity.Users;
import quickcart.quickcart.Exception.CartException;
import quickcart.quickcart.Repository.CartRepository;
import quickcart.quickcart.Repository.UserRepository;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Cart addCart(Long userId) {
        Cart cart = new Cart();
        Optional<Users> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            cart.setUser(u);
            u.getUserCarts().add(cart);
        });
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartsByUserId(Long userId) {
        return cartRepository.findCartByUserUserId(userId);
    }
   @Transactional
    @Override
    public Cart addItemToCart(Long cartId, Long productId, int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        
        Cart cart = cartOptional.orElseThrow(() -> new CartException("Cart not found for id: " + cartId));
       CartItem newCartItem =  cartItemService.addCartItem(cart, productId, quantity);
       Set<CartItem> cartItems = cart.getCartCartItems();
       cartItems.add(newCartItem);
        cart.setCartCartItems(cartItems);
        cart.setTotalPrice(calculateTotalAmount(cartItems));
        return cartRepository.save(cart);
    }

    // @Override
    // public void clearCart(Long userId) {
    //     Cart cart = getCartByUserId(userId);
    //     cartItemService.clearCartItems(cart.getCartId());
    //     cart.setTotalPrice(0.0);
    //     cartRepository.save(cart);
    // }

    // @Override
    // public double calculateTotalPrice(Long userId) {
    //     Cart cart = getCartByUserId(userId);
    //     return cartItemService.calculateCartTotal(cart.getCartId());
    // }

    @Override
    public Cart removeItemFromCart(Long cartId, Long cartItemId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);      
        Cart cart = cartOptional.orElseThrow(() -> new CartException("Cart not found for id: " + cartId));
        Set<CartItem> cartItems = cart.getCartCartItems();
        cartItems.removeIf(cartItem -> cartItem.getCartItemId().equals(cartItemId));
        cart.setCartCartItems(cartItems);
        cart.setTotalPrice(cartItems.stream().mapToDouble(cartItem -> cartItem.getItemTotalPrice()).sum());
        Cart updatedCart = cartRepository.save(cart);
        cartItemService.removeCartItem(cartItemId);
        return updatedCart;
    }

    @Override
    public Cart updateCartItemQuantity(Long cartId, Long cartItemId, int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);      
        Cart cart = cartOptional.orElseThrow(() -> new CartException("Cart not found for id: " + cartId));
      CartItem cartItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
      Set<CartItem> cartItems = cart.getCartCartItems();
      cartItems.removeIf(item -> item.getCartItemId().equals(cartItemId));
      cartItems.add(cartItem);
      cart.setCartCartItems(cartItems);
      cart.setTotalPrice(cartItems.stream().mapToDouble(Item -> Item.getItemTotalPrice()).sum());
      Cart updatedCart = cartRepository.save(cart);
      return updatedCart;
    }

    @Override
    public Optional<Cart> getCart(Long cartId) {
        // TODO Auto-generated method stub
       return cartRepository.findById(cartId);
    }
    @Override
    public Double calculateTotalAmount(Set<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(cartItem -> cartItem.getItemTotalPrice()).sum();
    }
}
