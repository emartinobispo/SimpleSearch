package org.simplesearch

import org.simplesearch.domain._
import org.simplesearch.utils.DirectoryUtils._

import java.io.File
import scala.language.postfixOps

object SimpleSearch {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) throw new IllegalArgumentException("No directory given to index.")

    val inputFile = new File(args.head)
    if (!inputFile.isDirectory) throw  new IllegalArgumentException("The given path does not correspond to a directory")

    val directory = Directory(inputFile)
    println(s"${directory.directoryFilesList.size} files read in directory ${directory.f.getName}")

    while (true) {
      val strInput: String = scala.io.StdIn.readLine("search> ")

      strInput match {
        case ":quit" => System.exit(0)
        case _ =>
          val input: Input = new Input(strInput)
          val ranking: List[PercentageFileScore] = directory.getTop10MatchingFiles(input)

          if (ranking.nonEmpty) {
            ranking foreach (result => println(result.fileName + " : " + result.score + "%"))
          } else println("no matches found")
      }
    }
  }
}
