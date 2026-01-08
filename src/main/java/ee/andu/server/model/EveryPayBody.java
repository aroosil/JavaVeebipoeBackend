package ee.andu.server.model;

import lombok.Data;

@Data
public class EveryPayBody {
    private String account_name;
    private String nonce;
    private String timestamp;
    private double amount;
    private String order_reference;
    private String customer_url;
    private String api_username;

//    private int structured_reference;
//    private String payment_description;
//    private boolean request_token;
//    private String token_agreement;
//    private String email;
//    private Object phone_number;
//    private String customer_ip;
//    private String locale;
//    private String preferred_country;
//    private String billing_city;
//    private String billing_country;
//    private String billing_line1;
//    private Object billing_line2;
//    private Object billing_line3;
//    private int billing_postcode;
//    private String billing_state;
//    private String shipping_city;
//    private String shipping_country;
//    private String shipping_line1;
//    private Object shipping_line2;
//    private Object shipping_line3;
//    private int shipping_postcode;
//    private String shipping_state;
//    private boolean mobile_payment;
//    private boolean token_consent_agreed;
//    private Object integration_details;
}
