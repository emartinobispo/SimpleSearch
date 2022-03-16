package org.simplesearch

import org.scalatest.FunSuite
import org.simplesearch.domain.{Input, WeightedWord}

class InputTest extends FunSuite{
  val input = new Input("Testing with a test to test")

  test("Input.wordsCount") {
    assert(input.wordsCount === 6)
  }

  test("Input.weightedWords") {
    assert(input.weightedWords === Set[WeightedWord](
      WeightedWord("Testing", 1),
      WeightedWord("with", 1),
      WeightedWord("a", 1),
      WeightedWord("test", 2),
      WeightedWord("to", 1)
    ))
  }

  test("Input.distinctWordsCount") {
    assert(input.distinctWordsCount === 5)
  }
}
