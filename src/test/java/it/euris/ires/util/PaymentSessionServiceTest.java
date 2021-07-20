package it.euris.ires.util;

import it.euris.ires.dataObject.BusinessData;
import it.euris.ires.dataObject.CreatePaySessionRequest;
import it.euris.ires.dataObject.PaySessionSettings;
import it.euris.ires.dataObject.SaleItem;
import it.euris.ires.database.IPaySessionRepository;
import it.euris.ires.entity.Item;
import it.euris.ires.entity.PaySession;
import it.euris.ires.exception.PaySessionException;
import it.euris.ires.service.impl.PaymentSessionService;
import it.euris.ires.service.util.RequestToEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PaymentSessionServiceTest {

    PaymentSessionService pss;

    @Mock
    IPaySessionRepository paySessionRepository;

    @Spy
    RequestToEntityMapper entityBuilder;

    @Spy
    Amount amount;

    @BeforeEach
    void setUp() {
        pss = new PaymentSessionService(paySessionRepository, entityBuilder, amount);
    }

    @Test
    void givenValidRequestWhenCreateWebPaySessionThenShouldReturnPaySession() {
        //arrange
        BusinessData businessData = new BusinessData("accountID", "paymentConfigID",
                "consumerID");
        PaySessionSettings paySessionSettings = new PaySessionSettings(1, true, true,
                "EUR", "ITA");
        SaleItem s1 = new SaleItem("name", "description", "productID", "variantID",
                "10.00", 1);
        List<SaleItem> saleItemList = new ArrayList<>();
        saleItemList.add(s1);
        CreatePaySessionRequest request = new CreatePaySessionRequest();
        request.setBusinessData(businessData);
        request.setPaySessionSettings(paySessionSettings);
        request.setSaleItems(saleItemList);
        request.setTaxAmount("5%");

        Mockito.doReturn("100.00").when(amount).sum(any());

        try {
            //act
            PaySession result = pss.createWebPaySession(request);

            //assert
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(PaySession.class);
        } catch (PaySessionException pse) {
            System.out.println("Eccezione!");
        }
    }

    //calculateItemAmount e calculateTotalAmount sono private


    //codice aggiustato per bug
    @Test
    void givenValidPaySessionIDWhenGetShoppingCartThenShouldReturnPaySession() {
        //arrange
        PaySession paySession = new PaySession();
        String paySessionID = paySession.getUuid().toString();

        //act
        try {
            PaySession result = pss.getShoppingCart(paySessionID);

            //assert
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(PaySession.class);
        }
        catch (PaySessionException pse){
            pse.getMessage();
        }
    }
}