package com.wickedwitch.cryptocurrency;

import com.wickedwitch.blockchain.Block;
import com.wickedwitch.blockchain.Blockchain;
import com.wickedwitch.constants.Constants;

public class Miner {

    private double minerReward;

    public void mine(Block block, Blockchain blockchain) {

        while(notGoldenHash(block)) {
            block.generateBlockHash();
            block.incrementNonce();
        }

        System.out.println(block+" has just mined...");
        System.out.println("Hash is: "+block.getHash());


        blockchain.addBlock(block);
        minerReward+=Constants.MINERS_REWARD;
    }

    public boolean notGoldenHash(Block block) {
        String leadingZeros = new String(new char[Constants.DIFFICULTY]).replace('\0', '0');
        return !block.getHash().substring(0,Constants.DIFFICULTY).equals(leadingZeros);
    }

    public double getReward() {
        return this.minerReward;
    }

}
