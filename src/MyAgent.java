import za.ac.wits.snake.DevelopmentAgent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
//27/10/2020

public class MyAgent extends DevelopmentAgent {

    public static void main(String args[]) throws IOException {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }
    Node start;
    String neck;

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            // read in obstacles and do something with them!
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            int boardRow = Integer.parseInt(temp[1]);
            int boardCol = Integer.parseInt(temp[2]);
            // read in obstacles and do something with them!
            int nObstacles = 3;
            String obsStuff = "";
            for (int obstacle = 0; obstacle < nObstacles; obstacle++) {
                String obs = br.readLine();
                /// do something with obs
                obsStuff += obs+ " ";
            }
            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                String obsEn =obsStuff;
                String allSnakeCo ="";// this will contain all the coords of the whole snake (including coords between snake bent )
                String afro="";
                String apple1 = line;
                //do stuff with apples
                String[] appleCo = apple1.split(" ");
                int apple_X = Integer.parseInt(appleCo[0]);
                int apple_Y = Integer.parseInt(appleCo[1]);
                Node goal = new Node(apple_Y, apple_X);
                goal.setBlocked(false);

                int mySnakeNum = Integer.parseInt(br.readLine());
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    String[] snakeInfo = snakeLine.split(" ");
                    String lifeStatus = snakeInfo[0];
                    String length = snakeInfo[1];
                    String kills = snakeInfo[2];

                    //check if snake is alive, if alive run code else continue
                    if(lifeStatus.equalsIgnoreCase("alive")) {
                        if (i == mySnakeNum) {// my snake
                            //hey! That's me :)
                            String head = snakeInfo[3];
                            neck = snakeInfo[4];
                            String tail = snakeInfo[snakeInfo.length-1];
                            //creat A* start node
                            String[] headCoord = head.split(",");
                            start = new Node(Integer.parseInt(headCoord[1]), Integer.parseInt(headCoord[0]));

                            for (int j = 4; j < snakeInfo.length; j++) {
                                allSnakeCo += snakeInfo[j]+" "+fillSnake(i, snakeInfo[j-1], snakeInfo[j]);// adds coords between snake bends
                            }

                        }//other snakes
                        //do stuff with other snakes
                        String head = snakeInfo[3];
                        String[] headCoord = head.split(",");
                        Node otherHead = new Node(Integer.parseInt(headCoord[1]), Integer.parseInt(headCoord[0]));
                        afro += poisonAfro(otherHead);
                        for (int j = 4; j < snakeInfo.length; j++) {
                            allSnakeCo += snakeInfo[j]+" "+fillSnake(i, snakeInfo[j-1], snakeInfo[j]);// adds coords between snake bends
                        }

                    }
                }
                //finished reading, calculate move:
                //modify wall coords to be processed into Nodes
                obsStuff = obsStuff.replaceAll("\\s+$", "");
                String[] walls = obsEn.split(" ");
                //modify the snakeCoords
                allSnakeCo = allSnakeCo.replaceAll("\\s+$", "");
                String[] badSnakeCoords = allSnakeCo.split(" ");
                //do A* stuff
                afro =afro.replaceAll("\\s+$", "");
                String[] killerAfro = afro.split(" ");
                try {
                    Astar aStar = new Astar(boardRow, boardCol, start, goal);
                    // create NoNoZones
                    aStar.setNoNoZone(walls);
                    aStar.setNoNoZone(badSnakeCoords);

                    //find A* path
                    List<Node> path;

                    path = aStar.findPath();


                    // get head node and the node where the snake needs to me next
                    Node head = path.get(0);
                    Node nextNode = path.get(1);
                    // calculate the next move
                    int move = nextMove(head, nextNode);
                    System.out.println(move);
                }catch (Exception ex){

                    // below will be the code that occurs when A* path isnt found
                    // maybe do something directional like in the one lab where we made it go in the correct direction

                    //set fake Start node to find fake path

                    System.out.println(5);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fillAllSnakes(int nSnake, String SnakeVert){
        String[] snakeCo = SnakeVert.split(" ");
        String snake = "";
        for( int i = 1; i< snakeCo.length;i++){
            snake = snake + fillSnake(nSnake, snakeCo[i-1], snakeCo[i]);
        }
        return snake;
    }
    public static String fillSnake (int nSake, String SnakeCo1, String SnakeCo2){

        String[] coord1 = SnakeCo1.split(",");
        String[] coord2 = SnakeCo2.split(",");

        //System.out.print(SnakeCo1+ "fun");
        //System.out.print(SnakeCo2+ "fun");

        int x1 = Integer.parseInt(coord1[0]);
        int y1 = Integer.parseInt(coord1[1]);

        int x2 = Integer.parseInt(coord2[0]);
        int y2 = Integer.parseInt(coord2[1]);

        String newCo="";

        // if x/horizontal is the same
        if (x1 == x2){
            //if y1 > y2
            if(y1> y2){
                for( int i = y2; i<= y1; i++){
                    newCo += x1 +","+ String.valueOf(i) + " ";
                    //Node currnode = new Node(x1 , i);
                    //currnode.setBlocked(true);

                }
            }
            //if y2 > y1
            else if (y2 > y1){
                for( int i = y1; i< y2; i++){
                    newCo += x1 +","+ String.valueOf(i) + " ";
                    //Node currnode = new Node(x1 , i);
                    //currnode.setBlocked(true);

                }
            }
        }
        // if y/vertical is the same
        if (y1 == y2){
            // if x1 > x2
            if( x1 > x2){
                for( int i = x2; i < x1; i++){
                    newCo +=  String.valueOf(i)+","+y1+ " ";
                    //Node currnode = new Node(i , y1);
                    //currnode.setBlocked(true);

                }
            }
            // if x2 > x1
            else if ( x2 > x1 ){
                for( int i = x1; i < x2; i++){
                    newCo += String.valueOf(i)+","+y1+ " ";
                    //Node currnode = new Node(i , y1);
                    //currnode.setBlocked(true);

                }
            }
        }

        return newCo ;
    }

    public int nextMove(Node head, Node nextNode){
        //String[] neckCoord = neck.split(",");
        if(head.getCol() == nextNode.getCol()){
            if(nextNode.getRow() == (head.getRow() -1)){
                return 0;
            }else if(nextNode.getRow() == (head.getRow() +1)){
                return 1;
            }
        }else if(head.getRow() == nextNode.getRow()) {
            if (nextNode.getCol() == (head.getCol() - 1)) {
                return 2;
            } else if (nextNode.getCol() == (head.getCol() + 1)) {
                return 3;
            }
        }
        //move straight relative to the snake
        //make right turn
        //same column right/left turn when head-neck is horizontal
        /*
        if(head.getCol() == Integer.parseInt(neckCoord[0]) && head.getRow() < apple.getRow()){
            if(apple.getCol() < Integer.parseInt(neckCoord[0])){
                return 6;
            }else if(apple.getCol() > Integer.parseInt(neckCoord[0])){
                return 4;
            }
        }else if(head.getCol() == Integer.parseInt(neckCoord[0]) && head.getRow() > apple.getRow() ){
            if(apple.getCol() < Integer.parseInt(neckCoord[0])){
                return 4;
            }else if(apple.getCol() > Integer.parseInt(neckCoord[0])){
                return 6;
            }
        }*/


        return 5;
    }

    public String poisonAfro(Node head){
        int upperRow = head.getRow()-1;
        int lowerRow = head.getRow()+1;
        int rightCol = head.getCol()+1;
        int leftCol = head.getCol()-1;
        String temp = "";

        //upper node
        if(upperRow != -1){
            temp += head.getCol()+","+upperRow+ " "; // in the form of xy not yx
        }
        if(lowerRow != 51){
            temp += head.getCol()+","+lowerRow+" ";
        }
        if(leftCol != -1){
            temp += leftCol+","+ head.getRow()+ " ";
        }
        if(rightCol != 51){
            temp += rightCol+","+head.getRow()+" ";
        }
        return temp;
    }


}