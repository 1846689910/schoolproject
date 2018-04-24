package utils.common;

public class Scala2Java {

    /**
     * scala array can be used in java directly
     * in scala
     *      val arr = Array(1, 2, 3)
     * in java
     *      int[] arr = scala_arr
     * */
    /**
     * scala Option[T] can be used in java directly
     * in scala
     *      val opt: Option[String] = Option[String]("abc")
     * in java
     *      scala.Option<String> opt = scala_opt
     *      then opt.isDefined() ... all the scala.Option method
     * java Optional<T> can also be used in scala directly
     * in java
     *      Optional<String> opt = Optional.ofNullable("abc");
     * in scala
     *      val opt: java.util.Optional[String] = opt
     *      then opt.isPresent()... all the java.util.Optional method
     * */
    /**
     * if scala use Seq[Int], Set[Int], Map[Int, String]
     * we can skip generic type let
     *      List list = Scala2Java.seq2List(seq)
     *      List<generic_type> list1 = new ArrayList<>();
     *      list.forEach(ele -> list1.add((generic_type)ele))
     *
     *      Set set = Scala2Java.set2Set(set)
     *      Set<generic_type> set1 = new HashSet<>();
     *      set.forEach(ele -> set1.add((generic_type)ele))
     *
     *      Map map = Scala2Java.map2Map(map)
     *      Map<K, V> map1 = new HashMap<>();
     *      map.forEach((k, v) -> map1.put((K)k, (V)v))
     * */

    /** scala Seq (mutable and immutable) to java List */
    public static <E> java.util.List<E> seq2List (scala.collection.Seq<E> seq){
        return scala.collection.JavaConverters.seqAsJavaListConverter(seq).asJava();
        /*
         * in scala
         * val seq1 = scala.collection.mutable.Seq[Integer](1, 2, 3)
         * val seq2 = scala.collection.immutable.Seq[Integer](1, 2, 3)
         * in java
         * List<Integer> list = Scala2Java.seq2List(seq1)
         * */
    }

    /** scala Set (mutable and immutable) to java set */
    public static <E> java.util.Set<E> set2Set (scala.collection.Set<E> set) {
        return scala.collection.JavaConverters.setAsJavaSetConverter(set).asJava();
        /*
         * in scala
         * val set = scala.collection.Set[Integer](1, 2, 3)
         * val setMutable = scala.collection.mutable.Set[Integer](1, 2, 3)
         * val setImmutable = scala.collection.immutable.Set[Integer](1, 2, 3)
         * in java
         * Set<Integer> set = Scala2Java.set2Set(set)
         * */
    }

    /** scala Map (mutable and immutable) to java map */
    public static <K, V> java.util.Map<K, V> map2Map (scala.collection.Map<K, V> map) {
        return scala.collection.JavaConverters.mapAsJavaMapConverter(map).asJava();
        /*
         * in scala
         * val map = scala.collection.Map[Integer, String](1 -> "a", 2 -> "b", 3 -> "c")
         * val mapMutable = scala.collection.mutable.Map[Integer, String](1 -> "a", 2 -> "b", 3 -> "c")
         * val mapImmutable = scala.collection.immutable.Map[Integer, String](1 -> "a", 2 -> "b", 3 -> "c")
         * in java
         * Map<Integer, String> map = Scala2Java.map2Map(map)
         * */
    }
}