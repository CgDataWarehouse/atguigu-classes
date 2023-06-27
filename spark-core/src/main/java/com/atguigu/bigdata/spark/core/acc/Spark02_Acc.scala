package com.atguigu.bigdata.spark.core.acc

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Acc {
  def main(args: Array[String]): Unit = {
    // todo  准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Acc")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4))

    // 案例分析:
    //    var sum =0  // Driver端变量
    //    rdd.foreach(
    //      num =>{
    //        sum +=num
    //      }
    //    )
    //    println(sum) // 结果发现还是0
    // 结果为何是0 ???
    // 因为sum变量是在foreach以外的Driver端,而task操作分布式运行在Executor端,即 sum+=num 在executor运行,但是executor没有sum=0这个变量,
    //即便闭包序列化,exe的sum变量有了变化,但从始至终driver端的sum变量没有变化过

    // 引入累加器 --分布式共享只写变量


    // 获取系统累加器
    // spark默认提供了简单数据聚合的累加器
    // 其他累加器如 : sc.doubleAccumulator()   sc.collectionAccumulator()

    val sumAcc = sc.longAccumulator("sum")   // 参数sum就是给累加器起个名...方便ui监控
    val mapRdd = rdd.map( // 注意!!!:这里 foreach 改为map了
      num => {
        // 使用累加器
        sumAcc.add(num)
        num
      }
    )

    // 获取累加器的值 .value
    // 转换算子懒加载,在转换算子中调用累加器,如果无行动算子,就不会执行

    // 所以一般情况下,累加器会放入行动算子中进行操作 ,比如foreach
    println(sumAcc.value)

    mapRdd.collect()

    sc.stop()
  }
}
