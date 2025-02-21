import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;;

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
    
    JFrame frame= new JFrame(" Black Jack");


    BlackJack(){
        startGame();
        frame.setVisible(true);
        frame.setSize(boardheight,boardwidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
}
