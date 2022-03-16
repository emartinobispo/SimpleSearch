package org.simplesearch.domain

class Input(userInput: String){
  private val words: Array[String] = userInput.replaceAll("[^A-Za-z0-9' ]", "").split(" ")

  val wordsCount: Int = words.length

  val weightedWords: Set[WeightedWord] = words
    .groupBy(identity)
    .mapValues(_.length)
    .map(iw => WeightedWord(iw._1, iw._2))
    .toSet

  val distinctWordsCount: Int = weightedWords.size
}
