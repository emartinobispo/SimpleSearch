package org.simplesearch

import org.scalatest.FunSuite
import org.simplesearch.domain.{Input, PercentageFileScore, WeightedWord, WeightedWordScore, WordScores}
import org.simplesearch.utils.TextFileUtils._

import java.io.File

class TextFileUtilsTest extends FunSuite{
  val testFile =  new File("./input/testFile1.txt")
  val textFile: TextFile = TextFile(testFile)

  test("Textfile.wordMap") {
    assert( textFile.wordMap === Map[String, Int](
      ("test", 2), ("file", 1), ("to", 1)
    ))
  }

  test("TextFile.computeWordScore") {
    val inputWeightedWords: Set[WeightedWord]= Set(
      WeightedWord("test", 1),
      WeightedWord("file", 3)
    )
    assert(
      textFile.computeWordScore(inputWeightedWords) === WordScores(List(
        WeightedWordScore(WeightedWord("test", 1), 2),
        WeightedWordScore(WeightedWord("file", 3), 1)
      ))
    )
  }

  test("TextFile.computePercentageScore") {
    val maxScores = Map(
      ("test", 2),
      ("file", 3)
    )

    val input1: Input = new Input("test")
    assert(
      textFile.computePercentageScore(input1, maxScores) ===
        PercentageFileScore(textFile.f.getName, 100)
    )

    val input2: Input = new Input("test file")
    assert(
      textFile.computePercentageScore(input2, maxScores) ===
        PercentageFileScore(textFile.f.getName, 100)
    )

    val input3: Input = new Input("test test file")
    assert(
      textFile.computePercentageScore(input3, maxScores) ===
        PercentageFileScore(textFile.f.getName, 100)
    )

    val input4: Input = new Input("testing file 1")
    assert(
      textFile.computePercentageScore(input4, maxScores) ===
        PercentageFileScore(textFile.f.getName, 11)
    )

    val input5: Input = new Input("hi")
    assert(
      textFile.computePercentageScore(input5, maxScores) ===
        PercentageFileScore(textFile.f.getName, 0)
    )
  }
}