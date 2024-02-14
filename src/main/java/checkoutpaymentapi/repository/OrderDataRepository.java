package checkoutpaymentapi.repository;

import java.util.UUID;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import checkoutpaymentapi.model.OrderData;

@CacheConfig(cacheNames = { OrderDataRepository.CACHE })
@Repository
public interface OrderDataRepository extends JpaRepository<OrderData, UUID> {
	static final String CACHE = "OrderData";
	static final String CACHE_findById = CACHE + "findById";

	@Override
	@Caching(evict = { @CacheEvict(value = CACHE_findById, allEntries = true) })
	@Transactional
	<S extends OrderData> S save(S entity);
}
