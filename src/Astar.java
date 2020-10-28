import java.util.*;

public class Astar {
    private static final int DEFALUT_HV_COST = 1;
    private int hvCost;

    private Node[][] Board;
    private PriorityQueue<Node> openList;
    private Set<Node> closedList;
    private Node startNode;
    private Node goalNode;

    public Astar(int boX, int boY, Node startNode, Node goalNode, int hvCost){
        this.hvCost = hvCost;
        setFinalNode(goalNode);
        setInitialNode(startNode);
        this.Board = new Node[boX][boY];
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node0, Node node1) {
                return Integer.compare(node0.getF(), node1.getF());
            }
        });
        setNodes();
        this.closedList = new HashSet<>();
    }
    public Astar(int x, int y, Node startNode, Node goalNode){
        this(x, y, startNode, goalNode,DEFALUT_HV_COST);
    }


    public List<Node> findPath(){
        openList.add(startNode);
        while (!isEmpty(openList)){
            Node currNode = openList.poll();
            closedList.add(currNode);
            if(isFinalNode(currNode)){
                return getPath(currNode);
            }else{
                addAdjNode(currNode);
            }
        }
        return new ArrayList<Node>();
    }

    private List<Node> getPath(Node currNode){
        List<Node> path = new ArrayList<Node>();
        path.add(currNode);
        Node parent;
        while ((parent = currNode.getParent()) != null){
            path.add(0,parent);
            currNode = parent;
        }
        return path;
    }


    private void addAdjNode(Node currNode){
        addUpperAdjacent(currNode);
        addMiddleAdjacent(currNode);
        addLowerAdjacent(currNode);
    }

    private void setNodes() {
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[0].length; j++) {
                Node node = new Node(i, j);
                node.hScore(getFinalNode());
                this.Board[i][j] = node;
            }
        }
    }

    public void setNoNoZone(String[] NoNoZone){
        for(int i = 0; i< NoNoZone.length; i++){
            String pt = NoNoZone[i];
            String[] wallPos = pt.split(",");
            int row = Integer.parseInt(wallPos[1]);
            int col = Integer.parseInt(wallPos[0]);
            setBlock(row, col);
        }
    }

    private void addUpperAdjacent(Node currNode){
        int row = currNode.getRow();
        int col = currNode.getCol();
        int upperrRow = row -1;
        if (upperrRow >= 0){
            checkNode(currNode,col, upperrRow, getHvCost());
        }

    }

    private void addMiddleAdjacent(Node currNode){
        int row = currNode.getRow();
        int col = currNode.getCol();
        int middleRow = row;
        if(col - 1 >= 0){
            checkNode(currNode, col-1, middleRow, getHvCost());
        }
        if(col + 1 < getSearchArea()[0].length){
            checkNode(currNode,col+1, middleRow, getHvCost());
        }
    }

    private void addLowerAdjacent(Node currNode){
        int row = currNode.getRow();
        int col = currNode.getCol();
        int lowerRow = row + 1;
        if(lowerRow< getSearchArea().length){
            checkNode(currNode, col, lowerRow,getHvCost());
        }
    }

    private void checkNode(Node currNode, int col, int row, int cost) {
        Node adjNode = getSearchArea()[row][col];
        if (!adjNode.isBlocked() && !getClosedList().contains(adjNode)) {
            if (!getOpenList().contains(adjNode)) {
                adjNode.setNodeData(currNode, cost);
                getOpenList().add(adjNode);
            } else {
                boolean changed = adjNode.checkBetterPath(currNode, cost);
                if (changed) {
                    getOpenList().remove(adjNode);
                    getOpenList().add(adjNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(goalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    public void setBlock(int row, int col) {
        this.Board[row][col].setBlocked(true);
    }

    public Node getInitialNode() {
        return startNode;
    }

    public void setInitialNode(Node initialNode) {
        this.startNode = initialNode;
    }

    public Node getFinalNode() {
        return goalNode;
    }

    public void setFinalNode(Node finalNode) {
        this.goalNode = finalNode;
    }

    public Node[][] getSearchArea() {
        return Board;
    }

    public void setSearchArea(Node[][] searchArea) {
        this.Board = searchArea;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public Set<Node> getClosedList() {
        return closedList;
    }

    public void setClosedList(Set<Node> closedSet) {
        this.closedList = closedSet;
    }

    public int getHvCost() {
        return hvCost;
    }

    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }


}
