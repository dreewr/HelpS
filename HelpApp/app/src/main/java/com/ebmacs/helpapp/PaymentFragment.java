package com.ebmacs.helpapp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSBilletListener;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSInstallmentsListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSInstallmentsResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PaymentResponseVO;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;
import br.com.uol.pslibs.checkout_in_app.wallet.view.components.PaymentButton;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSCheckoutRequest;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PagSeguroResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.uol.pslibs.checkout_in_app.wallet.PSCheckoutWallet.libStarted;

public class PaymentFragment extends Fragment {

    private static final String SELLER_EMAIL = "<Email do vendedor>";
    private static final String SELLER_TOKEN = "<Token do vendedor>";
    private final String NOTIFICATION_URL_PAYMENT = "https://pagseguro.uol.com.br/lojamodelo-qa/RetornoAutomatico-OK.jsp";

//    @BindView(R.id.wallet_payment_button)
//    PaymentButton cardWallet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_fragment, container, false);
        ButterKnife.bind(this, view);

        initWallet();
        initTransparent();
        //configurePayment();
        return view;
    }

    private void initWallet() {
        //Inicialização a lib com parametros necessarios
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
        //Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);

        //Inicializa apenas os recursos da carteira
        PSCheckout.initWallet(getActivity(), psCheckoutConfig);
    }

    private void initTransparent() {
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
        //Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);
        //Inicializa apenas os recursos de pagamento transparente e boleto
        PSCheckout.initTransparent(Objects.requireNonNull(getActivity()), psCheckoutConfig);
    }

    public void configurePayment() {
        String productId = "001";
        String description = "CAFE NESPRESSO";



      //  cardWallet.configurePayment(productId, description, 2.50, 1, R.id.fragment_container, Objects.requireNonNull(getActivity()),
        //        SELLER_EMAIL, SELLER_TOKEN, psCheckoutListener);
    }

    br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener psCheckoutListener = new br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener() {
        @Override
        public void onSuccess(PagSeguroResponse pagSeguroResponse, Context context) {
            Toast.makeText(getActivity(), "Sucesso de pagamento", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(PagSeguroResponse pagSeguroResponse, Context context) {
            Toast.makeText(getActivity(), "Falha no pagamento", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProgress(Context context) {
            Toast.makeText(getActivity(), "Pagamento em andamento", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCloseProgress(Context context) {

        }
    };

    @OnClick(R.id.list_card)
    public void listCards() {
        Toast.makeText(getActivity(), "Listar cartões", Toast.LENGTH_LONG).show();
        if (libStarted) {
            PSCheckout.showListCards();
        }
        else {
            Toast.makeText(getActivity(), "Something Went wrong", Toast.LENGTH_LONG).show();
        }
    }


    @OnClick(R.id.wallet)
    public void paymentCreditCardWallet() {
        Toast.makeText(getActivity(), "Pagamento com cartão de credito carteira", Toast.LENGTH_LONG).show();
        //Valor do produto / serviço
        Double productPrice = 1.0;
        //id do produto
        String productId = "001";
        //Descrição do produto
        String description = "Produto Exemplo";

        PSCheckoutRequest psCheckoutRequest =
                new PSCheckoutRequest().withReferenceCode("123")
                        .withNewItem(description, "1", productPrice, productId);

        PSCheckout.payWallet(psCheckoutRequest, psCheckoutListener);
    }


    PSInstallmentsListener psInstallmentsListener = new PSInstallmentsListener() {
        @Override
        public void onSuccess(PSInstallmentsResponse responseVO) {
            //responseVO objeto com a lista de parcelas
            // Item da lista de parcelas InstallmentVO:
            // (String) installmentVO.getCardBrand() - Bandeira do cartão;
            // (int) - installmentVO.getQuantity() - quantidade da parcela;
            // (Double) - installmentVO.getAmount() - valor da parcela;
            // (Double) - installmentVO.getTotalAmount() - Valor total da transação parcelada;
            InstallmentVO installmentVO = responseVO.getInstallments().get(responseVO.getInstallments().size() - 1);
            Toast.makeText(getActivity(), "Parcelado em " +
                    installmentVO.getQuantity() + " x R$" + installmentVO.getAmount(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(String message) {
            // falha na requisicao
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    };

    private PSCheckoutListener psCheckoutListenerTransparent = new PSCheckoutListener() {
        @Override
        public void onSuccess(PSCheckoutResponse responseVO) {
            // responseVO.getCode() - Codigo da transação
            // responseVO.getStatus() - Status da transação
            // responseVO.getMessage() - Mensagem de retorno da transação(Sucesso/falha)
            Toast.makeText(getActivity(), "Success: " + responseVO.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(PSCheckoutResponse responseVO) {
            Toast.makeText(getActivity(), "Fail: " + responseVO.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProcessing() {
            Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();
        }
    };

    private PSBilletListener psBilletListener = new PSBilletListener() {
        @Override
        public void onSuccess(PaymentResponseVO responseVO) {
            // responseVO.getBookletNumber() - numero do codigo de barras do boleto
            // responseVO.getPaymentLink() - link para download do boleto
            Toast.makeText(getActivity(), "Gerou boleto com o numero: " + responseVO.getBookletNumber(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            // Error
            Toast.makeText(getActivity(), "Falha ao gerar boleto", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProcessing() {
            // Progress
            Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();
        }
    };

}
