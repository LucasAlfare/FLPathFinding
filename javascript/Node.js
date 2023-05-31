import Coordinate from "./Coordinate.js";
import Utils from "./Utils.js";

/**
 * A node is a representation of a point inside a two-dimensional space.
 *
 * A node has coordinate and fields to store costs. Also, contains references to
 * its parent node, if avaliable.
 */
class Node {
    /**
     * Creates a new node object with the coord from param.
     *
     * @param {Coordinate} coordinate the coordinate of this node.
     */
    constructor(coordinate) {
        this.coordinate = coordinate;
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.parent = null;
    }

    /**
     * Calculates the costs values to this current node, based on
     * start and end coordinates.
     *
     * @param {Coordinate} start the initial coordinate of the path.
     * @param {Coordinate} end the targeted end coordiante that the path aims on.
     */
    processCosts(start, end) {
        this.g = Utils.euclideanDistance(this.coordinate, start);
        this.h = Utils.euclideanDistance(this.coordinate, end);
        this.f = this.g + this.h;
    }

    /**
     * This method is used to get the actual neighbors (other surrounding nodes)
     * of this current node.
     * @param {Map} map the actual map that this node is in.
     *
     * @returns {Array<Node>} list of the actual neighbors of this current node.
     */
    getNeighbors(map) {
        const results = [];
        for (let y = -1; y <= 1; y++) {
            for (let x = -1; x <= 1; x++) {
                if (x !== 0 && y !== 0) {
                    const nextNeighborCoordinate = new Coordinate(x, y);
                    if (nextNeighborCoordinate.inBounds(map)) {
                        nextNeighborCoordinate.parent = this;
                        results.push(nextNeighborCoordinate);
                    }
                }
            }
        }

        return results;
    }
}

export default Node;