package com.briup.bigdata.day01

class User {
  var id:Int = _;
  var name:String=_;
  private var age:Short=_;
  private var salary:Double=_;

  def this(inid:Int){
    this()
    id = inid
  }

  def this(inid:Int,inname:String,inage:Short,insalary:Double){
    this(inid)
    name = inname
    age = inage
    salary = insalary
  }

  override def toString: String = {
    "id="+id+" name="+name+" age="+age+" salary="+salary
  }
}
object User {

  def apply(id:Int): User = new User(id,"asdd",5,2.2)
}
