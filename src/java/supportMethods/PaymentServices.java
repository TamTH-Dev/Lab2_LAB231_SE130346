/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supportMethods;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class PaymentServices {

    private static final String CLIENT_ID = "AVcRc-anhgodVuKsmFK_3GeQ0mMPQ-XdQM1CcLfJFfXVL6S8rLz9AIyDj2mhHvMxnz-j5H9w61BiOnrX";
    private static final String CLIENT_SECRET = "EC9LI3SpNhKYzeVJdiHQy2dsMINVz_osE89FZiWrUbb9dFsHmu0A98lX9MSv7HKFYm6EizJnHSaa9cMe";
    private static final String MODE = "sandbox";

    public String authorizePayment(double billPriceTotal) {
        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectUrls();
        List<Transaction> listTransaction = getTransactionInformation(billPriceTotal);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction)
                .setRedirectUrls(redirectUrls)
                .setPayer(payer)
                .setIntent("authorize");

        Payment approvedPayment = null;
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        try {
            approvedPayment = requestPayment.create(apiContext);
        } catch (PayPalRESTException ex) {
            ex.printStackTrace();
        }

        return getApprovalLink(approvedPayment);
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
            }
        }

        return approvalLink;
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("Mad")
                .setLastName("Life")
                .setEmail("labdemo130346@gmail.com");

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private List<Transaction> getTransactionInformation(double billPriceTotal) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", billPriceTotal));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Bill Total");

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/Lab2_Lab231_TranHoangTam_SE130346/DataLoading");
        redirectUrls.setReturnUrl("http://localhost:8080/Lab2_Lab231_TranHoangTam_SE130346/ReviewPayment");
        return redirectUrls;
    }
}
