package com.wickedwitch.cryptocurrency;

import com.wickedwitch.blockchain.Blockchain;

import java.security.KeyPair;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(){
        KeyPair keyPair = CryptographyHelper.elipticCurveCrypto();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public double calculateBalance(){

        double balance = 0;

        for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()){
            TransactionOutput transactionOutput = item.getValue();
            if(transactionOutput.isMine(publicKey)) {
                balance += transactionOutput.getAmount() ;
            }
        }

        return balance;
    }

    public Transaction transferMoney(PublicKey receiver, double amount){
        if(calculateBalance() < amount){
            System.out.println("Invalid transaction. Not enough funds...");
            return null;
        }
        //Store inputs for transactions in Array:
        List<TransactionInput> inputs = new ArrayList<TransactionInput>();

        //Find unspent transactions (the blockchain stores all UTXOs)
        for(Map.Entry<String, TransactionOutput> item : Blockchain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();

            if(UTXO.isMine(this.publicKey)){
                inputs.add(new TransactionInput(UTXO.getId()));
            }
        }

        //Create new transaction
        Transaction newTransaction = new Transaction(publicKey, receiver, amount, inputs);

        //sender signs transaction
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
