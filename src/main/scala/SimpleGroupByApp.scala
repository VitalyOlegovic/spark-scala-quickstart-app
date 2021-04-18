/* SimpleGroupByApp.scala */

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SimpleGroupByApp {
  def main(args: Array[String]) {
    val logFile = "./src/main/resources/pg10.txt" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Accumulator Application")
      .config("spark.master", "local")
      .getOrCreate()
    import spark.implicits._
    
    val logData = spark.read.textFile(logFile).cache()
    val countsMap = collection.mutable.Map[String, Long]()
    val splittedLogData = logData.flatMap(_.toLowerCase.split("\\W+"))
    val filteredLogData = splittedLogData.filter(args.contains(_))
    val sumLogData = filteredLogData
        .map(word => (word,1))
        .groupBy(col("_1"))
        .sum("_2")
    sumLogData.show()

    val renamedColumnsLogData = sumLogData
        .withColumnRenamed("_1", "word")
        .withColumnRenamed("sum(_2)", "count")
    renamedColumnsLogData.show()

    renamedColumnsLogData.createOrReplaceGlobalTempView("words")
    val devil = spark.sql("select * from global_temp.words where word='devil'")
    devil.show()

    spark.stop()
  }
}