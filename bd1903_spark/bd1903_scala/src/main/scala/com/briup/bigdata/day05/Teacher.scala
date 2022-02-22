package com.briup.bigdata.day05

class Teacher extends Person {
  override val name: String = "tea"
  override val age: Int = 30

  override def eat(food: String): Unit = {
    println("teacher" + food)
  }
}