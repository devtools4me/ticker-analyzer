package me.devtools4.ticker

import com.d3x.morpheus.csv.CsvSource
import com.d3x.morpheus.frame.{DataFrame, DataFrameColumn}
import com.d3x.morpheus.viz.chart.Chart
import com.d3x.morpheus.viz.chart.xy.XyPlot
import me.devtools4.ticker.api.Etf

import java.awt.Color
import java.io.{ByteArrayOutputStream, OutputStream}
import java.time.LocalDate
import scala.jdk.FunctionWrappers.{AsJavaConsumer, AsJavaPredicate}
import scala.jdk.javaapi.OptionConverters
import scala.util.{Try, Using}

package object df {

  type Ohlcv = DataFrame[LocalDate, String]

  type Etfs = DataFrame[Integer, String]

  def df(csv: String): Try[Ohlcv] = {
    import me.devtools4.ticker.ops._
    csv.toIS { is =>
      DataFrame.read(is).csv(
        classOf[LocalDate],
        new AsJavaConsumer[CsvSource.Options](x => x.setRowKeyColumnName("Date"))
      )
    }
  }

  implicit class OhlcvHelper(df: Ohlcv) {
    def png(col: String, w: Integer, h: Integer): Try[Array[Byte]] =
      Using(new ByteArrayOutputStream()) { os =>
        png(os, col, w, h)
        os.toByteArray
      }

    def png(os: OutputStream, col: String, w: Integer, h: Integer): Unit = {
      val close = df.cols.select(
        new AsJavaPredicate[DataFrameColumn[LocalDate, String]](x => x.key.equalsIgnoreCase(col))
      )
      Chart.create.asSwing.withLinePlot(close, new AsJavaConsumer[Chart[XyPlot[java.time.LocalDate]]](chart => {
        chart.title().withText("Close");
        chart.plot().style("Close").withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.writerPng(os, w, h, false);
      }))
    }

    def smaPng(col: String, w: Integer, h: Integer): Try[Array[Byte]] =
      Using(new ByteArrayOutputStream()) { os =>
        smaPng(os, col, w, h)
        os.toByteArray
      }

    def smaPng(os: OutputStream, col: String, w: Integer, h: Integer): Unit = {
      val close = df.cols.select(
        new AsJavaPredicate[DataFrameColumn[LocalDate, String]](x => x.key.equalsIgnoreCase(col))
      )
      val shortSize = 40
      val st = close
        .cols()
        .stats().rolling(shortSize).mean()
        .cols()
        .replaceKey(col, "Short Term")
      val longSize = 70
      val lt = close
        .cols()
        .stats().rolling(longSize).mean()
        .cols()
        .replaceKey(col, "Long Term")
      val sam = DataFrame.concatColumns(close, st, lt);
      Chart.create().asSwing().withLinePlot(sam, new AsJavaConsumer[Chart[XyPlot[java.time.LocalDate]]](chart => {
        chart.title().withText("Close")
        chart.plot().axes().domain().label().withText("Date")
        chart.plot().axes().range(0).label().withText("Price")
        chart.plot().style(col).withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().style("Short Term").withLineWidth(1f).withColor(Color.GREEN);
        chart.plot().style("Long Term").withLineWidth(1f).withColor(Color.ORANGE);
        chart.legend().on().bottom();
        chart.writerPng(os, w, h, false)
      }))
    }
  }

  def etfs(resource: String): Try[Etfs] = {
    import me.devtools4.ticker.ops._
    ResourceName(resource).str.toIS { is =>
      DataFrame.read(is).csv
    }
  }

  implicit class EtfsHelper(etfs: Etfs) {
    def find(sym: String): Option[Etf] = {
      val opt = etfs.rows.first(x => x.getValue("Symbol").equals(sym.toUpperCase))
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
}