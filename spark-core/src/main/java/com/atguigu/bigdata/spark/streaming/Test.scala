package com.atguigu.bigdata.spark.streaming

object Test {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5,6,7,8,9,10)
    // size 窗口长度   step 步长
    val iterator: Iterator[List[Int]] = list.sliding(3, 2)
    iterator.foreach(println)
  }
}
