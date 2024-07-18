package millet.demo.service;

import millet.demo.models.BannerAd;
import millet.demo.repo.BannerAdRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.util.List;
// import java.util.Map;
import java.util.Optional;
// import java.util.UUID;

@Service
public class BannerAdService {
    @Autowired
    private BannerAdRepository bannerAdRepository;
    // private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/";

    public BannerAd saveBannerAd(BannerAd bannerAd) {
        return bannerAdRepository.save(bannerAd);
    }

    public List<BannerAd> getAllBannerAds() {
        return bannerAdRepository.findAll();
    }

    public Optional<BannerAd> getBannerAdById(String id) {
        return bannerAdRepository.findById(id);
    }

    public void deleteBannerAd(String id) {
        bannerAdRepository.deleteById(id);
    }

    public ResponseEntity<?> updateBannerAd(String id,BannerAd banner) {
        try{  
            
            
            bannerAdRepository.save(banner);
            return ResponseEntity.ok(banner);
    }catch(Exception e){
            return ResponseEntity.status(500).body("Error in banner service updating banner");
    }
    }

}
    
    


