package me.devtools4.ticker.df

import com.d3x.morpheus.csv.CsvSource
import com.d3x.morpheus.frame.{DataFrame, DataFrameColumn}
import com.d3x.morpheus.viz.chart.Chart
import com.d3x.morpheus.viz.chart.xy.XyPlot

import java.awt.Color
import java.io.{ByteArrayOutputStream, OutputStream}
import java.time.LocalDate
import scala.jdk.FunctionWrappers.{AsJavaConsumer, AsJavaPredicate}
import scala.util.{Try, Using}

class Ohlcv(df: DataFrame[LocalDate, String]) {
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

object Ohlcv {
  import me.devtools4.ticker.ops._
  def of(csv: String): Try[Ohlcv] = {
    csv.toIS { is =>
      val df = DataFrame.read(is).csv(
        classOf[LocalDate],
        new AsJavaConsumer[CsvSource.Options](x => x.setRowKeyColumnName("Date"))
      )
      new Ohlcv(df)
    }
  }
}