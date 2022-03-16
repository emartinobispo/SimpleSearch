package org.simplesearch.utils

import org.simplesearch.domain.{Input, PercentageFileScore, WordScores}
import org.simplesearch.utils.TextFileUtils.TextFile

import java.io.File
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

object DirectoryUtils {
  implicit class Directory(val f: File) {
    val directoryFilesList: List[TextFile] = {
      val these = f.listFiles
      println(these.map(_.getName).mkString("Array(", ", ", ")"))
      these.filter(_.isFile).map(TextFile) ++ these.filter(_.isDirectory).flatMap(dir => dir.directoryFilesList) toList
    }

    private def computeWordScoresOfTheTextFilesInDirectory(input: Input): List[WordScores] =
      directoryFilesList map { textFile =>
        textFile.computeWordScore(input.weightedWords)
      }

    private def computeMaxScore(input: Input): Map[String, Int] = {
      val fileWordScoresList = computeWordScoresOfTheTextFilesInDirectory(input)
      fileWordScoresList.flatMap(_.weightedWordScores)
        .groupBy(_.weightedWord.word)
        .mapValues(_.map(_.score).max)
    }

    private def computeWordScoreOfTheDirectoryFiles(input: Input, maxScores: Map[String, Int]): List[PercentageFileScore] =
      directoryFilesList.foldLeft(ListBuffer.empty[PercentageFileScore]) { (accPercFilesScores, textFile) =>
        val percFileScore = textFile.computePercentageScore(input, maxScores)
        accPercFilesScores :+ percFileScore
      } toList

    def getTop10MatchingFiles(input: Input): List[PercentageFileScore] = {
      val maxScores = computeMaxScore(input)
      val percentageFileScores = computeWordScoreOfTheDirectoryFiles(input, maxScores) filter (!_.score.equals(0))

      percentageFileScores sortWith (_.score > _.score) take 10
    }
  }
}
