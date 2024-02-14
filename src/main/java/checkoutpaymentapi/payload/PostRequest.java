package checkoutpaymentapi.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
	/**
	 * Transaction identifier.
	 */
	@NotEmpty(message = "Not null")
	private String transacion;
}
