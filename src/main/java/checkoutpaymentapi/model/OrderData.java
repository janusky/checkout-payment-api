package checkoutpaymentapi.model;

import java.util.UUID;

import checkoutpaymentapi.state.ProcessData;
import checkoutpaymentapi.state.ProcessEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "order_data", schema = "pay")
public class OrderData implements ProcessData {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID orderId;

	private double payment;

	@Enumerated
	private OrderEvent event;

	@Override
	public ProcessEvent getEvent() {
		return this.event;
	}
}
