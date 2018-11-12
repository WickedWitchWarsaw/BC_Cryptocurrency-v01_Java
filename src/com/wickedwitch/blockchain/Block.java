package com.wickedwitch.blockchain;

import com.wickedwitch.constants.Constants;
import com.wickedwitch.cryptocurrency.CryptographyHelper;
import com.wickedwitch.cryptocurrency.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    private int id;
    private int nonce;
    private Long timeStamp;
    private String previousHash;
    private String hash;
    private List<Transaction> transactions;


    public Block(String previousHash) {
        this.transactions = new ArrayList<Transaction>();
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        generateBlockHash();
    }

    public void generateBlockHash(){
        String dataToHash = Integer.toString(id)+previousHash+Long.toString(timeStamp)+transactions.toString()+Integer.toString(nonce);
        String hashValue = CryptographyHelper.generateHash(dataToHash);
        this.hash = hashValue;
    }


    public void incrementNonce(){
        this.nonce++;
    }

    public boolean addTransaction(Transaction transaction){

        if(transaction == null) return false;

        if((!previousHash.equals(Constants.GENESIS_PREV_HASH))){
            if((!transaction.verifyTransaction())){
                System.out.println("Transaction is not valid...");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction is valid and added to block " + this);
        return true;
    }

    public String getHash() {
        return this.hash;
    }
}
