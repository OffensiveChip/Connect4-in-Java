package swingConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import connectEngine.*;

public class GUI extends JFrame {
	mainEngine connection = new mainEngine();
    private SquarePanel square;
    private int userHolder = 1;
    private static int[] list;
    private static int playerWin1 = 0;
    private static int playerWin2 = 0;
    private static boolean show = false;
    private int[][] board = new int[6][7];
    private int currentPlayer = 1;
    private int droppingCircleY = -100;
    private int droppingCircleColumn = 3;
    private int targetRow = -1;
    private boolean isAnimating = false;
    private List<CircleData> placedCircles = new ArrayList<>();
    
    private static class CircleData {
        int row;
        int col;
        int player;
        
        CircleData(int row, int col, int player) {
            this.row = row;
            this.col = col;
            this.player = player;
        }
    }
    
    public GUI() {
        super("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null);
        square = new SquarePanel();
        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAnimating) {
                    handleColumnClick(e.getX());
                }
            }
        });
        
        JButton reset = new JButton("Reset Game");
        reset.setFont(new Font("Arial", Font.BOLD, 12));
        reset.setBackground(getForeground());
        reset.setForeground(Color.BLACK);
        reset.setFocusPainted(false);
        reset.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		resetGame();
        	}
        });
        
        setLayout(new BorderLayout());
        add(square, BorderLayout.CENTER);
        add(reset, BorderLayout.SOUTH);
        Timer frames = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnimation();
            }
        });
        frames.start();
        setVisible(true);
    }
    
    private void handleColumnClick(int mouseX) {
        int column = square.getColumnFromX(mouseX);
        if (column != -1) {
            int row = findLowestEmptyRow(column);
            if (row != -1) {
                startDropAnimation(column, row);
                list = connection.placePiece(column, connection.grid, userHolder);
                userHolder = list[0];
                if(!connection.conditions(connection.grid)) {
                	show = true;
                	JOptionPane.showMessageDialog(square,"The winner is player: " + connection.alternateUser(userHolder) );
                	switch(connection.alternateUser(userHolder)) {
                		case 1:
                			playerWin1++;
                			break;
                		case 2:
                			playerWin2++;
                	}
                	resetGame();
                }
            }
        }
    }
    
    private void resetGame() {
    	board = new int[6][7];
    	connection.grid = new int[6][7];
    	userHolder = 1;
    	placedCircles.clear();
    	currentPlayer = 1;
    	square.repaint();
    }
    
    private int findLowestEmptyRow(int column) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row;
            }
        }
        return -1; 
    }
    
    private void startDropAnimation(int column, int targetRow) {
        this.droppingCircleColumn = column;
        this.targetRow = targetRow;
        this.droppingCircleY = -100;
        this.isAnimating = true;
    }
    
    private void updateAnimation() {
        if (isAnimating) {
            droppingCircleY += 15;
            
            int targetY = square.getYPosition(targetRow, droppingCircleColumn);
            if (droppingCircleY >= targetY) {
                droppingCircleY = targetY;
                isAnimating = false;
                

                board[targetRow][droppingCircleColumn] = currentPlayer;
                placedCircles.add(new CircleData(targetRow, droppingCircleColumn, currentPlayer));
                

                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                

                droppingCircleY = -100;
                targetRow = -1;
            }
            
            square.repaint();
        }
    }
    
    public class SquarePanel extends JPanel {
        private static final int COLUMNS = 7;
        private static final int ROWS = 6;
        private static final int CIRCLE_SIZE = 80;
        private static final int GAP = 15;
        private int startX, startY;
        
        public SquarePanel() {
            setDoubleBuffered(true);
            setBackground(new Color(69, 69, 69)); 
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1280, 720);
        }
        
        public int getColumnFromX(int mouseX) {
            int width = getWidth();
            int totalWidthNeeded = COLUMNS * CIRCLE_SIZE + (COLUMNS + 1) * GAP;
            startX = (width - totalWidthNeeded) / 2;
            
            for (int col = 0; col < COLUMNS; col++) {
                int columnX = startX + col * (CIRCLE_SIZE + GAP);
                if (mouseX >= columnX && mouseX <= columnX + CIRCLE_SIZE + GAP) {
                    return col;
                }
            }
            return -1;
        }
        
        public int getYPosition(int row, int col) {
            int height = getHeight();
            int totalHeightNeeded = ROWS * CIRCLE_SIZE + (ROWS + 1) * GAP;
            startY = (height - totalHeightNeeded) / 2;
            
            return startY + row * (CIRCLE_SIZE + GAP) + GAP;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (show) {
            	drawWinLOGO(g2d);
            }
            drawBoardBackground(g2d);
            drawPlacedCircles(g2d);
            if (isAnimating) {
                drawDroppingCircle(g2d);
            }       
            drawGridOverlay(g2d); 
            drawCurrentPlayerIndicator(g2d);
            drawLeaderBoard(g2d);
            
        }
        
        private void drawBoardBackground(Graphics2D g2d) {
            int width = getWidth();
            int height = getHeight();
            

            int totalWidthNeeded = COLUMNS * CIRCLE_SIZE + (COLUMNS + 1) * GAP;
            int totalHeightNeeded = ROWS * CIRCLE_SIZE + (ROWS + 1) * GAP;
            
            startX = (width - totalWidthNeeded) / 2;
            startY = (height - totalHeightNeeded) / 2;
            

            g2d.setColor(new Color(69, 69, 69)); 
            g2d.fillRoundRect(startX - GAP, startY - GAP, 
                            totalWidthNeeded + 2 * GAP, totalHeightNeeded + 2 * GAP, 
                            20, 20);
        }
        
        private void drawPlacedCircles(Graphics2D g2d) {
            for (CircleData circle : placedCircles) {
                int x = startX + circle.col * (CIRCLE_SIZE + GAP) + GAP;
                int y = startY + circle.row * (CIRCLE_SIZE + GAP) + GAP;
                
                Color circleColor = (circle.player == 1) ? 
                    new Color(255, 50, 50) :
                    new Color(50, 255, 50);  
                

                GradientPaint gradient = new GradientPaint(
                    x, y, circleColor.brighter(),
                    x + CIRCLE_SIZE, y + CIRCLE_SIZE, circleColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);
                

                g2d.setColor(circleColor.darker().darker());
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);
            }
        }
        
        private void drawGridOverlay(Graphics2D g2d) {
            int totalWidthNeeded = COLUMNS * CIRCLE_SIZE + (COLUMNS + 1) * GAP;
            int totalHeightNeeded = ROWS * CIRCLE_SIZE + (ROWS + 1) * GAP;
            

            Area boardOverlay = new Area(new RoundRectangle2D.Double(
                startX - GAP, startY - GAP,
                totalWidthNeeded + 2 * GAP, totalHeightNeeded + 2 * GAP,
                20, 20
            ));
            

            for (int col = 0; col < COLUMNS; col++) {
                for (int row = 0; row < ROWS; row++) {
                    int x = startX + col * (CIRCLE_SIZE + GAP) + GAP;
                    int y = startY + row * (CIRCLE_SIZE + GAP) + GAP;
                    

                    Area hole = new Area(new Ellipse2D.Double(x, y, CIRCLE_SIZE, CIRCLE_SIZE));
                    boardOverlay.subtract(hole);
                }
            }
            

            g2d.setColor(new Color(30, 144, 255, 230)); 
            g2d.fill(boardOverlay);
            g2d.setColor(new Color(25, 25, 112));
            g2d.setStroke(new BasicStroke(6));
            g2d.draw(boardOverlay);
            g2d.setColor(new Color(0, 0, 140));
            g2d.setStroke(new BasicStroke(4));
            for (int col = 0; col < COLUMNS; col++) {
                for (int row = 0; row < ROWS; row++) {
                    int x = startX + col * (CIRCLE_SIZE + GAP) + GAP;
                    int y = startY + row * (CIRCLE_SIZE + GAP) + GAP;
                    g2d.drawOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);
                }
            }
        }
        
        private void squareBorder(Graphics2D g2d) {
        	
        }
        private void drawDroppingCircle(Graphics2D g2d) {
            int x = startX + droppingCircleColumn * (CIRCLE_SIZE + GAP) + GAP;
            
            Color circleColor = (currentPlayer == 1) ? 
                new Color(255, 50, 50) :
                new Color(50, 255, 50); 
            
            GradientPaint gradient = new GradientPaint(
                x, droppingCircleY, circleColor.brighter(),
                x + CIRCLE_SIZE, droppingCircleY + CIRCLE_SIZE, circleColor.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillOval(x, droppingCircleY, CIRCLE_SIZE, CIRCLE_SIZE);
            

            g2d.setColor(circleColor.darker().darker());
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(x, droppingCircleY, CIRCLE_SIZE, CIRCLE_SIZE);
        }
        
        private void drawCurrentPlayerIndicator(Graphics2D g2d) {
            int indicatorSize = 40;
            int x = getWidth() - indicatorSize - 20;
            int y = 20;
            
            Color indicatorColor = (currentPlayer == 1) ? 
                new Color(255, 50, 50) :
                new Color(50, 255, 50);
            
            g2d.setColor(indicatorColor);
            g2d.fillOval(x, y, indicatorSize, indicatorSize);
            }
        
        private void drawWinLOGO(Graphics2D g2d) {
        	g2d.setFont(new Font("Arial", Font.BOLD, 40));
        	int centerY = getHeight()/2;
        	int centerX = getWidth()/2;
        	
        	g2d.drawString("PLAYER WINS!", centerX, centerY);
        	int waiting = 4000;
        	Timer time = new Timer(waiting, new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("waiting");
        		((Timer) e.getSource()).stop();
        	}
        	});
        	time.setRepeats(false);
        	time.start();
        	show = false;
        }
        private void drawLeaderBoard(Graphics2D g2d) {
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.setColor(Color.WHITE);
            int x = 20;
            int y = 40;
            
            g2d.drawString("Player 1: " + playerWin1, x, y);
            g2d.drawString("Player 2: " + playerWin2, x, y + 25);
        }
    }}
    
