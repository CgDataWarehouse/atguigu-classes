package com.atguigu.bigdata.spark.core.acc

import scala.collection.mutable

object Test1 {
  def main(args: Array[String]): Unit = {

    // 合并两个Map
    val map1 = Map("a" -> 1, "b" -> 2, "c" -> -3) //可变的
    val map2 = mutable.Map("a" -> 4, "b" -> 6, "c" -> -5, "d" -> 0)


    // mergeMap 其实就是map2 ,kv是map1的元素元组
    // 遍历map2的元素,getOrElse(map1的k值),有则 +v ,无则赋值0 + v
    val map3=map1.foldLeft(map2)(
      (mergeMap,kv) =>{
        val k =kv._1
        val v =kv._2
        mergeMap(k) = mergeMap.getOrElse(k,0) + v
        mergeMap
      }
    )

    println(map3)

  }
}
