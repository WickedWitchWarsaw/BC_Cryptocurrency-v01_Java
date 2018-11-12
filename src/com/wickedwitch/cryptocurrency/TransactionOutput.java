package com.wickedwitch.cryptocurrency;

import java.security.PublicKey;

public class TransactionOutput {

    //Identifier of the transaction output
    private String id;

    //Transaction id of the parent (transaction it was created in)
    private String parentTransactionId;

    //New owner of the Coins/Transaction
    private PublicKey receiver;

    private double amount;


    public TransactionOutput(PublicKey receiver, double amount, String parentTransactionId) {
        this.receiver = receiver;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        generateId();
    }

    private void generateId() {
        this.id = CryptographyHelper.generateHash(receiver.toString()+Double.toString(amount)+parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey){
        return publicKey == receiver;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
