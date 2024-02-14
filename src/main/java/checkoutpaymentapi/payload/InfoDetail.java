package checkoutpaymentapi.payload;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * Detalles de información.
 * </p>
 * 
 */
@Data
@Builder
public class InfoDetail {
	private String name;
	private String status;
	private Map<String, Object> details;
}
