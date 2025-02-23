package quickcart.quickcart.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quickcart.quickcart.Entity.Seller;
import quickcart.quickcart.Exception.SellerException;
import quickcart.quickcart.Repository.SellerRepository;
@Service
public class SellerService implements ISellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public String addSeller(Seller seller) {
        if(sellerRepository.existsByContactEmail(seller.getContactEmail())){
            return "Seller already exists with this email"+ seller.getContactEmail();
        }
        if(sellerRepository.existsByContactPhone(seller.getContactPhone())) {
            return "Seller already exists with this phone number"+ seller.getContactPhone();
        }
      Seller savedSeller = sellerRepository.save(seller);
        return "Success "+ savedSeller.getSellerId();
    }

    @Override
    public String updateSeller(Seller seller) {
        // TODO Auto-generated method stub
     Seller updatedSeller =   sellerRepository.findById(seller.getSellerId()).orElseThrow(()-> new SellerException("Seller not found"));
     if(seller.getContactEmail() != null){
         updatedSeller.setContactEmail(seller.getContactEmail());
     }
     if(seller.getContactPhone() != null){     
         updatedSeller.setContactPhone(seller.getContactPhone());
     }
     if(seller.getStoreName() != null){
         updatedSeller.setStoreName(seller.getStoreName());
     }
     if(seller.getStoreDescription() != null){
         updatedSeller.setStoreDescription(seller.getStoreDescription());
     }
     sellerRepository.save(updatedSeller);
     return "Success "+ updatedSeller.getSellerId();
    
    }

    

    @Override
    public String deleteSeller(Long sellerId) {
      Seller seller = sellerRepository.findById(sellerId).orElseThrow(()-> new SellerException("Seller not found"));
      sellerRepository.delete(seller);
      return "Seller deleted successfully";
    }

    @Override
    public Seller getSeller(Long sellerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSeller'");
    }

    @Override
    public List<Seller> getAllSellers() {
        // TODO Auto-generated method stub
        return sellerRepository.findAll();
    }

   

   
    
}
