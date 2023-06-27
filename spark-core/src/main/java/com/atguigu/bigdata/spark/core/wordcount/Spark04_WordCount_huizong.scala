package com.atguigu.bigdata.spark.core.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark04_WordCount_huizong {
  def main(args: Array[String]): Unit = {
    // 创建 Spark 运行配置对象
    val sparkConf = new SparkConf().setMaster("local").setAppName("WorkCount")
    // 创建 Spark 上下文环境对象（连接对象）
    val sc: SparkContext = new SparkContext(sparkConf)

    wordcount9(sc)

    sc.stop()
  }

  //todo 方法1 groupBy
  def wordcount1(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val groupRdd: RDD[(String, Iterable[String])] = words.groupBy(word => word)
    val wordCount: RDD[(String, Int)] = groupRdd.mapValues(
      iter => {
        iter.size
      }
    )
  }

  //todo 方法2 groupByKey
  def wordcount2(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val group: RDD[(String, Iterable[Int])] = words.map((_, 1)).groupByKey()   //map(_,1) 是因为groupByKey参数为Map
    val wordCount: RDD[(String, Int)] = group.mapValues(
      iter => {
        iter.size
      }
    )
  }

  //todo 方法3 reduceByKey
  def wordcount3(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val wordCount: RDD[(String, Int)] = words.map((_, 1)).reduceByKey(_ + _)

  }

  //todo 方法4 aggregateByKey  (初始值)(分组内规则,分组间规则)
  def wordcount4(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val wordOne: RDD[(String, Int)] = words.map((_, 1))
    val wordCount: RDD[(String, Int)] = wordOne.aggregateByKey(0)(_ + _, _ + _)

  }

  //todo 方法5 foldByKey  (初始值)(分组内与分组间规则相同)
  def wordcount5(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val wordOne: RDD[(String, Int)] = words.map((_, 1))
    val wordCount: RDD[(String, Int)] = wordOne.foldByKey(0)(_ + _)

  }

  //todo 方法6 combineByKey  (分组内第一个值可以被结构化,()=>{},()=>{})
  def wordcount6(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val wordOne: RDD[(String, Int)] = words.map((_, 1))
    val wordCount: RDD[(String, Int)] = wordOne.combineByKey(
      v => v, //分组内第一个值无变化
      (x: Int, y) => x + y,
      (x: Int, y: Int) => x + y
    )

  }

  //todo 方法7 行动算子 :countByKey
  def wordcount7(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))
    val wordOne: RDD[(String, Int)] = words.map((_, 1))
    val wordCount: collection.Map[String, Long] = wordOne.countByKey()

  }

  //todo 方法8 行动算子 :countByValue
  def wordcount8(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))

    val wordCount: collection.Map[String, Long] = words.countByValue()

  }

  //todo 方法9 行动算子 :reduce
  def wordcount9(sc: SparkContext) = {
    val rdd = sc.makeRDD(List("hello scala", "hello spark"))
    val words: RDD[String] = rdd.flatMap(_.split(" "))


    // 直接用.reduce 发现是聚合rdd的所有String元素,并没有count计数,所以考虑有可变Map
    val mapWord: RDD[mutable.Map[String, Long]] = words.map(
      word => {
        mutable.Map[String, Long]((word, 1))  //利用每个元素String,创建可变Map
      }
    )
    //    mapWord.foreach(println)    这里说明mapWord里的元素是一个个的Map(key->1)
    // 我们用reduce聚集RDD 中的所有元素，先聚合分区内数据，再聚合分区间数据,这里只有一个分区
    // 两两map元素合并,第二元素合并到一元素里,相同key的value聚合,不同key的value累计
    val wordCount: mutable.Map[String, Long] = mapWord.reduce(
      (map1, map2) => {
        map2.foreach {  // 模式匹配 用{}
          case (word, count) => {
            val newCount: Long = map1.getOrElse(word, 0L) + count  //0L是因为类型是Long
            map1.update(word, newCount)  //map的update方法,若存在word的key则更新对应value,若不存在,则添加
          }
        }
        map1  // 返回map1的值,此时map1的值合并了其他map的相同key或不同key
      }
    )
    println(wordCount)

  }

}
