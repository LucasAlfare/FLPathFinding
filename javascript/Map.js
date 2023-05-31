const flags = {
    empty: "_",
    waypoint: ".",
    start: "!",
    end: "X",
    block: "#"
};

/**
 * Class to repsent a map.
 */
class Map {

    /**
     * Creates a new map of dimensions width and height.
     * 
     * @param {number} width width of this map, in tiles unities.
     * @param {number} height height of this map, in tiles unities.
     */
    constructor(width, height) {
        this.width = width;
        this.height = height;
        this.blocks = [];
        this.state = [[]];
        this.resetState();
    }

    resetState() {
        this.state = [];

        for (let y = 0; y < this.height; y++) {
            const tmp = [];
            for (let x = 0; x < this.width; x++) {
                tmp.push(flags.empty);
            }
            this.state.push(tmp);
        }
    }

    /**
     * Find the costless path (a.k.a. shortest path) between the
     * passed coordinates.
     *
     * @param {Coordinate} start the initial coordinate that the path starts on.
     * @param {Coordinate} end the target end coordinate to the path.
     */
    findpath(start, end) {
        let open = [];
        const closed = [];
        let current = new Node(start);

        open.push(current);

        while (true) {
            if (current.coordinate.x === end.x && current.coordinate.y === end.y) {
                console.log("done!");
                break;
            }

            const currentNeighbors = current.getNeighbors(this);
            currentNeighbors.forEach(function (n) {
                if (!open.includes(n) && !closed.includes(n)) {
                    n.processCosts(start, end);
                    open.push(n);
                }
            });

            closed.push(current);
            Utils.removeNode(open, current);

            open = Utils.sortNodesByFCost(open);
            current = open[0];
        }
    }
}

// export default Map; 