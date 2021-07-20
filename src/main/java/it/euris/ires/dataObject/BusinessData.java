package it.euris.ires.dataObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor //aggiunto per poter creare più facilmente l'istanza della struttura dati
public class BusinessData {

	private String accountId;

	private String paymentConfigId;

	private String consumerId;

}
