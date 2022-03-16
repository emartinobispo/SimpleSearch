package org.simplesearch

import org.scalatest.{FunSuite, PrivateMethodTester}
import org.simplesearch.domain.{Input, PercentageFileScore}
import org.simplesearch.utils.DirectoryUtils.Directory

import java.io.File

class DirectoryUtilsTest extends FunSuite {
  val testDirectory =  new File("./input")
  val directory: Directory = new Directory(testDirectory)

  test("Directory.directoryFilesList") {
    assert( directory.directoryFilesList.map(_.f.getPath) == List[String](
      new File(s"${testDirectory.getPath}/testFile1.txt").getPath,
      new File(s"${testDirectory.getPath}/testFile2.txt").getPath,
      new File(s"${testDirectory.getPath}/subDir/testFile3.txt").getPath,
      new File(s"${testDirectory.getPath}/subDir/testFile4.txt").getPath,
      new File(s"${testDirectory.getPath}/subDir/testFile5.txt").getPath
    ))
  }

  test("Directory.getTop10MatchingFiles") {
    val input = new Input("testing word scores computation")
    assert(directory.getTop10MatchingFiles(input) == List())

    val input2 = new Input("test")
    assert(directory.getTop10MatchingFiles(input2) == List(
      PercentageFileScore("testFile1.txt",100),
      PercentageFileScore("testFile2.txt",100),
      PercentageFileScore("testFile3.txt",100))
    )

    val input3 = new Input("test file")
    assert(directory.getTop10MatchingFiles(input3) == List(
      PercentageFileScore("testFile1.txt",100),
      PercentageFileScore("testFile2.txt",100),
      PercentageFileScore("testFile3.txt",50),
      PercentageFileScore("testFile5.txt",50),
      PercentageFileScore("testFile4.txt",25)
    ))

    val input4 = new Input("test file file file")
    assert(directory.getTop10MatchingFiles(input4) == List(
      PercentageFileScore("testFile1.txt",100),
      PercentageFileScore("testFile2.txt",100),
      PercentageFileScore("testFile5.txt",75),
      PercentageFileScore("testFile4.txt",37),
      PercentageFileScore("testFile3.txt",25)
    ))
  }
}
