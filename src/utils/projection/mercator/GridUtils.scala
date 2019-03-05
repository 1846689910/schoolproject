package utils.projection.mercator

import com.vividsolutions.jts.geom.Coordinate
import org.junit.Test

class GridUtils {
    /**
      * Mercator projection:
      * Given a coordinate with (x, y, z) in Integer, get the coordinate of this square when z + 1, i.e. zoom in
      * */
    def nextGrids(coord: Coordinate): Array[Array[Coordinate]] = nextNGrids(coord, 1)
    /**
      * Given a coordinate with (x, y, z) in Integer
      * get the coordinate of this square when z + n
      * */
    def nextNGrids(coord: Coordinate, n: Int): Array[Array[Coordinate]] ={
        val width = 1 << n  // num of squares in x or y direction
        val rightBottomX = (coord.x.toInt + 1) * width - 1  // x of right bottom square
        val rightBottomY = (coord.y.toInt + 1) * width - 1  // y of right bottom square
        val leftTopX = rightBottomX - width + 1
        val leftTopY = rightBottomY - width + 1
        val z = coord.z + n
        var coords: Array[Array[Coordinate]] = Array()
        for (i <- leftTopY to rightBottomY) {
            var row: Array[Coordinate] = Array()
            for (j <- leftTopX to rightBottomX) {
                row = row :+ new Coordinate(j, i, z)
            }
            coords = coords :+ row
        }
        coords
    }
    /**
      * the count of grids in x(lng) or y(lat) direction
      * */
    def zoomLevelWidth(z: Int): Int = 1 << z
    /**
      * total count of grids at this zoom level, should be pow(width, 2)
      * */
    def zoomLevelGridsCount(z: Int): Int = 1 << (z * 2)
    /**
      * for the mercator coordinate (x: Int, y: Int, z: Int), to find where the grid is located in
      * z - n level grid, get the parent grid coordinate, which represent the coordinate will be located where in
      * its parent grid
      * */
    def toParentGridPercentage(coord: Coordinate, n : Int): ParentGridCoordinate = {
        val width: Int = zoomLevelWidth(coord.z.toInt)  // whole width of grids in current zoom level
        val parentWidth: Int = zoomLevelWidth(coord.z.toInt - n)  // whole width of grids in z - n zoom level
        val blockWidth: Int = zoomLevelWidth(n)  // zoom from z - n to z, each grid could be split into how many sub-grids in x or y direction
        val subGridPercent: Double = 1 / blockWidth.toDouble

        val minX: Double = parentWidth * (coord.x.toDouble / width.toDouble)
        val maxX: Double = minX + subGridPercent

        val minY: Double = parentWidth * (coord.y.toDouble / width.toDouble)
        val maxY: Double = minY + subGridPercent

        ParentGridCoordinate(minX, maxX, minY, maxY, coord.z.toInt - n)
    }
    @Test
    def test(): Unit ={
        nextNGrids(new Coordinate(21, 15, 5), 2).foreach(arr => println(arr.mkString(",")))
        val parentGrid = toParentGridPercentage(new Coordinate(85, 60, 7), 2)

        println(parentGrid.getPositionPercentage)
        println(parentGrid.getCoordinate)
        println(parentGrid.getXYBorder(4096).mkString(","))
    }
}
//case class Coordinate(x: Int, y: Int, z: Int)
case class ParentGridCoordinate(minX: Double, maxX: Double, minY: Double, maxY: Double, z: Int){
    def getCoordinate: Coordinate = new Coordinate(minX.toInt, minY.toInt, z)
    def getPositionPercentage: PositionPercentage = {
        val coord = getCoordinate
        PositionPercentage(minX - coord.x, maxX - coord.x, minY - coord.y, maxY - coord.y)
    }
    /**
      * given a tile width, like 4096. convert the position percentage to the position in tile square with width 4096
      * */
    def getXYBorder(tileWidth: Int): Array[Double] ={
        val percents = getPositionPercentage
        Array(
            percents.minPercentX * tileWidth.toDouble,
            percents.maxPercentX * tileWidth.toDouble,
            percents.minPercentY * tileWidth.toDouble,
            percents.maxPercentY * tileWidth.toDouble
        )
    }
}
case class PositionPercentage(minPercentX: Double, maxPercentX: Double, minPercentY: Double, maxPercentY: Double)