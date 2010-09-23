package swp.service;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import swp.model.AuctionPayment;
import swp.web.exception.AuctionPaymentExistException;

public class InMemoryAuctionPaymentService implements AuctionPaymentService {

    private ArrayList<AuctionPayment> payments;

    public InMemoryAuctionPaymentService(){
        payments = new ArrayList<AuctionPayment>();
    }

    public void create(AuctionPayment payment) throws AuctionPaymentExistException{
        if(payments.contains(payment))
            throw new AuctionPaymentExistException("The AuctionPayment does all ready exist");
        payments.add(payment);
    }

    @Override
    public boolean excist(URL host, URI id) {
        AuctionPayment temp = new AuctionPayment(host, id, null);
        System.out.println(payments.contains(temp));
        return payments.contains(temp);
    }

    @Override
    public List<AuctionPayment> getPaymentsByUser(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AuctionPayment getPayment(URL host, URI id) {
        AuctionPayment temp = new AuctionPayment(host, id, null);
        int index = payments.indexOf(temp);
        if(index != -1){
            return payments.get(index);
        } 
        return null;
    }




}
