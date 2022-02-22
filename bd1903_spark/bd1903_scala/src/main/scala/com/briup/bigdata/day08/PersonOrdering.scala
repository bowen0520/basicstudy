package com.briup.bigdata.day08

class PersonOrdering extends Ordering[Person] {
  override def compare(x: Person, y: Person): Int = {
    x.age-y.age
  }
}
