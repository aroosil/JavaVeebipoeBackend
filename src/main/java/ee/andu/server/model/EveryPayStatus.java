package ee.andu.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class EveryPayStatus {
    private String account_name;
    private String order_reference;
    private Object email;
    private String customer_ip;
    private String customer_url;
    private Date payment_created_at;
    private int initial_amount;
    private int standing_amount;
    private String payment_reference;
    private String payment_link;
    private String api_username;
    private int stan;
    private int fraud_score;
    private String payment_state;
    private String payment_method;
    private Date transaction_time;

    private Object ob_details;
    private Object warnings;
}
