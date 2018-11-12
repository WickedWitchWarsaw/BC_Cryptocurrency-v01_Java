package com.wickedwitch.cryptocurrency;

import com.wickedwitch.blockchain.Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    //Id of the transaction is a hash
    private String transactionId;
    private PublicKey sender;
    private PublicKey receiver;

    //Amount of coins the transaction sends from sender to receiver
    private double amount;

    //Signature to ensure the only the receiver can spend the coins
    private byte[] signature;

    //Every transaction has inputs and outputs
    public List<TransactionInput> inputs;
    public List<TransactionOutput> outputs;


    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs) {
        this.inputs = new ArrayList<TransactionInput>();
        this.outputs = new ArrayList<TransactionOutput>();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        calculateHash();
    }

    public boolean verifyTransaction() {

        if (!verifySignature()) {
            System.out.println("Invalid transaction because of invalid signature...");
            return false;
        }

        //gather unspent transactions (to consider the inputs)
        for (TransactionInput transactionInput : inputs) {
            transactionInput.setUTXO(Blockchain.UTXOs.get(transactionInput.getTransactionOutputId()));
        }

        //transactions have 2 parts: send an amount to the receiver + send the (balance-amount) back to the sender
        outputs.add(new TransactionOutput(this.receiver, amount, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(this.sender, getInputsSum() - amount, transactionId)); //send the left over 'change' back to sender

        //Remove transactions inputs from blockchain's UTXOs list - they have been spent
        for (TransactionInput transactionInput : inputs) {
            if (transactionInput.getUTXO() != null) {
                Blockchain.UTXOs.remove(transactionInput.getUTXO().getId());
            }
        }

        //Outputs will be Inputs for other transactions (put them in the blockchain's UTXOs)
        for (TransactionOutput transactionOutput : outputs) {
            Blockchain.UTXOs.put(transactionOutput.getId(), transactionOutput);
        }

        return true;
    }

    private double getInputsSum() {

        double sum = 0;

        for (TransactionInput transactionInput : inputs) {
            if (transactionInput.getUTXO() != null) {
                sum += transactionInput.getUTXO().getAmount();
            }
        }
        return sum;
    }


    public void generateSignature(PrivateKey privateKey) {
        String data = sender.toString() + receiver.toString() + Double.toString(amount);
        signature = CryptographyHelper.applyECDSASignature(privateKey, data);
    }

    public boolean verifySignature() {
        String data = sender.toString() + receiver.toString() + Double.toString(amount);
        return CryptographyHelper.verifyECDSASignature(sender, data, signature);
    }

    private void calculateHash() {
        String hashData = sender.toString() + receiver.toString() + Double.toString(amount);
        this.transactionId = CryptographyHelper.generateHash(hashData);
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
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

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }
}
