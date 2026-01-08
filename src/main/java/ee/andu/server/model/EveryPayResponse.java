package ee.andu.server.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class EveryPayResponse {
    public String account_name;
    public String order_reference;
    public Object email;
    public Object customer_ip;
    public String customer_url;
    public Date payment_created_at;
    public double initial_amount;
    public double standing_amount;
    public String payment_reference;
    public String payment_link;
    public ArrayList<Object> payment_methods;
    public String api_username;
    public Object warnings;
    public Object stan;
    public Object fraud_score;
    public String payment_state;
    public Object payment_method;
    public String currency;
    public Object applepay_merchant_identifier;
    public String descriptor_country;
    public Object googlepay_merchant_identifier;
}
