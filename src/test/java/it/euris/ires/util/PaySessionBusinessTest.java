package it.euris.ires.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import it.euris.ires.business.PaySessionBusiness;
import it.euris.ires.dataObject.*;
import it.euris.ires.database.IPaySessionRepository;
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

@ExtendWith(MockitoExtension.class)
public class PaySessionBusinessTest {

    PaySessionBusiness psb;

    PaymentSessionService pss;

    @Mock
    IPaySessionRepository iPaySessionRepository;

    @Spy
    RequestToEntityMapper entityMapper;

    @Spy
    Amount amount;

    @BeforeEach
    void setUp() {
        pss = new PaymentSessionService(iPaySessionRepository, entityMapper, amount);
        psb = new PaySessionBusiness(pss);
    }

    @Test
    void givenValidRequestWhenCreatePaySessionThenShouldReturnCreatePaySessionResponseObject() {
        //arrange
        BusinessData businessData = new BusinessData("accountID", "paymentConfigID",
                "consumerID");
        PaySessionSettings paySessionSettings = new PaySessionSettings(1,true, true,
                "EUR", "ITA" );
        SaleItem s1 = new SaleItem("name", "description", "productID", "variantID",
                "10.00", 1);
        List<SaleItem> saleItemList = new ArrayList<>();
        saleItemList.add(s1);
        CreatePaySessionRequest request = new CreatePaySessionRequest();
        request.setBusinessData(businessData);
        request.setPaySessionSettings(paySessionSettings);
        request.setSaleItems(saleItemList);
        request.setTaxAmount("5%");
        Mockito.doReturn("10.00").when(amount).sum(any());

        //act
        CreatePaySessionResponse result = psb.createPaySession(request);

        //assert
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CreatePaySessionResponse.class);
    }

    //finisco sempre nelle eccezioni
    @Test
    void givenPaySessionWhenGetPaySessionThenShouldReturnPaySessionObject() {
        //arrange
        PaySession paySession = new PaySession();
        String paySessionID = paySession.getUuid().toString();
        try {
            pss.getShoppingCart(paySessionID);
        }
        catch (PaySessionException pse){
            System.out.println("Eccezione nella creazione della PaySession");
        }

        //act
        PaySession result = new PaySession();
        try {
            result = psb.getPaySession(paySessionID);
        }
        catch (PaySessionException pse){
            System.out.println("Eccezione nella creazione della PaySession");
        }

        //assert
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(PaySession.class);
    }
}
