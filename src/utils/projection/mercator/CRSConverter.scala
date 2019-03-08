package utils.projection.mercator

/**
  * reference to global-mercator package in npm.js
  * https://www.npmjs.com/package/global-mercator
  * */
import com.vividsolutions.jts.geom.Coordinate

object CRSConverter {

    private val originShift = 2 * Math.PI * 6378137 / 2.0

    /**
      * convert (lng, lat, zoom) to (X, Y, Z) in mercator Projection with given tile size
      * default tile Size 4096
      * */
    def coordinateToTileCoords(coord: Coordinate, zoom: Int, tileSize: Int, tileX: Int, tileY: Int): Coordinate ={
        val lng = coord.x
        val lat = coord.y
        val (dx, dy, z) = pointToTileFraction(lat, lng, zoom)
        val X = fraction(dx, tileX) * tileSize
        val Y = fraction(dy, tileY) * tileSize
        new Coordinate(X, Y)
    }

    def coordinateToTileCoords(coord: Coordinate, zoom: Int, tileX: Int, tileY: Int): Coordinate = {
        coordinateToTileCoords(coord, zoom, 4096, tileX, tileY)
    }

    /**
      * convert mercator projection coord (X, Y, Z) in Double to coordinate (lng, lat, zoom) in Double
      * */
    def tileCoordsToCoordinate(coord: Coordinate): Coordinate ={
        val (minLng, minLat, maxLng, maxLat) = tileToBBox(coord)
        new Coordinate(minLng, maxLat, coord.z)
    }

    def pointToTileFraction(lat: Double, lng: Double, zoom: Int): (Double, Double, Int)={
        val lng1 = longitude(lng)
        val lat1 = latitude(lat)
        val sin = Math.sin(Math.toRadians(lat1))
        val z: Double = (1 << zoom).toDouble
        val x = z * (lng1 / 360 + 0.5)
        val y = z * (0.5 - 0.25 * Math.log((1 + sin) / (1 - sin)) / Math.PI)
        validateTile(x, y, zoom)
    }

    def fraction(num: Double, actual: Int): Double = num - actual

    def pointToTile(lat: Double, lng: Double, zoom: Int): (Int, Int, Int) ={
        val (x, y, z) = pointToTileFraction(lat, lng, zoom)
        (Math.floor(x).toInt, Math.floor(y).toInt, z)
    }

    def tileToBox(x: Double, y: Double, z: Int): (Double, Double, Double, Double) ={
        val x1 = x
        val y1 = (1 << z) - y - 1  // pow(2, z) - y - 1
        val z1 = z
        if (z1 == 0) {
            (-180, -85.051129, 180, 85.051129)  // [minLng, minLat, maxLng, maxLat]
        } else {
            val (minX, minY, maxX, maxY) = tileToBoxMeters(x1, y1, z1)
            val (minLng, minLat) = metersToLatLng(minX, minY, z1)
            val (maxLng, maxLat) = metersToLatLng(maxX, maxY, z1)
            (minLng, minLat, maxLng, maxLat)
        }
    }

    private def metersToLatLng(mx: Double, my: Double, z: Int): (Double, Double) = {
        var lng = (mx / originShift) * 180.0
        var lat = (my / originShift) * 180.0
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0)
        lng = numberToFixed(lng, 6)
        lat = numberToFixed(lat, 6)
        (lng, lat)
    }

    private def tileToBoxMeters(x: Double, y: Double, z: Int): (Double, Double, Double, Double) = {
        val (x1, y1, z1) = validateTile(x, y, z)
        val (minX, minY) = pixelToMeters(Pixel(x * 256, y * 256, z))
        val (maxX, maxY) = pixelToMeters(Pixel((x + 1) * 256, (y + 1) * 256, z))
        (minX, minY, maxX, maxY)
    }

    private def pixelToMeters(pixel: Pixel, tileSize: Int): (Double, Double) = {
        val res = resolution(pixel.z, tileSize)
        var mx: Double = pixel.px * res - originShift
        var my: Double = pixel.py * res - originShift
        mx = numberToFixed(mx, 1)
        my = numberToFixed(my, 1)
        (mx, my)
    }

    /**
      * @param digits: precision to digits after dot
      * */
    def numberToFixed(num: Double, digits: Int): Double = {
        BigDecimal(num).setScale(digits, BigDecimal.RoundingMode.HALF_UP).toDouble
    }

    private def pixelToMeters(pixel: Pixel): (Double, Double) = pixelToMeters(pixel, 256)

    private def resolution(zoom: Int, tileSize: Int): Double = initialResolution(tileSize) / (1 << zoom) // pow(2, zoom)

    private def initialResolution(tileSize: Int): Double = 2 * Math.PI * 6378137 / tileSize

    private def validateTile(x: Double, y: Double, z: Int): (Double, Double, Int) = {
        if (z < 0) {
            throw new RuntimeException("Zoom level cannot be less than 0")
        } else if (z > 32) {
            throw new RuntimeException("zoom level cannot be larger than 32")
        }
        val maxTile = 1 << z  // pow(2, z)
        var x1 = x % maxTile
        if (x1 < 0) x1 += maxTile
        (x1, y, z)
    }

    private def longitude(lng: Double): Double ={
        var lng1 = lng
        if (lng1 > 180 || lng1 < - 180) {
            lng1 = lng1 % 360
            if (lng1 > 180) lng1 -= 360
            if (lng1 < -180) lng1 += 360
            if (lng1 == 0) lng1 = 0
        }
        lng1
    }

    private def latitude(lat: Double): Double ={
        var lat1 = lat
        if (lat1 > 90 || lat1 < -90) {
            lat1 = lat1 % 180
            if (lat1 > 90) lat1 -= 180
            if (lat1 < -90) lat1 += 180
            if (lat1 == 0) lat1 = 0
        }
        lat1
    }
    /**
      * Converts TMS Tile to bbox in Meters coordinates.
      *
      * @param tileCoord tile Tile [x, y, zoom]
      * @param tileSize [tileSize=256] Tile size
      * @param validate [validate=true] validates Tile
      * @return BBox bbox extent in [minX, minY, maxX, maxY] order
      * @example
      * var bbox = globalMercator.tileToBBoxMeters([6963, 5003, 13])
      * //=[ 14025277.4, 4437016.6, 14030169.4, 4441908.5 ]
      */
    def tileToBBoxMeters(tileCoord: Coordinate, tileSize: Int = 256, validate: Boolean = true): (Double, Double, Double, Double) ={
        validateTile(tileCoord.x, tileCoord.y, tileCoord.z.toInt)
        val (minX, minY) = pixelToMeters(Pixel(tileCoord.x * tileSize, tileCoord.y * tileSize, tileCoord.z.toInt))
        val (maxX, maxY) = pixelToMeters(Pixel((tileCoord.x + 1) * tileSize, (tileCoord.y + 1) * tileSize, tileCoord.z.toInt))
        (minX, minY, maxX, maxY)
    }

    def tileToBBox(tileCoord: Coordinate, validate: Boolean = true): (Double, Double, Double, Double) ={
        validateTile(tileCoord.x, tileCoord.y, tileCoord.z.toInt)
        if (tileCoord.z.toInt == 0) {
            (-180, -85.051129, 180, 85.051129)
        } else {
            val (minX, minY, maxX, maxY) = tileToBBoxMeters(tileCoord)
            val (minLng, minLat) = metersToLatLng(minX, minY, tileCoord.z.toInt)
            val (maxLng, maxLat) = metersToLatLng(maxX, maxY, tileCoord.z.toInt)
            (minLng, minLat, maxLng, maxLat)
        }
    }
}
case class Pixel(px: Double, py: Double, z: Int)
