package com.krynju.ai;

class Node {
    int x;  //node's x cord
    int y;  //node's y cord
    int step;  //node's step
    int destroyableWallsCount = 0;
    Node previousNode;
    Node(int x, int y, int step) {
        this.x = x;
        this.y = y;
        this.step = step;
    }
    Node(int x, int y, int step, Node prev) {
        this.x = x;
        this.y = y;
        this.step = step;
        this.previousNode = prev;
    }
}
