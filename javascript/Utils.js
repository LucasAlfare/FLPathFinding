// import Coordinate from "./Coordinate.js";
// import Node from "./Node.js";

const Utils = {
    /**
     * Function used to remove a Node object from a array of Nodes.
     *
     * @param {Array<Node>} nodes the array of elements containing the targetNode.
     * @param {Node} targetNode the node to be removed from [nodes].
     */
    removeNode: function (nodes, targetNode) {
        const targetNodeIndex = nodes.findIndex((n) =>
            (n.coordinate.x === targetNode.coordinate.x && n.coordinate.y === targetNode.coordinate.y)
        );

        // takes effect to the array or must be returned?
        nodes.splice(targetNodeIndex, 1);
    },

    /**
     * This function sorts all nodes of the param array by their numeric [f]
     * value.
     *
     * @param {Array<Node>} nodes the source array of nodes to be sorted.
     */
    sortNodesByFCost: function (nodes) {
        return nodes.sort(function (a, b) {
            return a.f - b.f;
        });
    },

    /**
     * 
     * @param {Coordinate} a first coordinate.
     * @param {Coordinate} b second coordinate.
     * 
     * @returns {number} the euclidean distance between {a} and {b}.
     */
    euclideanDistance: function (a, b) {
        const aa = a.x - b.x;
        const bb = a.y - b.y;
        return Math.sqrt((aa * aa) + (bb * bb));
    }
};

// export default Utils;