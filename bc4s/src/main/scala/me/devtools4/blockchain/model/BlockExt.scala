package me.devtools4.blockchain.model

import org.apache.commons.codec.binary.Hex

import java.security.MessageDigest
import scala.annotation.tailrec

object BlockExt {

  implicit class BlockOps(b: Block) {
    implicit def generateHash(): Block = generateHash(0)

    @tailrec
    private def generateHash(nonce: NonceType): Block = {
      val data = s"${b.id}_${b.previousHash}_${b.timeStamp}_${nonce}_${b.transaction}"
      val out = hash(data)
      out match {
        case s if isHashGood(s) => b.copy(nonce = nonce, hash = s)
        case _ => generateHash(nonce + 1)
      }
    }
  }

  def hash(in: String): String = Hex.encodeHexString(hash(in.getBytes()))

  def hash(in: Array[Byte]): Array[Byte] = {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.digest(in)
  }

  private def isHashGood(s: String) = {
    s.startsWith("0000")
  }
}