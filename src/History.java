import java.util.ArrayList;

public class History {
    ArrayList<Card> historyOfStore;
    ArrayList<Card> historyOfDrop;
    ArrayList<ArrayList<Card>> historyOfDeal;


    History(){
        historyOfStore = new ArrayList<>();
        historyOfDrop = new ArrayList<>();
        historyOfDeal = new ArrayList<>();
    }

    public void setHistoryOfStore(Card card){
        historyOfStore.add(card);
    }

    public ArrayList<Card> getHistoryOfStore() {
        return historyOfStore;
    }

    public void setHistoryOfDrop(Card card){
        historyOfDrop.add(card);
    }

    public ArrayList<Card> getHistoryOfDrop() {
        return historyOfDrop;
    }
}
