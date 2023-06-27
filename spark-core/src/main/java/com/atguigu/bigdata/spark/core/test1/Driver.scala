package com.atguigu.bigdata.spark.core.test1

import java.io.{ObjectOutputStream, OutputStream}
import java.net.Socket

object Driver {
  def main(args: Array[String]): Unit = {
    // 连接服务器,开启客户端
    val client1: Socket = new Socket("localhost", 9999)
    val client2: Socket = new Socket("localhost", 8888)
    // 开启输出流
    val out: OutputStream = client1.getOutputStream

    // 利用输出流,使用对象输出流
    val objOutStream: ObjectOutputStream = new ObjectOutputStream(out)

    // 拿到数据对象
    val data: Task = new Task

    // 分布式,数据需要拆分给两个服务端处理,服务端接收数据和逻辑
    println("----------------server服务端1------")
    val subTask1: SubTask = new SubTask
    subTask1.datas = data.datas.take(2)  // 拆分一半数据给client1
    subTask1.logic = data.logic
    // 数据对象发送给服务端1
    objOutStream.writeObject(subTask1)
    objOutStream.flush()
    objOutStream.close()
    client1.close()
    println("客户端数据发送服务端1完毕")

    println("----------------server服务端2------")

    // 开启输出流
    val out2: OutputStream = client2.getOutputStream

    // 利用输出流,使用对象输出流
    val objOutStream2: ObjectOutputStream = new ObjectOutputStream(out2)

    // 拿到数据对象
    val data2: Task = new Task

    val subTask2: SubTask = new SubTask
    subTask2.datas = data.datas.takeRight(2) // 拆分一半数据给client2
    subTask2.logic = data.logic
    // 数据对象发送给服务端1
    objOutStream2.writeObject(subTask2)
    objOutStream2.flush()
    objOutStream2.close()
    client2.close()
    println("客户端端 数据发送服务端2完毕")
  }
}
