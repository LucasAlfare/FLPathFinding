import Map from './Map.js'

/**
 * Class used to represent a single coordinate of a two-dimensional space.
 *
 * Also, this coord should be used in context where the actual coordinate system
 * lies in the 4-th quadrant.
 */
class Coordinate {
    /**
     * Creates a new coordinate object with x and y coordinates based on params.
     * @param {number} x the x coordinate to this object.
     * @param {number} y the y coordinate to this object.
     */
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Function used to check if this coodinates pair is inside a rect of dimensions
     * width and height.
     *
     * @param {Map} map the actual map that this coordinate is in.
     *
     * @returns {boolean} true if this coordinate is inside the bounds, otherwise {boolean} false
     */
    inBounds(map) {
        return (
            (x >= 0 && x < map.width) &&
            (y >= 0 && y < map.height)
        );
    }
}

export default Coordinate;