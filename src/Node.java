public class Node {
    private int row, col;
    private int f;
    private int g;
    private int h;
    private boolean isBlocked;
    private boolean isApple;
    private Node parent;
    public Node(int row, int col){
        this.row = row;
        this.col = col;
    }

    public void hScore(Node finalNode) {
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }

    public void setNodeData(Node currNode, int cost){
        int gCost = currNode.getG() + cost;
        setParent(currNode);
        setG(gCost);
        calcFscore();
    }
    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    public void calcFscore(){
        int finalCost = getG()+ getH();
        setF(finalCost);
    }

    @Override
    public boolean equals(Object arg0) {
        Node other = (Node) arg0;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + "]";
    }

    public int getH(){
        return h;
    }

    public int getG(){
        return g;
    }

    public int getF(){
        return f;
    }

    public void setH(int h){
        this.h = h;
    }

    public void setG(int g){
        this.g = g;
    }

    public void setF(int f){
        this.f = f;
    }

    public Node getParent(){
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public int getCol(){
        return col;
    }

    public void setCol(int x){
        this.col = x;
    }

    public int getRow(){
        return row;
    }

    public void setRow(int y){
        this.row  = y;
    }

    public boolean isBlocked(){
        return isBlocked;
    }

    public void setBlocked(boolean isBlocked){
        this.isBlocked = isBlocked;
    }

    public boolean isApple(){
        return isApple;
    }

    public void setApple(boolean isApple){
        this.isApple = isApple;
    }

}
