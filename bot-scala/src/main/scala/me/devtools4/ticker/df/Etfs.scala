package me.devtools4.ticker.df

import com.d3x.morpheus.frame.DataFrame

import scala.io.Source
import scala.jdk.javaapi.OptionConverters
import scala.util.Try

class Etfs(df: DataFrame[Integer, String]) {
  def find(sym: String): Option[Etf] = {
    val opt = df.rows.first(x => x.getValue("Symbol").equals(sym.toUpperCase))
      .map(x => Etf(sym,
        x.getValue("Start Date"),
        x.getValue("Name"),
        x.getValue("Segment"),
        x.getValue("Issuer"),
        x.getValue("Expense Ratio"),
        x.getValue("AUM")))
    OptionConverters.toScala(opt)
  }
}

object Etfs {

  def of(resource: String): Try[Etfs] = {
    str2is(Source.fromResource(resource).mkString) { is =>
      new Etfs(DataFrame.read(is).csv)
    }
  }
}