package com.wickedwitch;

import com.wickedwitch.blockchain.Block;
import com.wickedwitch.blockchain.Blockchain;
import com.wickedwitch.constants.Constants;
import com.wickedwitch.cryptocurrency.Miner;
import com.wickedwitch.cryptocurrency.Transaction;
import com.wickedwitch.cryptocurrency.TransactionOutput;
import com.wickedwitch.cryptocurrency.Wallet;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        //Security provider - Bouncy Castle
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //create wallets + blockchain + the single miner in the network
        Wallet userA = new Wallet();
        Wallet userB = new Wallet();
        Wallet lender = new Wallet();
        Blockchain blockchain = new Blockchain();
        Miner miner = new Miner();

        Transaction genesisTransaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 500, null);
        genesisTransaction.generateSignature(lender.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getReceiver(), genesisTransaction.getAmount(), genesisTransaction.getTransactionId()));
        Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));

        System.out.println("Constructing the GENESIS block...");
        Block genesisBlock = new Block(Constants.GENESIS_PREV_HASH);
        genesisBlock.addTransaction(genesisTransaction);
        miner.mine(genesisBlock, blockchain);

        Block block1 = new Block(genesisBlock.getHash());
        System.out.println("\n userA's balance is: " + userA.calculateBalance());
        System.out.println("\n userA tries to send 120 coins to userB...");
        block1.addTransaction(userA.transferMoney(userB.getPublicKey(), 120));
        miner.mine(block1, blockchain);
        System.out.println("\n userA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\n userA sends 600 coins...");
        block2.addTransaction(userA.transferMoney(userB.getPublicKey(), 600));
        miner.mine(block2, blockchain);
        System.out.println("\n userA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\n userB attempting to send 110 coins to userA...");
        block3.addTransaction(userB.transferMoney(userA.getPublicKey(), 110));
        System.out.println("\n userA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());
        miner.mine(block3, blockchain);

        System.out.println("Miner's reward: " + miner.getReward());
    }
}
