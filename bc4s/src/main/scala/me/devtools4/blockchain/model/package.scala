package me.devtools4.blockchain

import java.time.{LocalDateTime, ZoneId}

package object model {
  type RewardType = Double
  type NonceType = Int
  type Transaction = String
  type BlockId = Long

  val REWARD: RewardType = 10.0
  val GENESIS_PREV_HASH = "0000000000000000000000000000000000000000000000000000000000000000"

  case class Block(
                    id: BlockId,
                    transaction: Transaction,
                    timeStamp: Long,
                    previousHash: String,
                    nonce: NonceType,
                    hash: String) {
    def incrementNonce: Block = copy(nonce = nonce + 1)
  }

  case class BlockChain(chain: List[Block]) {
    def add(b: Block): BlockChain = BlockChain(chain :+ b)
  }

  case class MinerState(reward: RewardType, bc: BlockChain) {
    def change(delta: RewardType, block: Block): MinerState = copy(reward = reward + delta, bc = bc.add(block))
  }

  object Block {
    def apply(id: BlockId, transaction: Transaction, previousHash: String): Block = Block(
      id = id,
      transaction = transaction,
      timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond,
      previousHash = previousHash,
      nonce = 0,
      hash = ""
    )
  }

  object MinerState {
    def apply(): MinerState = MinerState(0.0, BlockChain(Nil))

    def apply(bc: BlockChain): MinerState = MinerState(0.0, bc)
  }
}