import Node from "./Node";

const Utils = {
    /**
     * Function used to remove a Node object from a array of Nodes.
     *
     * @param {Array<Node>} nodes the array of elements containing the targetNode
     * @param {Node} targetNode the node to be removed from [nodes].
     */
    removeNode: function (nodes, targetNode) {
        const targetNodeIndex = nodes.findIndex((n) =>
            (n.coordinate.x === targetNode.coordinate.x && n.coordinate.y === targetNode.coordinate.y
        ));

        // takes effect to the array or must be returned?
        nodes.splice(targetNodeIndex, 1);
    },

    /**
     * This function sorts all nodes of the param array by their numeric [f]
     * value.
     *
     * @param {Array<Node>} nodes the source array of nodes to be sorted.
     */
    sortNodesByFCost: function(nodes) {
        // TODO
    }
};

export default Utils;