package com.briup.bigdata.core

import java.sql.{PreparedStatement}

class MyPstmt(pstmt: PreparedStatement) extends Serializable {
  val myPstmt = pstmt

  def store(gender:String,count:Int){
    myPstmt.setString(1, gender)
    pstmt.setInt(2, count)
    pstmt.execute()
    pstmt.close()
  }
}
