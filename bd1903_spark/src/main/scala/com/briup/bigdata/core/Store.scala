package com.briup.bigdata.core

import java.sql.{DriverManager, PreparedStatement}

import org.apache.spark.{SparkConf, SparkContext}

object Store {
  def main(args: Array[String]): Unit = {
    //获取spark context对象
    val conf = new SparkConf()
      .set("spark.app.name","movies")
      .set("spark.master","local[*]")
    val sc = SparkContext.getOrCreate(conf)

    val users=sc.textFile("E:\\briup\\Spark\\spark\\ml-1m\\ml-1m\\users.dat")

    //数据清洗
    val genderrdd = users.map(line => {
      val msgs = line.split("::")
      (msgs(1),1)
    })

    Class.forName("com.mysql.cj.jdbc.Driver");
    val (url,user,password)=("jdbc:mysql://localhost:8017/spark?useUnicode=true&characterEncoding=utf8","root","root");
    val conn=DriverManager.getConnection(url,user,password)
    val sql="insert into spark_movie(gender,count) values(?,?)";
    val pstmt: PreparedStatement =conn.prepareStatement(sql);


    val genders = genderrdd.reduceByKey(_+_).foreach { case (gender, count) =>
      val conn = DriverManager.getConnection(url, user, password)
      val sql = "insert into spark_movie(gender,count) values(?,?)";
      val pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, gender);
      pstmt.setInt(2, count);
      pstmt.execute();
      pstmt.close();
      conn.close();
    }
  }
}
