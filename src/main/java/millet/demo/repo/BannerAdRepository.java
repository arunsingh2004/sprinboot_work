package millet.demo.repo;

import millet.demo.models.BannerAd;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannerAdRepository extends MongoRepository<BannerAd, String> {
    // You can define additional query methods here if needed
}
