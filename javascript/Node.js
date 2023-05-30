import Coordinate from "./Coordinate";

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
     * @param {Coordinate} coordinate
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
        this.g = Math.abs(this.coordinate.x - start.x) + Math.abs(this.coordinate.y - start.y);
        this.h = Math.abs(this.coordinate.x - end.x) + Math.abs(this.coordinate.y - end.y);
        this.f = this.g + this.h;
    }

    /**
     * This method is used to get the actual neighbors (other surrounding nodes)
     * of this current node.
     * @param {number} width the current width of the space where this node is being processed.
     * @param {number} height the current height of the space where this node is being processed.
     *
     * @returns {Array<Node>} list of the actual neighbors of this current node.
     */
    getNeighbors(width, height) {
        const results = [];

        const north = new Coordinate(this.coordinate.x, this.coordinate.y - 1);
        const south = new Coordinate(this.coordinate.x, this.coordinate.y + 1);
        const east = new Coordinate(this.coordinate.x + 1, this.coordinate.y);
        const west = new Coordinate(this.coordinate.x - 1, this.coordinate.y);

        if (north.inBounds(width, height)) results.push(new Node(north));
        if (south.inBounds(width, height)) results.push(new Node(south));
        if (east.inBounds(width, height)) results.push(new Node(east));
        if (west.inBounds(width, height)) results.push(new Node(west));

        // err...?
        const self = this;
        results.forEach(n => { n.parent = self });

        return results;
    }
}

export default Node;