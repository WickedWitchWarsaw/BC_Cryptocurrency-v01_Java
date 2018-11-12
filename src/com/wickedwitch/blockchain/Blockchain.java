package com.wickedwitch.blockchain;

import com.wickedwitch.cryptocurrency.TransactionOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Blockchain {

    public static ArrayList<Block> blockchain;
    public static Map<String, TransactionOutput> UTXOs;

    public Blockchain() {
        Blockchain.UTXOs = new HashMap<String, TransactionOutput>();
        Blockchain.blockchain = new ArrayList<>();
    }


    public void addBlock(Block block) {
        Blockchain.blockchain.add(block);
    }

    public int blockchainSize() {
        return blockchain.size();
    }

    @Override
    public String toString() {

        String blockchainSting = "";
        for(Block block : blockchain){
            blockchainSting += block.toString() + "\n";
        }
        return blockchainSting;
    }
}
