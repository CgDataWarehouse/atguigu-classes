package com.atguigu.bigdata.spark.core.test

import java.io.{ObjectOutputStream, OutputStream}
import java.net.Socket


object Driver1 {
  def main(args: Array[String]): Unit = {
    val client1 = new Socket("localhost", 7777)
    val out: OutputStream = client1.getOutputStream

    val objStream: ObjectOutputStream = new ObjectOutputStream(out)
    val data: Task = new Task
    objStream.writeObject(data)
    objStream.flush()
    objStream.close()
    client1.close()
    //
    println("客户端发送数据完毕")
  }
}
