import scala.io.Source

object PlainScala{
    def main(args: Array[String]) {
        val logFile = "./src/main/resources/pg10.txt" // Should be some file on your system
        val lines = Source.fromFile(logFile)
            .getLines.toSeq
        val splittedLines = lines.flatMap(_.toLowerCase.split("\\W+"))
        val filteredLines = splittedLines.filter(args.contains(_))
        val wordCount = filteredLines
            .map( x => (x,1) )
            .groupBy(_._1)
            .mapValues(seq => seq.map(_._2).reduce(_+_))
        println(wordCount)
    }
}