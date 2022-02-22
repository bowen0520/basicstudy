package com.briup.bigdata.day03

class Student (var name:String,var age:Int) extends Ordered[Student]{
  override def toString: String = {
    s"$name $age"
  }

  override def compare(that: Student): Int = {
    name.compareTo(that.name)
  }
}
