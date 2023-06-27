package com.atguigu.bigdata.spark.core.test

import java.io.{InputStream, ObjectInputStream}
import java.net.{ServerSocket, Socket}

object Executor {
  def main(args: Array[String]): Unit = {
    // 启动服务器,接受数据
    val server =new ServerSocket(9999)
    println("服务器启动,等待接受数据")

    // 等待客户端的连接
    val client: Socket = server.accept()
    val in: InputStream = client.getInputStream
    // 接受一个对象,用对象输入流
    val objIn: ObjectInputStream = new ObjectInputStream(in)

    val task: Task = objIn.readObject().asInstanceOf[Task]
    val ints: List[Int] = task.compute()
    println("计算节点计算的结果为:" + ints)
    objIn.close()
    client.close()
    server.close()
  }
}
