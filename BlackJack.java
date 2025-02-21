import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
 
public class BlackJack {

    private class Card{
        String value;
        String type;

        Card(String value,String type){
            this.value=value;
            this.type=type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue(){
            if("AJQK".contains(value)){
                if(value=="A"){
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce(){
            return value == "A";
        }

        public String getImagePath(){
            return "./Deck-Images/"+ toString()+".png";
        }
    }

    ArrayList<Card> deck;
    Random random= new Random();


    //DEALER
    Card hiddenCard;
    ArrayList<Card> dealerhand;
    int dealersum;
    int Dealeracecount;

    //PLAYER
    ArrayList<Card> playerhand;
    int playersum;
    int playeracecount;

    //UI Window
    int boardwidth = 600;
    int boardheight = boardwidth;

    int cardWidth=110;
    int cardHeight=154;
    
    JFrame frame= new JFrame(" Black Jack");
    JPanel gamepanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try{
                //DRAW HIDDEN CARD
                Image hiddenCardimg = new ImageIcon("./Deck-Images/BACK.png").getImage();
                if(!stayButton.isEnabled()){
                    hiddenCardimg= new ImageIcon(hiddenCard.getImagePath()).getImage();
                }
                g.drawImage(hiddenCardimg, 20, 20, cardWidth, cardHeight,null);

                //DRAW DEALER'S HAND
                for(int i=0; i<dealerhand.size();i++){
                    Card card = dealerhand.get(i); 
                    Image cardImage = new ImageIcon(card.getImagePath()).getImage();
                    g.drawImage(cardImage, cardWidth + 25 + (cardWidth+5)*i, 20, cardWidth,cardHeight,null);
                }

                //DRAW PLAYER'S HAND
                for(int i=0; i<playerhand.size();i++){
                    Card card = playerhand.get(i); 
                    Image cardImage = new ImageIcon(card.getImagePath()).getImage();
                    g.drawImage(cardImage, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
                }

                if(!stayButton.isEnabled()){
                    dealersum = reduceDealerAce();
                    playersum = reducePlayerAce();
                    
                    System.out.println("Stay: ");
                    System.out.println(dealersum);
                    System.out.println(playersum);

                    String message ="";
                    if (playersum > 21) {
                        message = "You Lose!";
                    }
                    else if (dealersum > 21) {
                        message = "You Win!";
                    }
                    else if(playersum==dealersum){
                        message="Tied";
                    }
                    else if(playersum>dealersum){
                        message = "You Win!";
                    }
                    else if(playersum<dealersum){
                        message = "You Lose!";
                    }

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
    };
    JPanel buttonpanel = new JPanel();
    JButton hitbutton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    BlackJack(){
        startGame();
        frame.setVisible(true);
        frame.setSize(boardheight,boardwidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamepanel.setLayout(new BorderLayout());
        gamepanel.setBackground(new Color(53,101,77));
        frame.add(gamepanel);

        hitbutton.setFocusable(false);
        buttonpanel.add(hitbutton);

        stayButton.setFocusable(false);
        buttonpanel.add(stayButton);

        frame.add(buttonpanel,BorderLayout.SOUTH);

        hitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                playersum += card.getValue();
                playeracecount += card.isAce() ? 1 : 0;
                playerhand.add(card);
                if (reducePlayerAce() > 21) { 
                    hitbutton.setEnabled(false); 
                }
                gamepanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitbutton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealersum < 17) {
                    Card card = deck.remove(deck.size()-1);
                    dealersum += card.getValue();
                    Dealeracecount += card.isAce() ? 1 : 0;
                    dealerhand.add(card);
                }
                gamepanel.repaint();
            }
        });


        gamepanel.repaint();
    }


    public void startGame(){
        buildDeck();
        shuffleDeck();

        //DEALER
        dealerhand = new ArrayList<>();
        dealersum=0;
        Dealeracecount=0;
        hiddenCard = deck.remove(deck.size()-1);
        dealersum += hiddenCard.getValue();

        Dealeracecount += hiddenCard.isAce() ? 1:0;

        Card card = deck.remove(deck.size()-1);
        dealersum +=card.getValue();
        Dealeracecount += card.isAce() ? 1:0;
        dealerhand.add(card);

        System.out.println("Dealer's Card:");
        System.out.println(hiddenCard);
        System.out.println(dealerhand);
        System.out.println(dealersum);
        System.out.println(Dealeracecount);

        //PLAYER
        playerhand = new ArrayList<>();
        playersum=0;
        playeracecount=0;
        
        for(int i=0; i<2; i++){
            card = deck.remove(deck.size()-1);
            playersum+=card.getValue();
            playeracecount+=card.isAce() ? 1: 0;
            playerhand.add(card);
        }

        System.out.println("Player's Card:");
        System.out.println(playerhand);
        System.out.println(playersum);
        System.out.println(playeracecount);
    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("Build Deck:");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for(int i=0;i<deck.size();i++){
            int j= random.nextInt(deck.size());
            Card currentcard= deck.get(i);
            Card randomcard = deck.get(j);
            deck.set(i, randomcard);
            deck.set(j,currentcard);
        }
        System.out.println("After Shuffling the card");
        System.out.println(deck);
    }

    public int reducePlayerAce() {
        while (playersum > 21 && playeracecount > 0) {
            playersum -= 10;
            playeracecount -= 1;
        }
        return playersum;
    }

    public int reduceDealerAce() {
        while (dealersum > 21 && Dealeracecount > 0) {
            dealersum -= 10;
            Dealeracecount -= 1;
        }
        return dealersum;
    }
}
