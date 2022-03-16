package org.simplesearch.utils

import org.simplesearch.domain.{Input, PercentageFileScore, WeightedWord, WeightedWordScore, WordScores}

import java.io.File
import scala.collection.mutable
import scala.io.Source
import scala.language.postfixOps

object TextFileUtils {
  implicit class TextFile(val f: File) {
    private val text: String = Source.fromFile(f).mkString

    private val words: Array[String] = text.replaceAll("[^A-Za-z0-9' ]", "").split(" ")

    val wordMap: Map[String, Int] = {
      val wordsMap = mutable.Map.empty[String, Int] withDefaultValue 0
      words foreach (wordsMap(_) += 1)
      wordsMap toMap
    }

    def computeWordScore(inputWeightedWords: Set[WeightedWord]): WordScores =
      WordScores(
        inputWeightedWords collect {
          case inputWeightedWord if wordMap.contains(inputWeightedWord.word) =>
            WeightedWordScore(
              inputWeightedWord, wordMap(inputWeightedWord.word)
            )
        } toList
      )


    def computePercentageScore(input: Input, maxScores: Map[String, Int]): PercentageFileScore = {
      val fileWordScores = computeWordScore(input.weightedWords)
      val weightedWordScores: List[WeightedWordScore] = fileWordScores.weightedWordScores

      val percentageFileScore: Double =
        if (weightedWordScores.count(!_.score.equals(0)).equals(input.distinctWordsCount)) 100
        else weightedWordScores.foldLeft(0.toDouble) { (acc, wordScore) =>
          acc + (wordScore.score.toDouble * wordScore.weightedWord.weight * 100) / (maxScores(wordScore.weightedWord.word) * input.wordsCount)
        }

      PercentageFileScore(f.getName, percentageFileScore.toInt)
    }
  }
}
