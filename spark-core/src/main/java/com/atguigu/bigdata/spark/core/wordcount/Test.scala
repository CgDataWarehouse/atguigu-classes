package com.atguigu.bigdata.spark.core.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.mutable

object Test {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc = new SparkContext(sparkConf)

    //    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    //
    //
    //    val ints: List[(Int,Int)] = List((1,2),(2, 3))
    //    val iter: Iterator[(Int,Int)] = ints.iterator
    //    iter.foreach(println)
    //
    //
    //    val dataRDD: RDD[Int] = sc.makeRDD(List(5,2,3,4),2)
    //    val result: RDD[Int] = dataRDD.mapPartitionsWithIndex{
    //      (index,iter)=>{
    //        if(index==1){
    //          iter
    //        }else{
    //          Nil.iterator
    //        }
    //      }
    //    }
    //    result.foreach(println)

    //    for (list <- Array(List(0), List(1, 0), List(0, 0, 0), List(1, 0, 0), List(88),5)) {
    //      val result: Any = list match {
    //        case List(0) => "0"
    //        case List(x, y) => x + "," + y
    //        case List(x, y, z) => x + "," + y + "," + z
    //        case List(x) => x
    //        case _ => "something else"
    //
    //      }
    //      println(result)
    //    }
    //val rdd: RDD[Int] = sc.parallelize(1 to 4,2)
    //    val max: RDD[Int] = rdd.glom().map(_.max)
    //    max.collect.foreach(println)
    //    sc.stop()

    //    val rdd: RDD[String] = sc.makeRDD(List("hello", "scala", "spark","Hi"))
    //    val charAtRdd: RDD[(Char, Iterable[String])] = rdd.groupBy(_.charAt(0))
    //    charAtRdd.collect().foreach(println)

    //    val rdd = sc.textFile("datas/apache.log")
    //    val mapRdd: RDD[(String, Int)] = rdd.map {
    //      line => {
    //        val arr: Array[String] = line.split(" ")
    //        (arr(3).substring(11, 13), 1)
    //      }
    //    }
    //    val result: RDD[(String, Int)] = mapRdd.groupBy(_._1)
    //      .map {
    //        kv => {
    //          (kv._1, kv._2.size)
    //        }
    //      }
    //    result.collect().foreach(println)
    //    val mapRdd: RDD[(String, Int)] = rdd.map {
    //      line => {
    //        val arr: Array[String] = line.split(" ")
    //        val time: String = arr(3)
    //        // 输入目标的时间格式
    //        val inFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
    //        // 把字符串转换成Date
    //        val date: Date = inFormat.parse(time)
    //        // 输出目标的时间格式 --小时
    //        val outFormat = new SimpleDateFormat("HH")
    //        // 把Date转成HH
    //        val hour: String = outFormat.format(date)
    //        (hour, 1)
    //      }
    //    }
    //    val result: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    //
    //    result.collect().foreach(println)

    //    val newRdd: RDD[String] = rdd.filter {
    //      line => {
    //        val arr: Array[String] = line.split(" ")
    //        val str: String = arr(3)
    //        val inFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
    //        // string 转 date
    //        val date: Date = inFormat.parse(str)
    //        // 目标时间格式
    //        val outFormat = new SimpleDateFormat("dd/MM/yyyy")
    //        // date 转指定string格式
    //        val time: String = outFormat.format(date)
    //        time.equals("17/05/2015")
    //      }
    //    }.map {
    //      line => {
    //        line.split(" ")(6)
    //      }
    //    }
    //
    //    newRdd.collect().foreach(println)

    //    val dataRDD = sc.makeRDD(List(1, 2, 3, 4, 1, 2
    //    ), 1)
    //    //    val dataRDD1 = dataRDD.distinct() //去重
    //    //    dataRDD1.foreach(println)
    //    val result: RDD[Int] = dataRDD.groupBy(word => word).map(_._1) // groupby 去重
    //    println(result.collect().toList)
    //    val rdd1: RDD[Int] = sc.makeRDD(List(2,1,5,3,4,6))
    //
    //    rdd1.sortBy(x=>x,false).collect().foreach(println)
    //    val rdd1 = sc.makeRDD(List(
    //      ("a", 1), ("a", 2), ("b", 3), ("b", 4), ("b", 5), ("a", 6)
    //    ), 2)
    //
    //
    //    // 取分区内最大值,分区间求和
    //    rdd1.aggregateByKey(0)(
    //      (x, y) => math.max(x, y), //索引0分区 a,2 b,3  索引1分区 b,5 a,6
    //      (x, y) => x + y // a,8 b8
    //    ).collect().foreach(println)
    //
    //
    //    // 获取key的平均值
    //    val result= rdd1.aggregateByKey((0, 0))(
    //      (kv, v) => (kv._2 + v, kv._2 + 1),
    //      (kv1, kv2) => (kv1._1 + kv2._1, kv1._2 + kv2._2)
    //    ).map {
    //      case (key,(v1,v2))=> (key,v1/v2)
    //    }
    //    result.collect().foreach(println)

    //    val rdd2 = sc.makeRDD(List(
    //      ("a", 1), ("a", 2), ("b", 3),
    //      ("b", 4), ("b", 5), ("a", 6)
    //    ), 2)

    //求每个 key 的平均值
    //    val combineRdd: RDD[(String, (Int, Int))] = rdd2.combineByKey(
    //      v => (v, 1)
    //      , (kv: (Int, Int), v) => (kv._1 + v, kv._2 + 1)
    //      , (kv: (Int, Int), kv1: (Int, Int)) => (kv._1 + kv1._1, kv._2 + kv1._2)
    //    )
    //    combineRdd.mapValues{
    //      case (sum,cnt)=> sum/cnt
    //    }.collect().foreach(println)


    // req 需求
    //    val sourceRdd: RDD[String] = sc.textFile("datas/agent.log")
    //
    //    val mapRDD: RDD[((String, String), Int)] = sourceRdd.map {
    //      line => {
    //        val arr: Array[String] = line.split(" ")
    //        ((arr(0), arr(4)), 1)
    //      }
    //    }
    //    val reduceRdd: RDD[((String, String), Int)] = mapRDD.reduceByKey(_ + _)
    //
    //    val tempRdd: RDD[(String, (String, Int))] = reduceRdd.map {
    //      case ((pre, ad), cnt) => {
    //        (pre, (ad, cnt))
    //      }
    //    }
    //
    //    val groupByRdd: RDD[(String, Iterable[(String, Int)])] = tempRdd.groupByKey()
    //
    //    // mapValues只对(k,v)的value值做操作,所以才操作前要先分组
    //    val result: RDD[(String, List[(String, Int)])] = groupByRdd.mapValues {
    //      iter => {
    //        iter.toList.sortWith(_._2 > _._2).take(3)
    //      }
    //    }
    //
    //    result.collect().foreach(println)
    //    val fileRDD: RDD[String] = sc.textFile("datas/1.txt")
    //    println(fileRDD.toDebugString)
    //    println("	")
    //
    //    val wordRDD: RDD[String] = fileRDD.flatMap(_.split(" "))
    //    println(wordRDD.toDebugString)
    //    println("	")
    //
    //    val mapRDD: RDD[(String, Int)] = wordRDD.map((_,1))
    //    println(mapRDD.toDebugString)
    //    println("	")
    //
    //    val resultRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_+_)
    //    println(resultRDD.toDebugString)
    //
    //    resultRDD.collect()

    //    val fileRDD: RDD[String] = sc.textFile("datas/1.txt")
    ////    println(fileRDD.dependencies)
    ////    println("	")
    ////
    ////    val wordRDD: RDD[String] = fileRDD.flatMap(_.split(" "))
    ////    println(wordRDD.dependencies)
    ////    println("	")
    ////
    ////    val mapRDD: RDD[(String, Int)] = wordRDD.map((_,1))
    ////    println(mapRDD.dependencies)
    ////    println("	")
    ////
    ////    val resultRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_+_)
    ////    println(resultRDD.dependencies)
    ////
    ////    resultRDD.collect()
    //
    //    sc.sequenceFile[Int, Int]("", classOf[Int], classOf[Int])

    //    val rdd: RDD[String] = sc.makeRDD(List("hello", "hello", "world", "cg"))
    //
    //    // 使用自定义累加器计算key的count值
    //    // 创建累加器对象
    //    val acc = new MyAcc1
    //    // 注册累加器对象
    //    sc.register(acc)
    //
    //    rdd.foreach {
    //      data => {
    //        acc.add(data)
    //      }
    //    }
    //    println(acc.value)

    //    val sourceRdd: RDD[String] = sc.textFile("datas/user_visit_action.txt")
    //
    //    // 点击事件
    //    val clickRdd: RDD[(String, Int)] = sourceRdd.filter {
    //      line => {
    //        val arrs: Array[String] = line.split("_")
    //        arrs(6) != "-1" && arrs(7) != "-1"
    //      }
    //    }.map {
    //      line => {
    //        val arr: Array[String] = line.split("_")
    //        (arr(6), 1)
    //      }
    //    }
    //    val clickSum: RDD[(String, Int)] = clickRdd.reduceByKey(_ + _)
    //
    //    // 下单事件
    //    val orderRdd: RDD[(String, Int)] = sourceRdd.filter {
    //      line => {
    //        val arrs: Array[String] = line.split("_")
    //        arrs(8) != "null"
    //      }
    //    }.flatMap {
    //      line => {
    //        val arr: Array[String] = line.split("_")
    //        val strings: Array[String] = arr(8).split(",")
    //        strings.map(id => (id, 1))
    //      }
    //    }
    //
    //    val orderSum: RDD[(String, Int)] = orderRdd.reduceByKey(_ + _)
    //    //    orderSum.sortBy(_._2,false).take(10).foreach(println)
    //
    //    // 支付事件
    //    val payRdd: RDD[(String, Int)] = sourceRdd.filter {
    //      line => {
    //        val arr: Array[String] = line.split("_")
    //        arr(10) != "null"
    //      }
    //    }.flatMap {
    //      line => {
    //        val arr: Array[String] = line.split("_")
    //        val strings: Array[String] = arr(10).split(",")
    //        strings.map(id => (id, 1))
    //      }
    //    }
    //
    //    val paySum: RDD[(String, Int)] = payRdd.reduceByKey(_ + _)
    //
    //    val cogroupRdd: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickSum.cogroup(orderSum, paySum)
    //    val result: RDD[(String, (Int, Int, Int))] = cogroupRdd.map {
    //      case (key, (iter1, iter2, iter3)) => {
    //        var clickCnt = 0
    //        var orderCnt = 0
    //        var payCnt = 0
    //        val i1: Iterator[Int] = iter1.iterator
    //        val i2: Iterator[Int] = iter2.iterator
    //        val i3: Iterator[Int] = iter3.iterator
    //        if (i1.hasNext) {
    //          clickCnt = i1.next()
    //        }
    //        if (i2.hasNext) {
    //          orderCnt = i2.next()
    //        }
    //        if (i3.hasNext) {
    //          payCnt = i3.next()
    //        }
    //        (key, (clickCnt, orderCnt, payCnt))
    //      }
    //    }
    //
    //    result.sortBy(_._2, false).take(10).foreach(println)








    // 1.单value类型 sortBy 第二个参数可以设置false倒序
    val rdd: RDD[Int] = sc.makeRDD(List(2,1,5,3,4,6))
    val valueSort: RDD[Int] = rdd.sortBy(x => x, false)

    // 2.双value类型 ：元組排序方式，先按第一个元素降序，再按第二个元素降序
    val rdd1: RDD[(Int, Int)] = sc.makeRDD(List((2, 3), (5, 6), (1, 5),(2,2)))
    val TupleSort: RDD[(Int, Int)] = rdd1.sortBy(t => (-t._1, t._2))
    TupleSort.collect().foreach(println)

    // 3.keyValue类型： SortByKey
    val rdd2: RDD[(String,Int)] = sc.makeRDD(Array(("aa",3),("cc",6),("bb",2),("dd",1)))
    val sortByKeyRdd = rdd2.sortByKey(false)
    sortByKeyRdd.collect().foreach(println)

    println("---自定义排序------")
    // 自定义排序
    val users = Array("li 30","zhao 29","zhang 30","wang 35")
    val usersRdd = sc.makeRDD(users)
    val personRdd: RDD[Person] = usersRdd.map {
      line => {
        val arr: Array[String] = line.split(" ")
        Person(arr(0), arr(1).toInt)
      }
    }
    personRdd.sortBy(x=>x).collect().foreach(println)

    sc.stop()
  }
}

class MyAcc1 extends AccumulatorV2[String, mutable.Map[String, Int]] {
  // 定义累加器的值 注意是累加器返回类型的值,并不是整个累加器
  private var accValue: mutable.Map[String, Int] = mutable.Map[String, Int]()

  override def isZero: Boolean = accValue.isEmpty

  override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = new MyAcc1

  override def reset(): Unit = accValue.clear()

  override def add(v: String): Unit = {
    //可以理解为分区内合并,每来一个v 进行处理
    val newCnt = accValue.getOrElse(v, 0) + 1
    accValue.update(v, newCnt)
  }

  override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {
    // merge 是Driver端合并
    val map1 = this.accValue
    val map2 = other.value
    //    accValue = map1.foldLeft(map2)( // 注意这个地方一定要重新赋值,否则累加值没变化
    //      (innermap, kv) => {
    //        val count = innermap.getOrElse(kv._1, 0) + kv._2
    //        innermap.update(kv._1, count)
    //        innermap
    //      }
    //
    //    )
    map2.foreach {
      case (key, count) => {
        val newCnt = map1.getOrElse(key, 0) + count
        map1.update(key, newCnt)
      }
    }

  }

  override def value: mutable.Map[String, Int] = accValue
}

case class Person(val name:String,val age:Int) extends Ordered[Person] {
  override def compare(that: Person): Int = {
    // 先按年龄降序,再按姓名升序
    var result= - (this.age -that.age)
//    var result= - this.age.compareTo(that.age)

    if(result==0){
      result==this.name.compareTo(that.name)
    }
    result
  }
}