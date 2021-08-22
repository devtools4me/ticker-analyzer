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
    Chart.create.asSwing.withLinePlot(close, new AsJavaConsumer[Chart[XyPlot[java.time.LocalDate]]](chart => {
      chart.title().withText("Close");
      chart.plot().style("Close").withLineWidth(1f).withColor(Color.BLUE);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText("Price");
      chart.writerPng(os, w, h, false);
    }))
  }
}

object Ohlcv {
  def of(csv: String): Try[Ohlcv] = {
    str2is(csv) { is =>
      val df = DataFrame.read(is).csv(
        classOf[LocalDate],
        new AsJavaConsumer[CsvSource.Options](x => x.setRowKeyColumnName("Date"))
      )
      new Ohlcv(df)
    }
  }
}