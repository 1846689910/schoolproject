package utils.common;

import java.util.*;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.Set;

public class Java2Scala {

    // java array can be used in scala directly

    public static <E> Seq<E> list2Seq (List<E> list) {
        return JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
        /*
         * then in scala
         * val seq1 = Java2Seq.list2Seq(javaList)
         * for (item <- seq1) println(item)
         * */
    }

    public static <E> scala.collection.Set<E> set2Set (java.util.Set<E> set) {
        return JavaConverters.asScalaSetConverter(set).asScala().toSet();
        /*
         * then in scala
         * val set1 = Java2Seq.set2Set(javaSet)
         * for (item <- set1) println(item)
         * */
    }

    public static <E> scala.collection.Iterator<E> it2It (java.util.Iterator<E> it) {
        return JavaConverters.asScalaIteratorConverter(it).asScala().toIterator();
        /*
         * then in scala
         * val it1 = Java2Scala.it2It(Temp1.it)
         * while(it1.hasNext) println(it1.next())
         * */

    }

    public static <K, V> scala.collection.mutable.Map<K, V> map2Map (java.util.Map<K, V> map) {
        return JavaConverters.mapAsScalaMapConverter(map).asScala();
        /*
         * then in scala
         * val map1 = Java2Scala.map2Map(Temp1.getMap)
         * for ((k, v) <- map1) println(k + ":" + v)
         * */
    }

    public static <K, V> scala.collection.concurrent.Map<K, V> concurrentMap2ConcurrentMap (java.util.concurrent.ConcurrentMap<K, V> map){
        return JavaConverters.mapAsScalaConcurrentMapConverter(map).asScala();
        /*
         * then in scala
         * val map2 = Java2Scala.concurrentMap2ConcurrentMap(Temp1.getConcurrentMap)
         * for ((k, v) <- map2) println(k + ":" + v)
         * */
    }

}